package net.whitedesert.photosign.threads;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by yazeed44 on 9/3/14.
 */
public final class BitmapThread extends Thread {

    private final String path;
    private final int width, height;
    private Bitmap bitmap;

    public BitmapThread(String path, int width, int height) {
        this.path = path;
        this.width = width;
        this.height = height;
        this.setName("Bitmap Thread - " + path);
    }

    @Override
    public void run() {
        bitmap = BitmapFactory.decodeFile(path);
        if (width != -1 && height != -1) {
            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        }


    }

    public Bitmap getBitmap() {
        return this.bitmap;
    }
}
