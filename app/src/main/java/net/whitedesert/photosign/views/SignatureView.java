package net.whitedesert.photosign.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

import net.whitedesert.photosign.utils.Signature;

/**
 * Created by yazeed44 on 9/12/14.
 */
public class SignatureView extends ImageView {

    // public final float cornerRadius = 20F;
    // private Corners corners = new Corners();
    private Signature signature;

    public SignatureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SignatureView(Context context, Bitmap sign) {
        super(context);
        setImageBitmap(sign);
    }

    public SignatureView(Context context, Signature signature) {
        super(context);
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
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //      initCorners();

    }


    public void setSignature(Signature signature) {
        this.signature = signature;
        setImageBitmap(signature.getBitmap());
    }

}
