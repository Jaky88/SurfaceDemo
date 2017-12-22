package com.jaky.demo.surface;


import android.content.Context;
import android.content.Intent;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Bitmap;

import com.onyx.android.sdk.data.cms.OnyxCmsCenter;
import com.onyx.android.sdk.data.cms.OnyxMetadata;
import com.onyx.android.sdk.data.cms.OnyxThumbnail;
import com.onyx.android.sdk.data.util.RefValue;
import com.onyx.android.sdk.reader.IMetadataService;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String FILE_NAME = "test.epub";
    private static final String SERVICE_PKG_NAME = "com.onyx.kreader";
    private static final String SERVICE_CLASS_NAME = "com.onyx.kreader.ui.ReaderMetadataService";
    private SurfaceHolder holder;
    private int screenWidth;
    private int screenHeight;
    private int surfaceHeight;
    protected static OnyxThumbnail.ThumbnailKind defaultThumbnailKind = OnyxThumbnail.ThumbnailKind.Original;
    private SurfaceView mSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    public void onExtractMetadata(View v) {
        mExtractMetaData.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void onGetThumbnail(View v) {
        mLoadCover.subscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe(mDrawCover);
    }

    Observable mExtractMetaData = new Observable() {
        @Override
        protected void subscribeActual(Observer observer) {
            File file = new File("/sdcard/" + FILE_NAME);
            if (!extractMetadata(MainActivity.this, file)) {
                Log.w(TAG, "===============extract file failed.");
            }
        }
    };

    Observable mLoadCover = Observable.create(new ObservableOnSubscribe<Bitmap>() {
        @Override
        public void subscribe(@NonNull ObservableEmitter<Bitmap> e) throws Exception {
            e.onNext(loadCover());
            e.onComplete();
        }
    });

    Observer<Bitmap> mDrawCover = new Observer<Bitmap>() {
        @Override
        public void onSubscribe(@NonNull Disposable d) {

        }

        @Override
        public void onNext(@NonNull Bitmap bitmap) {
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
            bitmap.recycle();
        }

        @Override
        public void onError(@NonNull Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onComplete() {

        }
    };

    private boolean extractMetadata(Context context, final File file) {
        final MetadataServiceConnection connection = new MetadataServiceConnection();
        boolean ret = false;

        try {
            final Intent intent = new Intent();
            intent.setComponent(new ComponentName(SERVICE_PKG_NAME, SERVICE_CLASS_NAME));
            context.bindService(intent, connection, Context.BIND_AUTO_CREATE | Context.BIND_NOT_FOREGROUND);
            connection.waitUntilConnected();

            IMetadataService extractService = IMetadataService.Stub.asInterface(connection.getRemoteService());
            ret = extractService.extractMetadataAndThumbnail(file.getAbsolutePath(), -1);
            context.unbindService(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    private Bitmap loadCover() {
        Cursor cursor = getContentResolver().query(OnyxMetadata.CONTENT_URI, null, null, null, null);
        if (cursor == null) {
            return null;
        }
        Bitmap bitmap = null;
        while (cursor.moveToFirst()) {
            OnyxMetadata item = OnyxMetadata.Columns.readColumnData(cursor);
            if (item == null) {
                break;
            }
            if (!FILE_NAME.equals(item.getName())) {
                continue;
            }
            bitmap = loadCoverImpl(MainActivity.this, defaultThumbnailKind, item.getMD5());
            if (bitmap != null) {
                break;
            }
        }

        if (cursor != null) {
            cursor.close();
        }
        return bitmap;
    }

    private Bitmap loadCoverImpl(Context context, OnyxThumbnail.ThumbnailKind thumbnailKind, final String md5) {
        RefValue<Bitmap> bitmapRefValue = new RefValue<Bitmap>();
        if (OnyxCmsCenter.getThumbnailByMD5(context, md5, thumbnailKind, bitmapRefValue)) {
            final Bitmap bitmap = bitmapRefValue.getValue();
            if (bitmap != null && bitmap.getWidth() > 0 && bitmap.getHeight() > 0) {
                return bitmap;
            }
        }
        return null;
    }

    private class MetadataServiceConnection implements ServiceConnection {
        private volatile boolean connected = false;
        private volatile IBinder remoteService;

        public MetadataServiceConnection() {
            super();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            connected = true;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            connected = true;
            remoteService = service;
        }

        public IBinder getRemoteService() {
            return remoteService;
        }

        public void waitUntilConnected() throws InterruptedException {
            while (!connected) {
                Thread.sleep(100);
            }
        }

    }

}
