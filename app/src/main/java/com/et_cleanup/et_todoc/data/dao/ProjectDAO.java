package com.et_cleanup.et_todoc.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.et_cleanup.et_todoc.model.Project;

import java.util.List;

@Dao
public interface ProjectDAO {

    @Insert
    void insert(Project Project);

    @Query("SELECT * FROM project")
    LiveData<List<Project>> getAllProjects();

    @Delete
    void delete(Project Project);

    @Query("SELECT * FROM project WHERE :id = id")
    LiveData<Project> getProjectFromID(long id);


}
