package net.whitedesert.photosign.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.view.View;

import net.whitedesert.photosign.R;
import net.whitedesert.photosign.threads.SaveBitmapThread;
import net.whitedesert.photosign.threads.SigningThread;
import net.whitedesert.photosign.ui.SigningView;

/**
 * Created by yazeed44 on 8/16/14.
 * Class for save and add
 */
public final class SaveUtil {

    public static final String SIGNS_DIR = "/signs";
    public static final String SIGNS_FOLDER_NAME = "signs";
    public static final String SIGNED_PHOTO_DIR = "/signed_photos";
    public static final String SIGNED_PHOTO_FOLDER_NAME = "signed_photo";


    private SaveUtil() {
        throw new AssertionError();
    }


    public static void saveSignatures(final String[] photosPath, final Activity activity) {

        final long[] results = SignatureUtil.addSigns(photosPath);

        final boolean noException = CheckUtil.checkSigns(results, true);

        if (noException) {
            ViewUtil.toastShort(R.string.saved_signatures_success);
        }

        showWannaSignDialog(activity);
    }

    public static void saveSignature(final Bitmap sign, final Activity activity) {
        final Signature signature = new Signature();

        final String signatureName = generateRandomSignName(sign.getDensity() + activity.toString());
        signature.setName(signatureName);

        final String path = saveSign(sign, signatureName, activity);
        signature.setPath(path);

        if (!CheckUtil.checkSign(signature)) {
            return;
        }
        addSignature(signature, true);
        showWannaSignDialog(activity);
    }

    public static void saveSignature(final View drawView, final Activity activity) {

        final Signature signature = new Signature();

        final String signatureName = generateRandomSignName(drawView.getDrawingTime() + activity.toString());
        signature.setName(signatureName);

        drawView.setBackgroundColor(Color.TRANSPARENT);
        final String path = saveDrawSign(drawView, signatureName, activity);
        signature.setPath(path);
        drawView.setBackgroundColor(Color.WHITE);

        if (!CheckUtil.checkSign(signature)) {
            return;
        }

        addSignature(signature, true);
        showWannaSignDialog(activity);

    }

    private static String generateRandomSignName(final String name) {

        String randomName = name + " - Signature " + RandomUtil.getRandomInt((9 * 1 * 2 * 3) * (2 * 1) * (6 * 1)); //Puzzle. Hint:old phone

        while (SignatureUtil.isDuplicatedSign(randomName)) {
            randomName += RandomUtil.getRandomInt(44);
            if (!SignatureUtil.isDuplicatedSign(randomName)) {
                break;
            }
        }

        return randomName;

    }

    private static void showWannaSignDialog(final Activity activity) {
        ViewUtil.createWannaSignDialog(activity).build().show();

    }

    private static void addSignature(final Signature signature, boolean toast) {
        final boolean isFirstSignature = SignatureUtil.noSigns();

        if (isFirstSignature) {
            SignatureUtil.setDefaultSignature(signature);
        }


        SignatureUtil.addSign(signature, toast);

    }


    public static void doneSigningPhoto(final SigningView signingView, final Activity activity) {
        final Signature signature = signingView.getSignature();
        final Bitmap photo = signingView.getPhoto(), signBitmap = signingView.getSignBitmap();
        final PointF signingXY = signingView.getXY();
        final Point originalPhotoDimen = signingView.getOrgPhotoDimen();
        final Point signDimension = signingView.getSignDimension();

        final SigningOptions options = new SigningOptions().setSignature(signature)
                .setSigningXY(signingXY)
                .setOriginalPhotoDimen(originalPhotoDimen)
                .setSignDimension(signDimension).setPhoto(photo);


        final SigningThread signThread = new SigningThread(options, activity);
        signThread.start();

    }

    public static String savePicFromBitmap(Bitmap finalBitmap, Activity activity, String dir, String name, boolean toast) {
        final SaveBitmapThread thread = new SaveBitmapThread.Builder(activity)
                .bitmap(finalBitmap)
                .dir(dir)
                .name(name)
                .toast(toast)
                .build();


        ThreadUtil.startAndJoin(thread);
        return thread.getPath();
    }

    public static String saveSign(final Bitmap signBitmap, final String name, final Activity activity) {
        return savePicFromBitmap(signBitmap, activity, SIGNS_DIR, name, false);
    }

    public static String saveDrawSign(final View drawView, final String name, final Activity activity) {
        return savePicFromView(drawView, activity, SIGNS_DIR, name, false);
    }

    public static String savePicFromView(View drawView, Activity activity, String dir, String name, boolean toast) {

        drawView.setDrawingCacheEnabled(true);
        final String path = savePicFromBitmap(drawView.getDrawingCache(true), activity, dir, name, toast);
        drawView.setDrawingCacheEnabled(false);
        return path;
    }


    public static String saveSignedPhoto(final Bitmap signedPhoto, final Activity activity, final String name) {
        return savePicFromBitmap(signedPhoto, activity, SIGNED_PHOTO_DIR, name, false);
    }


}
