package com.jaky.demo.surface.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceView;

import com.jaky.demo.surface.data.book.Page;
import com.jaky.demo.surface.data.book.Reader;
import com.jaky.demo.surface.ui.handler.HandlerManager;

/**
 * Created by jaky on 2018/1/2 0002.
 */

public class ReaderView extends SurfaceView {

    private GestureDetector gestureDetector;
    private ScaleGestureDetector scaleDetector;

    public ReaderView(Context context) {
        this(context, null);
    }

    public ReaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        gestureDetector = new GestureDetector(getContext(), new MyOnGestureListener());
        scaleDetector = new ScaleGestureDetector(getContext(), new MyScaleGestureListener());
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        HandlerManager.getManager().setTouchStartEvent(event);

        scaleDetector.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);

        if (event.getAction() == MotionEvent.ACTION_UP) {
            HandlerManager.getManager().getCurrentHandler().onActionUp(event);
            HandlerManager.getManager().resetTouchStartPosition();
        }
        if (event.getAction() == MotionEvent.ACTION_CANCEL) {
            HandlerManager.getManager().getCurrentHandler().onActionCancel(event);
            HandlerManager.getManager().resetTouchStartPosition();
        }
        HandlerManager.getManager().getCurrentHandler().onTouchEvent(event);

        return true;
    }

    class MyOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        public MyOnGestureListener() {
            super();
    }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return HandlerManager.getManager().getCurrentHandler().onSingleTapUp(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            HandlerManager.getManager().getCurrentHandler().onLongPress(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return HandlerManager.getManager().getCurrentHandler().onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return HandlerManager.getManager().getCurrentHandler().onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public void onShowPress(MotionEvent e) {
            HandlerManager.getManager().getCurrentHandler().onShowPress(e);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return HandlerManager.getManager().getCurrentHandler().onDown(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return HandlerManager.getManager().getCurrentHandler().onDoubleTap(e);
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return HandlerManager.getManager().getCurrentHandler().onDoubleTapEvent(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return HandlerManager.getManager().getCurrentHandler().onSingleTapConfirmed(e);
        }

        @Override
        public boolean onContextClick(MotionEvent e) {
            return HandlerManager.getManager().getCurrentHandler().onContextClick(e);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.FROYO)
    class MyScaleGestureListener implements ScaleGestureDetector.OnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            return HandlerManager.getManager().getCurrentHandler().onScale(detector);
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return HandlerManager.getManager().getCurrentHandler().onScaleBegin(detector);
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            HandlerManager.getManager().getCurrentHandler().onScaleEnd(detector);
        }
    }

    private void update(Page page){
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        Canvas canvas = getHolder().lockCanvas();
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(page.getBitmap(),
                Reader.getVisableRect().left, Reader.getVisableRect().top, paint);
        getHolder().unlockCanvasAndPost(canvas);
    }
}
