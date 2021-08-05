package com.cleanup.et_todoc.data.repositories;

import androidx.lifecycle.LiveData;

import com.cleanup.et_todoc.data.dao.ProjectDAO;
import com.cleanup.et_todoc.model.Project;

import java.util.List;

public class ProjectRepository {

    private final ProjectDAO dao;

    public ProjectRepository(ProjectDAO dao){
        this.dao = dao;
    }

    public LiveData<List<Project>> getProjectList(){
        return dao.getAllProjects();
    }

    public void delete(Project project){
        dao.delete(project);
    }

    public void insert(Project project){
        dao.insert(project);
    }

    public LiveData<Project> getProjectFromID(long id){
        return dao.getProjectFromID(id);
    }

}
