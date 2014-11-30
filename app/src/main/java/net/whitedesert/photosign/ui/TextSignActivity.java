package net.whitedesert.photosign.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import net.whitedesert.photosign.R;
import net.whitedesert.photosign.utils.SaveUtil;

import yuku.ambilwarna.AmbilWarnaDialog;

/**
 * Created by yazeed44 on 9/20/14.
 */
public class TextSignActivity extends AdActivity {


    private TypeSignPreviewView preview;
    private EditText text;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_sign_customize);
        preview = (TypeSignPreviewView) this.findViewById(R.id.signTextPreview);
        text = (EditText) this.findViewById(R.id.signTextEdit);

        getSupportActionBar().setTitle(R.string.type_sign_title);

        setPreviewText("Preview");
        preview.setTypeface(null, Typeface.BOLD);
        setUpText();

    }

    public void onClickDone() {
        SaveUtil.askNameAndAddSign(preview.getSignRaw().createBitmap(true), this);
    }


    public void onClickBoldCheck(View view) {
        final CheckBox box = (CheckBox) view;

        if (box.isChecked()) {
            //TODO it will be a bug when we add fonts Because of null

            preview.setTypeface(null, Typeface.BOLD);
        } else {
            preview.setTypeface(null, Typeface.NORMAL);
        }


    }

    //When user click on choose color btn
    private void onClickChooseColor() {

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
            case R.id.menu_done:
                onClickDone();
                return true;


            case R.id.menu_type_sign:
                onClickChooseColor();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
