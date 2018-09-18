package com.setproject.bilifo.a3dtest;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.support.annotation.NonNull;
import android.util.Log;

import com.setproject.bilifo.a3dtest.model.Model;
import com.threed.jpct.Camera;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.GLSLShadowInjector;
import com.threed.jpct.Light;
import com.threed.jpct.Loader;
import com.threed.jpct.Matrix;
import com.threed.jpct.Object3D;
import com.threed.jpct.Projector;
import com.threed.jpct.RGBColor;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.World;
import com.threed.jpct.util.ExtendedPrimitives;
import com.threed.jpct.util.MemoryHelper;
import com.threed.jpct.util.ShadowHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyRenderer implements GLSurfaceView.Renderer {
    @NonNull
    private Model mmodel;

    private FrameBuffer fb = null;
    private World world = null;
    private RGBColor back = new RGBColor(50, 50, 100);

    private float xpos = -1;
    private float ypos = -1;

    private Object3D cube = null;
    private Object3D obj = null;

    private Object3D he = null;
    private int fps = 0;

    private Light sun = null;
    private ShadowHelper sh;

    Context mcontext;

    public MyRenderer(Context context, Model model) {
        mmodel = model;
        mcontext = context;
    }


    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int w, int h) {
        // Renew the frame buffer
        Log.i("HelloWorld", "Init buffer");
        if (fb != null) {
            fb.dispose();
        }
        fb = new FrameBuffer(w, h);
        //缺少该句,导致onDrawFrame时world.draw没有东西
        fb.setVirtualDimensions(fb.getWidth(), fb.getHeight());


    }

    boolean firstLoad = true;

    @Override
    public void onDrawFrame(GL10 gl) {
        if (obj != null && firstLoad) {
            world = new World();
            world.setAmbientLight(120, 120, 120);

            sun = new Light(world);
            sun.setIntensity(120, 0, 0);


            world.addObject(obj);

            cube = ExtendedPrimitives.createPlane(15, 2);
            obj.addChild(cube);
            cube.translate(0, 3.5f, 0);

            cube.setTransparency(0);
            cube.setTransparencyMode(Object3D.TRANSPARENCY_MODE_DEFAULT);
            world.addObject(cube);
            world.buildAllObjects();

            Camera cam = world.getCamera();
            cam.moveCamera(Camera.CAMERA_MOVEOUT, 50);
            cam.lookAt(obj.getTransformedCenter());

            SimpleVector sv = new SimpleVector();
            sv.set(obj.getTransformedCenter());
            sv.x += 70;
            sv.y -= 100;
            sv.z -= 70;
            sun.setPosition(sv);

            Projector projector = new Projector();
            projector.setClippingPlanes(1f, 300f);
            projector.setFOVLimits(0, 20);
            float fov = projector.convertDEGAngleIntoFOV(90);
            projector.setFOV(fov);
            projector.setYFOV(fov);
            projector.setPosition(sun.getPosition());
            projector.lookAt(obj.getTransformedCenter());

            ShadowHelper.setShadowMode(GLSLShadowInjector.PCF_FILTERED_SHADOWS_WITH_EDGE_SMOOTHING);

            sh = new ShadowHelper(fb, projector, 1024);
            sh.setLightSource(projector);
            sh.setAmbientLight(new RGBColor(50, 50, 50));
            sh.addCaster(obj);
            sh.addReceiver(cube);
            sh.setFilterSize(1);

            MemoryHelper.compact();
            firstLoad = false;
        }
        if (world != null) {
            if (listen != null) {
                listen.rotateX();
                listen.rotateY();
                listen.scale();
            }
            sh.updateShadowMap(fb, world);
// 用给定的颜色(backColor)清除FrameBuffer
            fb.clear();
            // 变换和灯光所有多边形
            world.renderScene(fb);
            // 绘制
            world.draw(fb);
            // 渲染图像显示
            fb.display();
        }

    }

    public void scale(float value) {
        if (listen != null) {
            obj.scale(value);
        }
    }

    public void rotateY(float value) {
        if (listen != null) {
            obj.rotateY(value);
        }
    }

    public void rotateX(float value) {
//        if (listen != null) {
//        obj.rotateX(value);
//        }
    }

    private onObject3DChangeState listen = null;

    public interface onObject3DChangeState {
        public boolean scale();

        public boolean rotateY();

        public boolean rotateX();
    }

    public void registerListener(onObject3DChangeState listener) {
        listen = listener;
    }

    public void unregisterListener() {
        listen = null;
    }

    private Object3D loadAssets3DSModels(String fileName, String texture, float scale) {
//        try {
//            InputStream stream = mcontext.getResources().getAssets().open(fileName);
//            Object3D[] model = Loader.load3DS(stream, scale);
//            Object3D temp = model[0];
//            if (texture != null && (!TextUtils.isEmpty(texture))) {
//                temp.setTexture(texture);
//            }
//            temp.strip();
//            temp.build();
//
//            return temp;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        InputStream stream = null;
        try {
            stream = mcontext.getResources().getAssets().open(fileName);
            Object3D[] model = Loader.load3DS(stream, scale);
            Object3D o3d = new Object3D(0);
            Object3D temp = null;
            for (int i = 0; i < model.length; i++) {
                temp = model[i];
                temp.setCenter(SimpleVector.ORIGIN);
                temp.rotateX((float) (-.5 * Math.PI));
                temp.rotateMesh();
                temp.setRotationMatrix(new Matrix());
                o3d = Object3D.mergeObjects(o3d, temp);
                o3d.build();
            }
            return o3d;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Object3D loadSDcard3DSModels(Model sdmodel, float scale) {

        InputStream stream = null;
        try {
            stream = new FileInputStream(new File(sdmodel.getModelPath()));
            Object3D[] model = Loader.load3DS(stream, scale);
            Object3D o3d = new Object3D(0);
            Object3D temp = null;
            for (int i = 0; i < model.length; i++) {
                temp = model[i];
                temp.setCenter(SimpleVector.ORIGIN);
                temp.rotateX((float) (-.5 * Math.PI));
                temp.rotateMesh();
                temp.setRotationMatrix(new Matrix());
                o3d = Object3D.mergeObjects(o3d, temp);
                o3d.build();
            }
            return o3d;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void create3DSObjModels(String filePathName, int drawableID) {
//        Texture texture2 = new Texture(BitmapHelper.rescale(BitmapHelper.convert(mcontext.getResources().getDrawable(drawableID)), 512, 512));
//        TextureManager.getInstance().addTexture("texture2", texture2);
        obj = loadAssets3DSModels(filePathName + ".3ds", null, 5f);
        obj.setCulling(true);
        obj.setRotationPivot(SimpleVector.ORIGIN);
        obj.setLighting(Object3D.LIGHTING_ALL_ENABLED);
        obj.translate(0, 20, 0);

    }

    public void create3DSObjModels(Model model) {
        if (model.getObj() == null) {
            obj =loadSDcard3DSModels(model,5f);
        } else {
            obj = model.getObj();
        }
        obj.setCulling(true);
        obj.setRotationPivot(SimpleVector.ORIGIN);
        obj.setLighting(Object3D.LIGHTING_ALL_ENABLED);
        obj.translate(0, 20, 0);
    }

    public void loadModel(Model model) {

    }
}
