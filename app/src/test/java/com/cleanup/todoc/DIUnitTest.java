package com.cleanup.todoc;

import android.content.Context;
import android.content.res.Resources;

import com.cleanup.todoc.data.model.Project;
import com.cleanup.todoc.injections.DI;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.concurrent.Executor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class DIUnitTest {

    @Mock
    private Context mockContext;

    @Mock
    private Resources mockResources;

    /**
     * Checks if DI class provides Executor correctly
     */
    @Test
    public void test_injection_return_executor() {
        Executor executor = DI.provideExecutor();
        assertNotNull(executor);
    }

    /**
     *  - Defines mock Context and Resources objects needed to call DI method
     *  - Call DI method providing a Project[] object
     *  - Checks size of Project[]
     *  - Check each item of Project[]
     */
    @Test
    public void test_injection_projects_provider() {
        mockContext = Mockito.mock(Context.class);
        mockResources = Mockito.mock(Resources.class);

        // Define context mock properties
        Mockito.when(mockContext.getResources()).thenReturn(mockResources);

        // Define resources mock properties
        Mockito.when(mockResources.getColor(R.color.project_tartampion)).thenReturn(R.color.project_tartampion);
        Mockito.when(mockResources.getColor(R.color.project_lucidia)).thenReturn(R.color.project_lucidia);
        Mockito.when(mockResources.getColor(R.color.project_circus)).thenReturn(R.color.project_circus);
        Mockito.when(mockResources.getString(R.string.project_tartampion)).thenReturn("Project Tartampion");
        Mockito.when(mockResources.getString(R.string.project_lucidia)).thenReturn("Project Lucidia");
        Mockito.when(mockResources.getString(R.string.project_circus)).thenReturn("Project Circus");

        // Get projects from dependency injector
        Project[] projects = DI.provideProjects(mockContext);
        assertNotNull(projects);
        assertEquals(3, projects.length);

        // Check projects names
        assertEquals("Project Tartampion", projects[0].getName());
        assertEquals("Project Lucidia", projects[1].getName());
        assertEquals("Project Circus", projects[2].getName());

        // Check projects colors
        assertEquals(R.color.project_tartampion, projects[0].getColor());
        assertEquals(R.color.project_lucidia, projects[1].getColor());
        assertEquals(R.color.project_circus, projects[2].getColor());
    }
}
