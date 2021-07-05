package com.cleanup.todoc.data.repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cleanup.todoc.data.TodocDataBase;
import com.cleanup.todoc.data.dao.ProjectDAO;
import com.cleanup.todoc.model.Project;

import java.util.ArrayList;
import java.util.List;

public class ProjectRepository {

    private final ProjectDAO dao;

    public ProjectRepository(ProjectDAO dao){
        this.dao = dao;
    }

    public LiveData<List<Project>> getProjectList(){
        return dao.getAllProjects();
    }

    public void update(Project project){
        dao.update(project);
    }

    public void delete(Project project){
        dao.delete(project);
    }

    public void insert(Project project){
        dao.insert(project);
    }

    public Project getProjectFromID(long id){
        return dao.getProjectFromID(id);
    }

}
