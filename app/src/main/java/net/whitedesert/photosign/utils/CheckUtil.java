package net.whitedesert.photosign.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View;

import net.whitedesert.photosign.R;

/**
 * Created by yazeed44 on 8/16/14.
 */
public final class CheckUtil {

    private CheckUtil() {
        throw new AssertionError();
    }

    public static boolean checkSign(final String name, final View drawView, final Activity activity) {

        final DialogInterface.OnDismissListener askAgain = new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface1) {
                SaveUtil.askNameAndAddSign(drawView, activity);
            }
        };

        if (name.length() == 0) {


            Dialog errorDialog = DialogUtil.createErrorDialog(R.string.error_name_empty, activity);
            errorDialog.setOnDismissListener(askAgain);

            errorDialog.show();
            return false;
        } else if (SignUtil.isDuplicatedSign(name, activity)) {
            Dialog error = DialogUtil.createErrorDialog(R.string.error_name_repeated, activity);
            error.setOnDismissListener(askAgain);
            error.show();
            return false;

        }

        return true;

    }


    public static boolean noSigns(final Activity activity) {
        return SignUtil.getSigns(activity).isEmpty();
    }


}
