package com.ravijain.sankalp.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.ravijain.sankalp.R;
import com.ravijain.sankalp.support.SpConstants;

/**
 * Created by ravijain on 10/7/2016.
 */
public class SpSortDialog extends SpSimpleAlertDialog {

    @Override
    public void configureView(View view) {

        Bundle b = getArguments();
        if (b != null) {
            int sortId = b.getInt(SpConstants.INTENT_SORTID);
            RadioGroup sTRG = (RadioGroup) view.findViewById(R.id.rg_sort);
            if (sTRG == null) return;
            sTRG.check(sortId);
        }

    }
}
