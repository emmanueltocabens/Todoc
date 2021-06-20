package com.cleanup.todoc.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.cleanup.todoc.model.Project;

import java.util.List;

@Dao
public interface ProjectDAO {

    @Insert
    void insert(Project Project);

    @Query("SELECT * FROM Project")
    LiveData<List<Project>> getAllProjects();

    @Update
    void update(Project Project);

    @Delete
    void delete(Project Project);


}
