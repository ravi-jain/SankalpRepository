package com.ravijain.sankalp.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.Spinner;

import com.ravijain.sankalp.R;
import com.ravijain.sankalp.activities.SpConstants;
import com.ravijain.sankalp.data.SpExceptionOrTarget;

import java.util.ArrayList;

/**
 * Created by ravijain on 9/9/2016.
 */
public class SpNumberPickerDialog extends DialogFragment {
    public static final int NUMBER_PICKER_DIALOG_TYPE_EXTAR = 1;
    public static final int NUMBER_PICKER_DIALOG_TYPE_EXTAR_CURRENT = 2;

    public interface EditNumberPickerDialogListener {
        void onFinishEditNumberPicker(int type, int id, int number);
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String title = "";
        final int type;
        Bundle b = getArguments();
        if (b != null) {
            title = b.getString(SpConstants.INTENT_KEY_NUMBER_PICKER_TITLE);
            type = b.getInt(SpConstants.INTENT_KEY_NUMBER_PICKER_TYPE, -1);
        } else {
            type = -1;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view;

        if (type == NUMBER_PICKER_DIALOG_TYPE_EXTAR) {
            view = inflater.inflate(R.layout.fragment_two_number_picker_dialog, null);
            final ArrayList<Integer> list = ((SpAddSankalpFragment) getTargetFragment()).getExceptionFrequencyList();
            String[] labels = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                labels[i] = SpExceptionOrTarget.getLabelById(getContext(), list.get(i));
            }
            NumberPicker np2 = (NumberPicker) view.findViewById(R.id.number_picker_left);
            np2.setMinValue(SpExceptionOrTarget.EXCEPTION_OR_TARGET_TOTAL);
            //np2.setValue(SpExceptionOrTarget.EXCEPTION_OR_TARGET_TOTAL);
            np2.setDisplayedValues(labels);
            np2.setMaxValue(labels.length - 1);
        }
        else {
            view = inflater.inflate(R.layout.fragment_number_picker_dialog, null);
        }

        NumberPicker np = (NumberPicker) view.findViewById(R.id.number_picker);
        np.setMinValue(0);
        np.setWrapSelectorWheel(false);
        np.setMaxValue(10000);

        builder.setTitle(title).setView(view).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                EditNumberPickerDialogListener listener = (EditNumberPickerDialogListener) getTargetFragment();
                NumberPicker np = (NumberPicker) ((AlertDialog) dialog).findViewById(R.id.number_picker);
                int id1 = -1;
                View v = ((AlertDialog) dialog).findViewById(R.id.number_picker_left);
                if (v != null) {
                    NumberPicker left = (NumberPicker)v;
                    id1 = left.getValue();
                }
                listener.onFinishEditNumberPicker(type, id1, np.getValue());
                dismiss();
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dismiss();
            }
        });
        return builder.create();
    }
}
