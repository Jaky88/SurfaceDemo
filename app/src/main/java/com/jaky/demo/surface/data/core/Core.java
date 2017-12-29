package com.jaky.demo.surface.data.core;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by jaky on 2017/12/29 0029.
 */

public class Core {

    private static Core core = null;
    private final Paint paint;
    private Canvas canvas;

    public static Core init() {
        if (core == null) {
            core = new Core();
        }
        return core;
    }

    private Core() {
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
    }

    public void drawBitmap(Bitmap bitmap) {
        canvas = new Canvas(bitmap);
        String str = "这是一个Demo!";
        paint.setTextSize(50);
        canvas.drawText(str, 40f, 40f, paint);
    }
}
