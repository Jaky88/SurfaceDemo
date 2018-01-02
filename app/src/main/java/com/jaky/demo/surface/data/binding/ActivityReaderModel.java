package com.jaky.demo.surface.data.binding;

import android.content.Context;

import com.jaky.demo.surface.data.rx.ObservableFactory;
import com.jaky.demo.surface.data.rx.ObserverFactory;

import java.io.File;

import io.reactivex.schedulers.Schedulers;

/**
 * Created by jaky on 2017/12/22 0022.
 */

public class ActivityReaderModel {

    private static final String TAG = ActivityReaderModel.class.getSimpleName();

    private Context context;


    public ActivityReaderModel(Context context) {
        this.context = context;
    }

    public void showCover(File file) {
        if (!file.exists()) {
//            ObserverFactory.callback.showTost("File is not exists!");
            return;
        }
        ObservableFactory.getOpenBookObservable(file).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe(ObserverFactory.getUpdateSurfaceObserver());
    }
}
