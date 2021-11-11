package com.cleanup.todoc.data.repositories;

import androidx.lifecycle.LiveData;

import com.cleanup.todoc.data.model.Project;
import com.cleanup.todoc.data.room.dao.ProjectDao;

import java.util.List;

/**
 * <p>Repository class to communicate with projects from TodocDB,
 * using ProjectDao interface methods</p>
 */
public class ProjectRepository {

    /**
     * DAO interface
     */
    private final ProjectDao projectDao;

    public ProjectRepository(ProjectDao projectDao) {
        this.projectDao = projectDao;
    }

    public LiveData<List<Project>> getAllProjects() {
        return projectDao.getAllProject();
    }

    /**
     * DAO method
     */
    public void insertProject(Project project) {
        projectDao.insertProject(project);
    }
}
