package net.whitedesert.photosign.utils;

import android.app.Activity;
import android.content.res.Resources;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;

import net.whitedesert.photosign.R;

/**
 * Created by yazeed44 on 8/16/14.
 * Class for making simple dialogs
 */
public final class DialogUtil {

    public static final MaterialDialog.SimpleCallback DISMISS_CALLBACK = new MaterialDialog.FullCallback() {
        @Override
        public void onNeutral(MaterialDialog materialDialog) {


        }

        @Override
        public void onPositive(MaterialDialog materialDialog) {

        }

        @Override
        public void onNegative(MaterialDialog materialDialog) {
            materialDialog.dismiss();
        }
    };


    public static MaterialDialog.Builder dialog; //Used for optimzation

    public static Resources r;


    private DialogUtil() {
        throw new AssertionError();
    }


    public static MaterialDialog.Builder initDialog(String title, String msg, final Activity activity) {


        dialog = new MaterialDialog.Builder(activity)
                .title(title)
                .content(msg)
                .theme(Theme.LIGHT)
        ;


        return dialog;
    }

    public static MaterialDialog.Builder initDialog(int titleId, int msgId, final Activity activity) {
        r = activity.getResources();
        return initDialog(r.getString(titleId), r.getString(msgId), activity);
    }


    public static MaterialDialog.Builder createErrorDialog(String msg, final Activity activity) {

        String title = r.getString(R.string.error_title);
        final MaterialDialog.Builder dialog = initDialog(title, msg, activity);


        return dialog;

    }


    public static MaterialDialog.Builder createErrorDialog(int msgId, final Activity activity) {
        r = activity.getResources();
        return createErrorDialog(r.getString(msgId), activity);

    }


    public static MaterialDialog.Builder getSingleChooseDialog(String title, String[] choices, final Activity activity) {
        MaterialDialog.Builder dialog = initDialog(title, null, activity);
        //the choices won't appear if you set a message !!

        dialog.items(choices)
                .negativeText(R.string.cancel_btn)
                .callback(DISMISS_CALLBACK);


        return dialog;
    }

    public static MaterialDialog.Builder getSingleChooseDialog(int titleId, int choicesId, final Activity activity) {

        r = activity.getResources();
        String title = r.getString(titleId);
        String[] choices = r.getStringArray(choicesId);
        return getSingleChooseDialog(title, choices, activity);
    }


}
