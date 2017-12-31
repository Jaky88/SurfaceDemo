package com.jaky.demo.surface.data.core;

import android.content.Context;
import android.content.Intent;

import com.jaky.demo.surface.data.BookConfig;
import com.jaky.demo.surface.data.Constants;
import com.jaky.demo.surface.ui.ReaderActivity;

/**
 * Created by Jack on 2017/12/31.
 */

public class Reader {

    private Context context;
    public static final String INTENT_BOOK_ID = "book_id";

    public Reader(Context context) {
        this.context = context;
//        new DbAdapter(context);
    }

    public void openBook(String assetOrSdcardPath) {
        Intent intent = getIntentFromUrl(assetOrSdcardPath, 0);
        context.startActivity(intent);
    }

    public void openBook(int rawId) {
        Intent intent = getIntentFromUrl(null, rawId);
        context.startActivity(intent);
    }

    public void openBook(String assetOrSdcardPath, BookConfig BookConfig) {
        Intent intent = getIntentFromUrl(assetOrSdcardPath, 0);
        intent.putExtra(BookConfig.INTENT_CONFIG, BookConfig);
        context.startActivity(intent);
    }

    public void openBook(int rawId, BookConfig BookConfig) {
        Intent intent = getIntentFromUrl(null, rawId);
        intent.putExtra(BookConfig.INTENT_CONFIG, BookConfig);
        context.startActivity(intent);
    }

    public void openBook(String assetOrSdcardPath, BookConfig BookConfig, int port) {
        Intent intent = getIntentFromUrl(assetOrSdcardPath, 0);
        intent.putExtra(BookConfig.INTENT_CONFIG, BookConfig);
        intent.putExtra(BookConfig.INTENT_PORT, port);
        context.startActivity(intent);
    }

    public void openBook(int rawId, BookConfig BookConfig, int port) {
        Intent intent = getIntentFromUrl(null, rawId);
        intent.putExtra(BookConfig.INTENT_CONFIG, BookConfig);
        intent.putExtra(BookConfig.INTENT_PORT, port);
        context.startActivity(intent);
    }

    public void openBook(String assetOrSdcardPath, BookConfig BookConfig, int port, String bookId) {
        Intent intent = getIntentFromUrl(assetOrSdcardPath, 0);
        intent.putExtra(BookConfig.INTENT_CONFIG, BookConfig);
        intent.putExtra(BookConfig.INTENT_PORT, port);
        intent.putExtra(INTENT_BOOK_ID, bookId);
        context.startActivity(intent);
    }

    public void openBook(int rawId, BookConfig BookConfig, int port, String bookId) {
        Intent intent = getIntentFromUrl(null, rawId);
        intent.putExtra(BookConfig.INTENT_CONFIG, BookConfig);
        intent.putExtra(BookConfig.INTENT_PORT, port);
        intent.putExtra(INTENT_BOOK_ID, bookId);
        context.startActivity(intent);
    }

    private Intent getIntentFromUrl(String assetOrSdcardPath, int rawId) {
        Intent intent = new Intent(context, ReaderActivity.class);
        if (rawId != 0) {
//            intent.putExtra(ReaderActivity.INTENT_EPUB_SOURCE_PATH, rawId);
//            intent.putExtra(ReaderActivity.INTENT_EPUB_SOURCE_TYPE, FolioActivity.EpubSourceType.RAW);
        } else if (assetOrSdcardPath.contains(Constants.ASSET)) {
//            intent.putExtra(ReaderActivity.INTENT_EPUB_SOURCE_PATH, assetOrSdcardPath);
//            intent.putExtra(ReaderActivity.INTENT_EPUB_SOURCE_TYPE, FolioActivity.EpubSourceType.ASSETS);
        } else {
//            intent.putExtra(ReaderActivity.INTENT_EPUB_SOURCE_PATH, assetOrSdcardPath);
//            intent.putExtra(ReaderActivity.INTENT_EPUB_SOURCE_TYPE, FolioActivity.EpubSourceType.SD_CARD);
        }
        return intent;
    }
}
