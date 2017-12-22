package com.jaky.demo.surface;


import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.jaky.demo.surface.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private SurfaceHolder holder;
    private int screenWidth;
    private int screenHeight;
    private int surfaceHeight;
    private SurfaceView mSurfaceView;
    private ActivityMainBinding bindingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindingView = DataBindingUtil.setContentView(this, R.layout.activity_main);
        MainActivityModel mainModel = new MainActivityModel(MainActivity.this, callback);
        bindingView.setMainModel(mainModel);
        initView();
        initData();
    }

    private void initData() {
        Display d = getWindow().getWindowManager().getDefaultDisplay();
        screenWidth = d.getWidth();
        screenHeight = d.getHeight();
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
        surfaceHeight = mSurfaceView.getHeight();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    private SurfaceCallback callback =  new SurfaceCallback(){

        @Override
        public void drawBitmap(Bitmap bitmap) {
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(1f);
            paint.setStyle(Paint.Style.STROKE);
            Canvas canvas = holder.lockCanvas();
            if (canvas != null && bitmap != null && !bitmap.isRecycled()) {
                canvas.drawColor(Color.WHITE);
                Rect srcArea = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
                Rect destArea = new Rect(0, 0, screenWidth, surfaceHeight);
                canvas.drawBitmap(bitmap, srcArea, destArea, paint);
                holder.unlockCanvasAndPost(canvas);
            }
        }
    };
}
