package com.setproject.bilifo.a3dtest;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.setproject.bilifo.a3dtest.model.Model;

public class MyGLSurfaceView extends GLSurfaceView {
    @NonNull
    private MyRenderer renderer;
    private AsyncTask task;
    private ProgressDialog mProgressDialog;

    MyRenderer.onObject3DChangeState listen = new MyRenderer.onObject3DChangeState() {

        @Override
        public boolean scale() {
            if (scaleValue != 0) {
                renderer.scale(scaleValue);
                scaleValue = 0;
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean rotateY() {
            if (turnX != 0) {
                renderer.rotateY(turnX);
                turnX = 0;
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean rotateX() {
            if (turnY != 0) {
                renderer.rotateX(turnY);
                turnY = 0;
                return true;
            } else {
                return false;
            }
        }
    };
    private Context mContext;

    public MyGLSurfaceView(Context context, @Nullable Model model) {
        super(context);
        this.mContext = context;
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        //创建一个OpenGL ES 2.0 context
        setEGLContextClientVersion(2);
        setZOrderOnTop(true);

        renderer = new MyRenderer(mContext, model);
        setRenderer(renderer);

    }

    float downX, downY, moveX, moveY;
    float turnX, turnY;
    float scaleValue, afterScaleValue, beforeScaleValue;
    int touchPoint = 0;
    boolean touchEventUsed = false;//是否被双指事件占用

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Log.d("pjl", "KEYCODE_BACK");
            if (task.isCancelled()) {
                Log.d("pjl", "task.isCancelled");
                mProgressDialog.dismiss();
                task.cancel(true);
                renderer.unregisterListener();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //必须加上MotionEvent.ACTION_MASK,双指缩放才会正确进入对应action事件
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
//                Log.d("pjl++","ACTION_DOWN");
                downX = event.getX();
                downY = event.getY();
                touchPoint = 1;
                return true;
            case MotionEvent.ACTION_POINTER_DOWN:
//                Log.d("pjl++","ACTION_POINTER_DOWN");
                //已有一个按压点的情况下,再按下
                beforeScaleValue = spacing(event);//后
                touchPoint++;
                touchEventUsed = true;
                return true;
            case MotionEvent.ACTION_MOVE:
                if (touchPoint == 2) {
//                    Log.d("pjl++","ACTION_MOVE---2");
                    if (listen != null) {
                        afterScaleValue = spacing(event);
                        scaleValue = afterScaleValue / beforeScaleValue;
                        if (scaleValue != 0) {
                            listen.scale();
                        }
                        beforeScaleValue = afterScaleValue;
                    }
                } else {
                    if (touchEventUsed == false) {
//                        Log.d("pjl++", "ACTION_MOVE---1");
                        moveX = event.getX();
                        moveY = event.getY();
                        turnX = (moveX - downX) / -100f;
                        turnY = (moveY - downY) / -100f;
                        if (listen != null) {
                            listen.rotateX();
                            listen.rotateY();
                        }
                        downX = moveX;
                        downY = moveY;
                    }
                }
                return true;
            case MotionEvent.ACTION_UP:
//                Log.d("pjl++","ACTION_UP");
                downX = -1;
                downY = -1;
                turnX = 0;
                turnY = 0;
                touchPoint = 0;
                touchEventUsed = false;
                return true;
            case MotionEvent.ACTION_POINTER_UP:
//                Log.d("pjl++","ACTION_POINTER_UP");
                beforeScaleValue = 0;
                touchPoint--;
                return true;
        }
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return super.onTouchEvent(event);
    }

    public void loadModel(final Model model) {

        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage("加载模型中");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);

        task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                loadModelPolicy(model);
                renderer.registerListener(listen);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                mProgressDialog.dismiss();

            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mProgressDialog.show();
            }
        };
        task.execute();
    }

    /**
     * 加载模型的策略,区分模型是从asset,raw,sdcard等地方来的
     */
    private void loadModelPolicy(Model model) {
        if (model == null) {
            renderer.create3DSObjModels("t458", R.drawable.dragon_ground_color);
        }else{
            renderer.create3DSObjModels(model);
        }
    }


    /**
     * 计算两个点的距离 * * @param event * @return
     */
    private float spacing(MotionEvent event) {
        if (event.getPointerCount() == 2) {
            float x = event.getX(0) - event.getX(1);
            float y = event.getY(0) - event.getY(1);
            return (float) Math.sqrt(x * x + y * y);
        } else {
            return 0;
        }
    }
}
