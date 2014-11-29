package net.whitedesert.photosign.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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
import net.whitedesert.photosign.utils.ToastUtil;

import java.io.File;
import java.util.ArrayList;


public class MainActivity extends AdActivity {

    public static ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUtils();
        checkSigns();


    }

    private void initUtils() {
        SignsDB.initializeInstance(new MyDBHelper(getApplicationContext()));
        ToastUtil.initializeInstance(this);
        ThreadUtil.initalizeInstance(this);
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


            ImageLoaderConfiguration config = builder.build();
            imageLoader = ImageLoader.getInstance();
            imageLoader.init(config);

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

        final MaterialDialog.Builder retrieveDialog = DialogUtil.initDialog(title, getRetrieveMsg(signatures.size()), this);

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


    private void retrieveSigns(final ArrayList<Signature> signatures) {


        final long[] ids = SignatureUtil.addSigns(signatures);
        final boolean noError = CheckUtil.checkSigns(ids, true);

        if (noError) {
            ToastUtil.toastLong(R.string.retrieved_signs_success_toast);
            SignatureUtil.setDefaultSignature(signatures.get(0));


        } else {
            //TODO handle the error
            Log.e("retrieveSigns", "Some error happened when retrieving the signatures ");

        }
    }

    private void deleteSigns() {
        boolean deleted = FileUtil.deleteDirectory(new File(Environment.getExternalStorageDirectory(), SaveUtil.SIGNS_FOLDER_NAME));

        if (deleted) {
            ToastUtil.toastShort(R.string.deleted_signs_toast);
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

    public void onClickMySignatures(View view) {

        final Intent signatureIntent = new Intent(this, SignaturesActivity.class);
        startActivity(signatureIntent);
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

    private void showAbout() {


        final String title = getString(R.string.about_title);
        final Spanned content = Html.fromHtml(getString(R.string.about_body_html));
        DialogUtil.initDialog(title, null, this)
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

}
