package net.whitedesert.photosign.threads;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.os.Looper;

import net.whitedesert.photosign.utils.CheckUtil;
import net.whitedesert.photosign.utils.DialogUtil;
import net.whitedesert.photosign.utils.PhotoUtil;
import net.whitedesert.photosign.utils.SigningUtil;
import net.whitedesert.photosign.utils.ThreadUtil;
import net.whitedesert.photosign.utils.XY;

import java.util.Random;

/**
 * Created by yazeed44 on 8/7/14.
 * A thread to sign on photos
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

        final float x = xy.getX(), y = xy.getY();

        Looper.prepare();

        //final ProgressDialog progressDialog = DialogUtil.getProgressDialog(R.string.wait_please_title,R.string.wait_signing_msg,activity);
        // progressDialog.show();

        final Bitmap signed = SigningUtil.signOnPhoto(photo, signBitmap, x, y);
        pathSigned = PhotoUtil.savePicFromBitmap(signed, activity, PhotoUtil.SIGNED_PHOTO_DIR, "Signed Photo - " + signName + new Random().nextInt((int) Math.abs(x + 1)), true);

        if (!CheckUtil.checkSign(pathSigned, activity)) {
            //TODO

        }

        //progressDialog.dismiss();
        final AlertDialog.Builder previewDialog = DialogUtil.getImageViewDialog("Test", "Test", signed, activity);
        ThreadUtil.showDialog(previewDialog, activity);

    }

    public String getPath() {
        return pathSigned;
    }
}
