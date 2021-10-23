package com.cleanup.todoc.viewmodel;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cleanup.todoc.di.Injection;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.repository.Repository;
import com.cleanup.todoc.utils.SortMethod;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

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

    // RV visibility
    public LiveData<Integer> recyclerViewVisibilityState() {
        MutableLiveData<Integer> rvState;
        if (Objects.requireNonNull(tasksMLD.getValue()).size() == 0) {
            rvState = new MutableLiveData<>();
            rvState.setValue(View.GONE);
        }else{
            rvState = new MutableLiveData<>();
            rvState.setValue(View.VISIBLE);
        }
        return rvState;
    }

    // TV visibility
    public LiveData<Integer> illustrationVisibilityState() {
        MutableLiveData<Integer> lbState;
        if (Objects.requireNonNull(tasksMLD.getValue()).size() == 0) {
            lbState = new MutableLiveData<>();
            lbState.setValue(View.VISIBLE);
        }else{
            lbState = new MutableLiveData<>();
            lbState.setValue(View.GONE);
        }
        return lbState;
    }

    // Task sort by method
    public void TaskSort(SortMethod sortMethod, List<Task> tasks) {
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
