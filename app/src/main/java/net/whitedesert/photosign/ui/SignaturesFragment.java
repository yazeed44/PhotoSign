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
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.whitedesert.photosign.R;
import net.whitedesert.photosign.adapters.SignaturesGridAdapter;
import net.whitedesert.photosign.utils.CheckUtil;
import net.whitedesert.photosign.utils.Signature;
import net.whitedesert.photosign.utils.SignatureUtil;
import net.whitedesert.photosign.utils.ViewUtil;

/**
 * Created by yazeed44 on 11/4/14.
 */
public class SignaturesFragment extends Fragment {

    private HeaderGridView mSignaturesGrid;

    private boolean mIsListEmpty = false;

    private FrameLayout mLayout;
    private TextView mEmptyView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mLayout = (FrameLayout) inflater.inflate(R.layout.fragment_signatures, container, false);

        mSignaturesGrid = (HeaderGridView) mLayout.findViewById(R.id.list_signatures);

        mEmptyView = (TextView) mLayout.findViewById(R.id.empty_signatures_view);

        mIsListEmpty = SignatureUtil.noSigns();


        setupEmptyView();
        setupAddFab();


        setupDefaultSign();


        setupAdapter();


        return mLayout;
    }

    private void setupEmptyView() {
        updateVisibility();
    }

    private void updateVisibility() {
        mIsListEmpty = SignatureUtil.noSigns();

        if (mIsListEmpty) {
            mSignaturesGrid.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mSignaturesGrid.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        }
    }

    private void setupAddFab() {
        final FloatingActionButton fab = (FloatingActionButton) mLayout.findViewById(R.id.add_signature_fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewUtil.createChooseMethodToSignDialog(SignaturesFragment.this.getActivity()).show();
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


        final int height = getResources().getDimensionPixelSize(R.dimen.def_signature_height);

        final View defSignLayout = LayoutInflater.from(getActivity()).inflate(R.layout.item_default_sign, (ViewGroup) getView(), false);

        defSignLayout.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));

        final ImageView defImage = (ImageView) defSignLayout.findViewById(R.id.signature_image);
        defImage.setScaleType(ImageView.ScaleType.FIT_XY);


        ImageLoader.getInstance().displayImage(ViewUtil.GLOBAL_PATH + defSign.getPath(), defImage);


        final ImageView starImage = (ImageView) defSignLayout.findViewById(R.id.def_signature_star);
        starImage.setColorFilter(getResources().getColor(R.color.star_default_color));

        final View shareView = defSignLayout.findViewById(R.id.def_signature_share);
        shareView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String title = getResources().getString(R.string.share_signature_title);
                ViewUtil.shareImage(title, defSign.getPath(), v.getContext());
            }
        });


        mSignaturesGrid.addHeaderView(defSignLayout);
    }

    public void setupAdapter() {


        if (!mIsListEmpty) {
            SignaturesGridAdapter adapter = new SignaturesGridAdapter(SignatureUtil.getSigns(false), this);
            mSignaturesGrid.setAdapter(adapter);
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        updateVisibility();
        setupDefaultSign();
        setupAdapter();


    }

}
