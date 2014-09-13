package net.whitedesert.photosign.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import net.whitedesert.photosign.R;
import net.whitedesert.photosign.activities.GalleryActivity;
import net.whitedesert.photosign.activities.Types;

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

        XY xy = PhotoUtil.getWidthHeight(photo, sign);
        int width = xy.getX(), height = xy.getY();

        signed = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas comboImage = new Canvas(signed);

        comboImage.drawBitmap(photo, 0f, 0f, null);
        comboImage.drawBitmap(sign, x, y, null);

        return signed;
    }


    public static void openGalleryToSignSingle(final Activity activity) {
        if (SignUtil.noSigns()) {
            ToastUtil.toastShort(R.string.oops_no_sign);
            AskUtil.selectMethodSign(activity);
            return;
        }
        Intent i = new Intent(activity, GalleryActivity.class);
        i.putExtra(Types.TYPE, Types.OPEN_GALLERY_SINGLE_SIGNING_TYPE);
        activity.startActivity(i);
    }


}
