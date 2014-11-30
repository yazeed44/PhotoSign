package net.whitedesert.photosign.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;

import net.whitedesert.photosign.R;
import net.whitedesert.photosign.threads.SaveBitmapThread;
import net.whitedesert.photosign.threads.SigningThread;
import net.whitedesert.photosign.ui.SigningView;

import java.io.File;

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

    public static void askNameAndAddSign(final String picPath, final Activity activity) {

        final File pic = new File(picPath);
        final EditText nameInput = ViewUtil.createEditTextForAskingName(pic.getName(), activity);
        nameInput.setText(pic.getName());
        final MaterialDialog.FullCallback posListener = getPosListenerForAskingName(BitmapUtil.decodeFile(picPath), nameInput, activity);

        getTypeNameDialog(nameInput, posListener, activity)
                .build()
                .show();


    }

    private static MaterialDialog.FullCallback getPosListenerForAskingName(final Bitmap signBitmap, final EditText nameInput, final Activity activity) {
        return new MaterialDialog.FullCallback() {
            @Override
            public void onNeutral(MaterialDialog materialDialog) {
                materialDialog.dismiss();
            }

            @Override
            public void onPositive(MaterialDialog materialDialog) {
                final String signName = nameInput.getText().toString();

                if (!CheckUtil.checkSign(signName, signBitmap, activity)) {
                    return;
                }

                final Signature signature = new Signature();
                signature.setName(signName);

                final String savedPath = SaveUtil.saveSign(signBitmap, signName, activity);

                if (!CheckUtil.checkSign(savedPath, activity)) {
                    return;
                }

                signature.setPath(savedPath);
                showWannaSignDialog(signature, activity);
            }

            @Override
            public void onNegative(MaterialDialog materialDialog) {

            }
        };
    }


    public static void askNameAndAddSign(final Bitmap sign, final Activity activity) {
        // Don't waste your time on this
        final EditText nameInput = ViewUtil.createEditTextForAskingName(RandomUtil.getRandomInt(SIGNED_PHOTO_DIR.length() + SIGNS_DIR.length()) + sign.getWidth() + sign.getDensity(), activity);//Random

        final MaterialDialog.FullCallback posListener = getPosListenerForAskingName(sign, nameInput, activity);

        getTypeNameDialog(nameInput, posListener, activity)
                .build()
                .show();
    }

    public static void askNameAndAddSign(final View drawView, final Activity activity) {
        final EditText nameInput = ViewUtil.createEditTextForAskingName(RandomUtil.getRandomInt(drawView.getWidth()), activity);

        final MaterialDialog.FullCallback posListener = getPosListenerForAskingName(drawView, nameInput, activity);

        getTypeNameDialog(nameInput, posListener, activity)
                .build()
                .show();
    }

    private static MaterialDialog.FullCallback getPosListenerForAskingName(final View drawView, final EditText nameInput, final Activity activity) {
        return new MaterialDialog.FullCallback() {
            @Override
            public void onNeutral(MaterialDialog materialDialog) {
                materialDialog.dismiss();
            }

            @Override
            public void onPositive(MaterialDialog materialDialog) {
                final String signName = nameInput.getText().toString();
                final Signature signature = new Signature();
                if (!CheckUtil.checkSign(signName, drawView, activity)) {
                    return;
                }

                signature.setName(signName);

                drawView.setBackgroundColor(Color.TRANSPARENT);
                final String savedPath = saveDrawSign(drawView, signName, activity);
                drawView.setBackgroundColor(Color.WHITE);

                if (!CheckUtil.checkSign(savedPath, activity)) {
                    return;
                }

                signature.setPath(savedPath);
                showWannaSignDialog(signature, activity);
            }

            @Override
            public void onNegative(MaterialDialog materialDialog) {

            }
        };
    }


    public static MaterialDialog.Builder getTypeNameDialog(final EditText nameInput, MaterialDialog.FullCallback callback, final Activity activity) {
        final MaterialDialog.Builder dialog = DialogUtil.createDialog(R.string.save_title, R.string.save_msg, activity);
        dialog.customView(nameInput)
                .callback(callback)
                .positiveText(R.string.ok_btn)
                .neutralText(R.string.dismiss_btn)
        ;

        return dialog;

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

    public static String savePicFromView(View drawView, Activity activity, String dir, String name, boolean toast) {

        drawView.setDrawingCacheEnabled(true);
        final String path = savePicFromBitmap(drawView.getDrawingCache(true), activity, dir, name, toast);
        drawView.setDrawingCacheEnabled(false);
        return path;
    }

    public static String saveDrawSign(final View drawView, final String name, final Activity activity) {
        return savePicFromView(drawView, activity, SIGNS_DIR, name, false);
    }


    public static String saveSignedPhoto(final Bitmap signedPhoto, final Activity activity, final String name) {
        return savePicFromBitmap(signedPhoto, activity, SIGNED_PHOTO_DIR, name, false);
    }


    public static void showWannaSignDialog(final Signature signature, final Activity activity) {
        Log.d("DrawSignActivity : onClickSave", "sign name : " + signature.getName() + " , sign Path : " + signature.getPath());
        final boolean isFirstSignature = SignatureUtil.noSigns();

        if (isFirstSignature) {
            signature.setDefault(true);
        }

        SignatureUtil.addSign(signature, true);


        DialogUtil.createWannaSignDialog(activity).build().show();

    }


}
