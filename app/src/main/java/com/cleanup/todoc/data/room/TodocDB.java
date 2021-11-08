package com.cleanup.todoc.data.room;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.cleanup.todoc.data.model.Project;
import com.cleanup.todoc.data.model.Task;

@Database(entities = {Project.class, Task.class}, version = 1, exportSchema = false)
public abstract class TodocDB extends RoomDatabase {

    private static TodocDB instance;

    public abstract TodocDao todocDao();

    public static synchronized TodocDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    TodocDB.class, "db")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static final RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private final TodocDao todocDao;

        private PopulateDbAsyncTask (TodocDB todocDb) {
            todocDao = todocDb.todocDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            todocDao.insertProject(new Project("Projet Tartampion", 0xFFEADAD1));
            todocDao.insertProject(new Project("Projet Lucidia", 0xFFB4CDBA));
            todocDao.insertProject(new Project("Projet Circus", 0xFFA3CED2));
            return null;
        }
    }
}
