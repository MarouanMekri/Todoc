package com.cleanup.todoc.data.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.cleanup.todoc.data.model.Project;

import java.util.List;

/**
 * <p>DAO interface defining methods to access projects</p>
 */
@Dao
public interface ProjectDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertProject(Project project);

    @Query("SELECT * FROM projects WHERE id = :id")
    Project getProject(int id);

    @Query("SELECT * FROM projects ORDER BY id")
    LiveData<List<Project>> getAllProject();
}
