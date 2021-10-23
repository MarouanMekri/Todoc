package com.cleanup.todoc.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cleanup.todoc.di.Injection;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.repository.Repository;

import java.util.List;

public class MainViewModel extends ViewModel {

    private final Repository repository = Injection.createRepository();
    private final MutableLiveData<List<Task>> tasksMLD = new MutableLiveData<>();

    // Return tasks list from repository
    public LiveData<List<Task>> getTasksLiveData() {
        tasksMLD.setValue(repository.getTasks());
        return tasksMLD;
    }

    // Add task in repository
    public void addTask(Task task) {
        repository.addTask(task);
    }

    // Delete task from repository
    public void deleteTask(Task task) {
        repository.deleteTask(task);
    }
}
