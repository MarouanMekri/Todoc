package com.cleanup.todoc.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.repository.Repository;
import com.cleanup.todoc.utils.SortMethod;

import java.util.Collections;
import java.util.List;

public class MainViewModel extends ViewModel {

    private final Repository repository;
    private final LiveData<List<Task>> tasks;

    // Constructor
    public MainViewModel(Application application) {
        repository = new Repository(application);
        tasks = repository.getAllTasks();
    }

    // Return projects list from repository
    public List<Project> getAllProjects() {
        return repository.getAllProjects();
    }

    // Return tasks list from repository
    public LiveData<List<Task>> getAllTasks() {
        return tasks;
    }

    // Insert task using repository
    public void insertTask(Task task) {
        repository.insertTask(task);
    }

    // Delete task using repository
    public void deleteTask(Task task) {
        repository.deleteTask(task);
    }

    // Task sort by method
    public void taskSort(SortMethod sortMethod, List<Task> tasks) {
        switch (sortMethod) {
            case ALPHABETICAL:
                Collections.sort(tasks, new Task.TaskAZComparator());
                break;
            case ALPHABETICAL_INVERTED:
                Collections.sort(tasks, new Task.TaskZAComparator());
                break;
            case RECENT_FIRST:
                Collections.sort(tasks, new Task.TaskRecentComparator());
                break;
            case OLD_FIRST:
                Collections.sort(tasks, new Task.TaskOldComparator());
                break;
        }
    }
}
