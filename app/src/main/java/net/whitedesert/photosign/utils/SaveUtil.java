package net.whitedesert.photosign.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import net.whitedesert.photosign.R;
import net.whitedesert.photosign.threads.BitmapThread;
import net.whitedesert.photosign.threads.SigningThread;
import net.whitedesert.photosign.views.SigningView;

import java.io.File;

/**
 * Created by yazeed44 on 8/16/14.
 * Class for save and add
 */
public final class SaveUtil {

    public static final String SIGNS_DIR = "/signs";
    public static final String SIGNED_PHOTO_DIR = "/signed_photos";

    private SaveUtil() {
        throw new AssertionError();
    }

    public static void askNameAndAddSign(final String picPath, final Activity activity) {

        final File pic = new File(picPath);
        final EditText nameInput = ViewUtil.getEditText(pic.getName(), activity);
        nameInput.setText(pic.getName());
        final DialogInterface.OnClickListener posListener = getPosListenerForAskingName(BitmapUtil.decodeFile(picPath), nameInput, activity);
        DialogUtil.getInputDialog(R.string.save_title, R.string.save_message, posListener, nameInput, activity).show();

    }

    public static DialogInterface.OnClickListener getPosListenerForAskingName(final Bitmap signBitmap, final EditText nameInput, final Activity activity) {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                final String signName = nameInput.getText().toString();

                if (!CheckUtil.checkSign(signName, signBitmap, activity)) {
                    return;
                }

                final Sign sign = new Sign();
                sign.setName(signName);

                final String savedPath = SaveUtil.saveSign(signBitmap, signName, activity);

                if (!CheckUtil.checkSign(savedPath, activity)) {
                    return;
                }

                sign.setPath(savedPath);
                addSignShowDialog(sign, activity);

            }
        };
    }


    public static void askNameAndAddSign(final Bitmap sign, final Activity activity) {
        // Don't waste your time on this
        final EditText nameInput = ViewUtil.getEditText(RandomUtil.getRandomInt(SIGNED_PHOTO_DIR.length() + SIGNS_DIR.length()) + sign.getWidth() + sign.getDensity(), activity);
        final DialogInterface.OnClickListener posListener = getPosListenerForAskingName(sign, nameInput, activity);
        DialogUtil.getInputDialog(R.string.save_title, R.string.save_message, posListener, nameInput, activity).show();
    }

    public static void askNameAndAddSign(final View drawView, final Activity activity) {
        final EditText nameInput = ViewUtil.getEditText(RandomUtil.getRandomInt(drawView.getWidth()), activity);

        final DialogInterface.OnClickListener posListener = getPosListenerForAskingName(drawView, nameInput, activity);

        DialogUtil.getInputDialog(R.string.save_title, R.string.save_message, posListener, nameInput, activity).show();
    }

    public static DialogInterface.OnClickListener getPosListenerForAskingName(final View drawView, final EditText nameInput, final Activity activity) {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                final String signName = nameInput.getText().toString();
                final Sign sign = new Sign();
                if (!CheckUtil.checkSign(signName, drawView, activity)) {
                    return;
                }

                sign.setName(signName);

                drawView.setBackgroundColor(Color.TRANSPARENT);
                final String savedPath = saveDrawSign(drawView, signName, activity);
                drawView.setBackgroundColor(Color.WHITE);

                if (!CheckUtil.checkSign(savedPath, activity)) {
                    return;
                }

                sign.setPath(savedPath);
                addSignShowDialog(sign, activity);

            }
        };

    }


    public static void saveSignedPhoto(final SigningView signingView, final Activity activity) {
        final Sign sign = signingView.getSign();
        final Bitmap photo = signingView.getPhoto(), signBitmap = signingView.getSignBitmap();
        final XY.Float xy = signingView.getXY();
        final SigningThread signThread = new SigningThread(photo, signBitmap, sign.getName(), activity, xy, signingView.getOrgPhotoDimen());
        signThread.start();

    }

    public static String savePicFromBitmap(Bitmap finalBitmap, Activity activity, String dir, String name, boolean toast) {
        final BitmapThread.SaveBitmapThread thread = new BitmapThread.SaveBitmapThread(finalBitmap, activity, dir, name, toast);
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

    public static void addSignShowDialog(final Sign sign, final Activity activity) {
        Log.i("DrawSignActivity : onClickSave", "sign name : " + sign.getName() + " , sign Path : " + sign.getPath());
        SignUtil.addSign(sign);

        AskUtil.getWannaSignDialog(activity).show();

    }


}
