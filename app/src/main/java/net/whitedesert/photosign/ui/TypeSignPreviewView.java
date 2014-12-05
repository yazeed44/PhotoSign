package net.whitedesert.photosign.ui;

import android.content.Context;
import android.util.AttributeSet;

import net.whitedesert.photosign.utils.SignatureRaw;

import me.grantland.widget.AutofitTextView;

/**
 * Created by yazeed44 on 10/9/14.
 * A Text View that re size itself based on text
 */
public class TypeSignPreviewView extends AutofitTextView {


    // Default constructor override
    public TypeSignPreviewView(Context context) {
        this(context, null);
    }

    // Default constructor when inflating from XML file
    public TypeSignPreviewView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    // Default constructor override
    public TypeSignPreviewView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public SignatureRaw getSignRaw() {
        final SignatureRaw raw = new SignatureRaw();
        raw.setColor(getCurrentTextColor());
        raw.setText(getText().toString());
        raw.setTypeface(getTypeface());
        raw.setTextSize(getTextSize());
        return raw;
    }


}
