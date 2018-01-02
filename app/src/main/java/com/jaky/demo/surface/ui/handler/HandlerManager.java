package com.jaky.demo.surface.ui.handler;

import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jaky on 2018/1/2 0002.
 */

public class HandlerManager {

    public static final String NORMAL_HANDLER = "normal_handler";
    public static final String WORD_SELECTION_HANDLER = "word_selection_handler";
    public static final String SCRIBBLE_HANDLER = "scribble_handler";
    private String currentHandlerName;
    private static HandlerManager manager;
    private PointF touchStartPosition;
    private Map<String, BaseHandler> handlerMap = new ConcurrentHashMap<String, BaseHandler>();


    public static HandlerManager getManager() {
        if (manager == null) {
            manager = new HandlerManager();
        }
        return manager;
    }

    private HandlerManager() {
        initProviderMap();
    }

    private void initProviderMap() {
        handlerMap.put(NORMAL_HANDLER, new NormalHandler());
        handlerMap.put(WORD_SELECTION_HANDLER, new WordSelectionHandler());
        handlerMap.put(SCRIBBLE_HANDLER, new ScribbleHandler());
        setCurrentHandler(NORMAL_HANDLER);
    }

    public BaseHandler getCurrentHandler() {
        return handlerMap.get(currentHandlerName);
    }

    public void setCurrentHandler(String currentHandlerName) {
        this.currentHandlerName = currentHandlerName;
        Log.d("","=========currentHandlerName========="+currentHandlerName);
    }

    public void setTouchStartEvent(MotionEvent event) {
        if (touchStartPosition == null) {
            touchStartPosition = new PointF(event.getX(), event.getY());
        }
    }

    public void resetTouchStartPosition() {
        touchStartPosition = null;
    }
}
