package net.whitedesert.photosign.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import net.whitedesert.photosign.R;

import java.util.ArrayList;

/**
 * Created by yazeed44 on 8/16/14.
 */
public final class DialogUtil {


    public static final DialogInterface.OnClickListener DISMISS_LISTENER = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
        }
    };

    private DialogUtil() {
        throw new AssertionError();
    }

    public static Dialog createErrorDialog(String message, Context context) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage(message);
        dialog.setTitle(R.string.error_title);
        dialog.setIcon(android.R.drawable.ic_dialog_alert);
        dialog.setPositiveButton(R.string.ok, DISMISS_LISTENER);

        return dialog.create();

    }

    public static Dialog createErrorDialog(int resId, Context context) {
        return createErrorDialog(context.getResources().getString(resId), context);

    }

    public static void styleAll(int styleId, Activity activity, TextView... views) {

        for (int i = 0; i < views.length; i++) {
            views[i].setTextAppearance(activity, styleId);
        }
    }

    public static AlertDialog.Builder getListDialog(ArrayList<String> texts, AdapterView.OnItemClickListener listener, Activity activity) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        final View listDialog = activity.getLayoutInflater().inflate(R.layout.dialog_list, null);
        final ListView list = (ListView) listDialog.findViewById(R.id.dialogList);
        final ArrayAdapter adapter = new ArrayAdapter(activity, android.R.layout.simple_list_item_1, texts);
        list.setOnItemClickListener(listener);
        list.setAdapter(adapter);
        dialog.setView(listDialog);
        return dialog;
    }
}
