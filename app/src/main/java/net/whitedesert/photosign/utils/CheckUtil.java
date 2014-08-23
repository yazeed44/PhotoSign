package net.whitedesert.photosign.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.View;

import net.whitedesert.photosign.R;

/**
 * Created by yazeed44 on 8/16/14.
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

        final Dialog errorDialog = DialogUtil.createErrorDialog(R.string.error_name_empty, activity);
        errorDialog.setOnDismissListener(askAgain);


        if (name.length() == 0) {
            errorDialog.show();
            return false;
        } else if (SignUtil.isDuplicatedSign(name, activity)) {
            errorDialog.show();
            return false;

        }
        return true;

    }

    public static boolean checkSign(final String name, final View drawView, final Activity activity) {
        return checkSign(name, drawView.getDrawingCache(true), activity);
    }

    public static boolean noSigns(final Activity activity) {
        return SignUtil.getSigns(activity).isEmpty();
    }


}
