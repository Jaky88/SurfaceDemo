package com.jaky.demo.surface.data.book;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;

import com.jaky.mupdf.core.MuPDFCore;
import com.jaky.utils.FileUtils;

import java.io.File;

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
    public boolean gotoPage(Page page) {
        Log.d("","================gotoPage======================");
        core.drawPage(page.getBitmap(), page.getPageNum(),
                page.getWidth(), page.getHeight(),
                page.getLeft(), page.getTop(),
                page.getWidth(), page.getHeight(),
                core.new Cookie());
        return true;
    }


    @Override
    public void searchPage(Page page) {

    }

}
