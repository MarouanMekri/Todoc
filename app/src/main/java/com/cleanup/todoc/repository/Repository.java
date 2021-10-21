package com.cleanup.todoc.repository;

import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.service.ApiService;

import java.util.List;

public class Repository {

    private final ApiService apiService;

    public Repository(ApiService apiService) {
        this.apiService = apiService;
    }

    public List<Task> getTasks() {
        return apiService.getTasks();
    }

    public void addTask(Task task) {
        apiService.addTask(task);
    }

    public void deleteTask(Task task) {
        apiService.deleteTask(task);
    }
}
