package net.whitedesert.photosign.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;

import net.whitedesert.photosign.R;

import java.io.File;

/**
 * Created by yazeed44 on 8/16/14.
 * Class for checking objects , if they have something wrong it will return false (Method) also it will shows Error dialog
 */
public final class CheckUtil {

    private CheckUtil() {
        throw new AssertionError();
    }

    public static boolean checkSign(final String name, final Bitmap bitmap, final Activity activity) {

        final MaterialDialog.FullCallback askAgain = new MaterialDialog.FullCallback() {
            @Override
            public void onNeutral(MaterialDialog materialDialog) {

            }

            @Override
            public void onPositive(MaterialDialog materialDialog) {

            }

            @Override
            public void onNegative(MaterialDialog materialDialog) {
                SaveUtil.askNameAndAddSign(bitmap, activity);
            }
        };


        final MaterialDialog.Builder errorDialog = DialogUtil.createErrorDialog(null, activity);
        errorDialog.
                negativeText(R.string.dismiss_btn).
                callback(askAgain);


        if (!checkString(name)) {
            errorDialog.content(R.string.error_name_empty);

            errorDialog.build().show();
            //ToastUtil.toastShort(R.string.error_name_empty);
            return false;
        } else if (SignatureUtil.isDuplicatedSign(name)) {
            errorDialog.content(R.string.error_name_repeated);
            errorDialog.build().show();
            //ToastUtil.toastShort(R.string.error_name_repeated);
            return false;

        }
        return true;

    }

    public static boolean checkSign(final String name, final View drawView, final Activity activity) {
        return checkSign(name, drawView.getDrawingCache(true), activity);
    }

    public static boolean checkSign(final String path, Activity activity) {


        if (!checkString(path)) {
            final MaterialDialog.Builder errorDialog = DialogUtil.createErrorDialog(R.string.error_save_sign, activity);
            errorDialog.build().show();
            //ToastUtil.toastLong(R.string.error_save_sign);
            return false;
        }

        return true;

    }


    public static boolean checkSign(final long id, boolean toast) {
        if (id != -1) {
            Log.i("CheckSign", "it has successfully added the sign");
            if (toast)
                ToastUtil.toastSavedSignSuccess();

            return true;
        } else {
            Log.e("CheckSign", "Failed to add sign");

            return false;
        }
    }

    public static boolean checkSigns(final long[] ids, boolean toast) {
        boolean noError = true;

        for (long id : ids) {
            if (id == -1) {
                noError = false;
                Log.e("checkSigns", "Failed to add sign ");

                if (toast)
                    ToastUtil.toastLong(R.string.error_save_sign);
            }
        }

        return noError;
    }

    public static boolean checkSign(final Signature signature) {
        assert (signature != null);

        if (!checkPath(signature.getPath())) {
            Log.e("CheckSign", "Path is wrong");
            SignatureUtil.deleteSignature(signature, true);
            return false;
        }
        return true;
    }

    public static boolean checkPath(final String path) {
        boolean exists;

        try {
            exists = new File(path).exists();
        } catch (NullPointerException ex) {
            Log.e("checkPath", path + "     Dosen't exist");
            exists = false;
        }

        return exists;
    }

    public static boolean checkString(String s) {
        return !s.isEmpty();
    }

}
