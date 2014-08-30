package net.whitedesert.photosign.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import net.whitedesert.photosign.R;
import net.whitedesert.photosign.activities.GalleryActivity;
import net.whitedesert.photosign.activities.Types;

/**
 * Created by yazeed44 on 8/6/14.
 */
public final class SigningUtil {


    public static final int DEFAULT_SIGN_HEIGHT = 480;
    public static final int DEFAULT_SIGN_WIDTH = 320;

    private SigningUtil() {
        throw new AssertionError();
    }


    public static Bitmap signOnPhoto(Bitmap photo, Bitmap sign, float x, float y) {
        Bitmap signed;

        XY xy = getWidthHeight(photo, sign);
        int width = xy.getX(), height = xy.getY();

        signed = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas comboImage = new Canvas(signed);

        comboImage.drawBitmap(photo, 0f, 0f, null);
        comboImage.drawBitmap(sign, x, y, null);

        return signed;
    }


    public static XY getWidthHeight(Bitmap first, Bitmap second) {
        XY xy = new XY();
        int width, height;

        if (first.getWidth() > second.getWidth()) {
            width = first.getWidth() /*+ s.getWidth()*/;
            height = first.getHeight();
        } else {
            width = second.getWidth() + second.getWidth();
            height = first.getHeight();
        }

        xy.setX(width);
        xy.setY(height);
        return xy;
    }



    public static XY getCenter(int width, int height) {
        int x = (width) / 2;
        int y = height / 2;

        Log.i("Blend Util : left corner", "X = " + x + "  ,  Y  = " + y);
        return new XY(x, y);
    }

    public static XY getCenter(Bitmap photo) {

        return getCenter(photo.getWidth(), photo.getHeight());
    }

    public static Bitmap createBitmap(SignRaw signRaw, int width, int height) {
        Paint paint = signRaw.getPaint();

        float baseline = (int) (-paint.ascent() + 0.5f); // ascent() is negative
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText(signRaw.getText(), 0, baseline, paint);
        return image;
    }

    public static Bitmap createBitmap(SignRaw signRaw) {
        return createBitmap(signRaw, signRaw.getMeasuredWidth(), signRaw.getMeasuredHeight());
    }

    public static void openGalleryToSignSingle(final Activity activity) {
        if (CheckUtil.noSigns(activity)) {
            ToastUtil.toastShort(R.string.oops_no_sign, activity);
            AskUtil.selectMethodSign(activity);
            return;
        }
        Intent i = new Intent(activity, GalleryActivity.class);
        i.putExtra(Types.TYPE, Types.OPEN_GALLERY_SINGLE_SIGNING_TYPE);
        activity.startActivity(i);
    }
}
