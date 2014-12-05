package net.whitedesert.photosign.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.astuetz.PagerSlidingTabStrip;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.whitedesert.photosign.R;
import net.whitedesert.photosign.utils.BitmapUtil;
import net.whitedesert.photosign.utils.CheckUtil;
import net.whitedesert.photosign.utils.Signature;
import net.whitedesert.photosign.utils.SignatureUtil;
import net.whitedesert.photosign.utils.ViewUtil;

import java.util.ArrayList;

/**
 * Created by yazeed44 on 8/10/14.
 */
public class SigningActivity extends BaseActivity {

    public static final String PHOTO_PATH_KEY = "photoPathKey";
    private final int mFragmentContainer = R.id.container;
    private Signature signature;
    private AlertDialog changeDialog;
    private ViewPager mPager;
    private PagerSlidingTabStrip mTabs;
    private MyPagerAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signing);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.signing_title);

        initSignature();
        //setupSignatureView();
        setupTabs();


    }


    private void setupTabs() {

        mPager = (ViewPager) findViewById(R.id.pager);
        mTabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);

        final Intent i = this.getIntent();

        final String[] imagesPath = i.getStringArrayExtra(MainActivity.TO_SIGN_IMAGES_KEY);

        mAdapter = new MyPagerAdapter(getSupportFragmentManager(), imagesPath);

        mPager.setAdapter(mAdapter);
        mTabs.setViewPager(mPager);


    }


    private void initSignature() {
        signature = SignatureUtil.getDefaultSignature();

        if (!CheckUtil.checkSign(signature)) {
            //If there's no default signature
            signature = SignatureUtil.getLatestSign();
            ViewUtil.toastShort(R.string.latest_sign_is_used_toast);
        } else {
            ViewUtil.toastShort(R.string.default_sign_is_used_toast);
        }
    }


    private void onClickChangeSignature() {

        final String title = getResources().getString(R.string.choose_signature_title);


        changeDialog = new AlertDialog.Builder(this)
                .setTitle(title)
                .setView(getChooseView())
                .create();


        changeDialog.show();


    }

    private GridView getChooseView() {
        final ArrayList<Signature> signatures = getSignatures();

        final GridView gridView = new GridView(this);

        gridView.setColumnWidth(getResources().getDimensionPixelSize(R.dimen.mini_signature_column_width));

        gridView.setHorizontalSpacing(getResources().getDimensionPixelSize(R.dimen.signature_spacing));

        gridView.setNumColumns(3);


        final int height = (int) (getResources().getDimensionPixelSize(R.dimen.mini_signature_column_width) * (signatures.size() / 2) * (1.07));


        gridView.setAdapter(new ChooseSignatureAdapter(signatures, this));

        //gridView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));

        return gridView;

    }

    private ArrayList<Signature> getSignatures() {
        final ArrayList<Signature> signatures = SignatureUtil.getSigns(true);
        return signatures;
    }


    private void onClickDone() {

        //TODO SaveUtil.doneSigningPhoto(signingView, this);
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

    private static class ViewHolder {
        ImageView image;
    }


    private class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] mPhotos;

        public MyPagerAdapter(final FragmentManager fm, final String[] photos) {
            super(fm);
            this.mPhotos = photos;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return position + "";
        }

        @Override
        public Fragment getItem(int position) {
            final Fragment fragment = new SigningPhotoFragment();
            final Bundle bundle = new Bundle();
            bundle.putString(PHOTO_PATH_KEY, mPhotos[position]);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return mPhotos.length;
        }
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
                    //setupSignatureView();
                    changeDialog.dismiss();
                }
            });

        }


        private DisplayImageOptions getOptions() {
            return new DisplayImageOptions.Builder()
                    .showImageOnFail(R.drawable.ic_error)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .resetViewBeforeLoading(true)

                    .build();
        }


    }

}
