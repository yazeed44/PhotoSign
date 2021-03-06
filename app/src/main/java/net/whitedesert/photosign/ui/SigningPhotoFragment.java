package net.whitedesert.photosign.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import net.whitedesert.photosign.R;
import net.whitedesert.photosign.utils.Signature;

/**
 * Created by yazeed44 on 12/1/14.
 */
public class SigningPhotoFragment extends Fragment {


    public Signature signature;
    SigningView signingView;
    private LinearLayout mLayout;
    private TextView mOpacityText;
    private SeekBar mOpacityBar;
    private String mPhotoPath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mLayout = (LinearLayout) inflater.inflate(R.layout.fragment_signing, container, false);


        initPath();
        initViews();
        setupOpacity();
        return mLayout;
    }

    private void initViews() {
        mOpacityBar = (SeekBar) mLayout.findViewById(R.id.opacity_seek);
        mOpacityText = (TextView) mLayout.findViewById(R.id.opacity_text);
        signingView = (SigningView) mLayout.findViewById(R.id.signing_view);

        ImageLoader.getInstance().displayImage("file://" + mPhotoPath, signingView);
    }

    private void initPath() {

        mPhotoPath = getArguments().getString(SigningActivity.PHOTO_PATH_KEY);
    }

    private void setupOpacity() {
        mOpacityBar.setMax(getResources().getInteger(R.integer.opacity_max));
        mOpacityBar.setProgress(mOpacityBar.getMax());
        final String opacityString = getString(R.string.opacity_text) + " : ";

        mOpacityText.setText(opacityString + mOpacityBar.getProgress());

        mOpacityBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                //         signatureView.setImageAlpha(progress); // TODO Set opacity
                mOpacityText.setText(opacityString + progress);
                signingView.invalidate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

}
