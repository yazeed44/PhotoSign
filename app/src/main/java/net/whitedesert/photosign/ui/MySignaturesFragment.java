package net.whitedesert.photosign.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.melnykov.fab.FloatingActionButton;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.whitedesert.photosign.R;
import net.whitedesert.photosign.adapters.SignaturesGridAdapter;
import net.whitedesert.photosign.utils.BitmapUtil;
import net.whitedesert.photosign.utils.CheckUtil;
import net.whitedesert.photosign.utils.DialogUtil;
import net.whitedesert.photosign.utils.Signature;
import net.whitedesert.photosign.utils.SignatureUtil;

/**
 * Created by yazeed44 on 11/4/14.
 */
public class MySignaturesFragment extends Fragment {

    private HeaderGridView mSignaturesGrid;

    private boolean mIsListEmpty = false;

    private FrameLayout mLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mLayout = (FrameLayout) inflater.inflate(R.layout.fragment_my_signatures, container, false);

        mSignaturesGrid = (HeaderGridView) mLayout.findViewById(R.id.list_signatures);


        mIsListEmpty = SignatureUtil.noSigns();

        setupAddFab();

        setupEmptyView();

        if (!mIsListEmpty) {
            setupDefaultSign();
            setupAdapter();
        }


        return mLayout;
    }

    private void setupEmptyView() {
        if (mIsListEmpty) {
            mSignaturesGrid.setEmptyView(LayoutInflater.from(getActivity()).inflate(R.layout.empty_view_signatures, mLayout, false));
        }
    }

    private void setupAddFab() {
        final FloatingActionButton fab = (FloatingActionButton) mLayout.findViewById(R.id.add_signature_fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtil.createChooseMethodToSignDialog(MySignaturesFragment.this.getActivity()).build().show();
            }
        });


        fab.attachToListView(mSignaturesGrid);
    }

    private void setupDefaultSign() {


        final Signature defSign = SignatureUtil.getDefaultSignature();

        if (!CheckUtil.checkSign(defSign) || mSignaturesGrid.getHeaderViewCount() > 0) {
            Log.i("setupDefaultSign", "There's no need to setup Default signature");
            return;
        }


        final int width = getResources().getDimensionPixelSize(R.dimen.signature_column_width) * 2;
        final int height = width;

        final View defSignLayout = LayoutInflater.from(getActivity()).inflate(R.layout.item_default_sign, null);

        defSignLayout.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));

        final ImageView defImage = (ImageView) defSignLayout.findViewById(R.id.signature_image);


        ImageLoader.getInstance().displayImage(BitmapUtil.GLOBAL_PATH + defSign.getPath(), defImage);


        final ImageView starImage = (ImageView) defSignLayout.findViewById(R.id.def_sign_star);
        starImage.setColorFilter(getResources().getColor(R.color.star_default_color));


        mSignaturesGrid.addHeaderView(defSignLayout);
    }

    public void setupAdapter() {


        SignaturesGridAdapter adapter = new SignaturesGridAdapter(SignatureUtil.getSigns(false), this);
        mSignaturesGrid.setAdapter(adapter);

    }


}
