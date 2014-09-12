package net.whitedesert.photosign.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;

import net.whitedesert.photosign.R;
import net.whitedesert.photosign.activities.DrawSignActivity;
import net.whitedesert.photosign.activities.GalleryActivity;
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
    public static AlertDialog.Builder getWannaSignDialog(final Activity activity) {
        final Resources r = activity.getResources();
        final String[] choices = r.getStringArray(R.array.wanna_sign_choices);
        final String makeSign = choices[0];
        final String signPhoto = choices[1];

        final DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                String choice = choices[which];
                if (choice.equals(makeSign)) {
                    AskUtil.selectMethodSign(activity);
                } else if (choice.equals(signPhoto)) {
                    SigningUtil.openGalleryToSignSingle(activity);
                }
            }
        };


        return DialogUtil.getSingleChooseDialog(R.string.wanna_sign_title, R.array.wanna_sign_choices, listener, activity);
    }

    //the user choose how to sign
    public static void selectMethodSign(final Activity activity) {
        final Resources res = activity.getResources();
        final String[] choices = res.getStringArray(R.array.select_method_signature_choices);

        final String draw = choices[0];
        final String external = choices[1];
        final String text = choices[2];


        final DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {


            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                String method = choices[which];
                if (method.equals(draw)) {
                    Intent i = new Intent(activity, DrawSignActivity.class);
                    activity.startActivity(i);
                } else if (method.equals(text)) {
                    ToastUtil.toastUnsupported();
                    //TODO
                } else if (method.equals(external)) {
                    Intent i = new Intent(activity, GalleryActivity.class);
                    i.putExtra(Types.TYPE, Types.OPEN_GALLERY_SINGLE_CHOOSE_TYPE);
                    activity.startActivity(i);
                }
            }
        };


        DialogUtil.getSingleChooseDialog(R.string.sign_method_message, R.array.select_method_signature_choices, listener, activity).show();


    }


}
