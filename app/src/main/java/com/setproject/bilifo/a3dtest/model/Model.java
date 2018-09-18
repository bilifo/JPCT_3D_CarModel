package com.setproject.bilifo.a3dtest.model;

import com.threed.jpct.Object3D;

public class Model {
    Object3D obj;

    String modelPath;
    String texturePath;

    String modeName;
    //后缀
    enum Suffix{_3DS,_OBJ,_DEA}


    public Model(){

    }

    public Model(Object3D object){
        obj=object;
    }

    public Object3D getObj() {
        return obj;
    }

    public String getModelPath() {
        return modelPath;
    }

    public String getTexturePath() {
        return texturePath;
    }

    public void setModelPath(String modelPath) {
        this.modelPath = modelPath;
    }

    public void setObj(Object3D obj) {
        this.obj = obj;
    }

    public void setTexturePath(String texturePath) {
        this.texturePath = texturePath;
    }

//    /**
//     * 判断是什么后缀文件
//     */
//    private Suffix
}
