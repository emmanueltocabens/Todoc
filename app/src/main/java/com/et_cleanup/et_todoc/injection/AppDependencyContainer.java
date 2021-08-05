package com.et_cleanup.et_todoc.injection;

import android.app.Application;

import com.et_cleanup.et_todoc.data.TodocDataBase;
import com.et_cleanup.et_todoc.data.repositories.ProjectRepository;
import com.et_cleanup.et_todoc.data.repositories.TaskRepository;

public class AppDependencyContainer {
    public final ProjectRepository projectRepository;
    public final TaskRepository taskRepository;
    public final TodocDataBase database;


    public AppDependencyContainer(Application application){
        database = TodocDataBase.getInstance(application);
        projectRepository = new ProjectRepository(database.projectDAO());
        taskRepository = new TaskRepository(database.taskDAO());
    }
}
