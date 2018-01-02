package com.jaky.demo.surface.data.book;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Rect;

/**
 * Created by jaky on 2017/12/29 0029.
 */

public abstract class Book {

    protected Page page;
    protected int totalPageNum;

    public Book(Rect rect) {
        if (page == null) {
            Bitmap bitmap = Bitmap.createBitmap(rect.width(), rect.height(), Config.ARGB_8888);
            page = new Page(bitmap, 0, rect);
        }
    }

    public abstract boolean open(Context context, String path);

    public abstract boolean close();

    public abstract boolean gotoPage(Page page);

    public Page getCurrentPage() {
        return page;
    }

    public abstract void searchPage(Page page);

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public int getTotalPageNum() {
        return totalPageNum;
    }

    public void setTotalPageNum(int totalPageNum) {
        this.totalPageNum = totalPageNum;
    }
}
