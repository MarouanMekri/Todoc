package com.cleanup.todoc.ui;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.cleanup.todoc.data.repositories.ProjectRepository;
import com.cleanup.todoc.data.repositories.TaskRepository;
import com.cleanup.todoc.data.room.database.TodocDB;
import com.cleanup.todoc.di.Injection;
import com.cleanup.todoc.ui.list.MainViewModel;

import java.util.concurrent.Executor;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    private final Executor executor;

    public ViewModelFactory(@NonNull Context context) {
        TodocDB db = Injection.provideDatabase(context);
        this.executor = Injection.provideExecutor();
        this.taskRepository = new TaskRepository(db.taskDao());
        this.projectRepository = new ProjectRepository(db.projectDao());
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class))
            return (T) new MainViewModel(taskRepository, projectRepository, executor);
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
