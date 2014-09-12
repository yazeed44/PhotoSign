package net.whitedesert.photosign.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;

import net.whitedesert.photosign.R;

/**
 * Created by yazeed44 on 8/16/14.
 * Class for checking objects , if they have something wrong it will return false (Method) also it will shows Error dialog
 */
public final class CheckUtil {

    private CheckUtil() {
        throw new AssertionError();
    }

    public static boolean checkSign(final String name, final Bitmap bitmap, final Activity activity) {

        final DialogInterface.OnDismissListener askAgain = new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface1) {
                SaveUtil.askNameAndAddSign(bitmap, activity);
            }
        };

        final Dialog errorDialog = DialogUtil.createErrorDialog(R.string.error_title, activity);
        errorDialog.setOnDismissListener(askAgain);


        if (!checkString(name)) {
            errorDialog.setTitle(R.string.error_name_empty);

            errorDialog.show();
            ToastUtil.toastShort(R.string.error_name_empty);
            return false;
        } else if (SignUtil.isDuplicatedSign(name)) {
            errorDialog.setTitle(R.string.error_name_repeated);
            errorDialog.show();
            ToastUtil.toastShort(R.string.error_name_repeated);
            return false;

        }
        return true;

    }

    public static boolean checkSign(final String name, final View drawView, final Activity activity) {
        return checkSign(name, drawView.getDrawingCache(true), activity);
    }

    public static boolean checkSign(final String path, Activity activity) {
        final Dialog errorDialog = DialogUtil.createErrorDialog(R.string.error_save_sign, activity);

        if (!checkString(path)) {
            errorDialog.show();
            ToastUtil.toastLong(R.string.error_save_sign);
            return false;
        }

        return true;

    }

    public static boolean noSigns() {
        return SignUtil.getSigns().isEmpty();
    }

    public static boolean checkSign(final long id) {
        if (id != -1) {
            Log.i("CheckSign", "it has successfully added the sign");
            ToastUtil.toastSavedSignSuccess();
            return true;
        } else {
            Log.e("CheckSign", "Failed to add sign");
            return false;
        }
    }

    public static boolean checkString(String s) {
        return !s.isEmpty();
    }

}
