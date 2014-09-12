package net.whitedesert.photosign.utils;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by yazeed44 on 9/12/14.
 */
public final class ViewUtil {

    public static Point getDisplay(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size;
    }
}
