package net.whitedesert.photosign.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

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

    public static void initDialog(AlertDialog.Builder dialog, String title, String message, int iconId) {
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setIcon(iconId);
    }

    public static void initDialog(AlertDialog.Builder dialog, int titleId, int msgId, int iconId) {
        Resources r = dialog.getContext().getResources();
        String title = r.getString(titleId), message = r.getString(msgId);
        initDialog(dialog, title, message, iconId);
    }

    public static Dialog createErrorDialog(String message, Context context) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        String title = context.getResources().getString(R.string.error_title);
        initDialog(dialog, message, title, android.R.drawable.ic_dialog_alert);
        dialog.setPositiveButton(R.string.ok, DISMISS_LISTENER);
        return dialog.create();

    }

    public static Dialog createErrorDialog(int resId, Context context) {
        return createErrorDialog(context.getResources().getString(resId), context);

    }


    public static AlertDialog.Builder getListDialog(ArrayList<String> texts, String title, String message, AdapterView.OnItemClickListener listener, Activity activity) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setNegativeButton(R.string.cancel, DialogUtil.DISMISS_LISTENER);
        initDialog(dialog, title, message, android.R.drawable.ic_dialog_info);
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
     * @return A dialog with one input
     */
    public static AlertDialog.Builder getInputDialog(String title, String message, String inputHint, OnClickListener posListener, EditText userInput, final Activity activity) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        initDialog(dialog, title, message, android.R.drawable.ic_input_add);
        userInput.setHint(inputHint);
        dialog.setView(userInput);
        dialog.setPositiveButton(R.string.ok, posListener);
        dialog.setNegativeButton(R.string.cancel, DISMISS_LISTENER);
        return dialog;

    }

    public static AlertDialog.Builder getInputDialog(int titleId, int msgId, int inputHintId, OnClickListener posListener, EditText userInput, final Activity activity) {
        Resources r = activity.getResources();
        String title = r.getString(titleId), message = r.getString(msgId), inputHint = r.getString(inputHintId);
        return getInputDialog(title, message, inputHint, posListener, userInput, activity);
    }

    public static AlertDialog.Builder getInputDialog(int titleId, int msgId, OnClickListener posListener, EditText userInput, final Activity activity) {
        Resources r = activity.getResources();
        String message = r.getString(msgId), title = r.getString(titleId);
        return getInputDialog(title, message, "", posListener, userInput, activity);
    }

}
