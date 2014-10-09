package net.whitedesert.photosign.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import net.whitedesert.photosign.R;
import net.whitedesert.photosign.utils.SaveUtil;
import net.whitedesert.photosign.utils.SignUtil;
import net.whitedesert.photosign.views.TypeSignPreviewView;

import yuku.ambilwarna.AmbilWarnaDialog;

/**
 * Created by yazeed44 on 9/20/14.
 */
public class TextSignActivity extends AdActivity {

    //  private final SignRaw raw = new SignRaw();
    private TypeSignPreviewView preview;
    private EditText text;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_sign_customize);
        preview = (TypeSignPreviewView) this.findViewById(R.id.signTextPreview);


        text = (EditText) this.findViewById(R.id.signTextEdit);
        setPreviewText("Preview");
        setUpText();


    }

    public void onClickDone(View view) {
        SaveUtil.askNameAndAddSign(SignUtil.createBitmap(preview.getSignRaw(), true), this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        super.onWindowFocusChanged(hasFocus);
        //Here you can get the size!


    }

    public void onClickBoldCheck(View view) {
        final CheckBox box = (CheckBox) view;

        if (box.isChecked()) {
            //TODO
            //it will be a bug when we add fonts

            preview.setTypeface(null, Typeface.BOLD);
        } else {
            preview.setTypeface(null, Typeface.NORMAL);
        }


    }

    //When user click on choose color btn
    public void onClickChooseColor(View view) {

        AmbilWarnaDialog.OnAmbilWarnaListener listener = new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {

                preview.setTextColor(color);

            }

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }
        };

        final int initalColor = preview.getCurrentTextColor();
        new AmbilWarnaDialog(this, initalColor, listener).show();
    }


    private void setUpText() {

        text.addTextChangedListener(new TextWatcher() {
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
        final String DOUBLE_BYTE_SPACE = "\u3000";

        String fixString = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB_MR1
                && android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            fixString = DOUBLE_BYTE_SPACE;
        }
        preview.setText(fixString + text + fixString);
    }
}
