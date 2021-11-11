package com.cleanup.todoc.data.room.dao;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.cleanup.todoc.data.model.Task;

import java.util.List;

/**
 * <p>DAO interface defining methods to access tasks data</p>
 */
@Dao
public interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertTask(Task task);

    @Delete
    void deleteTask(Task task);

    @VisibleForTesting
    @Query("SELECT * FROM tasks WHERE id = :id")
    Task getTask(int id);

    @Query("SELECT * FROM tasks ORDER BY id")
    LiveData<List<Task>> getAllTask();

    @Query("DELETE FROM tasks")
    void deleteAllTasks();
}
