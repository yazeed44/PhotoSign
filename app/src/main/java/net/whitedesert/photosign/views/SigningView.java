package net.whitedesert.photosign.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import net.whitedesert.photosign.utils.PhotoUtil;
import net.whitedesert.photosign.utils.Sign;
import net.whitedesert.photosign.utils.XY;

/**
 * Created by yazeed44 on 8/9/14.
 */
public class SigningView extends ImageView {


    private Bitmap photo;
    private Bitmap signBitmap;
    private Sign sign;


    private float x = -1, y = -1;

    public SigningView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//view given size
        super.onSizeChanged(w, h, oldw, oldh);

        Log.i("Signing View : onSizeChanged", "Width  :  " + w + "    , height :  " + h);

        if (photo != null) {
            //  photo = Bitmap.createScaledBitmap(photo, w - 25, h - 25, true);
            // this.setImageBitmap(photo);

            if (signBitmap != null) {
                setXY(PhotoUtil.getCenter(photo));
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
                // fixXY(touches);
                setXY(getPointerCoords(this, event));
                invalidate();

                break;
        }

        return true;

    }

    public void setSign(Sign sign) {
        setSign(sign.getBitmap());
        this.sign = sign;
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

    public XY.Float getXY() {
        XY.Float xy = new XY.Float();
        xy.setX(this.x);
        xy.setY(this.y);
        return xy;
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

    public XY.Float getPointerCoords(ImageView view, MotionEvent e) {
        final int index = e.getActionIndex();
        final float[] coords = new float[]{e.getX(index), e.getY(index)};
        Matrix matrix = new Matrix();
        view.getImageMatrix().invert(matrix);
        matrix.postTranslate(view.getScrollX(), view.getScrollY());
        matrix.mapPoints(coords);
        XY.Float coordsFloat = new XY.Float();
        coordsFloat.setX(coords[0]);
        coordsFloat.setY(coords[1]);

        return coordsFloat;
    }
}
