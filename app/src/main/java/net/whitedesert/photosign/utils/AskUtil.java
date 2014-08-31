package net.whitedesert.photosign.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.view.View;
import android.widget.AdapterView;

import net.whitedesert.photosign.R;
import net.whitedesert.photosign.activities.DrawSignActivity;
import net.whitedesert.photosign.activities.GalleryActivity;
import net.whitedesert.photosign.activities.Types;

import java.util.ArrayList;

/**
 * Created by yazeed44 on 8/24/14.
 */
public final class AskUtil {
    private AskUtil() {
        throw new AssertionError();
    }

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

        AlertDialog.Builder dialog = DialogUtil.getSingleChooseDialog(R.string.wanna_sign_title, R.array.wanna_sign_choices, listener, activity);


        return dialog;
    }

    //the user choose how to sign
    public static void selectMethodSign(final Activity activity) {
        final Resources res = activity.getResources();
        final ArrayList<String> methods = new ArrayList<String>();
        final String text = res.getString(R.string.text_sign);
        methods.add(text);

        final String draw = res.getString(R.string.draw_sign);
        methods.add(draw);

        final String external = res.getString(R.string.ext_sign);
        methods.add(external);

        final AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                String method = methods.get(position);
                if (method.equals(draw)) {
                    Intent i = new Intent(activity, DrawSignActivity.class);
                    activity.startActivity(i);
                } else if (method.equals(text)) {
                    ToastUtil.toastUnsupported(activity);
                } else if (method.equals(external)) {
                    Intent i = new Intent(activity, GalleryActivity.class);
                    i.putExtra(Types.TYPE, Types.OPEN_GALLERY_SINGLE_CHOOSE_TYPE);
                    activity.startActivity(i);
                }
            }
        };

        final AlertDialog.Builder dialog = DialogUtil.getListDialog(methods, R.string.sign_method_title, R.string.sign_method_message, listener, activity);

        dialog.show();
    }


}
