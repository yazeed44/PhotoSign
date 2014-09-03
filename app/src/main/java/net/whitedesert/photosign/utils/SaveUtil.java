package net.whitedesert.photosign.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import net.whitedesert.photosign.R;
import net.whitedesert.photosign.threads.SigningThread;
import net.whitedesert.photosign.views.SigningView;

/**
 * Created by yazeed44 on 8/16/14.
 */
public final class SaveUtil {

    private SaveUtil() {
        throw new AssertionError();
    }


    public static void askNameAndAddSign(final Bitmap bitmap, final Activity activity) {
        final EditText nameInput = new EditText(activity);
        DialogInterface.OnClickListener posListener = getPosListenerForName(bitmap, null, nameInput, activity);
        AlertDialog.Builder dialog = DialogUtil.getInputDialog(R.string.save_title, R.string.save_message, posListener, nameInput, activity);
        dialog.show();

    }

    public static void askNameAndAddSign(final View drawView, final Activity activity) {


        final EditText nameInput = new EditText(activity);

        final DialogInterface.OnClickListener posListener = getPosListenerForName(null, drawView, nameInput, activity);

        AlertDialog.Builder dialog = DialogUtil.getInputDialog(R.string.save_title, R.string.save_message, posListener, nameInput, activity);

        dialog.show();
    }

    /**
     * @param bitmap    , make it null if you have view only
     * @param drawView  make it null if you have bitmap only
     * @param nameInput EditText
     * @return the posListener
     */
    public static DialogInterface.OnClickListener getPosListenerForName(final Bitmap bitmap, final View drawView, final EditText nameInput, final Activity activity) {

        DialogInterface.OnClickListener posListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                boolean useView = false;
                if (drawView != null) {
                    useView = true;
                }
                String name = nameInput.getText().toString();
                if (useView) {
                    if (!CheckUtil.checkSign(name, drawView, activity)) {
                        return;
                    }
                } else {
                    if (!CheckUtil.checkSign(name, bitmap, activity)) {
                        return;
                    }
                }
                Sign sign = new Sign();
                sign.setName(name);

                String path;

                if (useView) {
                    path = PhotoUtil.savePicFromView(drawView, activity, PhotoUtil.SIGNS_DIR, sign.getName(), false);
                } else {
                    path = PhotoUtil.savePicFromBitmap(bitmap, activity, PhotoUtil.SIGNS_DIR, sign.getName(), false);
                }
                if (!CheckUtil.checkSign(path, activity)) {
                    return;
                }

                sign.setPath(path);
                Log.i("DrawSignActivity : onClickSave", "sign name : " + sign.getName() + " , sign Path : " + sign.getPath());
                SignUtil.addSign(sign, activity);
                AskUtil.getWannaSignDialog(activity).show();

            }
        };

        return posListener;
    }


    public static void saveSignedPhoto(final SigningView signingView, final Activity activity) {
        final Sign sign = signingView.getSign();
        final Bitmap photo = signingView.getPhoto(), signBitmap = signingView.getSignBitmap();
        final XY.Float xy = signingView.getXY();
        SigningThread signThread = new SigningThread(photo, signBitmap, sign.getName(), activity, xy);
        signThread.start();
        ThreadUtil.join(signThread);
        final String path = signThread.getPath();
        if (!CheckUtil.checkSign(path, activity)) {
            return;
        }

    }


}
