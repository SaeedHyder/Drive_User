package com.ingic.driveuser.helpers;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

/**
 * Created on 4/26/2017.
 */

public class AlertHelper extends DialogFragment {
    public static void makeToast(Context context, String value, int duration) {
        Toast.makeText(context, value, duration).show();
    }

    public static void makeSnacbar(View view, String value, int duration, final String action, View.OnClickListener onClickListener) {
     /*   Snackbar.make(view, value, duration)
                .setAction(action, onClickListener).show();*/
    }

    public static Dialog showDialog(Context con, String title, String message, final String positiveText, final String NegativeText,
                                    DialogInterface.OnClickListener onPositiveClickListener, DialogInterface.OnClickListener onNegativeClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(con);
        builder.setMessage(message);
        builder.setTitle(title);

        builder.setPositiveButton(positiveText, onPositiveClickListener);

        builder.setNegativeButton("Cancel", onNegativeClickListener);
        AlertDialog dialog = builder.create();
      return  dialog;




    }
}
