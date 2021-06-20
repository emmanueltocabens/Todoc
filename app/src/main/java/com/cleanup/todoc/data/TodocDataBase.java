package com.cleanup.todoc.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.cleanup.todoc.data.dao.ProjectDAO;
import com.cleanup.todoc.data.dao.TaskDAO;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

@Database(entities = {Task.class, Project.class},version = 1, exportSchema = false)
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
                            "todocDataBase.db").build();
                }
            }
        }
        return INSTANCE;
    }

}
