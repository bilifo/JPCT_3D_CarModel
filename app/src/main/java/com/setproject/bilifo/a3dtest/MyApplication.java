package com.setproject.bilifo.a3dtest;

import android.app.Application;
import android.support.annotation.Nullable;

import com.setproject.bilifo.a3dtest.bean.Model3D;

public class MyApplication extends Application {

    private static MyApplication INSTANCE;

    @Nullable
    private Model3D currentModel = null;

    public static MyApplication getInstance() {
        return INSTANCE;
    }

    @Nullable
    public Model3D getCurrentModel() {
        return currentModel;
    }

    public void setCurrentModel(@Nullable Model3D model) {
        currentModel = model;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
    }
}
