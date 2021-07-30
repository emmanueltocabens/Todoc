package com.cleanup.todoc.data.dao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.cleanup.todoc.model.Task;

import java.util.ArrayList;
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
