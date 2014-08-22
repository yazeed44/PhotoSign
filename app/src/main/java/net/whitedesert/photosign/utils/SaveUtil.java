package net.whitedesert.photosign.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import net.whitedesert.photosign.R;
import net.whitedesert.photosign.activities.DrawSignActivity;

/**
 * Created by yazeed44 on 8/16/14.
 */
public final class SaveUtil {

    private SaveUtil() {
        throw new AssertionError();
    }

    public static void askNameAndAddSign(final View drawView, final Activity activity) {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(activity);

        dialog.setTitle(R.string.save_title);
        dialog.setMessage(R.string.save_message);

        final EditText nameInput = new EditText(activity);
        dialog.setView(nameInput);
        nameInput.requestFocus();
        dialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String name = nameInput.getText().toString();

                if (!CheckUtil.checkSign(name, drawView, activity)) {
                    return;
                }


                Sign sign = new Sign();
                sign.setName(name);
                String path = PhotoUtil.savePicFromView(drawView, activity, PhotoUtil.SIGNS_DIR, sign.getName());
                sign.setPath(path);
                Log.i("DrawSignActivity : onClickSave", "sign name : " + sign.getName() + " ,  sign Path  :  " + sign.getPath());
                SignUtil.addSign(sign, activity);

            }
        });

        dialog.setNegativeButton(R.string.cancel, DialogUtil.DISMISS_LISTENER);

        dialog.show();
    }

    //the user choose how to sign
    public static void selectMethodSign(final Activity activity) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        ListView view = null;

        final String drawTag = "draw", externalTag = "external", textTag = "text";
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getTag() == drawTag) {
                    Intent i = new Intent(activity, DrawSignActivity.class);
                    activity.startActivity(i);
                } else if (view.getTag() == externalTag) {

                } else if (view.getTag() == textTag) {

                }
            }
        };

        dialog.setIcon(android.R.drawable.ic_input_add);
        dialog.setTitle(R.string.sign_method_title);
        dialog.setMessage(R.string.sign_method_message);
        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.VERTICAL);
        //the methods of creating a sign
        Button text = new Button(activity);
        text.setTag(textTag);
        text.setClickable(false);//for now
        text.setOnClickListener(listener);
        text.setText(R.string.text_sign_btn);
        layout.addView(text);

        Button draw = new Button(activity);
        draw.setOnClickListener(listener);
        draw.setText(R.string.draw_sign_btn);
        draw.setTag(drawTag);
        layout.addView(draw);

        Button external = new Button(activity);
        external.setOnClickListener(listener);
        external.setText(R.string.ext_sign_btn);
        external.setTag(externalTag);
        layout.addView(external);

        DialogUtil.styleAll(R.style.button, activity, text, draw, external);
        dialog.setView(layout);

        dialog.setNegativeButton(R.string.cancel, DialogUtil.DISMISS_LISTENER);
        dialog.show();
    }
}
