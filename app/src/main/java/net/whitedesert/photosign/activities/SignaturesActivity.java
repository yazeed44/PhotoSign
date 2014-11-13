package net.whitedesert.photosign.activities;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.whitedesert.photosign.R;
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

    private boolean isListEmpty = false;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_signatures);
        signaturesList = (HeaderGridView) findViewById(R.id.list_signatures);

        setupDefaultSign();
        setupAdapter();


        if (isListEmpty) {
            // signaturesList.setEmptyView(getLayoutInflater().inf);
        }

        // defSignName = (TextView)findViewById(R.id.def_sign_name);
        // defSignImage = (ImageView)findViewById(R.id.def_sign_image);
        //initDefaultSign();

    }

    private void setupDefaultSign() {


        final Signature defSign = SignatureUtil.getDefaultSignature();

        if (defSign.equals(SignatureUtil.EMPTY_SIGNATURE)) {
            isListEmpty = true;
            return;
        }

        isListEmpty = false;


        final int width = getResources().getDimensionPixelSize(R.dimen.signature_column_width) * 2;
        final int height = width;

        final View defSignLayout = getLayoutInflater().inflate(R.layout.item_default_sign, null);

        final ImageView defImage = (ImageView) defSignLayout.findViewById(R.id.def_sign_image);

        final Drawable signImage = new BitmapDrawable(getResources(), defSign.getBitmap(width, height));

        defImage.setImageDrawable(signImage);


        final TextView defText = (TextView) defSignLayout.findViewById(R.id.def_sign_name);
        defText.setText(defSign.getName());

        final ImageView starImage = (ImageView) defSignLayout.findViewById(R.id.def_sign_star);
        starImage.setColorFilter(getResources().getColor(R.color.star_default_color));


        signaturesList.addHeaderView(defSignLayout);


    }

    private void setupAdapter() {
        final ArrayList<Signature> signs = SignatureUtil.getSigns(false);


        signaturesList.setAdapter(new CustomGridAdapter(this, signs));

    }

    private static class ViewHolder {
        TextView signName;
        ImageView star;
        ImageView signImage;
    }

    public class CustomGridAdapter extends BaseAdapter {
        private final ArrayList<Signature> signs;
        public Context context;


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

            return signs.get(position).getName().length();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Signature signature = signs.get(position);


            View grid;
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ViewHolder holder;
            if (convertView == null) {


                grid = inflater.inflate(R.layout.item_signature, null);
                setGridListener(grid, signature);

                holder = new ViewHolder();
                holder.signName = (TextView) grid.findViewById(R.id.signature_name);
                holder.star = (ImageView) grid.findViewById(R.id.signature_star);
                holder.signImage = (ImageView) grid.findViewById(R.id.signature_image);


                loadNormalSign(holder, signature);

            } else {
                grid = convertView;
                //holder = (ViewHolder) convertView.getTag();
            }


            return grid;
        }


        private void setGridListener(final View grid, final Signature sign) {

            grid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SignatureUtil.setDefaultSignature(sign.getName());
                    reloadActivity();

                }
            });
        }

        private void loadNormalSign(ViewHolder holder, Signature signature) {


            holder.signName.setText(signature.getName());

            final int columnWidth = getResources().getDimensionPixelSize(R.dimen.signature_column_width);

            final int columnHeight = (int) (columnWidth * 1.5);

            holder.signImage.setImageBitmap(signature.getBitmap(columnWidth, columnHeight));


        }


        private void reloadActivity() {

            startActivity(getIntent());
            finish();

        }


    }


}
