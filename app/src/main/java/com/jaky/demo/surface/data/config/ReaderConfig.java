package com.jaky.demo.surface.data.config;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.jaky.demo.surface.R;
import com.jaky.utils.JsonUtil;
import com.jaky.utils.RawResUtils;
import com.jaky.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jack on 2017/12/31.
 */

public class ReaderConfig {
    private static ReaderConfig sInstance;
    private boolean debug = false;
    private List<String> pdfBookCategory = new ArrayList<String>();
    private List<String> epubBookCategory = new ArrayList<String>();

    private ReaderConfig() {
    }

    private ReaderConfig(Context context) {
        String content = RawResUtils.readContent(context, R.raw.reader_config);
        if (!StringUtils.isNullOrEmpty(content)) {
//                sInstance = JsonUtil.jsonToObject(content,ReaderConfig.class);
            sInstance = JSON.parseObject(content, ReaderConfig.class);
            String s = JsonUtil.objectToJson(sInstance);
        }
        if (sInstance == null) {
            sInstance = new ReaderConfig();
        }
    }

    public static ReaderConfig getConfig(Context context) {
        if (sInstance == null) {
            new ReaderConfig(context);
        }
        return sInstance;
    }

    public static ReaderConfig getsInstance() {
        return sInstance;
    }

    public static void setsInstance(ReaderConfig sInstance) {
        ReaderConfig.sInstance = sInstance;
    }

    public List<String> getPdfBookCategory() {
        return pdfBookCategory;
    }

    public void setPdfBookCategory(List<String> pdfBookCategory) {
        this.pdfBookCategory = pdfBookCategory;
    }

    public List<String> getEpubBookCategory() {
        return epubBookCategory;
    }

    public void setEpubBookCategory(List<String> epubBookCategory) {
        this.epubBookCategory = epubBookCategory;
    }

}
