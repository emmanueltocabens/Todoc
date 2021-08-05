package com.et_cleanup.et_todoc.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.et_cleanup.et_todoc.model.Task;

import java.util.List;

@Dao
public interface TaskDAO {

    @Insert
    void insert(Task task);

    @Query("SELECT * FROM task")
    LiveData<List<Task>> getAllTasks();

    @Delete
    void delete(Task task);



}
