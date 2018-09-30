package com.setproject.bilifo.a3dtest.bean;

import com.threed.jpct.Object3D;

public class Model3D {
    Object3D obj;

    String modelPath;
    String texturePath;

    String modeName;
    Suffix modelSuffiy;

    /**
     * 后缀
     */
    public enum Suffix {
        _3DS, _OBJ, _DEA
    }


    public Model3D() {

    }

    public Model3D(Object3D object) {
        obj = object;
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


    public String getModeName() {
        return this.modeName;
    }

    public void setModeName(String name) {
        this.modeName = name;
    }

    /**
     * 获得后缀
     */
    public Suffix getModelSuffiy() {
        return this.modelSuffiy;
    }

    public void setModelSuffiy(Suffix modelSuffiy) {
        this.modelSuffiy = modelSuffiy;
    }

    public void setModelSuffiy(String suffiyStr) {
        if (suffiyStr.equals(".3ds") || suffiyStr.equals("3ds")) {
            this.modelSuffiy = Suffix._3DS;
        } else if (suffiyStr.equals(".dea") || suffiyStr.equals("dea")) {
            this.modelSuffiy = Suffix._DEA;
        } else if (suffiyStr.equals(".obj") || suffiyStr.equals("obj")) {
            this.modelSuffiy = Suffix._OBJ;
        } else {

        }
    }

    public String getModelSuffiyString() {
        if (this.modelSuffiy == Suffix._3DS) {
            return ".3ds";
        } else if (this.modelSuffiy == Suffix._DEA) {
            return ".dea";
        } else if (this.modelSuffiy == Suffix._OBJ) {
            return ".obj";
        } else {
            return null;
        }
    }

//    //是否显示模型
//    public void showModel(boolean isShowModel) {
//        if (obj != null) {
//            if (isShowModel) {
//                obj.build();
//            } else {
//
//            }
//        }
//    }

    //解析inputstream为object3D对象
}
