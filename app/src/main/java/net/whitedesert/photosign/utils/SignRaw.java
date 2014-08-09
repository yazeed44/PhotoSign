package net.whitedesert.photosign.utils;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

/**
 * Created by yazeed44 on 8/8/14.
 */
public final class SignRaw {

    private String text = "Test";
    private  Paint paint = new Paint();
    private int  color = Color.BLACK;
    private int textSize = 22;
    private int opacity = 0;
    private int style = Typeface.NORMAL;
    private String font = "";
    private String name = "";
    private int id;

    public void setText(String text){
        this.text = text;
    }
    public void setColor(int color){
        this.color = color;
    }
    public void setTextSize(int textSize){
        this.textSize = textSize;
    }

    public void setOpacity(int opacity){
        this.opacity = opacity;
    }

    public void setStyle(int style){
        this.style = style;
    }

    public void setFont(String font){
        this.font = font;
    }
    public void setName(String name){this.name = name;}
    public void setId(int id){this.id = id;}
    public Paint getPaint(){
        paint.setColor(color);
        paint.setTextSize(textSize);
        paint.setTypeface(Typeface.create(font, style));
        paint.setAlpha(opacity);
       return paint;
    }

    public String getText(){
        return this.text;
    }

    public int getOpacity(){
        return this.opacity;
    }

    public int getColor(){
        return this.color;
    }
    public String getFont(){
        return this.font;
    }
    public int getTextSize(){
        return this.textSize;
    }

    public int getStyle(){
        return this.style;
    }
    public String getName(){return this.name;}
    public int getId(){return this.id;}

}
