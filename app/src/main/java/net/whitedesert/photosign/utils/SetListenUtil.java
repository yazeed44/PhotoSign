package net.whitedesert.photosign.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import net.whitedesert.photosign.R;
import net.whitedesert.photosign.views.SigningView;

/**
 * Created by yazeed44 on 8/31/14.
 */
public final class SetListenUtil {
    private SetListenUtil() {
        throw new AssertionError();
    }

    /**
     * @param opacitySeek , the seek bar that used in opacity
     * @param sign        the signature
     * @param opacityText the text view that will get updated too
     */
    public static void setUpOpacitySeek(final SeekBar opacitySeek, final TextView opacityText, final SigningView signView, final Sign sign) {
        opacitySeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Bitmap updatedSign = BitmapUtil.updateOpacity(sign.getBitmap(), progress);
                opacityText.setText(opacityText.getResources().getString(R.string.opacity_text) + " : " + progress);
                signView.setSign(updatedSign);
                signView.invalidate();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    /**
     * @param bitmap    , make it null if you have view only
     * @param drawView  make it null if you have bitmap only
     * @param nameInput EditText
     * @return the positive listener for clicking OK Button, in the dialog for asking name
     */
    public static DialogInterface.OnClickListener getPosListenerForName(final Bitmap bitmap, final View drawView, final EditText nameInput, final Activity activity) {

        DialogInterface.OnClickListener posListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                boolean useView = false;
                if (drawView != null) {
                    useView = true;
                }
                String name = nameInput.getText().toString();
                if (useView) {
                    if (!CheckUtil.checkSign(name, drawView, activity)) {
                        return;
                    }
                } else {
                    if (!CheckUtil.checkSign(name, bitmap, activity)) {
                        return;
                    }
                }
                Sign sign = new Sign();
                sign.setName(name);

                String path;

                if (useView) {
                    path = PhotoUtil.savePicFromView(drawView, activity, PhotoUtil.SIGNS_DIR, sign.getName(), false);
                } else {
                    path = PhotoUtil.savePicFromBitmap(bitmap, activity, PhotoUtil.SIGNS_DIR, sign.getName(), false);
                }
                if (!CheckUtil.checkSign(path, activity)) {
                    return;
                }

                sign.setPath(path);
                Log.i("DrawSignActivity : onClickSave", "sign name : " + sign.getName() + " , sign Path : " + sign.getPath());
                SignUtil.addSign(sign, activity);
                AskUtil.getWannaSignDialog(activity).show();

            }
        };

        return posListener;
    }


}
