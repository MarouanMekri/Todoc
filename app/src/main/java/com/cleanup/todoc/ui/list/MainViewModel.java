package com.cleanup.todoc.ui.list;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.cleanup.todoc.data.model.Project;
import com.cleanup.todoc.data.model.Task;
import com.cleanup.todoc.data.Repository;

import java.util.Collections;
import java.util.List;

public class MainViewModel extends ViewModel {

    @NonNull
    private final Repository repository;

    // Constructor
    public MainViewModel(@NonNull Repository repository) {
        this.repository = repository;
    }

    // Return projects list from repository
    public List<Project> getAllProjects() {
        return repository.getAllProjects();
    }

    // Return tasks list from repository
    public LiveData<List<Task>> getAllTasks() {
        return repository.getAllTasks();
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
