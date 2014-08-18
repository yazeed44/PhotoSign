package net.whitedesert.photosign.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import net.whitedesert.photosign.R;

/**
 * Created by yazeed44 on 8/16/14.
 */
public final class SaveUtil {

    public static void askNameAndAddSign(final View drawView, final Activity activity) {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(activity);

        dialog.setTitle(R.string.save_title);
        dialog.setMessage(R.string.save_message);

        final EditText nameInput = new EditText(activity);
        dialog.setView(nameInput);
        dialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String name = nameInput.getText().toString();

                if (!CheckUtil.checkSign(name, drawView, activity)) {
                    return;
                }

                String path = PhotoUtil.savePicFromView(drawView, activity, PhotoUtil.SIGNS_DIR);
                Sign sign = new Sign();
                sign.setName(name);
                sign.setPath(path);
                Log.i("DrawSignActivity : onClickSave", "sign name : " + sign.getName() + " ,  sign Path  :  " + sign.getPath());
                SignUtil.addSign(sign, activity);

            }
        });

        dialog.setNegativeButton(R.string.cancel, DialogUtil.DISMISS_LISTENER);

        dialog.show();
    }
}
