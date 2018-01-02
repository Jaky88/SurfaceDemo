package com.jaky.demo.surface.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceView;

/**
 * Created by jaky on 2018/1/2 0002.
 */

public class ReaderView extends SurfaceView {

    public ReaderView(Context context) {
        this(context, null);
    }

    public ReaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
