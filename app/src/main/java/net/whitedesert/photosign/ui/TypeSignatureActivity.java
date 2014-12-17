package net.whitedesert.photosign.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;

import net.whitedesert.photosign.R;
import net.whitedesert.photosign.utils.SaveUtil;
import net.whitedesert.photosign.utils.ViewUtil;

/**
 * Created by yazeed44 on 9/20/14.
 */
public class TypeSignatureActivity extends BaseActivity {


    private TypeSignaturePreviewView mPreview;
    private EditText mText;
    private boolean mBold;
    private String mFont;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_signature);
        mPreview = (TypeSignaturePreviewView) this.findViewById(R.id.sign_text_Preview);
        mText = (EditText) this.findViewById(R.id.signTextEdit);

        getSupportActionBar().setTitle(R.string.type_sign_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setPreviewText("Preview");
        mPreview.setTypeface(null, Typeface.BOLD);
        setupText();

        updateFont();
    }

    public void onClickDone() {
        SaveUtil.saveSignature(mPreview.createSignatureRaw().createBitmap(), this);
    }


    public void onClickBoldCheck(View view) {
        final CheckBox box = (CheckBox) view;

        mBold = box.isChecked();

        updateFont();

    }

    private void updateFont() {

        final int style = mBold ? Typeface.BOLD : Typeface.NORMAL;

        //TODO Implement this



    }

    //When user click on choose color btn
    private void onClickChooseColor() {

        final String title = getResources().getString(R.string.choose_color);

        ViewUtil.createDialog(title, null, this)
                .customView(createColorPickerView())
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

                        mPreview.setTextColor(newColor);
                    }
                })
                .show();
    }

    private View createColorPickerView() {

        final View pickerLayout = LayoutInflater.from(this).inflate(R.layout.color_picker, (ViewGroup) findViewById(R.id.container), false);

        final OpacityBar opacityBar = (OpacityBar) pickerLayout.findViewById(R.id.opacity_bar);

        final SVBar svBar = (SVBar) pickerLayout.findViewById(R.id.sv_bar);

        final ColorPicker picker = (ColorPicker) pickerLayout.findViewById(R.id.color_picker);

        picker.addOpacityBar(opacityBar);
        picker.addSVBar(svBar);
        picker.setOldCenterColor(mPreview.getCurrentTextColor());


        return pickerLayout;
    }

    private void setupText() {


        mText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                setPreviewText(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setPreviewText(final String text) {
        /*final String DOUBLE_BYTE_SPACE = "\u3000";

        String fixString = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB_MR1
                && android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            fixString = DOUBLE_BYTE_SPACE;
        }
        mPreview.setText(fixString + text + fixString);
        */

        mPreview.setText(text);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_type_sign, menu);
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


            case R.id.action_choose_color:
                onClickChooseColor();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
