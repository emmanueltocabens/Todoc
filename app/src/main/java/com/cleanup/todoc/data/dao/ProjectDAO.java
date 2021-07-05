package com.cleanup.todoc.data.dao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.cleanup.todoc.model.Project;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface ProjectDAO {

    @Insert
    public void insert(Project Project);

    @Query("SELECT * FROM project")
    public LiveData<List<Project>> getAllProjects();

    @Update
    public void update(Project Project);

    @Delete
    public void delete(Project Project);

    @Query("SELECT * FROM project WHERE :id = id")
    Project getProjectFromID(long id);


}
