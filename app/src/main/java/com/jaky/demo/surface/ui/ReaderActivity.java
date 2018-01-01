package com.jaky.demo.surface.ui;


import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewTreeObserver;

import com.jaky.demo.surface.data.core.Page;
import com.jaky.demo.surface.data.core.Reader;
import com.jaky.demo.surface.data.model.ActivityReaderModel;
import com.jaky.demo.surface.R;
import com.jaky.demo.surface.databinding.ActivityReaderBinding;
import com.jaky.image.core.ImageBlurManager;

import java.io.File;

public class ReaderActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private SurfaceHolder holder;
    private SurfaceView mSurfaceView;
    private ActivityReaderBinding bindingView;
    private ActivityReaderModel mainModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindingView = DataBindingUtil.setContentView(this, R.layout.activity_reader);
        mainModel = new ActivityReaderModel(ReaderActivity.this, callback);
        bindingView.setMainModel(mainModel);
        initView();
    }

    private void initView() {
        mSurfaceView = (SurfaceView) findViewById(R.id.sv_content);
        holder = mSurfaceView.getHolder();
        holder.addCallback(this);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Reader.init(width, height);
        mainModel.showCover(new File("/sdcard/test.pdf"));
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    private SurfaceCallback callback = new SurfaceCallback() {

        @Override
        public void updateSurface(Bitmap bitmap) {
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            Canvas canvas = holder.lockCanvas();
            canvas.drawColor(Color.WHITE);
            if (canvas != null && bitmap != null && !bitmap.isRecycled()) {
                Rect srcArea = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
                Rect destArea = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
                bitmap = ImageBlurManager.doBlurJniArray(bitmap, ImageBlurManager.BLUR_RADIUS, false);
                canvas.drawBitmap(bitmap, srcArea, destArea, paint);
                holder.unlockCanvasAndPost(canvas);
            }
        }
    };
}
