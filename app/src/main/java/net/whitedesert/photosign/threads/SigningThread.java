package net.whitedesert.photosign.threads;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Looper;

import net.whitedesert.photosign.R;
import net.whitedesert.photosign.utils.BitmapUtil;
import net.whitedesert.photosign.utils.CheckUtil;
import net.whitedesert.photosign.utils.RandomUtil;
import net.whitedesert.photosign.utils.SaveUtil;
import net.whitedesert.photosign.utils.SigningOptions;
import net.whitedesert.photosign.utils.SigningUtil;

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

        final String signedPhotoFileName = "Signed Photo - " + signName + RandomUtil.getRandomInt((int) Math.abs(x + 1));

        pathSigned = SaveUtil.saveSignedPhoto(signed, activity, signedPhotoFileName);

        CheckUtil.checkSign(pathSigned, activity);


//        ThreadUtil.dismissDialog(progressDialog, activity);
        // final AlertDialog.Builder previewDialog = DialogUtil.getImageViewDialog("Test", "Test", signed, activity);
        // ThreadUtil.showDialog(previewDialog);

        showShare();

    }

    private void showShare() {
        final Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/png");

        share.putExtra(Intent.EXTRA_STREAM, Uri.parse(BitmapUtil.GLOBAL_PATH + getPath()));

        final String title = activity.getResources().getString(R.string.share_signed_photo);

        activity.startActivity(Intent.createChooser(share, title));

    }

    public String getPath() {
        return pathSigned;
    }
}
