package com.cleanup.todoc.data.repositories;

import androidx.lifecycle.LiveData;

import com.cleanup.todoc.data.model.Task;
import com.cleanup.todoc.data.room.dao.TaskDao;

import java.util.List;

/**
 * <p>Repository class to communicate with projects from TodocDB,
 * using TaskDao interface methods</p>
 */
public class TaskRepository {

    /**
     * DAO interface
     */
    private final TaskDao taskDao;

    public TaskRepository(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    public LiveData<List<Task>> getAllTasks() {
        return taskDao.getAllTask();
    }

    /**
     * DAO methods
     */
    public void insertTask(Task task) {
        taskDao.insertTask(task);
    }

    public void deleteTask(Task task) {
        taskDao.deleteTask(task);
    }

    public void deleteAllTask() {
        taskDao.deleteAllTasks();
    }
}
