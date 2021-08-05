package com.cleanup.et_todoc.injection;

import android.app.Application;

public class TodocApplication extends Application {
    public AppDependencyContainer dependencyContainer;

    @Override
    public void onCreate() {
        super.onCreate();
        dependencyContainer = new AppDependencyContainer(this);
    }
}
