package com.jaky.demo.surface.data.rx;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.jaky.demo.surface.R;
import com.jaky.demo.surface.data.book.Reader;
import com.jaky.demo.surface.ui.activity.SurfaceCallback;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * Created by jaky on 2018/1/2 0002.
 */

public class ObservableFactory {

    private static Context context;

    public static void init(Context c) {
        context = c;
    }

    public static Observable<Bitmap> getOpenBookObservable(final File file) {
        return Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter e) throws Exception {
                if (Reader.openBook(context, file.getAbsolutePath())) {
                    e.onNext(Reader.getBook().getCurrentPage().getBitmap());
                } else {
                    e.onNext(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
                }
                e.onComplete();
            }
        });
    }

    public static Observable<Bitmap> getNextPageObservable() {
        return Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter e) throws Exception {
                if (Reader.nextPage()) {
                    e.onNext(Reader.getBook().getCurrentPage().getBitmap());
                }
                e.onComplete();
            }
        });
    }

    public static Observable<Bitmap> getPrePageObservable() {
        return Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter e) throws Exception {
                if (Reader.prePage()) {
                    e.onNext(Reader.getBook().getCurrentPage().getBitmap());
                }
                e.onComplete();
            }
        });
    }
}
