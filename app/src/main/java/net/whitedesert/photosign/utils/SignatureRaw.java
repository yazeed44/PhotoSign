package net.whitedesert.photosign.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

/**
 * Created by yazeed44 on 8/8/14.
 * Class will be used when working on adding signature from typing in keyboard
 */
public final class SignatureRaw {


    private Paint paint = new Paint();
    private String text = "Preview";
    private int color = Color.WHITE;
    private float textSize = 50f;
    private int width = SignatureUtil.DEFAULT_SIGN_WIDTH, height = SignatureUtil.DEFAULT_SIGN_HEIGHT;

    private Typeface tf;

    public Paint getPaint() {

        paint.setColor(getColor());
        paint.setTextSize(getTextSize());
        paint.setTypeface(tf);
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


    public void setTypeface(final Typeface tf) {
        this.tf = tf;
    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int color) {
        this.color = color;
    }


    public float getTextSize() {
        return this.textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
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

    public Bitmap createBitmap(SignatureRaw signatureRaw, int width, int height) {
        final Paint paint = signatureRaw.getPaint();

        final float baseline = (int) (-paint.ascent() + 0.5f); // ascent() is negative
        final Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(image);
        canvas.drawText(signatureRaw.getText(), 0, baseline, paint);
        return image;
    }


    public Bitmap createBitmap(SignatureRaw signatureRaw, boolean measured) {

        if (measured)
            return createBitmap(signatureRaw, signatureRaw.getMeasuredWidth(), signatureRaw.getMeasuredHeight());
        else {
            return createBitmap(signatureRaw, signatureRaw.getWidth(), signatureRaw.getHeight());
        }
    }

}
