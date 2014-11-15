package net.whitedesert.photosign.activities;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.afollestad.materialdialogs.MaterialDialog;
import com.melnykov.fab.FloatingActionButton;

import net.whitedesert.photosign.R;
import net.whitedesert.photosign.utils.AskUtil;
import net.whitedesert.photosign.utils.CheckUtil;
import net.whitedesert.photosign.utils.DialogUtil;
import net.whitedesert.photosign.utils.PopupMenuUtil;
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

    private CustomGridAdapter adapter;

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

    @Override
    protected void onResume() {
        super.onResume();
        refreshAdapter();
    }


    public void onClickAdd(View view) {
        AskUtil.selectMethodSign(this);
    }

    private void setupEmptyView() {
        if (isListEmpty) {
            // signaturesList.setEmptyView(getLayoutInflater().inf);
        }
    }

    private void setupAddFab() {
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_signature_fab);


        fab.attachToListView(signaturesList);
    }

    private void setupDefaultSign() {


        final Signature defSign = SignatureUtil.getDefaultSignature();

        if (!CheckUtil.checkSign(defSign)) {
            return;
        }


        final int width = getResources().getDimensionPixelSize(R.dimen.signature_column_width) * 2;
        final int height = width;

        final View defSignLayout = getLayoutInflater().inflate(R.layout.item_default_sign, null);

        final ImageView defImage = (ImageView) defSignLayout.findViewById(R.id.def_sign_image);

        final Drawable signImage = new BitmapDrawable(getResources(), defSign.getBitmap(width, height));

        defImage.setImageDrawable(signImage);


        final ImageView starImage = (ImageView) defSignLayout.findViewById(R.id.def_sign_star);
        starImage.setColorFilter(getResources().getColor(R.color.star_default_color));


        signaturesList.addHeaderView(defSignLayout);


    }

    private void setupAdapter() {
        signaturesArray = SignatureUtil.getSigns(false);

        adapter = new CustomGridAdapter(this, signaturesArray);
        signaturesList.setAdapter(adapter);

    }

    private void refreshAdapter() {
        signaturesArray = SignatureUtil.getSigns(false);
        adapter = new CustomGridAdapter(this, signaturesArray);
        signaturesList.setAdapter(adapter);
    }

    private static class ViewHolder {
        ImageView menu;
        ImageView signImage;
    }

    public class CustomGridAdapter extends BaseAdapter {
        public Context context;
        private ArrayList<Signature> signs;


        public CustomGridAdapter(Context c, ArrayList<Signature> signs) {

            context = c;
            this.signs = signs;


        }

        @Override
        public int getCount() {

            return signs.size();
        }

        @Override
        public Object getItem(int position) {

            return signs.get(position);
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Signature signature = signs.get(position);


            View grid;
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ViewHolder holder = new ViewHolder();
            if (convertView == null) {


                grid = inflater.inflate(R.layout.item_signature, null);


                initializeGrid(grid, holder);
                loadNormalSign(holder, signature);

                setupMenu(holder, signature);

            } else {
                grid = convertView;


            }


            return grid;
        }

        private void initializeGrid(final View grid, ViewHolder holder) {
            holder.menu = (ImageView) grid.findViewById(R.id.signature_menu);
            holder.signImage = (ImageView) grid.findViewById(R.id.signature_image);

        }

        private void loadNormalSign(ViewHolder holder, Signature signature) {

            final int columnWidth = getResources().getDimensionPixelSize(R.dimen.signature_column_width);

            final int columnHeight = (int) (columnWidth * 1.5);

            holder.signImage.setImageBitmap(signature.getBitmap(columnWidth, columnHeight));


        }


        private void setupMenu(final ViewHolder holder, final Signature signature) {

            holder.menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {

                    final PopupMenu popupMenu = PopupMenuUtil.initMenu(holder.menu, R.menu.popup_signature);


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
            reloadActivity();
        }

        private void delete(final Signature signature) {
            createDeleteFileDialog(signature).show();
        }

        private MaterialDialog createDeleteFileDialog(final Signature signature) {
            final String msg = getResources().getString(R.string.delete_sign_file_msg);
            final MaterialDialog.Builder deleteFileDialog = DialogUtil.initDialog(null, msg, SignaturesActivity.this);

            return deleteFileDialog.positiveText(R.string.yes_btn)
                    .negativeText(R.string.delete_only_signature_btn)
                    .neutralText(R.string.dismiss_btn)
                    .callback(getCallBackForDeleteFileDialog(signature))
                    .build();

        }

        private MaterialDialog.FullCallback getCallBackForDeleteFileDialog(final Signature signature) {
            return new MaterialDialog.FullCallback() {
                @Override
                public void onNeutral(MaterialDialog materialDialog) {
                    materialDialog.dismiss();
                }

                @Override
                public void onPositive(MaterialDialog materialDialog) {
                    SignatureUtil.deleteSignature(signature.getName(), true);
                    refreshAdapter();
                    notifyDataSetInvalidated();
                }

                @Override
                public void onNegative(MaterialDialog materialDialog) {
                    SignatureUtil.deleteSignature(signature.getName(), false);
                    refreshAdapter();
                    notifyDataSetInvalidated();
                }
            };
        }




        private void reloadActivity() {

            startActivity(getIntent());
            finish();

        }


    }


}
