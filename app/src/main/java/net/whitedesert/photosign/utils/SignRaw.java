package net.whitedesert.photosign.utils;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

/**
 * Created by yazeed44 on 8/8/14.
 */
public final class SignRaw {

    private final Paint paint = new Paint();
    private String text = "Test";
    private int color = Color.BLACK;
    private int textSize = 20;
    private int opacity = 100;
    private int style = Typeface.NORMAL;

    private String font = "";
    private String name = "Test-name";
    private int id = -1;

    public Paint getPaint() {

        paint.setColor(getColor());
        paint.setTextSize(getTextSize());
        paint.setTypeface(Typeface.create(getFont(), getStyle()));
        paint.setAlpha(getOpacity());


        return paint;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getOpacity() {

        return Math.round((float) opacity / 100 * 255);
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

    public int getTextSize() {
        return this.textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public int getStyle() {
        return this.style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWidth() {
        Rect rect = new Rect();

        getPaint().getTextBounds(getText(), 0, getText().length(), rect);
        return rect.width() + 15;
    }

    public int getHeight() {
        Rect rect = new Rect();

        getPaint().getTextBounds(getText(), 0, getText().length(), rect);
        return rect.height() + 15;
    }


}
