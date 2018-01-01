package com.jaky.demo.surface.data.core;

import android.content.Context;
import android.support.annotation.IntDef;
import android.util.Log;
import android.view.WindowManager;

import com.jaky.demo.surface.data.config.ReaderConfig;
import com.jaky.utils.FileUtils;
import com.jaky.utils.StringUtils;

import java.util.List;

/**
 * Created by Jack on 2017/12/31.
 */

public class Reader {

    private static Book book = null;
    private static int visableWidth;
    private static int visableHeight;

    public static void init(int width, int height) {
        visableWidth = width;
        visableHeight = height;
    }

    public static boolean initBook(Context context, String path) {
        if (visableWidth == 0 || visableHeight == 0) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            int width = wm.getDefaultDisplay().getWidth();
            int height = wm.getDefaultDisplay().getHeight();
            visableWidth = width;
            visableHeight = height;
        }
        String ext = FileUtils.getFileExtension(path);
        if(StringUtils.isNullOrEmpty(ext)){
            return false;
        }

        List list = ReaderConfig.getConfig(context).getPdfBookCategory();
        if (ReaderConfig.getConfig(context).getPdfBookCategory().contains(ext)) {
            book = new PdfBook(visableWidth, visableHeight);
        } else if (ReaderConfig.getConfig(context).getPdfBookCategory().contains(ext)) {
            book = new EpubBook(visableWidth, visableHeight);
        }
        if (book == null) {
            return false;
        }
        return true;
    }

    public static boolean openBook(Context context, String path) {
        return (initBook(context, path) ? book.open(context, path) : false);
    }

    public static boolean closeBook() {
        if (book == null || book.close()) {
            book = null;
            return true;
        }
        return false;
    }

    public static boolean turnPage(Page page) {
        return book.gotoPage(page);
    }

    public static boolean nextPage() {
        if ((book.getCurrentPage().getPageNum() + 1) <= book.getTotalPage()) {
            book.getCurrentPage().setPageNum(book.getCurrentPage().getPageNum() + 1);
            return book.gotoPage(book.getCurrentPage());
        }
        return false;
    }

    public static boolean prePage() {
        if ((book.getCurrentPage().getPageNum() - 1) >= 0) {
            book.getCurrentPage().setPageNum(book.getCurrentPage().getPageNum() - 1);
            return book.gotoPage(book.getCurrentPage());
        }
        return false;
    }

    public static Book getBook() {
        return book;
    }

    public static void setBook(Book book) {
        Reader.book = book;
    }

    public static int getVisableWidth() {
        return visableWidth;
    }

    public static void setVisableWidth(int visableWidth) {
        Reader.visableWidth = visableWidth;
    }

    public static int getVisableHeight() {
        return visableHeight;
    }

    public static void setVisableHeight(int visableHeight) {
        Reader.visableHeight = visableHeight;
    }
}
