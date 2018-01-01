package com.jaky.demo.surface.data.core;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by Jack on 2017/12/31.
 */

public class EpubBook extends Book{

    public EpubBook(int visableWidth, int visableHeight) {
        super(visableWidth, visableHeight);
    }

    @Override
    public boolean open(Context context, String path) {
        return false;
    }

    @Override
    public boolean close() {
        return false;
    }

    @Override
    public boolean gotoPage(Page page) {
        return false;
    }

    @Override
    public int getTotalPage() {
        return 0;
    }

    @Override
    public void searchPage(Page page) {

    }
}
