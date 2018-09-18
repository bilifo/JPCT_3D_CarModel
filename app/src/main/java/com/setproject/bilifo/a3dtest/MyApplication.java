package com.setproject.bilifo.a3dtest;

import android.app.Application;
import android.support.annotation.Nullable;

import com.setproject.bilifo.a3dtest.model.Model;

public class MyApplication extends Application {

    private static MyApplication INSTANCE;

    // Store the current model globally, so that we don't have to re-decode it upon
    // relaunching the main or VR activities.
    // TODO: handle this a bit better.
    @Nullable
    private Model currentModel = null;

    public static MyApplication getInstance() {
        return INSTANCE;
    }

    @Nullable
    public Model getCurrentModel() {
        return currentModel;
    }

    public void setCurrentModel(@Nullable Model model) {
        currentModel = model;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
    }
}
