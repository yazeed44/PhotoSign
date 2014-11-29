package net.whitedesert.photosign.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.whitedesert.photosign.R;
import net.whitedesert.photosign.utils.BitmapUtil;
import net.whitedesert.photosign.utils.CheckUtil;
import net.whitedesert.photosign.utils.DialogUtil;
import net.whitedesert.photosign.utils.SaveUtil;
import net.whitedesert.photosign.utils.Signature;
import net.whitedesert.photosign.utils.SignatureUtil;
import net.whitedesert.photosign.utils.ToastUtil;
import net.whitedesert.photosign.views.SignatureView;
import net.whitedesert.photosign.views.SigningView;

import java.util.ArrayList;

/**
 * Created by yazeed44 on 8/10/14.
 */
public class SigningActivity extends AdActivity {

    private SigningView signingView;
    private SeekBar opacitySeek;
    private TextView opacityText;
    private SignatureView signatureView;

    private Signature signature;

    private MaterialDialog changeDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_signing);
        initViews();
        final Intent i = this.getIntent();

        final String photoPath = i.getStringExtra(Types.PATH_TYPE);


        final Bitmap photo = BitmapUtil.decodeFile(photoPath);


        initSignature();
        setupSignatureView();
        signingView.setPhoto(photo);


        setUpOpacity();


    }

    private void initViews() {
        signingView = (SigningView) this.findViewById(R.id.signingView);
        opacitySeek = (SeekBar) this.findViewById(R.id.opacity_seek);
        opacityText = (TextView) this.findViewById(R.id.opacity_text);
        signatureView = (SignatureView) findViewById(R.id.signatureView);
    }

    private void initSignature() {
        signature = SignatureUtil.getDefaultSignature();

        if (!CheckUtil.checkSign(signature)) {
            //If there's no default signature
            signature = SignatureUtil.getLatestSign();
            ToastUtil.toastShort(R.string.latest_sign_is_used_toast);
        } else {
            ToastUtil.toastShort(R.string.default_sign_is_used_toast);
        }
    }

    private void setupSignatureView() {

        signatureView.setSignature(signature);


        signingView.setSignatureView(signatureView);
    }

    private void setUpOpacity() {
        opacitySeek.setMax(getResources().getInteger(R.integer.opacity_max));
        opacitySeek.setProgress(opacitySeek.getMax());
        final String opacityString = getString(R.string.opacity_text) + " : ";

        opacityText.setText(opacityString + opacitySeek.getProgress());

        opacitySeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                signatureView.setImageAlpha(progress); // Set opacity
                opacityText.setText(opacityString + progress);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_done, menu);
        getMenuInflater().inflate(R.menu.menu_signing, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.menu_done:
                onClickDone();
                return true;

            case R.id.menu_change_signature:
                onClickChangeSignature();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onClickChangeSignature() {
        //TODO Fix bug

        final String title = getResources().getString(R.string.choose_signature_title);

        changeDialog = DialogUtil.initDialog(title, null, this)
                .customView(getChooseView())
                .build();


        changeDialog.show();







      /* new AlertDialog.Builder(this)
                .setTitle(title)
                .setView(getChooseView())
                .show();

*/


    }

    private GridView getChooseView() {
        final ArrayList<Signature> signatures = getSignatures();

        final GridView gridView = new GridView(this);

        gridView.setColumnWidth(getResources().getDimensionPixelSize(R.dimen.mini_signature_column_width));

        gridView.setHorizontalSpacing(getResources().getDimensionPixelSize(R.dimen.signature_spacing));

        gridView.setNumColumns(3);


        final int height = (int) (getResources().getDimensionPixelSize(R.dimen.mini_signature_column_width) * (signatures.size() / 2) * (1.07));


        gridView.setAdapter(new ChooseSignatureAdapter(signatures, this));

        gridView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));

        return gridView;

    }

    private ArrayList<Signature> getSignatures() {
        final ArrayList<Signature> signatures = SignatureUtil.getSigns(true);

        if (!signatures.remove(signature)) {
            final int index = signatures.indexOf(signature);

            if (index > 0) {
                signatures.remove(index);

            }

            Log.d("getSignatures", signature.getName() + "  On list   " + signatures.contains(signature));

        }

        return signatures;
    }


    private void onClickDone() {
        SaveUtil.doneSigningPhoto(signingView, this);
    }

    private static class ViewHolder {
        ImageView image;
    }

    private class ChooseSignatureAdapter extends BaseAdapter {

        private final ArrayList<Signature> signatures;
        private final Activity activity;

        public ChooseSignatureAdapter(final ArrayList<Signature> signatures, final Activity activity) {
            this.signatures = signatures;
            this.activity = activity;
        }


        @Override
        public int getCount() {
            return signatures.size();
        }


        @Override
        public Object getItem(int position) {
            return signatures.get(position);
        }


        @Override
        public long getItemId(int position) {
            return signatures.get(position).getName().length();
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Signature selectedSignature = signatures.get(position);

            if (!CheckUtil.checkPath(selectedSignature.getPath())) {
                SignatureUtil.deleteSignature(selectedSignature, true);
                return convertView;
            }

            android.view.View grid = convertView;

            final ViewHolder holder;

            if (convertView == null) {
                grid = activity.getLayoutInflater().inflate(R.layout.image_signature, parent, false);
                holder = new ViewHolder();
                holder.image = (ImageView) grid.findViewById(R.id.signature_image);
                grid.setTag(holder);
            } else {
                holder = (ViewHolder) grid.getTag();
            }

            setupImage(holder, grid, selectedSignature);


            return grid;
        }

        private void setupImage(final ViewHolder holder, final View grid, final Signature selectedSignature) {

            final int width = activity.getResources().getDimensionPixelSize(R.dimen.mini_signature_column_width);
            final int height = (int) (width * 1.5);

            grid.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));

            final String contentPath = BitmapUtil.GLOBAL_PATH + selectedSignature.getPath();


            ImageLoader.getInstance().displayImage(contentPath, holder.image, getOptions());

            //  holder.image.setImageBitmap(selectedSignature.getBitmap(true));

            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    signature = selectedSignature;
                    setupSignatureView();
                    changeDialog.dismiss();
                }
            });

        }


        private DisplayImageOptions getOptions() {
            return new DisplayImageOptions.Builder()
                    .showImageOnFail(R.drawable.ic_error)
                    .cacheInMemory(true)
                    .cacheOnDisc(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .resetViewBeforeLoading(true)

                    .build();
        }
    }

}
