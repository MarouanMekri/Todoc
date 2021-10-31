package com.cleanup.todoc.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

@Database(entities = {Project.class, Task.class}, version = 1, exportSchema = false)
public abstract class ProjectDB extends RoomDatabase {

    private static ProjectDB instance;

    public abstract ProjectDao projectDao();

    public static synchronized ProjectDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ProjectDB.class, "db")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private ProjectDao projectDao;

        private PopulateDbAsyncTask (ProjectDB projectDb) {
            projectDao = projectDb.projectDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            projectDao.insertProject(new Project("Projet Tartampion", 0xFFEADAD1));
            projectDao.insertProject(new Project("Projet Lucidia", 0xFFB4CDBA));
            projectDao.insertProject(new Project("Projet Circus", 0xFFA3CED2));
            return null;
        }
    }
}
