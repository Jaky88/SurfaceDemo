package com.jaky.demo.surface.data.book;

import android.content.Context;
import android.graphics.Rect;
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
    private static Rect visableRect;

    public static void init(Rect rect) {
        visableRect = rect;
    }

    public static boolean initBook(Context context, String path) {
        if (visableRect.width() == 0 || visableRect.height() == 0) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            int width = wm.getDefaultDisplay().getWidth();
            int height = wm.getDefaultDisplay().getHeight();
            visableRect = new Rect(0, 0, width, height);
        }
        String ext = FileUtils.getFileExtension(path);
        if(StringUtils.isNullOrEmpty(ext)){
            return false;
        }

        List list = ReaderConfig.getConfig(context).getPdfBookCategory();
        if (ReaderConfig.getConfig(context).getPdfBookCategory().contains(ext)) {
            book = new PdfBook(visableRect);
        } else if (ReaderConfig.getConfig(context).getPdfBookCategory().contains(ext)) {
            book = new EpubBook(visableRect);
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

    public static boolean gotoPage(int page) {
        if(page >=1 && page <= book.getTotalPageNum()){
            book.getPage().setPageNum(page);
            return book.gotoPage(book.getPage());
        }
        return false;
    }

    public static boolean nextPage() {
        if ((book.getCurrentPage().getPageNum() + 1) <= book.getTotalPageNum()) {
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

    public static Rect getVisableRect() {
        return visableRect;
    }

    public static void setVisableRect(Rect visableRect) {
        Reader.visableRect = visableRect;
    }
}
