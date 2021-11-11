package com.cleanup.todoc.data.room.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.cleanup.todoc.data.model.Project;
import com.cleanup.todoc.data.model.Task;
import com.cleanup.todoc.data.room.dao.ProjectDao;
import com.cleanup.todoc.data.room.dao.TaskDao;

/**
 * <p>This class defines Todoc database which contains two tables :
 * - projects
 * - tasks</p>
 */
@Database(entities = {Project.class, Task.class}, version = 1)
public abstract class TodocDB extends RoomDatabase {

    /**
     * Application database instance
     */
    private static TodocDB instance;

    /**
     * Create database singleton
     *
     * @param context : context application
     * @return : TodocDB instance
     */
    public static synchronized TodocDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), TodocDB.class, "db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    /**
     * Dao for both database tables
     */
    public abstract TaskDao taskDao();

    public abstract ProjectDao projectDao();
}
