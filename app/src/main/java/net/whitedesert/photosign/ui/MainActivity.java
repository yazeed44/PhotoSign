package net.whitedesert.photosign.ui;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
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

import rippleview.RippleView;


public class MainActivity extends BaseActivity implements AlbumsFragment.OnClickAlbum, ImagesFragment.OnPickImage {


    public static final String TO_SIGN_IMAGES_KEY = "toSignImageKey";
    private static SparseArray<String> sChosenPhotosPath = new SparseArray<>();
    private AlbumsFragment mAlbumsFragment;
    private ImagesFragment mImagesFragment;
    private SignaturesFragment mSignaturesFragment;
    private TextView mSignBtnBadge;
    private FrameLayout mSignBtnLayout;
    private Toolbar mToolbar;
    private Button mSpinnerLikeBtn;
    private RippleView mSignBtn;
    private Uri mCapturedPhotoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initUtils();

        setupToolbarSpinner();
        initViews();
        updateTextAndBadge();
        retrieveLostSignatures();


    }

    @Override
    public void onResume() {
        super.onResume();
        if (mImagesFragment != null && mImagesFragment.isVisible()) {
            ((BaseAdapter) mImagesFragment.gridView.getAdapter()).notifyDataSetChanged();
        }
        updateTextAndBadge();
    }

    private void initViews() {


        mSignBtnLayout = (FrameLayout) findViewById(R.id.sign_btn_layout);
        mSignBtn = (RippleView) findViewById(R.id.sign_btn);
        mSignBtnBadge = (TextView) findViewById(R.id.sign_btn_badge);

        if (!SignatureUtil.noSigns()) {
            showPhotos();
        } else {
            showSignatures();
        }
    }


    private void setupToolbarSpinner() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //getSupportActionBar().setIcon(R.drawable.ic_launcher);


        mSpinnerLikeBtn = new Button(this);


        mSpinnerLikeBtn.setText(getResources().getString(R.string.main_navigation_photos_popup));


        mSpinnerLikeBtn.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        final Drawable arrowDrawable = getResources().getDrawable(R.drawable.ic_arrow_down);

        mSpinnerLikeBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, arrowDrawable, null);

        mSpinnerLikeBtn.setTextAppearance(getSupportActionBar().getThemedContext(), R.style.SpinnerLikeBtn);

        if (Build.VERSION.SDK_INT >= 14) {
            mSpinnerLikeBtn.setAllCaps(false);

        }


        mSpinnerLikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNavigationPopup();
            }
        });


        mToolbar.addView(mSpinnerLikeBtn);


    }

    private void showNavigationPopup() {

        final PopupMenu menu = new PopupMenu(mSpinnerLikeBtn.getContext(), mSpinnerLikeBtn);
        menu.getMenuInflater().inflate(R.menu.popup_main_navigation, menu.getMenu());
        menu.setOnMenuItemClickListener(createListenerForNavigation());
        menu.show();
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
        final TypedValue value = new TypedValue();
        getResources().getValue(R.dimen.main_fragment_weight, value, true);
        final float weight = value.getFloat();

        mSignBtnLayout.setVisibility(View.VISIBLE);
        (findViewById(R.id.main_container)).setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, weight));

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, getPhotosFragment())
                .commit();
        mSpinnerLikeBtn.setText(getResources().getString(R.string.main_navigation_photos_popup));
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

    }

    private void showSignatures() {
        mSignBtnLayout.setVisibility(View.GONE);
        (findViewById(R.id.main_container)).setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, getSignaturesFragment())
                .commit();

        mSpinnerLikeBtn.setText(getResources().getString(R.string.main_navigation_signatures_popup));
        if (!SignatureUtil.noSigns())
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private Fragment getSignaturesFragment() {
        if (mSignaturesFragment == null) {
            mSignaturesFragment = new SignaturesFragment();
        }
        return mSignaturesFragment;
    }

    private Fragment getPhotosFragment() {
        if (mAlbumsFragment == null) {
            mAlbumsFragment = new AlbumsFragment();
        }
        return mAlbumsFragment;

    }

    private void initUtils() {
        SignsDB.initInstance(new MyDBHelper(getApplicationContext()));
        ViewUtil.initInstance(this);
        ThreadUtil.initInstance(this);
        ViewUtil.initImageLoader(this);
        CheckUtil.initIsFirstTimeOpened(this);


    }


    //Check the signs in /signs folder , and then ask user if he want to retrieve them
    private void retrieveLostSignatures() {


        if (CheckUtil.isFirstTime && SignatureUtil.noSigns()) {


            final File[] filesOfSignatures = FileUtil.getFilesOfSigns();

            final ArrayList<Signature> signatures = new ArrayList<>();

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
                .positiveText(android.R.string.yes)
                .neutralText(R.string.dismiss_btn)
                .callback(createCallBackForRetrieveDialog(signatures))
                .build()
                .show();
    }


    private MaterialDialog.FullCallback createCallBackForRetrieveDialog(final ArrayList<Signature> signatures) {
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

        final String html = "<br/> <b>White desert team</b>.<br/>\n" +
                "\n" +
                "       <a href=\'https://twitter.com/WhiteDesertT\'>Twitter</a>&nbsp;&nbsp;\n" +
                "       <a href=\'mailto:whitedesertteam@gmail.com\'>Email</a>";

        final Spanned content = Html.fromHtml(getString(R.string.about_body) + html);
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

        if (SignatureUtil.noSigns()) {
            showPhotos();
            ViewUtil.toastShort(R.string.you_have_to_create_signature_first);
            return;
        }

        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent signIntent = new Intent(MainActivity.this, SigningActivity.class);
                signIntent.putExtra(TO_SIGN_IMAGES_KEY, castPaths());
                sChosenPhotosPath.clear();
                startActivity(signIntent);

            }
        }, 450);


    }


    private String[] castPaths() {
        final String[] paths = new String[sChosenPhotosPath.size()];

        for (int i = 0; i < paths.length; i++) {
            paths[i] = sChosenPhotosPath.valueAt(i);
        }

        return paths;

    }


    private void updateTextAndBadge() {

        if (sChosenPhotosPath.size() == 0) {
            mSignBtn.setRippleColor(Color.BLACK, 0.5f);
            mSignBtn.setBackgroundColor(getResources().getColor(R.color.sign_btn_layout_disabled));
            mSignBtn.setClickable(false);
            mSignBtn.setTextColor(getResources().getColor(R.color.sign_btn_disabled_text));
            mSignBtnBadge.setVisibility(View.GONE);
            mSignBtn.setTypeface(Typeface.create("", Typeface.NORMAL));

        } else {
            mSignBtn.setRippleColor(Color.WHITE, 0.5f);
            mSignBtn.setBackgroundColor(getResources().getColor(R.color.sign_btn_layout));
            mSignBtn.setClickable(true);
            mSignBtn.setTextColor(getResources().getColor(R.color.sign_btn_text));
            mSignBtnBadge.setText(sChosenPhotosPath.size() + "");
            mSignBtnBadge.setVisibility(View.VISIBLE);
            mSignBtn.setTypeface(Typeface.create("", Typeface.BOLD));

        }
    }

    private void capture() {

        showPhotos();

        final File captureFolder = new File(Environment.getExternalStorageDirectory(), "capture" + System.currentTimeMillis() + ".png");

        mCapturedPhotoUri = Uri.fromFile(captureFolder);

        final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedPhotoUri);
        startActivityForResult(captureIntent, 0);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Log.d("onActivityResult", "Result is ok");

            if (requestCode == 0 && data == null) {

                sChosenPhotosPath.append((int) System.currentTimeMillis(), mCapturedPhotoUri.getPath());
                updateTextAndBadge();
            }
        } else {
            Log.d("onActivityResult", "Result is canceled");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //  getMenuInflater().inflate(R.menu.menu_settings, menu);
        getMenuInflater().inflate(R.menu.menu_about, menu);
        getMenuInflater().inflate(R.menu.menu_capture, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {

            case R.id.action_about:
                showAbout();
                return true;

            case R.id.action_capture:
                capture();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }


    @Override
    public void finish() {

        if (mImagesFragment != null && mImagesFragment.isVisible()) {
            showPhotos();

        } else if (mSignaturesFragment != null && mSignaturesFragment.isVisible()) {
            showPhotos();
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

        sChosenPhotosPath.put(photoEntry.imageId, photoEntry.path);
        updateTextAndBadge();

    }

    @Override
    public void onUnpickImage(AlbumUtil.PhotoEntry photo) {
        sChosenPhotosPath.remove(photo.imageId);
        updateTextAndBadge();
    }

    @Override
    public boolean onSupportNavigateUp() {
        if ((mImagesFragment != null && mImagesFragment.isVisible()) || (mSignaturesFragment != null && mSignaturesFragment.isVisible())) {
            finish();
            return true;
        }

        return false;
    }

    @Override
    public boolean toolbarUsed() {
        return true;
    }


    public static class ToSignImagesFragment extends ImagesFragment {

        @Override
        public void setupAdapter() {
            final AlbumUtil.AlbumEntry album = (AlbumUtil.AlbumEntry) getArguments().getSerializable(PickerActivity.ALBUM_KEY);
            final ToSignImagesAdapter adapter = new ToSignImagesAdapter(album, this);

            gridView.setAdapter(adapter);
        }
    }


    public static class ToSignImagesAdapter extends ImagesAdapter {
        public ToSignImagesAdapter(AlbumUtil.AlbumEntry album, ImagesFragment fragment) {
            super(album, fragment);
        }

        @Override
        public boolean isPicked(final AlbumUtil.PhotoEntry photoEntry) {

            boolean isPicked = false;

            for (int i = 0; i < sChosenPhotosPath.size(); i++) {

                if (photoEntry.path.equals(sChosenPhotosPath.valueAt(i))) {

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
                fragment.pickListener.onUnpickImage(photo);
            } else {
                //pick
                fragment.pickListener.onPickImage(photo);
            }

            drawGrid(convertView, holder, photo);
        }
    }


}
