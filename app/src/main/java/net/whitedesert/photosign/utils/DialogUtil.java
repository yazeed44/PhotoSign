package net.whitedesert.photosign.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
    private static String userInputStr = null;

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

    public static AlertDialog.Builder getListDialog(ArrayList<String> texts, String title, String message, AdapterView.OnItemClickListener listener, Activity activity) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setNegativeButton(R.string.cancel, DialogUtil.DISMISS_LISTENER);
        dialog.setTitle(title);
        dialog.setMessage(message);
        View listDialog = activity.getLayoutInflater().inflate(R.layout.dialog_list, null);
        ListView listView = (ListView) listDialog.findViewById(R.id.dialogList);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(listDialog.getContext(), android.R.layout.simple_list_item_1, texts); // i doubt this one
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(listener);
        dialog.setView(listDialog);
        return dialog;
    }

    public static AlertDialog.Builder getListDialog(ArrayList<String> texts, int titleId, int messageId, AdapterView.OnItemClickListener listener, Activity activity) {
        Resources res = activity.getResources();
        String title = res.getString(titleId);
        String message = res.getString(messageId);
        return getListDialog(texts, title, message, listener, activity);
    }

    public static AlertDialog.Builder getListDialog(ArrayList<String> texts, AdapterView.OnItemClickListener listener, Activity activity) {
        Resources res = activity.getResources();
        String title = res.getString(R.string.list_view_default_title);
        String message = res.getString(R.string.list_view_default_message);
        return getListDialog(texts, title, message, listener, activity);
    }

    /**
     * @param title
     * @param message
     * @param activity
     * @return the user input
     */

    public static String askUser(String title, String message, String inputHint, final Activity activity) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle(title);
        dialog.setMessage(message);

        final EditText userInput = new EditText(activity);
        userInput.setHint(inputHint);

        dialog.setView(userInput);

        dialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                userInputStr = userInput.getText().toString();
            }
        });

        dialog.setNegativeButton(R.string.cancel, DISMISS_LISTENER);

        return userInputStr;
    }

    public static String askUser(int titleId, int msgId, int inputHintId, final Activity activity) {
        Resources r = activity.getResources();
        String title = r.getString(titleId), message = r.getString(msgId), inputHint = r.getString(inputHintId);
        return askUser(title, message, inputHint, activity);
    }

    public static String askUser(int titleId, int msgId, final Activity activity) {
        Resources r = activity.getResources();

        String message = r.getString(msgId), title = r.getString(titleId);
        return askUser(title, message, "", activity);
    }
}
