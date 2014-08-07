package whitedessert.com.photosign.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by yazeed44 on 8/6/14.
 */
public final class BlendUtil {


    public static Bitmap combineImages(Bitmap c, Bitmap s)
    {

        Bitmap cs = null;

        int width, height = 0;

        if(c.getWidth() > s.getWidth()) {
            width = c.getWidth() + s.getWidth();
            height = c.getHeight();
        } else {
            width = s.getWidth() + s.getWidth();
            height = c.getHeight();
        }

        cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas comboImage = new Canvas(cs);
        PhotoLeftCorner xy = getLeftCorner(c);
        comboImage.drawBitmap(c, 0f, 0f, null);
        comboImage.drawBitmap(s, xy.getX(), xy.getY(), null);

        return cs;
    }

    public static PhotoLeftCorner getLeftCorner(Bitmap photo){

        int x = photo.getWidth()/photo.getHeight();
        int y = photo.getHeight() / photo.getWidth();
        return new PhotoLeftCorner(x,y);
    }
}
