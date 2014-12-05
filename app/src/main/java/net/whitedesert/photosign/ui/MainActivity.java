package net.whitedesert.photosign.ui;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import net.whitedesert.photosign.R;
import net.whitedesert.photosign.database.MyDBHelper;
import net.whitedesert.photosign.database.SignsDB;
import net.whitedesert.photosign.utils.CheckUtil;
import net.whitedesert.photosign.utils.FileUtil;
import net.whitedesert.photosign.utils.SaveUtil;
import net.whitedesert.photosign.utils.Signature;
import net.whitedesert.photosign.utils.SignatureUtil;
import net.whitedesert.photosign.utils.ThreadUtil;
import net.whitedesert.photosign.utils.ViewUtil;
import net.yazeed44.imagepicker.AlbumUtil;
import net.yazeed44.imagepicker.AlbumsFragment;
import net.yazeed44.imagepicker.ImagesAdapter;
import net.yazeed44.imagepicker.ImagesFragment;
import net.yazeed44.imagepicker.PickerActivity;

import java.io.File;
import java.util.ArrayList;


public class MainActivity extends BaseActivity implements AlbumsFragment.OnClickAlbum, ImagesFragment.OnPickImage {


    public static final String TO_SIGN_IMAGES_KEY = "toSignImageKey";
    public static SparseArray<String> sChosenImagePaths = new SparseArray<String>();
    private AlbumsFragment mAlbumsFragment;
    private ImagesFragment mImagesFragment;
    private SignaturesFragment mSignaturesFragment;
    private TextView mSignBtnBadge;
    private Button mSignBtn;
    private FrameLayout mSignBtnLayout;
    private Toolbar mToolbar;
    private boolean mIsFirstTime;
    private Button mSpinnerLikeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mIsFirstTime = CheckUtil.isFirstTimeOpened(this);


        initUtils();
        initViews();
        setupToolbarSpinner();
        updateTextAndBadge();
        retrieveLostSignatures();


    }

    private void initViews() {

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //getSupportActionBar().setIcon(R.drawable.ic_launcher);

        mSignBtnLayout = (FrameLayout) findViewById(R.id.sign_btn_layout);
        mSignBtn = (Button) findViewById(R.id.sign_btn);
        mSignBtnBadge = (TextView) findViewById(R.id.sign_btn_badge);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, getPhotosFragment())
                .commit();
    }


    private void setupToolbarSpinner() {

        mSpinnerLikeBtn = new Button(this);

        mSpinnerLikeBtn.setText(getResources().getString(R.string.main_navigation_photos_popup));

        mSpinnerLikeBtn.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        final Drawable arrowDrawable = getResources().getDrawable(R.drawable.ic_arrow_down);

        mSpinnerLikeBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, arrowDrawable, null);

        mSpinnerLikeBtn.setTextAppearance(getSupportActionBar().getThemedContext(), R.style.SpinnerLikeBtn);


        mSpinnerLikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNavigationPopup();
            }
        });


        mToolbar.addView(mSpinnerLikeBtn);


    }

    private PopupMenu showNavigationPopup() {

        final PopupMenu menu = new PopupMenu(mSpinnerLikeBtn.getContext(), mSpinnerLikeBtn);
        menu.getMenuInflater().inflate(R.menu.popup_main_navigation, menu.getMenu());
        menu.setOnMenuItemClickListener(createListenerForNavigation());
        menu.show();

        return menu;
    }

    private PopupMenu.OnMenuItemClickListener createListenerForNavigation() {
        return new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                if (menuItem.getItemId() == R.id.action_main_navigation_photos) {
                    showPhotos();
                } else if (menuItem.getItemId() == R.id.action_main_navigation_signatures) {
                    showSignatures();
                }

                return true;
            }
        };
    }

    private void showPhotos() {
        final float weight = 0.92F;

        mSignBtnLayout.setVisibility(View.VISIBLE);
        (findViewById(R.id.main_container)).setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, weight));

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, getPhotosFragment())
                .commit();
        mSpinnerLikeBtn.setText(getResources().getString(R.string.main_navigation_photos_popup));

    }

    private void showSignatures() {
        mSignBtnLayout.setVisibility(View.GONE);
        (findViewById(R.id.main_container)).setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, getSignaturesFragment())
                .commit();

        mSpinnerLikeBtn.setText(getResources().getString(R.string.main_navigation_signatures_popup));
    }




    private Fragment getSignaturesFragment() {
        mSignaturesFragment = new SignaturesFragment();

        return mSignaturesFragment;
    }

    private Fragment getPhotosFragment() {
        mAlbumsFragment = new AlbumsFragment();
        return mAlbumsFragment;

    }

    private void initUtils() {
        SignsDB.initInstance(new MyDBHelper(getApplicationContext()));
        ViewUtil.initInstance(this);
        ThreadUtil.initInstance(this);
        initImageLoader();


    }




    private void initImageLoader() {
        try {
            String CACHE_DIR = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/.temp_tmp";
            new File(CACHE_DIR).mkdirs();

            File cacheDir = StorageUtils.getOwnCacheDirectory(getBaseContext(),
                    CACHE_DIR);

            DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                    .cacheOnDisk(true).imageScaleType(ImageScaleType.EXACTLY)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .showImageOnFail(R.drawable.ic_error)
                    .resetViewBeforeLoading(true)
                    .build();


            ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                    getBaseContext())
                    .defaultDisplayImageOptions(displayImageOptions)
                    .diskCache(new UnlimitedDiscCache(cacheDir))
                    .memoryCache(new WeakMemoryCache());


            final ImageLoaderConfiguration config = builder.build();
            ImageLoader.getInstance().init(config);

        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage());
        }
    }

    //Check the signs in /signs folder , and then ask user if he want to retrieve them
    private void retrieveLostSignatures() {


        if (mIsFirstTime && SignatureUtil.noSigns()) {


            final File[] filesOfSignatures = FileUtil.getFilesOfSigns();

            final ArrayList<Signature> signatures = new ArrayList<Signature>();

            if (filesOfSignatures != null && filesOfSignatures.length != 0) {

                Log.d("retrieveLostSignatures", "The count of files  " + filesOfSignatures.length);
                for (File signatureFile : filesOfSignatures) {
                    final Signature signature = createSign(signatureFile);
                    signatures.add(signature);

                }

                showRetrieveDialog(signatures);
            }


        }

    }


    private void showRetrieveDialog(final ArrayList<Signature> signatures) {
        final String title = getResources().getString(R.string.retrieve_title);

        final MaterialDialog.Builder retrieveDialog = ViewUtil.createDialog(title, getRetrieveMsg(signatures.size()), this);

        retrieveDialog.negativeText(R.string.delete_unused_signatures_btn)
                .positiveText(R.string.yes_btn)
                .neutralText(R.string.dismiss_btn)
                .callback(getCallBackForRetrieveDialog(signatures))
                .build()
                .show();
    }


    private MaterialDialog.FullCallback getCallBackForRetrieveDialog(final ArrayList<Signature> signatures) {
        return new MaterialDialog.FullCallback() {
            @Override
            public void onNeutral(MaterialDialog materialDialog) {
                materialDialog.dismiss();
            }

            @Override
            public void onPositive(MaterialDialog materialDialog) {
                retrieveSigns(signatures);
            }

            @Override
            public void onNegative(MaterialDialog materialDialog) {
                deleteSigns();
            }
        };
    }


    private void retrieveSigns(final ArrayList<Signature> lostSignatures) {


        final long[] ids = SignatureUtil.addSigns(lostSignatures);
        final boolean noError = CheckUtil.checkSigns(ids, true);

        if (noError) {
            ViewUtil.toastLong(R.string.retrieved_signs_success_toast);
            SignatureUtil.setDefaultSignature(lostSignatures.get(0));


        } else {
            //TODO handle the error
            Log.e("retrieveSigns", "Some error happened when retrieving the signatures ");

        }
    }

    private void deleteSigns() {
        boolean deleted = FileUtil.deleteDirectory(new File(Environment.getExternalStorageDirectory(), SaveUtil.SIGNS_FOLDER_NAME));

        if (deleted) {
            ViewUtil.toastShort(R.string.deleted_signs_toast);
        } else {
            Log.e("Delete /signs folder", "Failed to delete");
        }
    }

    private Signature createSign(File signatureFile) {
        return new Signature(signatureFile.getPath());
    }

    private String getRetrieveMsg(int signaturesCount) {
        final String retrieveMsg = getResources().getString(R.string.retrieve_msg);

        return retrieveMsg.replace("0", signaturesCount + "");
    }


    private void showAbout() {


        final String title = getString(R.string.about_title);
        final Spanned content = Html.fromHtml(getString(R.string.about_body_html));
        ViewUtil.createDialog(title, null, this)
                .content(content)
                .contentLineSpacing(1.6f)
                .positiveText(R.string.dismiss_btn)
                .callback(getDismissCallback())
                .build()
                .show();


    }

    private MaterialDialog.SimpleCallback getDismissCallback() {
        return new MaterialDialog.SimpleCallback() {

            @Override
            public void onPositive(MaterialDialog materialDialog) {
                materialDialog.dismiss();
            }
        };
    }

    public void onClickSign(View view) {

        final Intent signIntent = new Intent(this, SigningActivity.class);
        signIntent.putExtra(TO_SIGN_IMAGES_KEY, castPaths());
        resetImages();
        updateTextAndBadge();
        startActivity(signIntent);


    }

    private void resetImages() {
        sChosenImagePaths.clear();
        ((BaseAdapter) mImagesFragment.gridView.getAdapter()).notifyDataSetChanged();
    }

    private String[] castPaths() {
        final String[] paths = new String[sChosenImagePaths.size()];

        for (int i = 0; i < paths.length; i++) {
            paths[i] = sChosenImagePaths.valueAt(i);
        }

        return paths;

    }


    private void updateTextAndBadge() {

        if (sChosenImagePaths.size() == 0) {
            mSignBtn.setBackgroundColor(getResources().getColor(R.color.sign_btn_layout_disabled));
            mSignBtn.setClickable(false);
            mSignBtn.setTextColor(getResources().getColor(R.color.sign_btn_disabled_text));
            mSignBtnBadge.setVisibility(View.GONE);
            mSignBtn.setTypeface(Typeface.create("", Typeface.NORMAL));

        } else {
            mSignBtn.setBackgroundColor(getResources().getColor(R.color.sign_btn_layout));
            mSignBtn.setClickable(true);
            mSignBtn.setTextColor(getResources().getColor(R.color.sign_btn_text));
            mSignBtnBadge.setText(sChosenImagePaths.size() + "");
            mSignBtnBadge.setVisibility(View.VISIBLE);
            mSignBtn.setTypeface(Typeface.create("", Typeface.BOLD));

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //  getMenuInflater().inflate(R.menu.menu_settings, menu);
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {

            case R.id.menu_about:
                showAbout();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }


    @Override
    public void finish() {

        if (mImagesFragment != null && mImagesFragment.isVisible()) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_container, mAlbumsFragment)
                    .commit();
            getSupportActionBar().setTitle(R.string.albums_title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        } else {
            super.finish();
        }
    }

    @Override
    public void onClickAlbum(AlbumUtil.AlbumEntry album) {
        final Bundle albumBundle = new Bundle();
        albumBundle.putSerializable(PickerActivity.ALBUM_KEY, album);


        mImagesFragment = new ToSignImagesFragment();
        mImagesFragment.setArguments(albumBundle);


        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, mImagesFragment)
                .commit();

        getSupportActionBar().setTitle(album.name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onPickImage(AlbumUtil.PhotoEntry photoEntry) {

        sChosenImagePaths.put(photoEntry.imageId, photoEntry.path);
        updateTextAndBadge();

    }

    @Override
    public void onUnpickImage(AlbumUtil.PhotoEntry photo) {
        sChosenImagePaths.remove(photo.imageId);
        updateTextAndBadge();
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (mImagesFragment != null && mImagesFragment.isVisible()) {
            finish();
            return true;
        }

        return false;
    }

    @Override
    public boolean toolbarUsed() {
        return true;
    }




    public static class ToSignImagesAdapter extends ImagesAdapter {
        public ToSignImagesAdapter(AlbumUtil.AlbumEntry album, ImagesFragment fragment) {
            super(album, fragment);
        }

        @Override
        public boolean isPicked(final AlbumUtil.PhotoEntry photoEntry) {

            boolean isPicked = false;

            for (int i = 0; i < sChosenImagePaths.size(); i++) {

                if (photoEntry.path.equals(sChosenImagePaths.valueAt(i))) {

                    isPicked = true;
                }
            }


            return isPicked;
        }

        @Override
        public void pickImage(final View convertView, final ViewHolder holder, final AlbumUtil.PhotoEntry photo) {
            final boolean isPicked = isPicked(photo);

            if (isPicked) {
                //Unpick
                mFragment.pickListener.onUnpickImage(photo);
            } else {
                //pick
                mFragment.pickListener.onPickImage(photo);
            }

            drawGrid(convertView, holder, photo);
        }
    }

    public static class ToSignImagesFragment extends ImagesFragment {

        @Override
        public void setupAdapter() {
            final AlbumUtil.AlbumEntry album = (AlbumUtil.AlbumEntry) getArguments().getSerializable(PickerActivity.ALBUM_KEY);
            final ToSignImagesAdapter adapter = new ToSignImagesAdapter(album, this);

            gridView.setAdapter(adapter);
        }
    }
}
