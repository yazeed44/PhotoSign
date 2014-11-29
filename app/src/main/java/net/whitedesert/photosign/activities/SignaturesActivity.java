package net.whitedesert.photosign.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.melnykov.fab.FloatingActionButton;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.whitedesert.photosign.R;
import net.whitedesert.photosign.adapters.SignaturesGridAdapter;
import net.whitedesert.photosign.utils.AskUtil;
import net.whitedesert.photosign.utils.BitmapUtil;
import net.whitedesert.photosign.utils.CheckUtil;
import net.whitedesert.photosign.utils.Signature;
import net.whitedesert.photosign.utils.SignatureUtil;
import net.whitedesert.photosign.views.HeaderGridView;

import java.util.ArrayList;

/**
 * Created by yazeed44 on 11/4/14.
 * //TODO Improve UI of this activty to be more like Google I/O 2014
 */
public class SignaturesActivity extends AdActivity {

    private HeaderGridView signaturesList;

    private ArrayList<Signature> signaturesArray;

    private boolean isListEmpty = false;

    private SignaturesGridAdapter adapter;


    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_signatures);
        signaturesList = (HeaderGridView) findViewById(R.id.list_signatures);

        getSupportActionBar().setTitle(R.string.my_signatures_title);


        isListEmpty = SignatureUtil.noSigns();

        setupAddFab();

        setupEmptyView();

        if (!isListEmpty) {
            setupDefaultSign();
            setupAdapter();
        }


    }


    public void onClickAdd(View view) {
        AskUtil.selectMethodSign(this);
    }

    private void setupEmptyView() {
        if (isListEmpty) {
            signaturesList.setEmptyView(getLayoutInflater().inflate(R.layout.empty_view_signatures, (ViewGroup) signaturesList.getParent()));
        }
    }

    private void setupAddFab() {
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_signature_fab);


        fab.attachToListView(signaturesList);
    }

    private void setupDefaultSign() {


        final Signature defSign = SignatureUtil.getDefaultSignature();

        if (!CheckUtil.checkSign(defSign) || signaturesList.getHeaderViewCount() > 0) {
            Log.i("setupDefaultSign", "There's no need to setup Default signature");
            return;
        }


        final int width = getResources().getDimensionPixelSize(R.dimen.signature_column_width) * 2;
        final int height = width;

        final View defSignLayout = getLayoutInflater().inflate(R.layout.item_default_sign, null);

        defSignLayout.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));

        final ImageView defImage = (ImageView) defSignLayout.findViewById(R.id.signature_image);

        // final Drawable signImage = new BitmapDrawable(getResources(), defSign.getBitmap(width, height));

//        defImage.setImageDrawable(signImage);

        ImageLoader.getInstance().displayImage(BitmapUtil.GLOBAL_PATH + defSign.getPath(), defImage);
        // defImage.setColorFilter(ViewUtil.makeSessionImageScrimColorFilter(getResources().getColor(R.color.def_sign_image_overlay)));
        // defSignLayout.setBackgroundResource(R.color.def_sign_image);


        final ImageView starImage = (ImageView) defSignLayout.findViewById(R.id.def_sign_star);
        starImage.setColorFilter(getResources().getColor(R.color.star_default_color));


        signaturesList.addHeaderView(defSignLayout);
    }

    public void setupAdapter() {


        adapter = new SignaturesGridAdapter(SignatureUtil.getSigns(false), this);
        signaturesList.setAdapter(adapter);

    }


    @Override
    protected void onResume() {
        super.onResume();
        setupDefaultSign();
    }


}
