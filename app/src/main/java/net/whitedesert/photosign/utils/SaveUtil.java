package net.whitedesert.photosign.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
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
        DialogInterface.OnClickListener posListener = SetListenUtil.getPosListenerForName(bitmap, null, nameInput, activity);
        AlertDialog.Builder dialog = DialogUtil.getInputDialog(R.string.save_title, R.string.save_message, posListener, nameInput, activity);
        dialog.show();

    }

    public static void askNameAndAddSign(final View drawView, final Activity activity) {


        final EditText nameInput = new EditText(activity);

        final DialogInterface.OnClickListener posListener = SetListenUtil.getPosListenerForName(null, drawView, nameInput, activity);

        AlertDialog.Builder dialog = DialogUtil.getInputDialog(R.string.save_title, R.string.save_message, posListener, nameInput, activity);

        dialog.show();
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
            //TODO
        }

    }


}
