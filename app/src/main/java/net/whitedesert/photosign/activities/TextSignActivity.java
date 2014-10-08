package net.whitedesert.photosign.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import net.whitedesert.photosign.R;
import net.whitedesert.photosign.utils.SaveUtil;
import net.whitedesert.photosign.utils.SignRaw;
import net.whitedesert.photosign.utils.SignUtil;

import yuku.ambilwarna.AmbilWarnaDialog;

/**
 * Created by yazeed44 on 9/20/14.
 */
public class TextSignActivity extends AdActivity {

    private final SignRaw raw = new SignRaw();
    private ImageView preview;
    private EditText text;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_sign_customize);
        preview = (ImageView) this.findViewById(R.id.signTextPreview);


        text = (EditText) this.findViewById(R.id.signTextEdit);
        setUpText();


    }

    public void onClickDone(View view) {
        SaveUtil.askNameAndAddSign(SignUtil.createBitmap(raw, true), this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        super.onWindowFocusChanged(hasFocus);
        //Here you can get the size!


        preview.setImageBitmap(SignUtil.createBitmap(raw, true));

    }

    public void onClickBoldCheck(View view) {
        final CheckBox box = (CheckBox) view;

        if (box.isChecked()) {
            raw.setBold(true);
        } else {
            raw.setBold(false);
        }

        preview.setImageBitmap(SignUtil.createBitmap(raw, true));
    }

    //When user click on choose color btn
    public void onClickChooseColor(View view) {

        AmbilWarnaDialog.OnAmbilWarnaListener listener = new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                raw.setColor(color);
                preview.setImageBitmap(SignUtil.createBitmap(raw, true));
            }

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }
        };

        final int initalColor = raw.getColor();
        new AmbilWarnaDialog(this, initalColor, listener).show();
    }


    private void setUpText() {

        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                raw.setText(charSequence.toString());
                preview.setImageBitmap(SignUtil.createBitmap(raw, true));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
