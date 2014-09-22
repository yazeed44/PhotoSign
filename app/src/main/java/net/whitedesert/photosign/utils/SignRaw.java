package net.whitedesert.photosign.utils;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

/**
 * Created by yazeed44 on 8/8/14.
 * Class will be used when working on adding signature from typing in keyboard
 */
public final class SignRaw {


    private Paint paint = new Paint();
    private String text = "Test";
    private int color = Color.WHITE;
    private float textSize = 20f;
    private int opacity = 255;
    private boolean bold = false;
    private int width = SignUtil.DEFAULT_SIGN_WIDTH, height = SignUtil.DEFAULT_SIGN_HEIGHT;

    private String font = "";
    private String name = "Test-name";

    public Paint getPaint() {

        paint.setColor(getColor());
        paint.setTextSize(getTextSize());

        int style;
        if (isBold()) {
            style = Typeface.BOLD;
        } else {
            style = Typeface.NORMAL;
        }

        paint.setTypeface(Typeface.create(getFont(), style));
        paint.setAlpha(getOpacity());


        return paint;
    }

    public void setPaint(final Paint paint) {
        this.paint = paint;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getOpacity() {

        return this.opacity;
    }

    public void setOpacity(int opacity) {
        this.opacity = opacity;
    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getFont() {
        return this.font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public float getTextSize() {
        return this.textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public boolean isBold() {
        return this.bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private Rect getRect() {
        Rect rect = new Rect();
        getPaint().getTextBounds(getText(), 0, getText().length(), rect);
        return rect;
    }

    public int getMeasuredWidth() {

        return getRect().width() + 30;
    }

    public int getMeasuredHeight() {
        return getRect().height() + 30;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

}
