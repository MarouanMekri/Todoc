package com.cleanup.todoc;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.runner.AndroidJUnit4;

import com.cleanup.todoc.data.model.Project;
import com.cleanup.todoc.data.model.Task;
import com.cleanup.todoc.data.repositories.ProjectRepository;
import com.cleanup.todoc.data.room.database.TodocDB;
import com.cleanup.todoc.injections.DI;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

@RunWith(AndroidJUnit4.class)
public class TaskDaoTest {

    private TodocDB database;

    @Before
    public void init_database() {
        Context context = ApplicationProvider.getApplicationContext();

        // Initialize database
        this.database = Room.inMemoryDatabaseBuilder(context, TodocDB.class).build();

        // Initialize repository
        ProjectRepository projectRepository = new ProjectRepository(database.projectDao());

        // Initialize projects in database
        Project[] projects = DI.provideProjects(context);
        for (Project project : projects) {
            projectRepository.insertProject(project);
        }
    }

    @After
    public void close_database() {
        database.close();
    }

    @Test
    public void myDatabase_insertTask_thenRead() {
        // Given : Create and insert task in database
        Task task = new Task(1, "Tester l'application", new Date().getTime());
        database.taskDao().insertTask(task);

        // When : Retrieve task from database
        Task taskRead = database.taskDao().getTask(1);

        // Then : Check task fields
        assertEquals(1, taskRead.getId());
        assertEquals(1, taskRead.getProjectId());
        assertEquals("Tester l'application", taskRead.getName());
    }

    @Test
    public void myDatabase_insertTasks_thenDeleteTask() {
        // Given : Create and insert tasks in database
        Task[] tasks = new Task[] {
                new Task(1, "Ajouter un header sur le site", new Date().getTime()),
                new Task(2, "Intégrer Google Analytics", new Date().getTime()),
                new Task(3, "Appeler le client", new Date().getTime())
        };
        for (Task task : tasks) {
            database.taskDao().insertTask(task);
        }

        // When : Retrieve and delete task from database
        Task taskToRemove = database.taskDao().getTask(1);
        database.taskDao().deleteTask(taskToRemove);

        // Then : Check if database do not contains taskToRemove
        assertNull(database.taskDao().getTask(1));
    }
}
