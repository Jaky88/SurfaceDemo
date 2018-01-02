package com.jaky.demo.surface.ui.activity;


import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.widget.Toast;

import com.jaky.demo.surface.R;
import com.jaky.demo.surface.data.binding.ActivityReaderModel;
import com.jaky.demo.surface.data.book.Reader;
import com.jaky.demo.surface.data.rx.ObservableFactory;
import com.jaky.demo.surface.data.rx.ObserverFactory;
import com.jaky.demo.surface.databinding.ActivityReaderBinding;
import com.jaky.demo.surface.ui.handler.HandlerManager;

import java.io.File;

public class ReaderActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private SurfaceHolder holder;
    private ActivityReaderBinding bindingView;
    private ActivityReaderModel mainModel;
    public static final String TAG = ReaderActivity.class.getSimpleName();
    public static final String TEST_FILE = "/sdcard/test.pdf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindingView = DataBindingUtil.setContentView(this, R.layout.activity_reader);
        mainModel = new ActivityReaderModel(ReaderActivity.this);
        bindingView.setMainModel(mainModel);
        initView();
    }

    private void initView() {
        holder = bindingView.readerView.getHolder();
        holder.addCallback(this);
        ObserverFactory.init(this, callback);
        ObservableFactory.init(this);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Reader.init(new Rect(bindingView.readerView.getLeft(),
                bindingView.readerView.getTop(),
                bindingView.readerView.getLeft() + width,
                bindingView.readerView.getTop() + height)
        );
        mainModel.showCover(new File(TEST_FILE));
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
                canvas.drawBitmap(bitmap, Reader.getVisableRect().left, Reader.getVisableRect().top, paint);
                holder.unlockCanvasAndPost(canvas);
            }
        }

        @Override
        public void showTost(String msg) {
            Toast.makeText(ReaderActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return HandlerManager.getManager().getCurrentHandler().onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return HandlerManager.getManager().getCurrentHandler().onKeyUp(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Reader.closeBook();
    }
}
