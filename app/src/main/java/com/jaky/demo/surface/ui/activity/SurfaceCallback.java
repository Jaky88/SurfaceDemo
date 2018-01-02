package com.jaky.demo.surface.ui.activity;

import android.graphics.Bitmap;

/**
 * Created by jaky on 2017/12/22 0022.
 */

public interface SurfaceCallback {
    void updateSurface(Bitmap bitmap);
    void showTost(String msg);
}
