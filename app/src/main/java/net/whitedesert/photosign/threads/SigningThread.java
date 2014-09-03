package net.whitedesert.photosign.threads;

import android.app.Activity;
import android.graphics.Bitmap;

import net.whitedesert.photosign.utils.PhotoUtil;
import net.whitedesert.photosign.utils.SigningUtil;
import net.whitedesert.photosign.utils.XY;

import java.util.Random;

/**
 * Created by yazeed44 on 8/7/14.
 */
public final class SigningThread extends Thread {


    private final Activity activity;
    private final String signName;

    private final Bitmap photo, signBitmap;
    private final XY.Float xy;
    private String pathSigned;

    public SigningThread(Bitmap photo, Bitmap signBitmap, String signName, Activity activity, XY.Float xy) {
        this.signName = signName;
        this.signBitmap = signBitmap;
        this.photo = photo;
        this.activity = activity;
        this.xy = xy;
        this.setName("Signing Thread - " + signName);
    }

    @Override
    public void run() {

        final XY.Float fixedXy = new XY.Float();
        final float x = xy.getX(), y = xy.getY();
        fixedXy.setX(x);
        fixedXy.setY(y);

        final Bitmap blended = SigningUtil.signOnPhoto(photo, signBitmap, fixedXy.getX(), fixedXy.getY());
        pathSigned = PhotoUtil.savePicFromBitmap(blended, activity, PhotoUtil.SIGNED_PHOTO_DIR, "Signed Photo - " + signName + new Random().nextInt((int) x), true);


    }

    public String getPath() {
        return pathSigned;
    }
}
