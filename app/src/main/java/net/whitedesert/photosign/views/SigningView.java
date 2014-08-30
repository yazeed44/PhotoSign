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


    private float x = -1, y = -1;

    public SigningView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//view given size
        super.onSizeChanged(w, h, oldw, oldh);

        Log.i("Blend View", "Width  :  " + w + "    , height :  " + h);

        if (photo != null) {
            photo = Bitmap.createScaledBitmap(photo, w - 25, h - 25, true);
            this.setImageBitmap(photo);

            if (signBitmap != null) {
                setXY(SigningUtil.getCenter(photo));
                invalidate();
            }
        }
    }


    @Override
    public void onDraw(Canvas canvas) {

        super.onDraw(canvas);


        if (photo != null && x != -1 && y != -1) {
            canvas.drawBitmap(signBitmap, x, y, null);
            Log.i("SigningView", "Signing at X : " + x + "  , Y : " + y);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
//detect user touch

        final float touchX = event.getX();
        final float touchY = event.getY();
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                //finger touches the screen
                break;

            case MotionEvent.ACTION_UP:
                //finger leaves the screen

                break;

            case MotionEvent.ACTION_MOVE:
                //finger moves on the screen
                XY.Float touches = new XY.Float();
                touches.setX(touchX);
                touches.setY(touchY);
                fixXY(touches);
                setXY(touches);
                invalidate();
                break;
        }

        return true;

    }

    public void setPhoto(Bitmap bitmap) {
        this.photo = bitmap;
        setImageBitmap(photo);
    }

    public void setSign(Bitmap sign) {
        this.signBitmap = sign;
    }

    private void setXY(XY xy) {
        this.x = xy.getX();
        this.y = xy.getY();
    }

    private void setXY(XY.Float xy) {
        this.x = xy.getX();
        this.y = xy.getY();
    }


    private void fixXY(XY.Float touches) {

        final int signW = signBitmap.getWidth(), signH = signBitmap.getHeight();
        touches.setX(touches.getX() - signW / 2);
        touches.setY(touches.getY() - signH / 2);

    }

}
