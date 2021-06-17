package com.cleanup.todoc.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;

import com.cleanup.todoc.model.Project;

@Dao
public interface ProjectDAO {
    @Insert
    void insert(Project Project);

    @Query("SELECT * FROM Project WHERE id = id")
    LiveData<Project> getProject(Project Project);

    @Update
    void update(Project Project);

    @Delete
    void delete(Project Project);


}
