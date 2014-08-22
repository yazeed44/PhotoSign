package net.whitedesert.photosign.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import net.whitedesert.photosign.utils.SigningUtil;
import net.whitedesert.photosign.utils.XY;

/**
 * Created by yazeed44 on 8/9/14.
 */
public class SigningView extends ImageView {


    private Bitmap photo;
    private Bitmap signBitmap;


    private float x = -1,y = -1;

    public SigningView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupDrawing();

    }
    private void setupDrawing(){
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//view given size
        super.onSizeChanged(w,h,oldw,oldh);

       Log.i("Blend View", "Width  :  " + w + "    , height :  " + h);

        if (photo != null) {
            photo = Bitmap.createScaledBitmap(photo, w, h, true);

            this.setImageBitmap(photo);

            if (signBitmap != null) {
                reDraw(SigningUtil.getCenter(photo));
            }
        }
    }


    @Override
    public void onDraw(Canvas canvas){

       super.onDraw(canvas);


        if(photo != null && x != -1 && y != -1 ){
            canvas.drawBitmap(signBitmap,x,y,null);
        }


    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
//detect user touch

       final float touchX = event.getX();
        final float touchY = event.getY();
        switch(event.getAction()){

            case MotionEvent.ACTION_UP:
                reDraw((touchX ) , (touchY ));
                break;
        }

        return true;

    }

    public void setPhoto(Bitmap bitmap){
        this.photo = bitmap;
    }

    public void setSign(Bitmap sign){
       this.signBitmap = sign;
    }


    private void reDraw(float x,float y){
        this.x = x;
        this.y = y;
        invalidate();
    }

    private void reDraw( XY xy){
        reDraw(xy.getX(),xy.getY());
    }


}
