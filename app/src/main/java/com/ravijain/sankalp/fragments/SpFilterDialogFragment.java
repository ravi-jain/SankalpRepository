package com.ravijain.sankalp.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;

import com.ravijain.sankalp.R;
import com.ravijain.sankalp.activities.SpConstants;

/**
 * Created by ravijain on 8/2/2016.
 */
public class SpFilterDialogFragment extends DialogFragment {
    private int _listFilter;
    private SpSankalpListFragment _parentFragment;

    public void setListFilter(int listFilter) {
        _listFilter = listFilter;
    }
    public void setParentFragment(SpSankalpListFragment f) {_parentFragment = f;};

    /* The activity that creates an instance of this dialog fragment must
        * implement this interface in order to receive event callbacks.
        * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NoticeDialogListener {
        public void onDialogPositiveClick(int sankalpType, int period);

        public void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    NoticeDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = new NoticeDialogListener() {
                @Override
                public void onDialogPositiveClick(int sankalpType, int period) {
                    _parentFragment.onDialogPositiveClick(sankalpType,period);
                }

                @Override
                public void onDialogNegativeClick(DialogFragment dialog) {

                }
            };
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    private boolean _isVisible()
    {
        return _listFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_TOMORROW ||
                _listFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_MONTH ||
                _listFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_YEAR;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.filter_dialog, null);
        if (_isVisible()) {
            view.findViewById(R.id.filterDialog_periodView).setVisibility(View.VISIBLE);
        }

        builder.setView(view)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        RadioGroup sTRG = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.rg_sankalpType);

                        int id1 = sTRG.getCheckedRadioButtonId();

                        int id2 = -1;
                        if (_isVisible()) {
                            RadioGroup sPRG = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.rg_sankalpPeriod);
                            id2 = sPRG != null ?sPRG.getCheckedRadioButtonId() : -1;
                        }

                        mListener.onDialogPositiveClick(id1, id2);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick(SpFilterDialogFragment.this);
                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }
}

