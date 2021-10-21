package com.cleanup.todoc.service;

import com.cleanup.todoc.model.Task;

import java.util.List;

public interface ApiService {
    List<Task> getTasks();
    void addTask(Task task);
    void deleteTask(Task task);
}
