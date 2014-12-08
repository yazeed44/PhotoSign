package net.whitedesert.photosign.adapters;

import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.whitedesert.photosign.R;
import net.whitedesert.photosign.ui.SignaturesFragment;
import net.whitedesert.photosign.utils.CheckUtil;
import net.whitedesert.photosign.utils.Signature;
import net.whitedesert.photosign.utils.SignatureUtil;
import net.whitedesert.photosign.utils.ViewUtil;

import java.util.ArrayList;

/**
 * Created by yazeed44 on 11/15/14.
 */
public class SignaturesGridAdapter extends BaseAdapter {


    private final SignaturesFragment mFragment;


    private final ArrayList<Signature> signatures;


    public SignaturesGridAdapter(final ArrayList<Signature> signatures, final SignaturesFragment fragment) {
        this.signatures = signatures;
        this.mFragment = fragment;
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
        final Signature signature = signatures.get(position);
        if (!CheckUtil.checkPath(signature.getPath())) {
            SignatureUtil.deleteSignature(signature, true);
            return convertView;
        }


        View grid = convertView;

        final ViewHolder holder;

        if (convertView == null) {
            grid = mFragment.getActivity().getLayoutInflater().inflate(R.layout.item_signature, parent, false);
            holder = new ViewHolder();
            initializeGrid(grid, holder);
            grid.setTag(holder);


        } else {
            holder = (ViewHolder) grid.getTag();
        }

        loadNormalSign(holder, grid, signature);

        setupMenu(holder, signature);

        return grid;


    }

    private void initializeGrid(final View grid, final ViewHolder holder) {


        holder.image = (ImageView) grid.findViewById(R.id.signature_image);
        holder.menu = (ImageView) grid.findViewById(R.id.signature_menu);


    }

    private void loadNormalSign(ViewHolder holder, final View grid, final Signature signature) {


        int colWidth = mFragment.getResources().getDimensionPixelSize(R.dimen.signature_column_width);

        int colHeight = (int) (colWidth * 1.5);

        //      int signColor = getSignColor();
        grid.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, colHeight));

        final String path = ViewUtil.GLOBAL_PATH + signature.getPath();

        ImageLoader.getInstance().displayImage(path, holder.image);


    }

    private void setupMenu(final ViewHolder holder, final Signature signature) {

        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                final PopupMenu popupMenu = ViewUtil.createMenu(holder.menu, R.menu.popup_signature);


                setupPopupMenuListener(popupMenu, signature);
                popupMenu.show();
            }
        });
    }

    private void setupPopupMenuListener(final PopupMenu popupMenu, final Signature signature) {


        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.popup_set_default_signature:
                        setDefault(signature);
                        break;

                    case R.id.popup_delete_signature:
                        delete(signature);
                        break;

                    default:
                        break;
                }


                return true;
            }
        });
    }

    private void setDefault(final Signature signature) {
        SignatureUtil.setDefaultSignature(signature);
        reloadFragment();
    }

    private void delete(final Signature signature) {
        createDeleteFileDialog(signature).show();
    }

    private MaterialDialog createDeleteFileDialog(final Signature signature) {
        final String msg = createMsgForDeleteDialog(signature);
        final MaterialDialog.Builder deleteFileDialog = ViewUtil.createDialog(null, msg, mFragment.getActivity());

        return deleteFileDialog.positiveText(android.R.string.yes)
                .negativeText(R.string.delete_only_signature_btn)
                .neutralText(R.string.dismiss_btn)
                .callback(createCallBackForDeleteDialog(signature))
                .build();

    }

    private String createMsgForDeleteDialog(final Signature signature) {
        return mFragment.getResources().getString(R.string.delete_sign_file_msg).replace("0", signature.getName());
    }

    private MaterialDialog.FullCallback createCallBackForDeleteDialog(final Signature signature) {
        return new MaterialDialog.FullCallback() {
            @Override
            public void onNeutral(MaterialDialog materialDialog) {
                materialDialog.dismiss();
            }

            @Override
            public void onPositive(MaterialDialog materialDialog) {
                SignatureUtil.deleteSignature(signature, true);
                refresh();

            }

            @Override
            public void onNegative(MaterialDialog materialDialog) {
                SignatureUtil.deleteSignature(signature, false);
                refresh();

            }
        };
    }

    private void refresh() {

        mFragment.setupAdapter();
    }

    private void reloadFragment() {

        mFragment.getActivity().getSupportFragmentManager().beginTransaction()
                .detach(mFragment)
                .attach(mFragment)
                .commit()
        ;

        refresh();
    }

    public static class ViewHolder {
        public ImageView image, menu;
    }


}
