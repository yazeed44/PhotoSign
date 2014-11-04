package net.whitedesert.photosign.threads;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.os.Looper;

import net.whitedesert.photosign.utils.CheckUtil;
import net.whitedesert.photosign.utils.DialogUtil;
import net.whitedesert.photosign.utils.RandomUtil;
import net.whitedesert.photosign.utils.SaveUtil;
import net.whitedesert.photosign.utils.SigningOptions;
import net.whitedesert.photosign.utils.SigningUtil;
import net.whitedesert.photosign.utils.ThreadUtil;
import net.whitedesert.photosign.utils.XY;

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

    public SigningThread(SigningOptions options, Activity activity) {
        this.signName = options.getSignature().getName();
        this.signBitmap = options.getSignature().getBitmap(options.getSignDimension());
        this.photo = options.getPhoto();
        this.activity = activity;
        this.signingXY = options.getSigningXY();
        this.orgPhotoDimen = options.getOriginalPhotoDimen();
        this.setName("Signing Thread - " + signName);
    }

    @Override
    public void run() {

        final float x = signingXY.getX(), y = signingXY.getY();

        Looper.prepare();

        //final ProgressDialog progressDialog = DialogUtil.getProgressDialog(R.string.wait_please_title, R.string.wait_signing_msg, activity);
        //ThreadUtil.showDialog(progressDialog, activity);


        final Bitmap signed = Bitmap.createScaledBitmap(SigningUtil.signOnPhoto(photo, signBitmap, x, y), orgPhotoDimen.getX(), orgPhotoDimen.getY(), true);//sign on photo then return it to it's originial size

        final String signedPhotoFileName = "Signed Photo - " + signName + RandomUtil.getRandomInt((int) Math.abs(x + 1));

        pathSigned = SaveUtil.saveSignedPhoto(signed, activity, signedPhotoFileName);

        CheckUtil.checkSign(pathSigned, activity);


//        ThreadUtil.dismissDialog(progressDialog, activity);
        final AlertDialog.Builder previewDialog = DialogUtil.getImageViewDialog("Test", "Test", signed, activity);
        ThreadUtil.showDialog(previewDialog);

    }

    public String getPath() {
        return pathSigned;
    }
}
