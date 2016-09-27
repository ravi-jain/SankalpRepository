package com.ravijain.sankalp.fragments;

import android.os.Bundle;
import android.view.View;

import com.ravijain.sankalp.R;
import com.ravijain.sankalp.support.SpConstants;

/**
 * Created by ravijain on 8/2/2016.
 */
public class SpFilterDialog extends SpSimpleAlertDialog {

    private boolean _isVisible(int listFilter) {
        return listFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_TOMORROW ||
                listFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_MONTH ||
                listFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_YEAR;
    }

    public void configureView(View view) {
        Bundle b = getArguments();
        if (b != null) {
            int listFilter = b.getInt(SpConstants.INTENT_KEY_SANKALP_LIST_FILTER);
            if (_isVisible(listFilter)) {
                view.findViewById(R.id.filterDialog_periodView).setVisibility(View.VISIBLE);
            }
        }
    }
}

