package com.jaky.demo.surface.data.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.jaky.mupdf.core.MuPDFCore;

/**
 * Created by Jack on 2017/12/31.
 */

public class PdfBook extends Book {


    private MuPDFCore core = null;

    public PdfBook(int visableWidth, int visableHeight) {
        super(visableWidth, visableHeight);

    }

    @Override
    public boolean open(Context context, String path) {

        try {
            core = new MuPDFCore(context, path);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        Log.d("", "=======drawPage=================");
        core.drawPage(page.getBitmap(), page.getPageNum(),
                page.getWidth(), page.getHeight(),
                0, 0,
                0, 0,
                core.new Cookie()
        );
        return true;
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

    public void showPage(Bitmap bitmap) {
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(20);
        canvas.drawText("这是一个PdfBook!", 20f, 20f, paint);
    }


}
