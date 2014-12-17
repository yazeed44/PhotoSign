package net.whitedesert.photosign.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;

import net.whitedesert.photosign.R;
import net.whitedesert.photosign.utils.SaveUtil;
import net.whitedesert.photosign.utils.ViewUtil;


/**
 * Created by yazeed44 on 8/15/14.
 */
public class DrawSignatureActivity extends BaseActivity {


    private DrawSignatureView mDrawSignatureView;
    private int mNewColor = -1;

    @Override
    public void onCreate(Bundle savedInstance) {

        super.onCreate(savedInstance);

        setContentView(R.layout.activity_sign_drawing);
        mDrawSignatureView = (DrawSignatureView) this.findViewById(R.id.draw_signature_view);


        getSupportActionBar().setTitle(R.string.sign_draw_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }


    //When user clicks on customizing Brush
    public void onClickBrushCustomize() {


        final String title = getResources().getString(R.string.draw_sign_customize_title);

        final MaterialDialog.Builder dialog = ViewUtil.createDialog(title, null, this);


        dialog.customView(createBrushCustomizeView())
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .callback(new MaterialDialog.Callback() {

                    @Override
                    public void onNegative(MaterialDialog materialDialog) {
                        materialDialog.dismiss();
                    }

                    @Override
                    public void onPositive(MaterialDialog materialDialog) {

                        final int newColor = ((ColorPicker) materialDialog.getCustomView().findViewById(R.id.color_picker)).getColor();

                        final String newWidthString = ((EditText) materialDialog.getCustomView().findViewById(R.id.brush_width_edit)).getText().toString();
                        final int newWidth = Integer.parseInt(newWidthString);

                        mDrawSignatureView.getDrawPaint().setStrokeWidth(newWidth);

                        if (mNewColor != -1) {
                            mDrawSignatureView.getDrawPaint().setColor(mNewColor);
                        }
                    }
                })
                .show()
        ;


    }

    private View createBrushCustomizeView() {
        final View brushCustomizeLayout = this.getLayoutInflater().inflate(R.layout.dialog_draw_sign_customize, (ViewGroup) findViewById(R.id.container), false);


        final int width = (int) mDrawSignatureView.getDrawPaint().getStrokeWidth();

        final EditText widthEditText = (EditText) brushCustomizeLayout.findViewById(R.id.brush_width_edit);
        widthEditText.setText(width + "");

        final SeekBar widthBar = (SeekBar) brushCustomizeLayout.findViewById(R.id.brush_width_bar);
        widthBar.setProgress(width);


        widthBar.setOnSeekBarChangeListener(createWidthListener(widthEditText));
        widthEditText.addTextChangedListener(createWatcherForWidthEdit(widthBar));


        final OpacityBar opacityBar = (OpacityBar) brushCustomizeLayout.findViewById(R.id.opacity_bar);


        final SVBar svBar = (SVBar) brushCustomizeLayout.findViewById(R.id.sv_bar);

        final ColorPicker colorPicker = (ColorPicker) brushCustomizeLayout.findViewById(R.id.color_picker);


        colorPicker.addSVBar(svBar);
        colorPicker.addOpacityBar(opacityBar);

        colorPicker.setOldCenterColor(mDrawSignatureView.getDrawPaint().getColor());

        colorPicker.setOnColorSelectedListener(new ColorPicker.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                mNewColor = color;
            }
        });

        return brushCustomizeLayout;
    }


    private SeekBar.OnSeekBarChangeListener createWidthListener(final EditText widthEditText) {
        return new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


                if (fromUser) {
                    widthEditText.setText(progress + "");
                }


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };
    }


    private TextWatcher createWatcherForWidthEdit(final SeekBar widthBar) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                widthBar.setProgress(Integer.parseInt(s.toString()));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }


    public void onClickReset() {
        mDrawSignatureView.reset();
    }

    public void onClickDone() {
        SaveUtil.saveSignature(mDrawSignatureView, this);

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
            case R.id.action_done:
                onClickDone();
                return true;

            case R.id.action_redo_drawing:
                onClickReset();
                return true;

            case R.id.action_brush_customization:
                onClickBrushCustomize();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
