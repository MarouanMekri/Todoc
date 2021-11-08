package com.cleanup.todoc.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.cleanup.todoc.data.Repository;
import com.cleanup.todoc.ui.list.MainViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    private final Repository repository;

    public ViewModelFactory(@NonNull Repository repository) {
        this.repository = repository;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
