package net.whitedesert.photosign.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import net.whitedesert.photosign.R;

import java.util.ArrayList;

/**
 * Created by yazeed44 on 8/16/14.
 * Class for making simple dialogs
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

    public static AlertDialog.Builder initDialog(String title, String message, int iconId, boolean cancelBtn, final Activity activity) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(new ContextThemeWrapper(activity, R.style.dialog));
        dialog.setCustomTitle(ViewUtil.getBlackTitle(title, activity));
        dialog.setMessage(message);
        dialog.setIcon(iconId);

        if (cancelBtn)
            dialog.setNegativeButton(R.string.cancel, DISMISS_LISTENER);

        return dialog;
    }

    public static AlertDialog initDialog(String title, String message, final Activity activity) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(new ContextThemeWrapper(activity, R.style.dialog));
        dialog.setCustomTitle(ViewUtil.getBlackTitle(title, activity));
        dialog.setMessage(message);
        return dialog.create();
    }


    public static Dialog createErrorDialog(String message, Activity activity) {

        String title = activity.getResources().getString(R.string.error_title);
        AlertDialog.Builder dialog = initDialog(title, message, android.R.drawable.ic_dialog_alert, false, activity);
        dialog.setPositiveButton(R.string.ok, DISMISS_LISTENER);
        return dialog.create();

    }


    public static Dialog createErrorDialog(int msgId, Activity activity) {
        return createErrorDialog(activity.getResources().getString(msgId), activity);

    }


    public static AlertDialog.Builder getListDialog(ArrayList<String> texts, String title, String message, AdapterView.OnItemClickListener listener, final Activity activity) {

        final AlertDialog.Builder dialog = initDialog(title, message, android.R.drawable.ic_dialog_info, true, activity);
        View listDialog = activity.getLayoutInflater().inflate(R.layout.list_view_dialog, null);
        ListView listView = (ListView) listDialog.findViewById(R.id.dialogList);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(listDialog.getContext(), android.R.layout.simple_list_item_1, texts);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(listener);
        dialog.setView(listDialog);
        return dialog;
    }

    public static AlertDialog.Builder getListDialog(ArrayList<String> texts, int titleId, int messageId, AdapterView.OnItemClickListener listener, final Activity activity) {
        Resources res = activity.getResources();
        String title = res.getString(titleId);
        String message = res.getString(messageId);
        return getListDialog(texts, title, message, listener, activity);
    }

    /**
     * @param title    title of dialog
     * @param message  message of the dialog
     * @param activity the activity is important to lanuch the dialog
     * @return A dialog with one input
     */
    public static AlertDialog.Builder getInputDialog(String title, String message, String inputHint, OnClickListener posListener, EditText userInput, final Activity activity) {
        AlertDialog.Builder dialog = initDialog(title, message, android.R.drawable.ic_input_add, true, activity);
        userInput.setHint(inputHint);
        dialog.setView(userInput);
        dialog.setPositiveButton(R.string.ok, posListener);
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


    public static AlertDialog.Builder getSingleChooseDialog(String title, String[] choices, OnClickListener choiceListener, final Activity activity) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        //the choices won't appear if you set a message !!
        dialog.setTitle(title);
        dialog.setIcon(android.R.drawable.ic_dialog_info);
        dialog.setNegativeButton(R.string.cancel, DISMISS_LISTENER);
        dialog.setSingleChoiceItems(choices, -1, choiceListener);

        Log.i("DialogUtil : getSingleChooseDialog : ", "We got " + choices.length + " choices !");


        return dialog;
    }

    public static AlertDialog.Builder getSingleChooseDialog(int titleId, int choicesId, OnClickListener listener, final Activity activity) {
        Resources r = activity.getResources();
        String title = r.getString(titleId);
        String[] choices = r.getStringArray(choicesId);
        return getSingleChooseDialog(title, choices, listener, activity);
    }

    public static AlertDialog.Builder getCustomViewDialog(String title, String message, View customView, final Activity activity) {

        final AlertDialog.Builder dialog = initDialog(title, message, android.R.drawable.ic_dialog_info, true, activity);
        dialog.setView(customView);
        return dialog;
    }

    public static AlertDialog.Builder getCustomViewDialog(int titleId, int messageId, View customView, final Activity activity) {
        Resources r = activity.getResources();
        String title = r.getString(titleId), message = r.getString(messageId);
        return getCustomViewDialog(title, message, customView, activity);
    }

    public static AlertDialog.Builder getImageViewDialog(final String title, final String message, final Bitmap image, final Activity activity) {
        ImageView imageView = new ImageView(activity);
        imageView.setImageBitmap(image);
        return getCustomViewDialog(title, message, imageView, activity);
    }

    public static AlertDialog.Builder getImageViewDialog(final int titleId, final int messageId, final Bitmap bitmap, final Activity activity) {
        Resources r = activity.getResources();
        String title = r.getString(titleId), message = r.getString(messageId);
        return getImageViewDialog(title, message, bitmap, activity);
    }

    public static ProgressDialog getProgressDialog(final String title, final String message, final Activity activity) {
        return (ProgressDialog) initDialog(title, message, activity);
    }

    public static ProgressDialog getProgressDialog(int titleId, int msgId, final Activity activity) {
        final Resources r = activity.getResources();
        return getProgressDialog(r.getString(titleId), r.getString(msgId), activity);
    }

}
