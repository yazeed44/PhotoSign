package net.whitedesert.photosign.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

import net.whitedesert.photosign.utils.Corners;
import net.whitedesert.photosign.utils.Sign;
import net.whitedesert.photosign.utils.XY;

/**
 * Created by yazeed44 on 9/12/14.
 */
public class SignView extends ImageView {

    public final float cornerRadius = 20F;
    private Corners corners = new Corners();
    private Sign sign;

    public SignView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SignView(Context context, Bitmap sign) {
        super(context);
        setImageBitmap(sign);
    }

    public SignView(Context context, Sign sign) {
        super(context);
        setSign(sign);
    }

    private void initCorners() {
        corners.setRightUp(new XY.Float(this.getWidth(), 0));
        corners.setRightDown(new XY.Float(this.getWidth(), this.getHeight()));
        corners.setLeftUp(new XY.Float(0, 0));
        corners.setLeftDown(new XY.Float(0, this.getHeight()));
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initCorners();

    }


    public void setSign(Sign sign) {
        this.sign = sign;
        setImageBitmap(sign.getBitmap());
    }

}
