package com.cleanup.todoc;

import androidx.room.Room;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.cleanup.todoc.Utils.DeleteViewAction;
import com.cleanup.todoc.Utils.RecyclerViewMatcher;
import com.cleanup.todoc.data.model.Project;
import com.cleanup.todoc.data.repositories.ProjectRepository;
import com.cleanup.todoc.data.room.database.TodocDB;
import com.cleanup.todoc.di.Injection;
import com.cleanup.todoc.ui.ViewModelFactory;
import com.cleanup.todoc.ui.list.MainActivity;
import com.cleanup.todoc.ui.list.MainViewModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.cleanup.todoc.Utils.RecyclerViewItemCountAssertion.withItemCount;

@RunWith(AndroidJUnit4.class)
public class UITest {

    private MainViewModel viewModel;

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void init_database() {
        MainActivity mainActivity = rule.getActivity();

        // Initialize database
        TodocDB database = Room.inMemoryDatabaseBuilder(mainActivity, TodocDB.class).build();

        // Initiates a Factory to create ViewModel instances
        ViewModelFactory factory = new ViewModelFactory(mainActivity);
        viewModel = factory.create(MainViewModel.class);

        // Initialize repository
        ProjectRepository projectRepository = new ProjectRepository(database.projectDao());

        // Initialize parent table in database
        Project[] projects = Injection.provideProjects(mainActivity);

        for (Project project : projects) {
            projectRepository.insertProject(project);
        }
    }

    @After
    public void clean_database() {
        // Clean tasks table for next test
        viewModel.deleteAllTasks();
    }

    @Test
    public void myTasksList_createTask() {
        // Perform click on Floating Action Button
        onView(withId(R.id.fab_add_task))
                .perform(click());

        // Perform click on Edit Text to enable focus
        onView(withId(R.id.txt_task_name))
                .perform(click());

        // Write text
        onView(withId(R.id.txt_task_name))
                .perform(typeText("Tache de test"));

        // Close keyboard
        closeSoftKeyboard();

        // Close Dialog by clicking on Positive Button
        onView(withText("Ajouter"))
                .perform(click());

        // Check if RecycleView is updated
        onView(withId(R.id.list_tasks))
                .check(withItemCount(1));
    }

    @Test
    public void myTasksList_createTasks_thenDeleteTask() {
        // Create 3 tasks
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name)).perform(replaceText("Tache 1"));
        onView(withText("Ajouter")).perform(click());
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name)).perform(replaceText("Tache 2"));
        onView(withText("Ajouter")).perform(click());
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name)).perform(replaceText("Tache 3"));
        onView(withText("Ajouter")).perform(click());

        // Check if RecycleView is updated
        onView(withId(R.id.list_tasks))
                .check(withItemCount(3));

        // Remove second item
        onView(withId(R.id.list_tasks))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, new DeleteViewAction()));

        // Wait for recyclerview to get updated
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check if list of Tasks has changed
        onView(withId(R.id.list_tasks))
                .check(withItemCount(2));
    }

    @Test
    public void myTasksList_createTasks_thenSort() {
        // Create 3 tasks
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name)).perform(replaceText("aaa Tâche example"));
        onView(withText("Ajouter")).perform(click());
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name)).perform(replaceText("zzz Tâche example"));
        onView(withText("Ajouter")).perform(click());
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name)).perform(replaceText("hhh Tâche example"));
        onView(withText("Ajouter")).perform(click());

        // Sort alphabetical
        onView(withId(R.id.action_filter)).perform(click());
        onView(withText(R.string.sort_alphabetical)).perform(click());
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("aaa Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("hhh Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("zzz Tâche example")));

        // Sort alphabetical inverted
        onView(withId(R.id.action_filter)).perform(click());
        onView(withText(R.string.sort_alphabetical_invert)).perform(click());
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("zzz Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("hhh Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("aaa Tâche example")));

        // Sort old first
        onView(withId(R.id.action_filter)).perform(click());
        onView(withText(R.string.sort_oldest_first)).perform(click());
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("aaa Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("zzz Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("hhh Tâche example")));

        // Sort recent first
        onView(withId(R.id.action_filter)).perform(click());
        onView(withText(R.string.sort_recent_first)).perform(click());
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("hhh Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("zzz Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("aaa Tâche example")));
    }
}
