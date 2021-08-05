package com.et_cleanup.et_todoc.data.repositories;

import androidx.lifecycle.LiveData;

import com.et_cleanup.et_todoc.data.dao.TaskDAO;
import com.et_cleanup.et_todoc.model.Task;

import java.util.List;

public class TaskRepository {

    private final TaskDAO dao;

    public TaskRepository(TaskDAO dao){
        this.dao = dao;
    }

    public LiveData<List<Task>> getTaskList(){
        return dao.getAllTasks();
    }

    public void insert(Task task){
        dao.insert(task);
    }

    public void delete(Task task){
        dao.delete(task);
    }
}
