package com.jaky.demo.surface.data.model;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.jaky.demo.surface.R;
import com.jaky.demo.surface.data.core.Book;
import com.jaky.demo.surface.data.core.Page;
import com.jaky.demo.surface.data.core.Reader;
import com.jaky.demo.surface.ui.ReaderActivity;
import com.jaky.demo.surface.ui.SurfaceCallback;
import com.onyx.android.sdk.data.cms.OnyxCmsCenter;
import com.onyx.android.sdk.data.cms.OnyxThumbnail;
import com.onyx.android.sdk.data.util.FileUtil;
import com.onyx.android.sdk.data.util.RefValue;
import com.onyx.android.sdk.reader.IMetadataService;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by jaky on 2017/12/22 0022.
 */

public class ActivityReaderModel {

    private static final String TAG = ActivityReaderModel.class.getSimpleName();
    private static final String FILE_NAME = "test.pdf";
    private static final String SERVICE_PKG_NAME = "com.onyx.kreader";
    private static final String SERVICE_CLASS_NAME = "com.onyx.kreader.ui.ReaderMetadataService";

    private Context context;
    private SurfaceCallback callback;


    public ActivityReaderModel(Context context, SurfaceCallback callback) {
        this.context = context;
        this.callback = callback;
    }

    public void onGetThumbnail(View view) {
        File file = new File("/sdcard/" + FILE_NAME);
//        showCover(file);
    }

    public void showCover(File file) {
        File file1 = new File("/sdcard/" + FILE_NAME);
        file = file1;
        if (!file.exists()) {
            return;
        }
        getLoadCoverObservable(file).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe(getDrawCoverObserver());
    }

    private Observable<Bitmap> getLoadCoverObservable(final File file) {
        return Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter e) throws Exception {
//                Bitmap bitmap = loadCoverImpl(context, OnyxThumbnail.ThumbnailKind.Original, getFileMD5(file));
//                if (bitmap == null) {
//                    e.onNext(extractMetadata(context, file) ? loadCoverImpl(context, OnyxThumbnail.ThumbnailKind.Original, getFileMD5(file)) : null);
//                } else {
//                    e.onNext(bitmap);
//                }
                Log.d("", "=======getLoadCoverObservable=================");
                if (Reader.openBook(context, file.getAbsolutePath())) {
                    e.onNext(Reader.getBook().getCurrentPage().getBitmap());
                } else {
                    e.onNext(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
                }

                e.onComplete();
            }
        });
    }

    private Observer<Bitmap> getDrawCoverObserver() {
        return new Observer<Bitmap>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Bitmap bitmap) {
                if (bitmap == null) {
                    bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
                }
                bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                processBitmap(bitmap);
                callback.updateSurface(bitmap);
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
    }

    private boolean extractMetadata(Context context, final File file) {
        boolean ret = false;
        if (!file.exists()) {
            return ret;
        }
        final MetadataServiceConnection connection = new MetadataServiceConnection();
        try {
            final Intent intent = new Intent();
            intent.setComponent(new ComponentName(SERVICE_PKG_NAME, SERVICE_CLASS_NAME));
            context.bindService(intent, connection, Context.BIND_AUTO_CREATE | Context.BIND_NOT_FOREGROUND);
            connection.waitUntilConnected();

            IMetadataService extractService = IMetadataService.Stub.asInterface(connection.getRemoteService());
            ret = extractService.extractMetadataAndThumbnail(file.getAbsolutePath(), -1);
            context.unbindService(connection);
        } catch (Exception e) {
            Log.d(TAG, "extractMetadata: " + e.getMessage());
            e.printStackTrace();
        } finally {
            return ret;
        }
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

    @Nullable
    private String getFileMD5(File file) {
        String md5 = null;
        try {
            md5 = FileUtil.computeMD5(file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } finally {
            return md5;
        }
    }

    private void processBitmap(Bitmap bitmap) {
//        core.drawBitmap(bitmap);
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
