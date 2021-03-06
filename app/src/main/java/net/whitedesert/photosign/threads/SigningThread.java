package net.whitedesert.photosign.threads;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Looper;

import net.whitedesert.photosign.R;
import net.whitedesert.photosign.utils.CheckUtil;
import net.whitedesert.photosign.utils.SaveUtil;
import net.whitedesert.photosign.utils.Signature;
import net.whitedesert.photosign.utils.SigningOptions;
import net.whitedesert.photosign.utils.SigningUtil;
import net.whitedesert.photosign.utils.ViewUtil;

/**
 * Created by yazeed44 on 8/7/14.
 * A thread to sign on photos
 */
public final class SigningThread extends Thread {


    private final Activity activity;
    private final String signName;

    private final Bitmap photo, signBitmap;
    private final PointF signingXY;
    private final Point orgPhotoDimen;
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

        final float x = signingXY.x, y = signingXY.y;

        Looper.prepare();
        //TODO clean this

        //final ProgressDialog progressDialog = DialogUtil.getProgressDialog(R.string.wait_please_title, R.string.wait_signing_msg, activity);
        //ThreadUtil.showDialog(progressDialog, activity);


        final Bitmap signed = Bitmap.createScaledBitmap(SigningUtil.signOnPhoto(photo, signBitmap, x, y), orgPhotoDimen.x, orgPhotoDimen.y, true);//sign on photo then return it to it's originial size

        final String signedPhotoFileName = "Signed Photo - " + System.currentTimeMillis();

        pathSigned = SaveUtil.saveSignedPhoto(signed, activity, signedPhotoFileName);

        CheckUtil.checkSign(new Signature(pathSigned));


//        ThreadUtil.dismissDialog(progressDialog, activity);
        // final AlertDialog.Builder previewDialog = DialogUtil.getImageViewDialog("Test", "Test", signed, activity);
        // ThreadUtil.showDialog(previewDialog);

        showShare();

    }

    private void showShare() {


        final String title = activity.getResources().getString(R.string.share_signed_photo_title);

        ViewUtil.shareImage(title, getPath(), activity);

    }

    public String getPath() {
        return pathSigned;
    }
}
