package com.cleanup.todoc.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.cleanup.todoc.database.ProjectDB;
import com.cleanup.todoc.database.ProjectDao;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import java.util.List;

public class Repository {

    private final ProjectDao projectDao;
    private final List<Project> allProjects;
    private final LiveData<List<Task>> allTasks;

    public Repository(Application application) {
        ProjectDB projectDB = ProjectDB.getInstance(application);
        projectDao = projectDB.projectDao();
        allProjects = projectDao.getAllProjects();
        allTasks = projectDao.getAllTasks();
    }

    public List<Project> getAllProjects() {
        return allProjects;
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public void insertTask(Task task) {
        new InsertTaskAsyncTask(projectDao).execute(task);
    }

    public void deleteTask(Task task) {
        new DeleteTaskAsyncTask(projectDao).execute(task);
    }

    private static class InsertTaskAsyncTask extends AsyncTask<Task, Void, Void> {
        private final ProjectDao projectDao;

        private InsertTaskAsyncTask(ProjectDao projectDao) {
            this.projectDao = projectDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            projectDao.insertTask(tasks[0]);
            return null;
        }
    }

    private static class DeleteTaskAsyncTask extends AsyncTask<Task, Void, Void> {
        private ProjectDao projectDao;

        private DeleteTaskAsyncTask(ProjectDao projectDao) {
            this.projectDao = projectDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            projectDao.deleteTask(tasks[0]);
            return null;
        }
    }
}
