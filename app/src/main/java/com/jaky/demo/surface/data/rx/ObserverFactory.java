package com.jaky.demo.surface.data.rx;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.jaky.demo.surface.R;
import com.jaky.demo.surface.ui.activity.SurfaceCallback;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by jaky on 2018/1/2 0002.
 */

public class ObserverFactory {

    private static SurfaceCallback callback;
    private static Context context;
    public static void init(Context c, SurfaceCallback cb){
        callback = cb;
        context = c;
    }

    public static  Observer<Bitmap> getUpdateSurfaceObserver() {
        return new Observer<Bitmap>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Bitmap bitmap) {
                Log.d("","================onNext======================");
                if (bitmap == null) {
                    bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
                }
                callback.updateSurface(bitmap);
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
}
