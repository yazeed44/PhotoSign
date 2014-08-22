package net.whitedesert.photosign.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;

/**
 * Created by yazeed44 on 8/6/14.
 */
public final class SigningUtil {


    private SigningUtil() {
        throw new AssertionError();
    }


    public static Bitmap combineImages(Bitmap c, Bitmap s, float x, float y) {
        Bitmap cs = null;

        int width = getWidthHeight(c, s).getX(), height = getWidthHeight(c, s).getY();

        cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas comboImage = new Canvas(cs);

        comboImage.drawBitmap(c, 0f, 0f, null);
        comboImage.drawBitmap(s, x, y, null);

        return cs;
    }

    public static XY getWidthHeight(Bitmap c, Bitmap s) {

        int width, height = 0;

        if (c.getWidth() > s.getWidth()) {
            width = c.getWidth() /*+ s.getWidth()*/;
            height = c.getHeight();
        } else {
            width = s.getWidth() + s.getWidth();
            height = c.getHeight();
        }

        XY xy = new XY(width, height);
        return xy;
    }

    public static XY getCenter(Bitmap photo) {

        return getCenter(photo.getWidth(), photo.getHeight());
    }

    public static XY getCenter(int width, int height) {
        int x = (width) / 2;
        int y = height / 2;
        Log.i("Blend Util : left corner", "X = " + x + "  ,  Y  = " + y);
        return new XY(x, y);
    }


    public static Bitmap createSign(SignRaw signRaw) {
        Paint paint = signRaw.getPaint();

        float baseline = (int) (-paint.ascent() + 0.5f); // ascent() is negative
        Bitmap image = Bitmap.createBitmap(signRaw.getWidth(), signRaw.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText(signRaw.getText(), 0, baseline, paint);
        return image;
    }

    //unused
    public static Bitmap writeOnDrawable(Bitmap bm, Paint paint, String text) {
        Bitmap bitmap = bm.copy(Bitmap.Config.ARGB_8888, true);

        Canvas canvas = new Canvas(bitmap);
        XY corner = getCenter(bitmap);
        canvas.drawText(text, corner.getX(), corner.getY(), paint);

        return bitmap;
    }

    //unused

    public static Bitmap writeOnPhoto(Activity activity, String text, String path) {
        SigningUtil util = new SigningUtil();
        Bitmap bitmap = BitmapFactory.decodeFile(path);

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(40f);
        paint.setTypeface(Typeface.create("", Typeface.BOLD));
        Bitmap drawable = util.writeOnDrawable(bitmap, paint, text);
        String pathToBlended = PhotoUtil.savePicFromBitmap(drawable, activity, PhotoUtil.SIGNED_PHOTO_DIR);
        return drawable;
    }


}
