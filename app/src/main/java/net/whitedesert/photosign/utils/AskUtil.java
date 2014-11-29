package net.whitedesert.photosign.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;

import net.whitedesert.photosign.R;
import net.whitedesert.photosign.activities.DrawSignActivity;
import net.whitedesert.photosign.activities.GalleryActivity;
import net.whitedesert.photosign.activities.TextSignActivity;
import net.whitedesert.photosign.activities.Types;

/**
 * Created by yazeed44 on 8/24/14.
 * Class for asking user, example : give the user choices and make user choose one of them
 */
public final class AskUtil {
    private AskUtil() {
        throw new AssertionError();
    }


    //the user to make another signature or sign a photo
    public static MaterialDialog.Builder getWannaSignDialog(final Activity activity) {
        final Resources r = activity.getResources();
        final String[] choices = r.getStringArray(R.array.wanna_sign_choices);
        final String makeSign = choices[0];
        final String signPhoto = choices[1];

        final MaterialDialog.ListCallback listener = new MaterialDialog.ListCallback() {
            @Override
            public void onSelection(MaterialDialog materialDialog, View view, int which, CharSequence text) {
                if (text.equals(makeSign)) {
                    selectMethodSign(activity);
                } else if (text.equals(signPhoto)) {
                    SigningUtil.openGalleryToSignSingle(activity);
                }
            }
        };


        final MaterialDialog.Builder wannaSignDialog = DialogUtil.getSingleChooseDialog(r.getString(R.string.wanna_sign_title), choices, activity)
                .itemsCallbackSingleChoice(-1, listener);


        return wannaSignDialog;
    }

    //the user choose how to sign
    public static void selectMethodSign(final Activity activity) {
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
                    Intent i = new Intent(activity, GalleryActivity.class);
                    i.putExtra(Types.TYPE, Types.OPEN_GALLERY_SINGLE_CHOOSE_TYPE);
                    activity.startActivity(i);
                }
            }
        };

        DialogUtil.getSingleChooseDialog(res.getString(R.string.sign_method_title), choices, activity)
                .itemsCallbackSingleChoice(-1, listener)
                .positiveText(R.string.choose_btn)
                .negativeText(R.string.cancel_btn)
                .build()
                .show();


    }


}
