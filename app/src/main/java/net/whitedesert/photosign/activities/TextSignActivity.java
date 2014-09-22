package net.whitedesert.photosign.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import net.whitedesert.photosign.R;
import net.whitedesert.photosign.utils.SetListenUtil;
import net.whitedesert.photosign.utils.SignRaw;
import net.whitedesert.photosign.utils.SignUtil;

/**
 * Created by yazeed44 on 9/20/14.
 */
public class TextSignActivity extends AdActivity {

    private final SignRaw raw = new SignRaw();
    private ImageView preview;
    private EditText text;
    private TextView textSizeView;
    private SeekBar sizeSeek;
    private Button chooseColorBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_sign_customize);
        preview = (ImageView) this.findViewById(R.id.signTextPreview);

        text = (EditText) this.findViewById(R.id.signTextEdit);
        setUpText();
        textSizeView = (TextView) this.findViewById(R.id.signTextSizeTV);

        sizeSeek = (SeekBar) this.findViewById(R.id.baseSizeSeek);
        textSizeView.append(sizeSeek.getProgress() + "");
        SetListenUtil.setUpSizeSeek(sizeSeek, textSizeView, null, preview, raw);

        chooseColorBtn = (Button) this.findViewById(R.id.baseChooseColorBtn);
        SetListenUtil.setUpChooseColorBtn(chooseColorBtn, null, raw, preview, this);


    }

    public void onClickDone(View view) {

        //TODO
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        super.onWindowFocusChanged(hasFocus);
        //Here you can get the size!

        Log.i("TextSignActivity : onCreate", "preview : , width = " + preview.getMeasuredWidth() + "   ,  height =  " + preview.getMeasuredHeight());

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
