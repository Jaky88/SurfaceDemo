package com.jaky.demo.surface.data.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.jaky.demo.surface.data.config.ReaderConfig;
import com.jaky.utils.FileUtils;

import java.io.File;

/**
 * Created by jaky on 2017/12/29 0029.
 */

public abstract class Book {

    protected Page page;

    public Book(int visableWidth, int visableHeight) {
        Bitmap bitmap = Bitmap.createBitmap(visableWidth, visableHeight, Bitmap.Config.ARGB_8888);
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888,true);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        page = new Page(bitmap, 2, bitmap.getWidth(), bitmap.getHeight());
    }

    public abstract boolean open(Context context, String path);
    public abstract boolean close();
    public abstract boolean gotoPage (Page page);
    public Page getCurrentPage (){
        return page;
    }
    public abstract int getTotalPage ();
    public abstract void searchPage (Page page);

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}
