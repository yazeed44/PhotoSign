package net.whitedesert.photosign.ui;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import net.whitedesert.photosign.R;
import net.whitedesert.photosign.utils.SaveUtil;
import net.whitedesert.photosign.utils.ViewUtil;

import yuku.ambilwarna.AmbilWarnaDialog;

/**
 * Created by yazeed44 on 8/15/14.
 */
public class DrawSignActivity extends BaseActivity {


    private final AmbilWarnaDialog.OnAmbilWarnaListener chooseColorListener = new AmbilWarnaDialog.OnAmbilWarnaListener() {
        @Override
        public void onCancel(AmbilWarnaDialog dialog) {

        }

        @Override
        public void onOk(AmbilWarnaDialog dialog, int color) {

            draw.getDrawPaint().setColor(color); // update color
        }
    };
    private final SeekBar.OnSeekBarChangeListener widthListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            final float px = ViewUtil.convertDpToPixel(progress, draw.getContext());
            final Paint paint = draw.getDrawPaint();
            paint.setStrokeWidth(px);
            draw.setDrawPaint(paint);
            widthText.setText(widthText.getResources().getText(R.string.draw_size_text) + "" + progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };
    private DrawSignView draw;
    private View brushCustomizeLayout;
    private String brushCustomizeTitle;
    private TextView widthText;
    private SeekBar widthSeek;
    private TextView opacityText;
    private String opacityString;
    private final SeekBar.OnSeekBarChangeListener opacityListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            draw.getDrawPaint().setAlpha(progress);

            opacityText.setText(opacityString + progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };
    private SeekBar opacitySeek;


    @Override
    public void onCreate(Bundle savedInstance) {

        super.onCreate(savedInstance);

        setContentView(R.layout.activity_sign_drawing);
        draw = (DrawSignView) this.findViewById(R.id.drawView);


        getSupportActionBar().setTitle(R.string.sign_draw_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    private void initializeBrushCustomizeDialog() {
        brushCustomizeLayout = this.getLayoutInflater().inflate(R.layout.dialog_draw_sign_customize, null);


        opacityText = (TextView) brushCustomizeLayout.findViewById(R.id.drawOpacityText);
        opacitySeek = (SeekBar) brushCustomizeLayout.findViewById(R.id.drawOpacitySeek);
        opacitySeek.setOnSeekBarChangeListener(opacityListener);


        widthText = (TextView) brushCustomizeLayout.findViewById(R.id.drawSizeText);
        widthSeek = (SeekBar) brushCustomizeLayout.findViewById(R.id.baseSizeSeek);
        widthSeek.setOnSeekBarChangeListener(widthListener);

        opacityString = getResources().getString(R.string.draw_opacity_text);
        brushCustomizeTitle = getResources().getString(R.string.draw_sign_customize_title);


    }


    //When user clicks on customizing Brush
    public void onClickBrushCustomize() {


        initializeBrushCustomizeDialog();


        final MaterialDialog.Builder dialog = ViewUtil.createDialog(brushCustomizeTitle, "", this);
        setUpSize(widthText, widthSeek);
        setUpOpacity(opacityText, opacitySeek);
        dialog.customView(brushCustomizeLayout);

        dialog.build().show();
    }


    private void setUpSize(final TextView sizeText, final SeekBar widthSeek) {
        widthSeek.setProgress((int) ViewUtil.convertPixelsToDp(draw.getDrawPaint().getStrokeWidth(), this));
        sizeText.setText(sizeText.getResources().getText(R.string.draw_size_text) + "" + widthSeek.getProgress());


    }


    private void setUpOpacity(final TextView opacityText, final SeekBar opacitySeek) {

        opacitySeek.setProgress(draw.getDrawPaint().getAlpha());
        opacityText.setText(opacityString + opacitySeek.getProgress());


    }


    private void onClickChooseColor() {


        final int initalColor = draw.getDrawPaint().getColor();
        new AmbilWarnaDialog(DrawSignActivity.this, initalColor, chooseColorListener).show();
    }

    public void onClickReset() {
        draw.reset();
    }

    public void onClickDone() {
        SaveUtil.saveSignature(draw, this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_draw_sign, menu);
        getMenuInflater().inflate(R.menu.menu_done, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.menu_done:
                onClickDone();
                return true;

            case R.id.menu_redo_drawing:
                onClickReset();
                return true;

            case R.id.menu_brush_settings:
                onClickBrushCustomize();
                return true;

            case R.id.menu_brush_color:
                onClickChooseColor();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
