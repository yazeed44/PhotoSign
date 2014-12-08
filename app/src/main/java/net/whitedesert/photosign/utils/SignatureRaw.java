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


    public static final int WIDTH = 1024, HEIGHT = 1024;
    private Paint mPaint = new Paint();
    private String mText = "Preview";
    private int mColor = Color.WHITE;
    private Typeface tf;


    public String getText() {
        return this.mText;
    }

    public void setText(String text) {
        this.mText = text;
    }


    public void setTypeface(final Typeface tf) {
        this.tf = tf;
    }

    public int getColor() {
        return this.mColor;
    }

    public void setColor(int color) {
        this.mColor = color;
        mPaint.setColor(mColor);
    }


    private float calculateTextSize() {

        final Rect bounds = new Rect();
        float textSize = 50f;
        mPaint.setTextSize(textSize);

        mPaint.getTextBounds(getText(), 0, getText().length(), bounds);

        while (WIDTH > bounds.width() && HEIGHT > bounds.height()) {
            textSize++;
            mPaint.setTextSize(textSize);
            mPaint.getTextBounds(getText(), 0, getText().length(), bounds);
        }


        return textSize;
    }

    private void initPaint() {
        mPaint.setColor(getColor());
        mPaint.setTextSize(calculateTextSize());
    }


    public Bitmap createBitmap() {
        initPaint();
        final Bitmap bitmap = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);

        canvas.drawText(getText(), 0, HEIGHT / 2, mPaint);
        return bitmap;
    }


}
