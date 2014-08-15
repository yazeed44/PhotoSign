package net.whitedesert.photosign.utils;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

/**
 * Created by yazeed44 on 8/8/14.
 */
public final class SignRaw {

    private String text = "Test";
    private final Paint paint = new Paint();
    private int  color = Color.BLACK;
    private int textSize = 20;
    private int opacity = 100;
    private int style = Typeface.NORMAL;

    private String font = "";
    private String name = "Test-name";
    private int id = -1;

    private int width = 100,height = 100;

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
    public void setHeight(int height){this.height = height;}
    public void setWidth(int width){this.width = width;}
    public Paint getPaint(){

        paint.setColor(getColor());
        paint.setTextSize(getTextSize());
        paint.setTypeface(Typeface.create(getFont(), getStyle()));
        paint.setAlpha(getOpacity());

       return paint;
    }

    public String getText(){
        return this.text;
    }

    public int getOpacity(){

        return Math.round((float)opacity/100*255);
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
    public int getWidth(){return this.width;}
    public int getHeight(){return this.height;}


}
