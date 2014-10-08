package net.whitedesert.photosign.threads;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.os.Looper;

import net.whitedesert.photosign.utils.CheckUtil;
import net.whitedesert.photosign.utils.DialogUtil;
import net.whitedesert.photosign.utils.SaveUtil;
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
    private final XY.Float signingXY;
    private final XY orgPhotoDimen;
    private String pathSigned;

    public SigningThread(Bitmap photo, Bitmap signBitmap, String signName, Activity activity, XY.Float signingXY, final XY orgPhotoDimen) {
        this.signName = signName;
        this.signBitmap = signBitmap;
        this.photo = photo;
        this.activity = activity;
        this.signingXY = signingXY;
        this.orgPhotoDimen = orgPhotoDimen;
        this.setName("Signing Thread - " + signName);
    }

    @Override
    public void run() {

        final float x = signingXY.getX(), y = signingXY.getY();

        Looper.prepare();

        //final ProgressDialog progressDialog = DialogUtil.getProgressDialog(R.string.wait_please_title, R.string.wait_signing_msg, activity);
        //ThreadUtil.showDialog(progressDialog, activity);

        final Bitmap signed = Bitmap.createScaledBitmap(SigningUtil.signOnPhoto(photo, signBitmap, x, y), orgPhotoDimen.getX(), orgPhotoDimen.getY(), true);//sign on photo then return it to it's originial size

        pathSigned = SaveUtil.savePicFromBitmap(signed, activity, SaveUtil.SIGNED_PHOTO_DIR, "Signed Photo - " + signName + new Random().nextInt((int) Math.abs(x + 1)), true);

        CheckUtil.checkSign(pathSigned, activity);


//        ThreadUtil.dismissDialog(progressDialog, activity);
        final AlertDialog.Builder previewDialog = DialogUtil.getImageViewDialog("Test", "Test", signed, activity);
        ThreadUtil.showDialog(previewDialog);

    }

    public String getPath() {
        return pathSigned;
    }
}
