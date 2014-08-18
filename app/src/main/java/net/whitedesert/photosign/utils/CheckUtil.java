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

    public static boolean checkSign(final String name, final View drawView, final Activity activity) {


        if (name.length() == 0) {


            Dialog errorDialog = DialogUtil.createErrorDialog(R.string.error_name_empty, activity);
            errorDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface1) {
                    SaveUtil.askNameAndAddSign(drawView, activity);
                }
            });
            errorDialog.show();
            return false;
        } else if (SignUtil.isDuplicatedSign(name, activity)) {
            Dialog error = DialogUtil.createErrorDialog(R.string.error_name_repeated, activity);
            error.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    SaveUtil.askNameAndAddSign(drawView, activity);
                }
            });
            error.show();
            return false;

        }

        return true;

    }
}
