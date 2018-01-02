package com.jaky.demo.surface.data.book;

import android.content.Context;
import android.graphics.Rect;

/**
 * Created by Jack on 2017/12/31.
 */

public class EpubBook extends Book{


    public EpubBook(Rect rect) {
        super(rect);
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
    public void searchPage(Page page) {

    }
}
