package com.jaky.demo.surface.data.core;

import android.graphics.Bitmap;

/**
 * Created by Jack on 2018/1/1.
 */

public class Page {

    private Bitmap bitmap;
    private int pageNum;
    private int margin;
    private int width;
    private int height;
    private int font;
    private int textSize;
    private int textColor;
    private int lineSpacing;
    private int segmentSpacing;
    private int textSpacing;

    public Page(Bitmap bitmap, int pageNum, int width, int height) {
        this.bitmap = bitmap;
        this.pageNum = pageNum;
        this.width = width;
        this.height = height;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getMargin() {
        return margin;
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getFont() {
        return font;
    }

    public void setFont(int font) {
        this.font = font;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getLineSpacing() {
        return lineSpacing;
    }

    public void setLineSpacing(int lineSpacing) {
        this.lineSpacing = lineSpacing;
    }

    public int getSegmentSpacing() {
        return segmentSpacing;
    }

    public void setSegmentSpacing(int segmentSpacing) {
        this.segmentSpacing = segmentSpacing;
    }

    public int getTextSpacing() {
        return textSpacing;
    }

    public void setTextSpacing(int textSpacing) {
        this.textSpacing = textSpacing;
    }
}
