package net.whitedesert.photosign.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

import net.whitedesert.photosign.utils.Signature;

/**
 * Created by yazeed44 on 9/12/14.
 */
public class SignatureView extends ImageView {

    // public final float cornerRadius = 20F;
    // private Corners corners = new Corners();
    private Signature signature;
    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.f;
    private Bitmap signatureBitmap;


    public SignatureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    public SignatureView(Context context) {
        super(context);
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());

    }

    public SignatureView(Context context, Signature signature) {
        super(context);
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        setSignature(signature);
    }

    /*    private void initCorners() {
            corners.setRightUp(new XY.Float(this.getWidth(), 0));
            corners.setRightDown(new XY.Float(this.getWidth(), this.getHeight()));
            corners.setLeftUp(new XY.Float(0, 0));
            corners.setLeftDown(new XY.Float(0, this.getHeight()));
        }
    */

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);



       /* final int oldWidth = signatureBitmap.getWidth();
        final int oldHeight = signatureBitmap.getHeight();
        signatureBitmap = signature.getBitmap((int)(oldWidth * mScaleFactor),(int)(oldHeight * mScaleFactor));
        setImageBitmap(signatureBitmap); // Just to make sure
*/
        canvas.save();
        canvas.scale(mScaleFactor, mScaleFactor);

        canvas.restore();

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // Let the ScaleGestureDetector inspect all events.
        if (mScaleDetector != null)
            mScaleDetector.onTouchEvent(ev);


        Log.i(getClass().getSimpleName() + " :  onTouchEvent", "Signature has been touched !! ,  X  =  " + ev.getX() + "    ,  Y = " + ev.getY());
        return true;
    }

    public Signature getSignature() {
        return signature;
    }

    public void setSignature(Signature signature) {
        this.signature = signature;
        signatureBitmap = signature.getBitmap();
        setImageBitmap(signatureBitmap);
    }

    private class ScaleListener
            extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();

            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));


            Log.i(getClass().getSimpleName() + ": onScale", "Scale factor  =  " + mScaleFactor);
            invalidate();
            return true;
        }

    }

}
