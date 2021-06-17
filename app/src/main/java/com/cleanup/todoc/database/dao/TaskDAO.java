package com.cleanup.todoc.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;

import com.cleanup.todoc.model.Task;

@Dao
public interface TaskDAO {
    @Insert
    void insert(Task task);

    @Query("SELECT * FROM Task WHERE id = :id")
    LiveData<Task> getTaskFromId(int id);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);



}
