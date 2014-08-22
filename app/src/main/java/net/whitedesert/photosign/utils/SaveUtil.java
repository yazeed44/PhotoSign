package net.whitedesert.photosign.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import net.whitedesert.photosign.R;
import net.whitedesert.photosign.activities.DrawSignActivity;

import java.util.ArrayList;

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
        final Resources res = activity.getResources();
        final ArrayList<String> methods = new ArrayList<String>();
        final String text = res.getString(R.string.text_sign_btn);
        methods.add(text);

        final String draw = res.getString(R.string.draw_sign_btn);
        methods.add(draw);

        final String external = res.getString(R.string.ext_sign_btn);
        methods.add(external);

        final AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(android.widget.AdapterView<?> adapterView, android.view.View view, int position, long id) {
                String method = methods.get(position);

                if (method.equals(text)) {

                } else if (method.equals(draw)) {
                    Intent i = new Intent(activity, DrawSignActivity.class);
                    activity.startActivity(i);
                } else if (method.equals(external)) {

                }
            }

            ;

        };

        final AlertDialog.Builder dialog = DialogUtil.getListDialog(methods, listener, activity);
        dialog.setTitle(R.string.sign_method_title);
        dialog.setMessage(R.string.sign_method_message);
        dialog.show();
    }
}
