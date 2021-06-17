package com.cleanup.todoc.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;

import com.cleanup.todoc.model.Project;

@Dao
public interface ProjectDAO {
    @Insert
    LiveData<Project> insert(Project Project);

    @Update
    LiveData<Project> update(Project Project);

    @Delete
    LiveData<Project> delete(Project Project);

    @Query("SELECT * FROM Project WHERE id = id")
    LiveData<Project> getProject(Project Project);

    @RawQuery
    LiveData<Project> rawQuery(Project Project);
}
