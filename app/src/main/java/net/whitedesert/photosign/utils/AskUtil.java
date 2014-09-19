package net.whitedesert.photosign.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import net.whitedesert.photosign.R;
import net.whitedesert.photosign.activities.DrawSignActivity;
import net.whitedesert.photosign.activities.GalleryActivity;
import net.whitedesert.photosign.activities.Types;
import net.whitedesert.photosign.views.DrawSignView;

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

    public static AlertDialog.Builder getDrawSignCustomizeDialog(final DrawSignView drawSignView, final Activity activity) {

        final View drawSignCustomize = activity.getLayoutInflater().inflate(R.layout.draw_sign_customize, null);

        final Button chooseColorBtn = (Button) drawSignCustomize.findViewById(R.id.chooseColorBtn);
        SetListenUtil.setUpChooseColorBtn(chooseColorBtn, drawSignView, activity);

        final TextView opacityText = (TextView) drawSignCustomize.findViewById(R.id.drawOpacityText);

        final SeekBar opacitySeek = (SeekBar) drawSignCustomize.findViewById(R.id.drawOpacitySeek);
        opacitySeek.setProgress(drawSignView.getDrawPaint().getAlpha());
        SetListenUtil.setUpOpacitySeek(opacitySeek, opacityText, drawSignView);

        opacityText.setText(opacityText.getContext().getResources().getText(R.string.draw_opacity_text) + "" + opacitySeek.getProgress());

        final TextView sizeText = (TextView) drawSignCustomize.findViewById(R.id.drawSizeText);

        final SeekBar sizeSeek = (SeekBar) drawSignCustomize.findViewById(R.id.baseSizeSeek);
        sizeSeek.setProgress((int) ViewUtil.convertPixelsToDp(drawSignView.getDrawPaint().getStrokeWidth(), activity));
        SetListenUtil.setUpSizeSeek(sizeSeek, sizeText, drawSignView);

        sizeText.setText(sizeText.getResources().getText(R.string.draw_size_text) + "" + sizeSeek.getProgress() + "dp");

        final AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setCustomTitle(ViewUtil.getBlackTitle(R.string.draw_sign_customize_title, activity));
        dialog.setView(drawSignCustomize);

        return dialog;
    }

}
