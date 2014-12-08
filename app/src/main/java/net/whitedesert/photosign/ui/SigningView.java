package net.whitedesert.photosign.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import net.whitedesert.photosign.utils.Signature;

/**
 * Created by yazeed44 on 8/9/14.
 */
public class SigningView extends ImageView {


    private final Point orgPhotoDimen = new Point();//Original photo size
    private Bitmap photo;
    private Bitmap signBitmap;
    private Signature signature;
    private float signingX = -1, signingY = -1;
    private float touchX, touchY;
    private ImageView signatureView;

    public SigningView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//view given size
        super.onSizeChanged(w, h, oldw, oldh);

        Log.i("Signing View : onSizeChanged", "Width  :  " + w + "    , height :  " + h);

        if (photo != null) {
            orgPhotoDimen.x = (photo.getWidth());
            orgPhotoDimen.y = (photo.getHeight());
            photo = Bitmap.createScaledBitmap(photo, w, h, true);
            setImageBitmap(photo);

        }
    }


  /*  @Override
    public void onDraw(Canvas canvas) {

        super.onDraw(canvas);


        if (photo != null && x != -1 && y != -1) {
          //  canvas.drawBitmap(signBitmap, x, y, null);
           //changeSignaturePlace();

            Log.i("SigningView", "Signing at X : " + x + "  , Y : " + y);
        }
    }*/


  /*  @Override
    public boolean onTouchEvent(MotionEvent event) {
//detect user touch


        touchX = event.getX();
        touchY = event.getY();
        Log.i("Original Touch Values", "X  = " + touchX + "  ,  Y   =  " + touchY);


        float x, y, dx = 0, dy = 0;
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                //finger touches the screen
                //  setupXY();
                x = event.getX();
                y = event.getY();
                dx = x - signatureView.getX();
                dy = y - signatureView.getY();

                break;

            case MotionEvent.ACTION_UP:
                //finger leaves the screen

                break;

            case MotionEvent.ACTION_MOVE:
                //finger moves on the screen
                //setupXY();
                //   changeSignaturePlace();
                //  signatureView.setX(event.getX()-dx);
                //  signatureView.setY(event.getY()-dy);
                break;
        }

        return true;

    }*/


    public Signature getSignature() {
        return this.signature;
    }

    public void setSignature(Signature signature) {
        this.signature = signature;
        setSign(signature.getBitmap());
    }

    public void setSignatureView(final ImageView signatureView) {
        this.signatureView = signatureView;
        // setSignature(signatureView.getSignature());
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

    private void setXY(final Point xy) {
        this.signingX = xy.x;
        this.signingY = xy.y;
    }


    public Point getSignWidthHeight() {
        return new Point(getSignBitmap().getWidth(), getSignBitmap().getHeight());
    }

    private void fixXY(PointF touches) {

        final int signW = signBitmap.getWidth(), signH = signBitmap.getHeight();
        touches.x = (touches.x - signW / 2);
        touches.y = (touches.y - signH / 2);
        setXY(touches);
    }

    public PointF getXY() {
        final PointF xy = fixXY(touchX, touchY);
        Log.i("getXY : Fixed coordiantes to save a signed photo", xy.toString());
        return xy;
    }

    private void setXY(PointF xy) {
        this.signingX = xy.x;
        this.signingY = xy.y;
    }

    public Point getOrgPhotoDimen() {
        return orgPhotoDimen;
    }

    private PointF fixXY(float pX, float pY) {
        //Finally i fixed it XY problem compleatly
        //the alogrithm : make the photo expand till it fills the View , then sign on it , then return it to it's originial size
        //that's way there won't be any problem

        final int signW = signBitmap.getWidth(), signH = signBitmap.getHeight();

        final Drawable drawable = this.getDrawable();
        final Rect imageBounds = drawable.getBounds();
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
        final float scaledImageOffsetX = pX - imageBounds.left - signW / 2;
        final float scaledImageOffsetY = pY - imageBounds.top - signH / 2;

        Log.i("fixXY : scaled Image Off set ", "X  =  " + scaledImageOffsetX + "   ,  Y  =  " + scaledImageOffsetY);
//scale these distances according to the ratio of your scaling
//For example, if the original image is 1.5x the size of the scaled
//image, and your offset is (10, 20), your original image offset
//values should be (15, 30).
        final float originalImageOffsetX = scaledImageOffsetX * widthRatio;
        final float originalImageOffsetY = scaledImageOffsetY * heightRatio;

        return new PointF(originalImageOffsetX, originalImageOffsetY);

    }

    public Point getSignDimension() {
        return new Point(signBitmap.getWidth(), signBitmap.getHeight());
    }
}
