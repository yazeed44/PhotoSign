package net.whitedesert.photosign.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;

import net.whitedesert.photosign.R;
import net.whitedesert.photosign.ui.MainActivity;

/**
 * Created by yazeed44 on 8/6/14.
 * Class for signing on photos
 */
public final class SigningUtil {


    private SigningUtil() {
        throw new AssertionError();
    }


    public static Bitmap signOnPhoto(Bitmap photo, Bitmap sign, float x, float y) {
        Bitmap signed;

        final Point widthHeight = getWidthHeight(photo, sign);
        int width = widthHeight.x, height = widthHeight.y;

        signed = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas comboImage = new Canvas(signed);

        comboImage.drawBitmap(photo, 0f, 0f, null);
        comboImage.drawBitmap(sign, x, y, null);

        return signed;
    }

    public static Point getWidthHeight(Bitmap first, Bitmap second) {
        final Point xy = new Point();
        int width, height;

        if (first.getWidth() > second.getWidth()) {
            width = first.getWidth();
            height = first.getHeight();
        } else {
            width = second.getWidth() + second.getWidth();
            height = first.getHeight();
        }

        xy.x = (width);
        xy.y = (height);
        return xy;
    }


    public static void showGalleryToSign(final Activity activity) {
        if (SignatureUtil.noSigns()) {
            ViewUtil.toastShort(R.string.oops_no_sign);
            ViewUtil.createChooseMethodToSignDialog(activity).show();
            return;
        }

        final Intent i = new Intent(activity, MainActivity.class);
        activity.startActivity(i);
    }


}
