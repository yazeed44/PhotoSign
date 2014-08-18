package net.whitedesert.photosign.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import net.whitedesert.photosign.R;

/**
 * Created by yazeed44 on 8/16/14.
 */
public final class DialogUtil {

    public static final DialogInterface.OnClickListener DISMISS_LISTENER = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
        }
    };

    public static Dialog createErrorDialog(String message, Context context) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage(message);
        dialog.setTitle(R.string.error_title);
        dialog.setIcon(android.R.drawable.ic_dialog_alert);
        dialog.setPositiveButton(R.string.ok, DISMISS_LISTENER);

        return dialog.create();

    }

    public static Dialog createErrorDialog(int resId, Context context) {
        return createErrorDialog(context.getResources().getString(resId), context);
    }


}
