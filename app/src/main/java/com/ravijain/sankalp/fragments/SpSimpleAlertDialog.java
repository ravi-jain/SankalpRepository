package com.ravijain.sankalp.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.ravijain.sankalp.R;

/**
 * Created by ravijain on 9/19/2016.
 */
public class SpSimpleAlertDialog extends DialogFragment {

    public static final String AD_LAYOUT_ID = "AD_LAYOUT_ID";
    public static final String AD_TITLE = "AD_TITLE";
    public static final String AD_OK_RESOURCE_ID = "AD_OK_RESOURCE_ID";
    public static final String AD_CANCEL_RESOURCE_ID = "AD_CANCEL_RESOURCE_ID";

    public interface SpSimpleAlertDialogListener {

        public void onSimpleAlertDialogPositiveClick(AlertDialog dialog, String tag);

        public void onSimpleAlertDialogNegativeClick(AlertDialog dialog, String tag);
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle b = getArguments();
        if (b != null) {

            int layoutId = b.getInt(AD_LAYOUT_ID);
            String title = b.getString(AD_TITLE);
            int okResourceId = b.getInt(AD_OK_RESOURCE_ID, R.string.ok);
            int cancelResourceId = b.getInt(AD_CANCEL_RESOURCE_ID, R.string.cancel);

            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(layoutId, null);

            configureView(view);

            builder = builder.setTitle(title).setView(view);
            if (hasOkButton()) {
                builder = builder.setPositiveButton(okResourceId, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Fragment f = getTargetFragment();
                        if (f != null && f instanceof SpSimpleAlertDialogListener) {
                            ((SpSimpleAlertDialogListener) f).onSimpleAlertDialogPositiveClick((AlertDialog) dialog, getTag());
                        }
                        dismiss();
                    }
                });
            }
            if (hasCancelButton()) {
                builder = builder.setNegativeButton(cancelResourceId, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Fragment f = getTargetFragment();
                        if (f != null && f instanceof SpSimpleAlertDialogListener) {
                            ((SpSimpleAlertDialogListener) f).onSimpleAlertDialogNegativeClick((AlertDialog) dialog, getTag());
                        }
                        dismiss();
                    }
                });
            }



            // Create the AlertDialog object and return it
            return builder.create();
        }
        return null;
    }

    protected boolean hasOkButton()
    {
        return true;
    }

    protected boolean hasCancelButton()
    {
        return true;
    }

    public void configureView(View view) {
        // Do Nothing
    }
}
