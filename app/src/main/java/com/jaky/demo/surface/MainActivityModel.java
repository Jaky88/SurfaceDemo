package com.jaky.demo.surface;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

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

/**
 * Created by jaky on 2017/12/22 0022.
 */

public class MainActivityModel {
    
    private static final String FILE_NAME = "test.epub";
    private static final String SERVICE_PKG_NAME = "com.onyx.kreader";
    private static final String SERVICE_CLASS_NAME = "com.onyx.kreader.ui.ReaderMetadataService";

    private Context context;
    private SurfaceCallback callback;

    public MainActivityModel(Context context, SurfaceCallback callback) {
        this.context = context;
        this.callback = callback;
    }

    public void onExtractMetadata(View view) {
        mExtractMetaData.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void onGetThumbnail(View view) {
        mLoadCover.subscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe(mDrawCover);
    }

    Observable mExtractMetaData = new Observable() {
        @Override
        protected void subscribeActual(Observer observer) {
            File file = new File("/sdcard/" + FILE_NAME);
            if (!extractMetadata(context, file)) {}
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
            callback.drawBitmap(bitmap);
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
        Cursor cursor = context.getContentResolver().query(OnyxMetadata.CONTENT_URI, null, null, null, null);
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
            bitmap = loadCoverImpl(context, OnyxThumbnail.ThumbnailKind.Original, item.getMD5());
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
