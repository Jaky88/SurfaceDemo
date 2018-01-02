package com.jaky.demo.surface.data.book;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by Jack on 2018/1/1.
 */

public class Page {

    private Bitmap bitmap;
    private Canvas canvas;
    private int pageNum;
    private int margin;
    private int left;
    private int top;
    private int width;
    private int height;
    private int font;
    private int textSize;
    private int textColor;
    private int lineSpacing;
    private int segmentSpacing;
    private int textSpacing;
    private final Paint paint;

    public Page(Bitmap bmp, int pageNum, Rect rect) {
        this.bitmap = bmp.copy(Bitmap.Config.ARGB_8888, true);
        this.canvas = new Canvas(bitmap);
        this.pageNum = pageNum;
        this.width = rect.width();
        this.height = rect.height();
        this.left = rect.left;
        this.top = rect.top;
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawColor(Color.WHITE);
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

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public Paint getPaint() {
        return paint;
    }
}
