package com.cleanup.todoc.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import java.util.List;

@Dao
public interface TodocDao {
    @Insert
    void insertProject(Project project);

    @Query("SELECT * FROM projects")
    List<Project> getAllProjects();

    @Insert
    void insertTask(Task task);

    @Delete
    void deleteTask(Task task);

    @Query("SELECT * FROM tasks")
    LiveData<List<Task>> getAllTasks();

    @Query("SELECT * FROM projects WHERE id = :projectId")
    Project getProject(int projectId);
}
