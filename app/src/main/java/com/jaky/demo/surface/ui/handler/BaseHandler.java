package com.jaky.demo.surface.ui.handler;

import android.content.Context;
import android.graphics.Matrix;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.jaky.demo.surface.data.book.Reader;

/**
 * Created by jaky on 2018/1/2 0002.
 */

public class BaseHandler extends GestureDetector.SimpleOnGestureListener implements ScaleGestureDetector.OnScaleGestureListener {
    protected Context context;

    public BaseHandler() {
        super();
    }

    //===================onTouchEvent=======================

    public boolean onTouchEvent(MotionEvent e) {
        return true;
    }

    public boolean onActionUp(MotionEvent event) {
        return true;
    }

    public boolean tryHitTest(float x, float y) {
        return false;
    }

    public boolean onActionCancel(MotionEvent event) {
        return true;
    }

    //===================onKey=======================

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        boolean ret = true;
        switch (keyCode) {
            case KeyEvent.KEYCODE_CLEAR:
                break;
            default:
                ret = false;
                break;
        }
        return ret;
    }

    //===================SimpleOnGestureListener=======================

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if (tryHitTest(e.getX(), e.getY())) {
        } else if (e.getX() > Reader.getVisableRect().width() * 2 / 3) {
            Reader.nextPage();
        } else if (e.getX() < Reader.getVisableRect().width() / 3) {
            Reader.prePage();
        } else {
//            showReaderMenu(readerDataHolder);
        }
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        super.onLongPress(e);
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return super.onScroll(e1, e2, distanceX, distanceY);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if(e1.getX() <= e2.getX()){
            return Reader.prePage();
        } else {
            return Reader.nextPage();
        }
    }

    @Override
    public void onShowPress(MotionEvent e) {
        super.onShowPress(e);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return super.onDown(e);
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return super.onDoubleTap(e);
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return super.onDoubleTapEvent(e);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return super.onSingleTapConfirmed(e);
    }

    @Override
    public boolean onContextClick(MotionEvent e) {
        return super.onContextClick(e);
    }

    //===================OnScaleGestureListener=======================

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float focusX = detector.getFocusX();
        float focusY = detector.getFocusY();
        Matrix transformationMatrix = new Matrix();
        Matrix scaleMatrix = new Matrix();
        return false;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return false;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }
}
