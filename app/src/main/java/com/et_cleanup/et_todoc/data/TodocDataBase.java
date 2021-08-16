package com.et_cleanup.et_todoc.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.et_cleanup.et_todoc.data.dao.ProjectDAO;
import com.et_cleanup.et_todoc.data.dao.TaskDAO;
import com.et_cleanup.et_todoc.model.Project;
import com.et_cleanup.et_todoc.model.Task;

import java.util.concurrent.Executors;

@Database(
        entities = {Task.class, Project.class},
        version = 5,
        exportSchema = false)
public abstract class TodocDataBase extends RoomDatabase {

    //Singleton
    private static volatile TodocDataBase INSTANCE = null;

    //DAO
    public abstract TaskDAO taskDAO();
    public abstract ProjectDAO projectDAO();

    //Instance
    public static TodocDataBase getInstance(Context context){
        if(INSTANCE == null){
            synchronized (ProjectDAO.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            TodocDataBase.class,
                            "todocDataBase.db")
                            .addCallback(prepopulate)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final Callback prepopulate = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Executors.newSingleThreadExecutor().execute(() -> {
                INSTANCE.projectDAO().insertAll(
                        new Project("Projet Tartampion", 0xFFEADAD1),
                        new Project("Projet Lucidia", 0xFFB4CDBA),
                        new Project("Projet Circus", 0xFFA3CED2)
                );
            });
        }
    };

}
