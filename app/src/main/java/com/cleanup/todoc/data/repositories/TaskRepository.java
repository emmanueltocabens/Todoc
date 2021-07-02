package com.cleanup.todoc.data.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cleanup.todoc.data.TodocDataBase;
import com.cleanup.todoc.data.dao.TaskDAO;
import com.cleanup.todoc.model.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskRepository {

    private TaskDAO dao;

    public TaskRepository(TaskDAO dao){
        this.dao = dao;
    }

    public LiveData<List<Task>> getTaskList(){
        return dao.getAllTasks();
    }

    public void insert(Task task){
        dao.insert(task);
    }

    public void update(Task task){
        dao.update(task);
    }

    public void delete(Task task){
        dao.delete(task);
    }
}
