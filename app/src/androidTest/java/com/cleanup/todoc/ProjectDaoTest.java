package com.cleanup.todoc;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.runner.AndroidJUnit4;

import com.cleanup.todoc.data.model.Project;
import com.cleanup.todoc.data.room.database.TodocDB;
import com.cleanup.todoc.di.Injection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class ProjectDaoTest {

    private TodocDB database;
    private Context context;

    @Before
    public void init_database() {
        this.context = ApplicationProvider.getApplicationContext();
        this.database = Room.inMemoryDatabaseBuilder(context, TodocDB.class).build();
    }

    @After
    public void close_database() {
        database.close();
    }

    @Test
    public void myDatabase_insertProjects_thenRead() {
        // Get existing projects
        final Project[] projects = Injection.provideProjects(context);

        // Insert all existing projects
        for (Project project : projects) {
            database.projectDao().insertProject(project);
        }

        // Get back projects from database
        Project firstProject = database.projectDao().getProject(1);
        Project secondProject = database.projectDao().getProject(2);
        Project thirdProject = database.projectDao().getProject(3);
        Project fourthProject = database.projectDao().getProject(4);

        // Read projects values
        assertEquals(1,firstProject.getId());
        assertEquals(projects[0].getName(), firstProject.getName());
        assertEquals(projects[0].getColor(), firstProject.getColor());

        assertEquals(2,secondProject.getId());
        assertEquals(projects[1].getName(), secondProject.getName());
        assertEquals(projects[1].getColor(), secondProject.getColor());

        assertEquals(3,thirdProject.getId());
        assertEquals(projects[2].getName(), thirdProject.getName());
        assertEquals(projects[2].getColor(), thirdProject.getColor());

        assertNull(fourthProject);
    }
}
