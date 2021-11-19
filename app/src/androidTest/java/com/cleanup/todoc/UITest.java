package com.cleanup.todoc;

import androidx.room.Room;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.cleanup.todoc.Utils.DeleteViewAction;
import com.cleanup.todoc.Utils.RecyclerViewMatcher;
import com.cleanup.todoc.data.model.Project;
import com.cleanup.todoc.data.repositories.ProjectRepository;
import com.cleanup.todoc.data.room.database.TodocDB;
import com.cleanup.todoc.injections.DI;
import com.cleanup.todoc.injections.ViewModelFactory;
import com.cleanup.todoc.ui.list.MainActivity;
import com.cleanup.todoc.ui.list.MainViewModel;
import com.cleanup.todoc.ui.utils.EspressoIdlingResource;

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
        Project[] projects = DI.provideProjects(mainActivity);
        for (Project project : projects) {
            projectRepository.insertProject(project);
        }

        // Register into EspressoIdlingResource before running any test
        IdlingRegistry.getInstance().register(EspressoIdlingResource.getIdlingResource());
    }

    @After
    public void clean_database() {
        // Clean tasks table for next test
        viewModel.deleteAllTasks();

        // Unregister from EspressoIdlingResource after test finished
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.getIdlingResource());
    }

    @Test
    public void myTasksList_createTask() {
        // Given : Fill out task form
        onView(withId(R.id.fab_add_task))
                .perform(click());
        onView(withId(R.id.txt_task_name))
                .perform(click());
        onView(withId(R.id.txt_task_name))
                .perform(typeText("Tache de test"));
        closeSoftKeyboard();

        // When : Perform positive button to create task
        onView(withText("Ajouter"))
                .perform(click());

        // Then : Check if RecycleView is updated
        onView(withId(R.id.list_tasks))
                .check(withItemCount(1));
    }

    @Test
    public void myTasksList_createTasks_thenDeleteTask() {
        // Given : Create 3 tasks
        onView(withId(R.id.fab_add_task))
                .perform(click());
        onView(withId(R.id.txt_task_name))
                .perform(replaceText("Tache 1"));
        onView(withText("Ajouter"))
                .perform(click());
        onView(withId(R.id.fab_add_task))
                .perform(click());
        onView(withId(R.id.txt_task_name))
                .perform(replaceText("Tache 2"));
        onView(withText("Ajouter"))
                .perform(click());
        onView(withId(R.id.fab_add_task))
                .perform(click());
        onView(withId(R.id.txt_task_name))
                .perform(replaceText("Tache 3"));
        onView(withText("Ajouter"))
                .perform(click());

        // When : Check if RecycleView is updated & remove second task
        onView(withId(R.id.list_tasks))
                .check(withItemCount(3));
        onView(withId(R.id.list_tasks))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, new DeleteViewAction()));

        // Then : Check if RecycleView is updated
        onView(withId(R.id.list_tasks))
                .check(withItemCount(2));
    }

    public void myTasksList_createTasks_thenSortAlphabetical() {
        // Given : Create 3 tasks
        onView(withId(R.id.fab_add_task))
                .perform(click());
        onView(withId(R.id.txt_task_name))
                .perform(replaceText("aaa Tâche example"));
        onView(withText("Ajouter"))
                .perform(click());
        onView(withId(R.id.fab_add_task))
                .perform(click());
        onView(withId(R.id.txt_task_name))
                .perform(replaceText("zzz Tâche example"));
        onView(withText("Ajouter"))
                .perform(click());
        onView(withId(R.id.fab_add_task))
                .perform(click());
        onView(withId(R.id.txt_task_name))
                .perform(replaceText("hhh Tâche example"));
        onView(withText("Ajouter"))
                .perform(click());

        // When : Perform menu button
        onView(withId(R.id.action_filter)).perform(click());
        onView(withText(R.string.sort_alphabetical)).perform(click());

        // Then : Sort alphabetical
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("aaa Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("hhh Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("zzz Tâche example")));
    }

    public void myTasksList_createTasks_thenSortAlphabeticalInverted() {
        // Given : Create 3 tasks
        onView(withId(R.id.fab_add_task))
                .perform(click());
        onView(withId(R.id.txt_task_name))
                .perform(replaceText("aaa Tâche example"));
        onView(withText("Ajouter"))
                .perform(click());
        onView(withId(R.id.fab_add_task))
                .perform(click());
        onView(withId(R.id.txt_task_name))
                .perform(replaceText("zzz Tâche example"));
        onView(withText("Ajouter"))
                .perform(click());
        onView(withId(R.id.fab_add_task))
                .perform(click());
        onView(withId(R.id.txt_task_name))
                .perform(replaceText("hhh Tâche example"));
        onView(withText("Ajouter"))
                .perform(click());

        // When : Perform menu button
        onView(withId(R.id.action_filter)).perform(click());
        onView(withText(R.string.sort_alphabetical_invert)).perform(click());

        // Then : Sort Alphabetical inverted
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("zzz Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("hhh Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("aaa Tâche example")));
    }

    public void myTasksList_createTasks_thenSortOldFirst() {
        // Given : Create 3 tasks
        onView(withId(R.id.fab_add_task))
                .perform(click());
        onView(withId(R.id.txt_task_name))
                .perform(replaceText("aaa Tâche example"));
        onView(withText("Ajouter"))
                .perform(click());
        onView(withId(R.id.fab_add_task))
                .perform(click());
        onView(withId(R.id.txt_task_name))
                .perform(replaceText("zzz Tâche example"));
        onView(withText("Ajouter"))
                .perform(click());
        onView(withId(R.id.fab_add_task))
                .perform(click());
        onView(withId(R.id.txt_task_name))
                .perform(replaceText("hhh Tâche example"));
        onView(withText("Ajouter"))
                .perform(click());

        // When : Perform menu button
        onView(withId(R.id.action_filter)).perform(click());
        onView(withText(R.string.sort_oldest_first)).perform(click());

        // Then : Sort Old first
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("aaa Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("zzz Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("hhh Tâche example")));
    }

    public void myTasksList_createTasks_thenSortRecentFirst() {
        // Given : Create 3 tasks
        onView(withId(R.id.fab_add_task))
                .perform(click());
        onView(withId(R.id.txt_task_name))
                .perform(replaceText("aaa Tâche example"));
        onView(withText("Ajouter"))
                .perform(click());
        onView(withId(R.id.fab_add_task))
                .perform(click());
        onView(withId(R.id.txt_task_name))
                .perform(replaceText("zzz Tâche example"));
        onView(withText("Ajouter"))
                .perform(click());
        onView(withId(R.id.fab_add_task))
                .perform(click());
        onView(withId(R.id.txt_task_name))
                .perform(replaceText("hhh Tâche example"));
        onView(withText("Ajouter"))
                .perform(click());

        // When : Perform menu button
        onView(withId(R.id.action_filter)).perform(click());
        onView(withText(R.string.sort_recent_first)).perform(click());

        // Then : Sort Recent first
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("hhh Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("zzz Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("aaa Tâche example")));
    }
}
