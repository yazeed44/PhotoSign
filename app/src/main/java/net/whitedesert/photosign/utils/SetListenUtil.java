package net.whitedesert.photosign.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import net.whitedesert.photosign.R;
import net.whitedesert.photosign.views.DrawSignView;
import net.whitedesert.photosign.views.SigningView;

import yuku.ambilwarna.AmbilWarnaDialog;

/**
 * Created by yazeed44 on 8/31/14.
 * Class for setting listeners that used more than one time
 */
public final class SetListenUtil {
    private SetListenUtil() {
        throw new AssertionError();
    }

    /**
     * @param opacitySeek , the seek bar that used in opacity seek bar in SigningActivity
     * @param sign        the signature
     * @param signView    , the signing view (where the user choose the place of signature)
     * @param opacityText the text view that will get updated too
     */
    public static void setUpOpacitySeek(final SeekBar opacitySeek, final TextView opacityText, final SigningView signView, final Sign sign) {
        opacitySeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                final Bitmap updatedSign = BitmapUtil.getUpdatedOpacity(sign.getBitmap(signView.getSignWidthHeight()), progress);
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
     * @param opacitySeek  , the seek bar that used in opacity seek bar in draw customize dialog
     * @param opacityText  the text view that will get updated too
     * @param drawSignView , The view that contains paint
     */
    public static void setUpOpacitySeek(final SeekBar opacitySeek, final TextView opacityText, final DrawSignView drawSignView) {
        opacitySeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                final Paint paint = drawSignView.getDrawPaint();
                paint.setAlpha(progress);
                drawSignView.setDrawPaint(paint);
                opacityText.setText(opacityText.getContext().getResources().getText(R.string.draw_opacity_text) + "" + progress);

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


        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                boolean useView = false;
                if (drawView != null) {
                    useView = true;
                }
                String name = nameInput.getText().toString();
                boolean checkName;
                if (useView) {
                    checkName = CheckUtil.checkSign(name, drawView, activity);
                } else {
                    checkName = CheckUtil.checkSign(name, bitmap, activity);
                }

                if (!checkName) {
                    return;
                }


                Sign sign = new Sign();
                sign.setName(name);

                String path;

                if (useView) {
                    drawView.setBackgroundColor(Color.TRANSPARENT);
                    path = SaveUtil.savePicFromView(drawView, activity, SaveUtil.SIGNS_DIR, sign.getName(), false);
                    drawView.setBackgroundColor(Color.WHITE);
                } else {
                    path = SaveUtil.savePicFromBitmap(bitmap, activity, SaveUtil.SIGNS_DIR, sign.getName(), false);
                }
                if (!CheckUtil.checkSign(path, activity)) {
                    return;
                }

                sign.setPath(path);
                Log.i("DrawSignActivity : onClickSave", "sign name : " + sign.getName() + " , sign Path : " + sign.getPath());
                SignUtil.addSign(sign);

                AskUtil.getWannaSignDialog(activity).show();

            }
        };
    }

    public static void setUpChooseColorBtn(final Button chooseColorBtn, final DrawSignView drawSignView, final SignRaw raw, final ImageView preview, final Activity activity) {
        chooseColorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final boolean usePreview;
                if (preview != null)
                    usePreview = true;
                else {
                    usePreview = false;
                }
                AmbilWarnaDialog.OnAmbilWarnaListener listener = new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {


                        if (!usePreview) {
                            final Paint paint = drawSignView.getDrawPaint();
                            paint.setColor(color);
                            drawSignView.setDrawPaint(paint); // update the paint with new color
                        } else {
                            raw.setColor(color);
                            preview.setImageBitmap(SignUtil.createBitmap(raw, true));
                        }
                    }

                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {
                        // cancel was selected by the user

                    }
                };

                final int initalColor;
                if (!usePreview) {
                    initalColor = drawSignView.getDrawPaint().getColor();
                } else {
                    initalColor = raw.getColor();
                }

                final AmbilWarnaDialog chooseColorDialog = new AmbilWarnaDialog(activity, initalColor, listener);

                chooseColorDialog.show();
            }
        });
    }

    /**
     * @param sizeSeek     the seek bar
     * @param sizeText     the text that will get updated
     * @param drawSignView make it null if you have preview only
     * @param preview      make it null if you have drawSignView only
     * @param raw          make it null if you have drawSignView only
     */
    public static void setUpSizeSeek(final SeekBar sizeSeek, final TextView sizeText, final DrawSignView drawSignView, final ImageView preview, final SignRaw raw) {
        sizeSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                boolean usePreview = false;
                if (preview != null)
                    usePreview = true;


                if (!usePreview) {
                    final float px = ViewUtil.convertDpToPixel(progress, drawSignView.getContext());
                    final Paint paint = drawSignView.getDrawPaint();
                    paint.setStrokeWidth(px);
                    drawSignView.setDrawPaint(paint);
                    sizeText.setText(sizeText.getResources().getText(R.string.draw_size_text) + "" + progress);
                } else {
                    final float px = ViewUtil.convertDpToPixel(progress, preview.getContext());
                    Log.i("SetListenUtil : setUpSizeSeek : preview", "PX When changing size value =  " + px);
                    raw.setTextSize(px);
                    sizeText.setText(sizeText.getResources().getString(R.string.text_size_view) + "" + progress);
                    preview.setImageBitmap(SignUtil.createBitmap(raw, true));

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


}
