package com.jaky.demo.surface.data.book;

import android.content.Context;
import android.graphics.Rect;

import com.jaky.mupdf.core.MuPDFCore;

/**
 * Created by Jack on 2017/12/31.
 */

public class PdfBook extends Book {


    private MuPDFCore core = null;

    public PdfBook(Rect rect) {
        super(rect);
    }


    @Override
    public boolean open(Context context, String path) {

        try {
            core = new MuPDFCore(context, path);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        setTotalPageNum(core.countPages());
        core.drawPage(page.getBitmap(), page.getPageNum(),
                page.getWidth(), page.getHeight(),
                page.getLeft(), page.getTop(),
                page.getWidth(), page.getHeight(),
                core.new Cookie());
        return true;
    }

    @Override
    public boolean close() {
        return false;
    }

    @Override
    public boolean gotoPage(Page page) {
        core.drawPage(page.getBitmap(), page.getPageNum(),
                page.getWidth(), page.getHeight(),
                page.getLeft(), page.getTop(),
                page.getWidth(), page.getHeight(),
                core.new Cookie());
        return false;
    }


    @Override
    public void searchPage(Page page) {

    }

}
