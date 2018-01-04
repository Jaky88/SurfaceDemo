package com.jaky.demo.surface.data.book;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;

import com.jaky.epub.core.EpubCore;

/**
 * Created by Jack on 2017/12/31.
 */

public class EpubBook extends Book{
    private String folder;
    private EpubCore core = null;

    public EpubBook(Rect rect) {
        super(rect);
    }

    @Override
    public boolean open(Context context, String path) {
        folder = "/sdcard/epubtemp";
        try {
            core = new EpubCore(context, path, folder);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        gotoPage(page);
        return false;
    }

    @Override
    public boolean close() {
        return false;
    }

    @Override
    protected Page drawPage(Page page) {
        core.drawPage(page.getBitmap(), page.getPageNum(),
                page.getWidth(), page.getHeight(),
                page.getLeft(), page.getTop(),
                page.getWidth(), page.getHeight());
        return page;
    }

    @Override
    public void searchPage(Page page) {

    }
}
