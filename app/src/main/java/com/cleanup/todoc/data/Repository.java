package com.cleanup.todoc.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.cleanup.todoc.data.model.Project;
import com.cleanup.todoc.data.model.Task;
import com.cleanup.todoc.data.room.TodocDB;
import com.cleanup.todoc.data.room.TodocDao;

import java.util.List;

public class Repository {

    private final TodocDao todocDao;
    private final List<Project> allProjects;
    private final LiveData<List<Task>> allTasks;

    // Constructor
    public Repository(Application application) {
        TodocDB todocDB = TodocDB.getInstance(application);
        todocDao = todocDB.todocDao();
        allProjects = todocDao.getAllProjects();
        allTasks = todocDao.getAllTasks();
    }

    // Return projects list calling database
    public List<Project> getAllProjects() {
        return allProjects;
    }

    // Return tasks list calling database
    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    // Insert task into database
    public void insertTask(Task task) {
        new InsertTaskAsyncTask(todocDao).execute(task);
    }

    // Delete task from database
    public void deleteTask(Task task) {
        new DeleteTaskAsyncTask(todocDao).execute(task);
    }

    private static class InsertTaskAsyncTask extends AsyncTask<Task, Void, Void> {
        private final TodocDao todocDao;

        private InsertTaskAsyncTask(TodocDao todocDao) {
            this.todocDao = todocDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            todocDao.insertTask(tasks[0]);
            return null;
        }
    }

    private static class DeleteTaskAsyncTask extends AsyncTask<Task, Void, Void> {
        private final TodocDao todocDao;

        private DeleteTaskAsyncTask(TodocDao todocDao) {
            this.todocDao = todocDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            todocDao.deleteTask(tasks[0]);
            return null;
        }
    }
}