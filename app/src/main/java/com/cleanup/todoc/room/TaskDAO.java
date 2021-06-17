package com.cleanup.todoc.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;

import com.cleanup.todoc.model.Task;

@Dao
public interface TaskDAO {
    @Insert
    LiveData<Task> insert(Task task);

    @Update
    LiveData<Task> update(Task task);

    @Delete
    LiveData<Task> delete(Task task);

    @Query("SELECT * FROM Task WHERE id = id")
    LiveData<Task> getTask(Task task);

    @RawQuery
    LiveData<Task> rawQuery(Task task);
}
