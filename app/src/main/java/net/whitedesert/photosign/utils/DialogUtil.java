package net.whitedesert.photosign.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;

import net.whitedesert.photosign.R;
import net.whitedesert.photosign.ui.DrawSignActivity;
import net.whitedesert.photosign.ui.TextSignActivity;
import net.yazeed44.imagepicker.PickerActivity;

/**
 * Created by yazeed44 on 8/16/14.
 * Class for creating simple dialogs
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


    public static MaterialDialog.Builder createDialog(String title, String msg, final Activity activity) {


        dialog = new MaterialDialog.Builder(activity)
                .title(title)
                .content(msg)
                .theme(Theme.LIGHT)
        ;


        return dialog;
    }

    public static MaterialDialog.Builder createDialog(int titleId, int msgId, final Activity activity) {
        r = activity.getResources();
        return createDialog(r.getString(titleId), r.getString(msgId), activity);
    }


    public static MaterialDialog.Builder createErrorDialog(String msg, final Activity activity) {

        final String title = r.getString(R.string.error_title);
        return createDialog(title, msg, activity);


    }


    public static MaterialDialog.Builder createErrorDialog(int msgId, final Activity activity) {
        r = activity.getResources();
        return createErrorDialog(r.getString(msgId), activity);

    }


    public static MaterialDialog.Builder getSingleChooseDialog(String title, String[] choices, final Activity activity) {
        MaterialDialog.Builder dialog = createDialog(title, null, activity);
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


    //the user to make another signature or sign a photo
    public static MaterialDialog.Builder createWannaSignDialog(final Activity activity) {
        final Resources r = activity.getResources();
        final String[] choices = r.getStringArray(R.array.wanna_sign_choices);
        final String makeSign = choices[0];
        final String signPhoto = choices[1];

        final MaterialDialog.ListCallback listener = new MaterialDialog.ListCallback() {
            @Override
            public void onSelection(MaterialDialog materialDialog, View view, int which, CharSequence text) {
                if (text.equals(makeSign)) {
                    createChooseMethodToSignDialog(activity);
                } else if (text.equals(signPhoto)) {
                    SigningUtil.openGalleryToSignSingle(activity);
                }
            }
        };


        return getSingleChooseDialog(r.getString(R.string.wanna_sign_title), choices, activity)
                .itemsCallbackSingleChoice(-1, listener);


    }

    //the user choose how to sign
    public static MaterialDialog.Builder createChooseMethodToSignDialog(final Activity activity) {
        final Resources res = activity.getResources();
        final String[] choices = res.getStringArray(R.array.select_method_signature_choices);

        //Methods to insert a signature
        final String draw = choices[0];
        final String external = choices[1];
        final String text = choices[2];


        final MaterialDialog.ListCallback listener = new MaterialDialog.ListCallback() {
            @Override
            public void onSelection(MaterialDialog materialDialog, View view, int which, CharSequence chosenMethod) {

                if (chosenMethod.equals(draw)) {
                    Intent i = new Intent(activity, DrawSignActivity.class);
                    activity.startActivity(i);
                } else if (chosenMethod.equals(text)) {
                    Intent i = new Intent(activity, TextSignActivity.class);
                    activity.startActivity(i);
                } else if (chosenMethod.equals(external)) {
                    final Intent i = new Intent(activity, PickerActivity.class);
                    activity.startActivityForResult(i, PickerActivity.PICK_REQUEST);
                }
            }
        };

        return getSingleChooseDialog(res.getString(R.string.sign_method_title), choices, activity)
                .itemsCallbackSingleChoice(-1, listener)
                .positiveText(R.string.choose_btn)
                .negativeText(R.string.cancel_btn);


    }
}
