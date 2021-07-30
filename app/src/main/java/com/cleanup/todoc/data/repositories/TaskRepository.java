package com.cleanup.todoc.data.repositories;

import androidx.lifecycle.LiveData;
import com.cleanup.todoc.data.dao.TaskDAO;
import com.cleanup.todoc.model.Task;

import java.util.ArrayList;
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
