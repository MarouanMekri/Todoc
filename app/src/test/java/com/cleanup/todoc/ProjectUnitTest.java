package com.cleanup.todoc;

import android.content.Context;
import android.content.res.Resources;

import com.cleanup.todoc.data.model.Project;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

public class ProjectUnitTest {

    @Mock
    private Context mockContext;

    @Mock
    private Resources mockResources;

    @Test
    public void test_project_objects() {
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

        // Create project objects
        final Project project1 = new Project(mockContext.getResources().getString(R.string.project_tartampion),
                mockContext.getResources().getColor(R.color.project_tartampion));
        final Project project2 = new Project(mockContext.getResources().getString(R.string.project_lucidia),
                mockContext.getResources().getColor(R.color.project_lucidia));
        final Project project3 = new Project(mockContext.getResources().getString(R.string.project_circus),
                mockContext.getResources().getColor(R.color.project_circus));

        // Check projects names
        assertEquals("Project Tartampion", project1.getName());
        assertEquals("Project Lucidia", project2.getName());
        assertEquals("Project Circus", project3.getName());

        // Check projects colors
        assertEquals(R.color.project_tartampion, project1.getColor());
        assertEquals(R.color.project_lucidia, project2.getColor());
        assertEquals(R.color.project_circus, project3.getColor());
    }
}
