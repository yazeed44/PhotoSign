package net.whitedesert.photosign.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

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

    public static void askNameAndAddSign(final Bitmap bitmap, final Activity activity) {

        String name = DialogUtil.askUser(R.string.save_title, R.string.save_message, activity);
        if (name == null) {
            askNameAndAddSign(bitmap, activity);
        }
        if (!CheckUtil.checkSign(name, bitmap, activity)) {
            askNameAndAddSign(bitmap, activity);
                }

                Sign sign = new Sign();
                sign.setName(name);
        String path = PhotoUtil.savePicFromBitmap(bitmap, activity, PhotoUtil.SIGNS_DIR, sign.getName());
                sign.setPath(path);
                Log.i("DrawSignActivity : onClickSave", "sign name : " + sign.getName() + " ,  sign Path  :  " + sign.getPath());
                SignUtil.addSign(sign, activity);

    }

    public static void askNameAndAddSign(final View drawView, final Activity activity) {
        askNameAndAddSign(drawView.getDrawingCache(true), activity);
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
                    ToastUtil.toastUnsupported(activity);
                }
            }
        };

        final AlertDialog.Builder dialog = DialogUtil.getListDialog(methods, R.string.sign_method_title, R.string.sign_method_message, listener, activity);

        dialog.show();
    }
}
