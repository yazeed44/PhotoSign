package net.whitedesert.photosign.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import net.whitedesert.photosign.R;
import net.whitedesert.photosign.activities.DrawSignActivity;
import net.whitedesert.photosign.activities.GalleryActivity;
import net.whitedesert.photosign.activities.Types;

import java.util.ArrayList;

/**
 * Created by yazeed44 on 8/16/14.
 */
public final class SaveUtil {

    private SaveUtil() {
        throw new AssertionError();
    }


    public static void askNameAndAddSign(final Bitmap bitmap, final Activity activity) {
        final EditText nameInput = new EditText(activity);
        DialogInterface.OnClickListener posListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String name = nameInput.getText().toString();
                if (!CheckUtil.checkSign(name, bitmap, activity)) {
                    return;
                }
                Sign sign = new Sign();
                sign.setName(name);
                String path = PhotoUtil.savePicFromBitmap(bitmap, activity, PhotoUtil.SIGNS_DIR, sign.getName(), false);
                if (!CheckUtil.checkSign(path, activity)) {
                    return;
                }

                sign.setPath(path);
                Log.i("DrawSignActivity : onClickSave", "sign name : " + sign.getName() + " , sign Path : " + sign.getPath());
                SignUtil.addSign(sign, activity);
            }
        };

        AlertDialog.Builder dialog = DialogUtil.getInput(R.string.save_title, R.string.save_message, posListener, nameInput, activity);
        dialog.show();

    }

    public static void askNameAndAddSign(final View drawView, final Activity activity) {


        final EditText nameInput = new EditText(activity);

        DialogInterface.OnClickListener posListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String name = nameInput.getText().toString();
                if (!CheckUtil.checkSign(name, drawView, activity)) {
                    return;
                }
                Sign sign = new Sign();
                sign.setName(name);
                String path = PhotoUtil.savePicFromView(drawView, activity, PhotoUtil.SIGNS_DIR, sign.getName(), false);
                if (!CheckUtil.checkSign(path, activity)) {
                    return;
                }

                sign.setPath(path);
                Log.i("DrawSignActivity : onClickSave", "sign name : " + sign.getName() + " , sign Path : " + sign.getPath());
                SignUtil.addSign(sign, activity);
            }
        };

        AlertDialog.Builder dialog = DialogUtil.getInput(R.string.save_title, R.string.save_message, posListener, nameInput, activity);

        dialog.show();
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
                    i.putExtra(Types.TYPE, Types.OPEN_GALLERY_MULTI_CHOOSE_TYPE);
                    activity.startActivity(i);
                }
            }
        };

        final AlertDialog.Builder dialog = DialogUtil.getListDialog(methods, R.string.sign_method_title, R.string.sign_method_message, listener, activity);

        dialog.show();
    }


}
