package net.whitedesert.photosign.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import net.whitedesert.photosign.utils.PhotoUtil;
import net.whitedesert.photosign.utils.Sign;
import net.whitedesert.photosign.utils.ViewUtil;
import net.whitedesert.photosign.utils.XY;

/**
 * Created by yazeed44 on 8/9/14.
 */
public class SigningView extends ImageView {


    private Bitmap photo;
    private Bitmap signBitmap;
    private Sign sign;

    private float x = -1, y = -1;
    private float touchX, touchY;

    public SigningView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//view given size
        super.onSizeChanged(w, h, oldw, oldh);

        Log.i("Signing View : onSizeChanged", "Width  :  " + w + "    , height :  " + h);
        final int widthFactor = 8;
        final int heightFactor = 8;

        if (photo != null) {

            if (signBitmap != null) {
                signBitmap = Bitmap.createScaledBitmap(signBitmap, photo.getWidth() / widthFactor, photo.getHeight() / heightFactor, true);
                setXY(fixXY(PhotoUtil.getCenter(photo)));
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


        touchX = event.getX();
        touchY = event.getY();
        Log.i("Original Touch Values", "X  = " + touchX + "  ,  Y   =  " + touchY);

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                //finger touches the screen
                break;

            case MotionEvent.ACTION_UP:
                //finger leaves the screen

                break;

            case MotionEvent.ACTION_MOVE:
                //finger moves on the screen
                setupXY();

                break;
        }

        return true;

    }

    public void setSign(Sign sign) {
        this.sign = sign;
        setSign(sign.getBitmap());
    }

    public Sign getSign() {
        return this.sign;
    }

    public void setSign(Bitmap bitmap) {
        this.signBitmap = bitmap;
    }

    public Bitmap getSignBitmap() {
        return this.signBitmap;
    }

    public Bitmap getPhoto() {
        return this.photo;
    }

    public void setPhoto(Bitmap bitmap) {
        this.photo = bitmap;
        setImageBitmap(photo);
    }

    private void setXY(XY xy) {
        this.x = xy.getX();
        this.y = xy.getY();
    }

    private void fixXY() {
        final XY.Float touches = new XY.Float(touchX, touchY);
        // fixXY(touches);
    }

    private void setupXY() {
        // fixXY();
        final int factor = 25;

        if (touchX > 0 && touchY > 0 && touchX < this.getWidth() - factor && touchY < this.getHeight() - factor) {
            this.x = touchX;
            this.y = touchY;
            invalidate();
        } else {
            Log.e(this.getClass().getSimpleName() + " : setupXY", "it's out of photo range !! ,  touchX = " + touchX + "    ,  touchY  =  " + touchY);
        }

    }

    public XY getSignWidthHeight() {
        return new XY(getSignBitmap().getWidth(), getSignBitmap().getHeight());
    }

    /*private void fixXY(XY.Float touches) {

        final int signW = signBitmap.getWidth(), signH = signBitmap.getHeight();
        touches.setX(touches.getX() - signW / 2);
        touches.setY(touches.getY() - signH / 2);
        setXY(touches);
    }*/

    public XY.Float getXY() {
        XY.Float xy = fixXY(touchX, touchY);
        Log.i("getXY : Fixed coordiantes to save a signed photo", xy.toString());
        return xy;
    }

    private void setXY(XY.Float xy) {
        this.x = xy.getX();
        this.y = xy.getY();
    }

    private XY.Float fixXY(float pX, float pY) {
        //lol i thought i got rid of the problem completely but i was wrong again
        //at least it's usable right now
        //TODO

        final Drawable drawable = this.getDrawable();
        final Rect imageBounds = drawable.getBounds();
        final Point display = ViewUtil.getDisplay(getContext());
        Log.i("fixXY : Orignial values ", "Touch X  = " + touchX + "   , Touch Y  =  " + touchY);

//original height and width of the bitmap
        final float intrinsicHeight = drawable.getIntrinsicHeight();
        final float intrinsicWidth = drawable.getIntrinsicWidth();
        Log.i("fixXY : intrinsic", "Height  =  " + intrinsicHeight + "   ,  Width  =  " + intrinsicWidth);
//height and width of the visible (scaled) image
        final float scaledHeight = this.getHeight();
        final float scaledWidth = this.getWidth();
        Log.i("fixXY : Scaled ", "Height  =  " + scaledHeight + "   ,  Width  =  " + scaledWidth);
//Find the ratio of the original image to the scaled image
//Should normally be equal unless a disproportionate scaling
//(e.g. fitXY) is used.
        final float heightRatio = intrinsicHeight / scaledHeight;
        final float widthRatio = intrinsicWidth / scaledWidth;
        Log.i("fixXY : Ratio ", "Height  =  " + heightRatio + "   ,  Width  =  " + widthRatio);
//do whatever magic to get your touch point
//MotionEvent event;


//get the distance from the left and top of the image bounds
        final float scaledImageOffsetX = pX - imageBounds.left;
        final float scaledImageOffsetY = pY - imageBounds.top;

        Log.i("fixXY : scaled Image Off set ", "X  =  " + scaledImageOffsetX + "   ,  Y  =  " + scaledImageOffsetY);
//scale these distances according to the ratio of your scaling
//For example, if the original image is 1.5x the size of the scaled
//image, and your offset is (10, 20), your original image offset
//values should be (15, 30).
        final float originalImageOffsetX = scaledImageOffsetX * widthRatio;
        final float originalImageOffsetY = scaledImageOffsetY * heightRatio;

        return new XY.Float(originalImageOffsetX, originalImageOffsetY);

    }

    private XY.Float fixXY(XY xy) {
        return fixXY(xy.getX(), xy.getY());
    }
}
