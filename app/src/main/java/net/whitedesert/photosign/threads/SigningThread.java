package net.whitedesert.photosign.threads;

import android.app.Activity;
import android.graphics.Bitmap;

import net.whitedesert.photosign.utils.PhotoUtil;
import net.whitedesert.photosign.utils.Sign;
import net.whitedesert.photosign.utils.SigningUtil;
import net.whitedesert.photosign.utils.XY;

/**
 * Created by yazeed44 on 8/7/14.
 */
public class SigningThread extends Thread {


    private final Activity activity;
    private final Sign sign;
    private final Bitmap photo;
    private final XY xy;
    private String pathBlended;

    public SigningThread(Bitmap photo, Sign sign, Activity activity, XY xy) {
        this.sign = sign;
        this.photo = photo;
        this.activity = activity;
        this.xy = xy;
    }

    @Override
    public void run() {


        Bitmap blended = SigningUtil.signOnPhoto(photo, sign.getBitmap(), xy.getX(), xy.getY());
        pathBlended = PhotoUtil.savePicFromBitmap(blended, activity, PhotoUtil.SIGNED_PHOTO_DIR, sign.getName(), true);


    }

    public String getPath() {
        return pathBlended;
    }
}
