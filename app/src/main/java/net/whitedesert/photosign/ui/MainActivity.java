package net.whitedesert.photosign.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import net.whitedesert.photosign.utils.DialogUtil;
import net.whitedesert.photosign.utils.FileUtil;
import net.whitedesert.photosign.utils.SaveUtil;
import net.whitedesert.photosign.utils.Signature;
import net.whitedesert.photosign.utils.SignatureUtil;
import net.whitedesert.photosign.utils.SigningUtil;
import net.whitedesert.photosign.utils.ThreadUtil;
import net.whitedesert.photosign.utils.ViewUtil;
import net.yazeed44.imagepicker.AlbumUtil;
import net.yazeed44.imagepicker.AlbumsFragment;
import net.yazeed44.imagepicker.ImagesFragment;
import net.yazeed44.imagepicker.PickerActivity;

import java.io.File;
import java.util.ArrayList;


public class MainActivity extends AdActivity implements AlbumsFragment.OnClickAlbum, ImagesFragment.OnPickImage {


    public static final String IS_FOR_SIGNING_KEY = "isForSigningKey";
    public static SparseArray<String> sChosenImagePaths = new SparseArray<String>();
    private AlbumsFragment mAlbumsFragment;
    private ImagesFragment mImagesFragment;
    private MySignaturesFragment mSignaturesFragment;
    private TextView mSignBtnBadge;
    private Button mSignBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUtils();


        initSignBtn();
        updateTextAndBadge();
        setupGalleryFragment(savedInstanceState);
        checkSigns();

    }

    private void initUtils() {
        SignsDB.initInstance(new MyDBHelper(getApplicationContext()));
        ViewUtil.initInstance(this);
        ThreadUtil.initInstance(this);
        initImageLoader();


    }

    private void initSignBtn() {
        mSignBtn = (Button) findViewById(R.id.sign_btn);
        mSignBtnBadge = (TextView) findViewById(R.id.sign_btn_badge);
    }

    private void setupGalleryFragment(final Bundle savedInstanceState) {


        if (savedInstanceState == null && findViewById(R.id.main_container) != null) {

            mAlbumsFragment = new AlbumsFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_container, mAlbumsFragment)
                    .commit();
        }

    }



    private void initImageLoader() {
        try {
            String CACHE_DIR = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/.temp_tmp";
            new File(CACHE_DIR).mkdirs();

            File cacheDir = StorageUtils.getOwnCacheDirectory(getBaseContext(),
                    CACHE_DIR);

            DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                    .cacheOnDisc(true).imageScaleType(ImageScaleType.EXACTLY)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .showImageOnFail(R.drawable.ic_error)
                            //.showImageOnLoading(R.drawable.no_media)
                    .resetViewBeforeLoading(true)
                    .build();


            ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                    getBaseContext())
                    .defaultDisplayImageOptions(displayImageOptions)
                    .discCache(new UnlimitedDiscCache(cacheDir))
                    .memoryCache(new WeakMemoryCache());


            final ImageLoaderConfiguration config = builder.build();
            ImageLoader.getInstance().init(config);

        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage());
        }
    }

    //Check the signs in /signs folder , and then ask user if he want to retrieve them
    private void checkSigns() {

        if (SignatureUtil.noSigns()) {


            final File[] filesOfSignatures = FileUtil.getFilesOfSigns();

            final ArrayList<Signature> signatures = new ArrayList<Signature>();

            if (filesOfSignatures != null && filesOfSignatures.length != 0) {

                Log.d("checkSigns", "The count of files  " + filesOfSignatures.length);
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

        final MaterialDialog.Builder retrieveDialog = DialogUtil.createDialog(title, getRetrieveMsg(signatures.size()), this);

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
        final String name = signatureFile.getName();
        final String path = signatureFile.getPath();
        final Signature signature = new Signature();
        signature.setPath(path);
        signature.setName(name);
        signature.setDefault(false);
        return signature;
    }

    private String getRetrieveMsg(int signaturesCount) {
        final String retrieveMsg = getResources().getString(R.string.retrieve_msg);

        return retrieveMsg.replace("0", signaturesCount + "");
    }


    public void onClickChoose(View view) {


        SigningUtil.openGalleryToSignSingle(this);


    }


    private void showAbout() {


        final String title = getString(R.string.about_title);
        final Spanned content = Html.fromHtml(getString(R.string.about_body_html));
        DialogUtil.createDialog(title, null, this)
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


    private void updateTextAndBadge() {

        Log.d("updateTextAndBadge", sChosenImagePaths.toString());

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        //When user import external images to use as signatures
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PickerActivity.PICK_REQUEST) {
            final String[] paths = data.getStringArrayExtra(PickerActivity.PICKED_IMAGES_KEY);
            final Signature[] chosenImages = new Signature[paths.length];

            for (int i = 0; i < paths.length; i++) {
                //TODO move this to utils
                final String path = paths[i];
                Signature image = chosenImages[i];
                image = new Signature(path);
                SignatureUtil.addSign(image, false);
            }

            ViewUtil.toastShort(R.string.saved_sign_success);

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
        albumBundle.putBoolean(IS_FOR_SIGNING_KEY, true);


        mImagesFragment = new ImagesFragment();
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
}
