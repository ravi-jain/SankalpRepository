package com.ravijain.sankalp.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;

import com.ravijain.sankalp.R;
import com.ravijain.sankalp.data.SpExceptionOrTarget;

import java.util.ArrayList;

/**
 * Created by ravijain on 9/9/2016.
 */
public class SpNumberPickerDialog extends SpSimpleAlertDialog {

    public void configureView(View view) {

        Bundle b = getArguments();
        if (b != null) {
            int layoutId = b.getInt(AD_LAYOUT_ID);
            if (layoutId == R.layout.fragment_two_number_picker_dialog) {

                final ArrayList<Integer> list = ((SpAddSankalpFragment) getTargetFragment()).getExceptionFrequencyList();
                String[] labels = new String[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    labels[i] = SpExceptionOrTarget.getLabelById(getContext(), list.get(i));
                }
                NumberPicker np2 = (NumberPicker) view.findViewById(R.id.number_picker_left);
                np2.setMinValue(SpExceptionOrTarget.EXCEPTION_OR_TARGET_TOTAL);
                np2.setDisplayedValues(labels);
                np2.setMaxValue(labels.length - 1);
            }

            NumberPicker np = (NumberPicker) view.findViewById(R.id.number_picker);
            np.setMinValue(0);
            np.setWrapSelectorWheel(false);
            np.setMaxValue(10000);
        }
    }
}
