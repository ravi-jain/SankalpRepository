package com.ravijain.sankalp.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;

import com.ravijain.sankalp.R;
import com.ravijain.sankalp.activities.SpConstants;
import com.ravijain.sankalp.activities.SpSankalpList;
import com.ravijain.sankalp.data.SpDataConstants;

import java.util.Date;

/**
 * Created by ravijain on 8/28/2016.
 */
public class SpIntervalDialogFragment extends DialogFragment {

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.interval_dialog, null);

        builder.setView(view)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        RadioGroup sTRG = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.rg_interval);
                        if (sTRG == null) return;
                        int id1 = sTRG.getCheckedRadioButtonId();
                        int listFilter = SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_DAY;
                        if (id1 == R.id.rb_day) {
                            listFilter = SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_DAY;
                        }
                        else if (id1 == R.id.rb_month) {
                            listFilter = SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_MONTH;
                        }
                        else if (id1 == R.id.rb_year) {
                            listFilter = SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_YEAR;
                        }

                        Intent intent = new Intent(getContext(), SpSankalpList.class);
                        intent.putExtra(SpConstants.INTENT_KEY_SANKALP_TYPE, SpDataConstants.SANKALP_TYPE_BOTH);
                        intent.putExtra(SpConstants.INTENT_KEY_SANKALP_LIST_FILTER, listFilter);
                        intent.putExtra(SpConstants.INTENT_KEY_SANKALP_LIST_FILTER_DATE_VALUE, new Date().getTime());
                        getContext().startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //mListener.onDialogNegativeClick(SpFilterDialogFragment.this);
                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }
}

