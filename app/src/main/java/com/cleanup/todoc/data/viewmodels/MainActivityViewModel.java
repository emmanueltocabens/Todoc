package com.cleanup.todoc.data.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.cleanup.todoc.data.repositories.ProjectRepository;
import com.cleanup.todoc.data.repositories.TaskRepository;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivityViewModel extends ViewModel {

    ProjectRepository projectRepository;
    TaskRepository taskRepository;
    private final Executor doInBackground;

    private MainActivityViewModel(ProjectRepository projectRepository, TaskRepository taskRepository){
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        doInBackground = Executors.newFixedThreadPool(2);
    }

    //Projects
    public LiveData<List<Project>> getProjectList(){
        return projectRepository.getProjectList();
    }

    public Project getProjectFromId(long id){
        return projectRepository.getProjectFromID(id);
    }

    //Tasks
    public void insertTask(Task task){
        doInBackground.execute(() -> taskRepository.insert(task));
    }

    public void deleteTask(Task task){
        doInBackground.execute(() -> taskRepository.delete(task));
    }

    public LiveData<List<Task>> getTaskList(){
        return taskRepository.getTaskList();
    }


    public static class Factory implements ViewModelProvider.Factory {

        final ProjectRepository projectRepository;
        final TaskRepository taskRepository;

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(MainActivityViewModel.class)) {
                //noinspection unchecked
                return (T) new MainActivityViewModel(projectRepository,taskRepository);
            } else {
                throw new IllegalArgumentException("ViewModel class not found.");
            }
        }

        public Factory(ProjectRepository projectRepository, TaskRepository taskRepository) {
            super();
            this.projectRepository = projectRepository;
            this.taskRepository = taskRepository;
        }
    }
}

