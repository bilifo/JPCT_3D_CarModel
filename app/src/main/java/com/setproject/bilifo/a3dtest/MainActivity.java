package com.setproject.bilifo.a3dtest;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContentResolverCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.setproject.bilifo.a3dtest.bean.Model3D;
import com.setproject.bilifo.a3dtest.utils.ModelUtils;
import com.threed.jpct.Object3D;

import java.io.Closeable;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    MyGLSurfaceView surfaceView;
    FrameLayout container_view;
    ImageView imag;

    Model3D model3D;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        container_view = findViewById(R.id.container_view);

        imag = new ImageView(this);
        imag.setBackgroundResource(R.mipmap.ic_launcher);
        container_view.addView(imag);

        surfaceView = new MyGLSurfaceView(MainActivity.this, null);
        surfaceView.setAlpha(0.5f);

        container_view.addView(surfaceView);

        model3D = new Model3D();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, "选择背景图");
        menu.add(0, 1, 1, "选择模型");
        menu.add(0, 2, 2, "演示模型");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                beginOpenModel();
                break;
            case 1:
                beginOpenModel();

                break;
            case 2:
                surfaceView.loadModel(null);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        surfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        surfaceView.onPause();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_MOVE:

                break;
        }

        return super.onTouchEvent(event);
    }

    /**
     * 原生文件浏览器
     */
    private void beginOpenModel() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        startActivityForResult(intent, OPEN_DOCUMENT_REQUEST);
    }

    private static final int OPEN_DOCUMENT_REQUEST = 101;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == OPEN_DOCUMENT_REQUEST && resultCode == RESULT_OK && resultData.getData() != null) {
            Uri uri = resultData.getData();

            grantUriPermission(getPackageName(), uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

            InputStream stream = null;
            try {
                ContentResolver cr = getApplicationContext().getContentResolver();
                String fileName = getFileName(cr, uri);
                Log.d("pjl", "path:" + fileName);
                stream = cr.openInputStream(uri);
                Log.d("pjl", "stream:" + stream);
                if(stream!=null) {
                    Log.d("pjl", "stream:" + "  !=null");
                    Object3D obj = ModelUtils.createModelObject3D(ModelUtils.getFileSuffiy(ModelUtils.getFileSuffiyStr(fileName)), stream);
                    model3D.setObj(obj);
//                    model3D.showModel(true);
                    Log.d("pjl", "model3D:" + "  showModel");
                    surfaceView.loadModel(model3D);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeSilently(stream);
            }
        }
    }

    public static void closeSilently(@Nullable Closeable c) {
        try {
            if (c != null) {
                c.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    private String getFileName(@NonNull ContentResolver cr, @NonNull Uri uri) {
        if ("content".equals(uri.getScheme())) {
            String[] projection = {MediaStore.MediaColumns.DISPLAY_NAME};
            Cursor metaCursor = ContentResolverCompat.query(cr, uri, projection, null, null, null, null);
            if (metaCursor != null) {
                try {
                    if (metaCursor.moveToFirst()) {
                        return metaCursor.getString(0);
                    }
                } finally {
                    metaCursor.close();
                }
            }
        }
        return uri.getLastPathSegment();
    }


}
