package net.whitedesert.photosign.activities;

import android.app.AlertDialog;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import net.whitedesert.photosign.R;
import net.whitedesert.photosign.utils.DialogUtil;
import net.whitedesert.photosign.utils.SaveUtil;
import net.whitedesert.photosign.utils.ViewUtil;
import net.whitedesert.photosign.views.DrawSignView;

import yuku.ambilwarna.AmbilWarnaDialog;

/**
 * Created by yazeed44 on 8/15/14.
 */
public class DrawSignActivity extends AdActivity {

    private DrawSignView draw;

    @Override
    public void onCreate(Bundle savedInstance) {

        super.onCreate(savedInstance);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sign_drawing);
        draw = (DrawSignView) this.findViewById(R.id.drawView);


    }


    //When user clicks on customizing pen
    public void onClickPenCustomize(View view) {
        final View drawSignCustomize = this.getLayoutInflater().inflate(R.layout.dialog_draw_sign_customize, null);

        final Button chooseColorBtn = (Button) drawSignCustomize.findViewById(R.id.baseChooseColorBtn);
        setUpChooseColorBtn(chooseColorBtn);

        final TextView opacityText = (TextView) drawSignCustomize.findViewById(R.id.drawOpacityText);
        final SeekBar opacitySeek = (SeekBar) drawSignCustomize.findViewById(R.id.drawOpacitySeek);
        setUpOpacity(opacityText, opacitySeek);


        final TextView sizeText = (TextView) drawSignCustomize.findViewById(R.id.drawSizeText);
        final SeekBar sizeSeek = (SeekBar) drawSignCustomize.findViewById(R.id.baseSizeSeek);
        setUpSize(sizeText, sizeSeek);


        final String title = this.getResources().getString(R.string.draw_sign_customize_title);
        final AlertDialog.Builder dialog = DialogUtil.initDialog(title, android.R.drawable.ic_dialog_info, false, this);
        dialog.setView(drawSignCustomize);

        dialog.show();
    }


    private void setUpSize(final TextView sizeText, final SeekBar sizeSeek) {
        sizeSeek.setProgress((int) ViewUtil.convertPixelsToDp(draw.getDrawPaint().getStrokeWidth(), this));
        sizeText.setText(sizeText.getResources().getText(R.string.draw_size_text) + "" + sizeSeek.getProgress());


        sizeSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                final float px = ViewUtil.convertDpToPixel(progress, draw.getContext());
                final Paint paint = draw.getDrawPaint();
                paint.setStrokeWidth(px);
                draw.setDrawPaint(paint);
                sizeText.setText(sizeText.getResources().getText(R.string.draw_size_text) + "" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    private void setUpOpacity(final TextView opacityText, final SeekBar opacitySeek) {
        final String opacityString = opacityText.getContext().getResources().getString(R.string.draw_opacity_text);
        opacitySeek.setProgress(draw.getDrawPaint().getAlpha());
        opacityText.setText(opacityString + opacitySeek.getProgress());

        opacitySeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                draw.getDrawPaint().setAlpha(progress);

                opacityText.setText(opacityString + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    private void setUpChooseColorBtn(final Button chooseColorBtn) {
        chooseColorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AmbilWarnaDialog.OnAmbilWarnaListener listener = new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {

                    }

                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {

                        draw.getDrawPaint().setColor(color); // update color
                    }
                };

                final int initalColor = draw.getDrawPaint().getColor();
                new AmbilWarnaDialog(DrawSignActivity.this, initalColor, listener).show();

            }
        });

    }

    public void onClickReset(View view) {
        draw.reset();
    }

    public void onClickDone(View view) {
        SaveUtil.askNameAndAddSign(draw, this);

    }


}
