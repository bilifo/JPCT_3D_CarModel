package com.setproject.bilifo.a3dtest.utils;

import android.util.Log;

import com.setproject.bilifo.a3dtest.bean.Model3D;
import com.threed.jpct.Loader;
import com.threed.jpct.Matrix;
import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.Texture;

import java.io.InputStream;

public class ModelUtils {
    /**
     * 根据后缀来加载不同的模型文件为Object3D
     */
    public static Object3D createModelObject3D(Model3D.Suffix suffix, InputStream stream) {
        Object3D[] obj = null;
        if (suffix != null) {
            if (suffix == Model3D.Suffix._3DS) {
                obj = Loader.load3DS(stream, 1f);
            } else if (suffix == Model3D.Suffix._DEA) {
//            obj= Loader.load(stream,0.5f);
            } else if (suffix == Model3D.Suffix._OBJ) {
                obj = Loader.loadOBJ(stream, null, 1f);
            }
        }
        Object3D o3d = new Object3D(0);
        Object3D temp = null;
        for (int i = 0; i < obj.length; i++) {
            temp = obj[i];
            o3d = Object3D.mergeObjects(o3d, temp);
        }
        obj = null;
        o3d.setCenter(SimpleVector.ORIGIN);
        o3d.rotateX((float) (-.5 * Math.PI));
        o3d.rotateMesh();
        o3d.setRotationMatrix(new Matrix());
        return o3d;
    }

    /**
     * 根据路径加载不同存放路径的模型文件
     */
    public static InputStream loadFileInputStream(String filepath) {
        if (filepath.indexOf("assets") != -1) {

        } else if (filepath.indexOf("assets") != -1) {

        }
        return null;
    }

    /**
     * 解析纹理的inputstream
     *
     * @param stream
     * @return
     */
    public static Texture loadTexture(InputStream stream) {
        Texture texture = new Texture(stream);
        return texture;
    }

    /**
     * 获得文件后缀
     *
     * @param filePath
     * @return
     */
    public static String getFileSuffiyStr(String filePath) {
        String tempStr = filePath.trim();
        int lastIndex = tempStr.lastIndexOf(".");
        String suffiy = null;
        try {
            suffiy = tempStr.substring(lastIndex, tempStr.length());
        } catch (Exception e) {
            new Throwable("没有后缀");
        }
        Log.d("pjl++", "suffiy:" + suffiy);
        tempStr = null;
        return suffiy;
    }

    /**
     * 根据路径获得后缀类型
     * @param filePath
     * @return
     */
    public static Model3D.Suffix getFileSuffiy(String filePath) {
        Model3D.Suffix suffix;
        if (filePath.equals(".dea") || filePath.equals("dea")) {
            return Model3D.Suffix._DEA;
        } else if (filePath.equals(".obj") || filePath.equals("obj")) {
            return Model3D.Suffix._OBJ;
        } else if (filePath.equals(".3ds") || filePath.equals("3ds")) {
            return Model3D.Suffix._3DS;
        }
        return null;
    }

    /**
     * 获得文件名称
     *
     * @param filePath
     * @return
     */
    public static String getFileNameStr(String filePath) {
        String tempStr = filePath.trim();
        int lastIndex = tempStr.lastIndexOf("/");
        String suffiy = null;
        try {
            suffiy = tempStr.substring(lastIndex + 1, tempStr.length());
        } catch (Exception e) {
            new Throwable("没有后缀");
        }
        Log.d("pjl++", "suffiy:" + suffiy);
        tempStr = null;
        return suffiy;
    }

}
