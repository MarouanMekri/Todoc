package com.cleanup.todoc.service;

import com.cleanup.todoc.model.Task;

import java.util.ArrayList;
import java.util.List;

public class FakeApiService implements ApiService {

    private final List<Task> tasks = new ArrayList<>();

    @Override
    public List<Task> getTasks() {
        return tasks;
    }

    @Override
    public void addTask(Task task) {
        tasks.add(task);
    }

    @Override
    public void deleteTask(Task task) {
        tasks.remove(task);
    }
}
