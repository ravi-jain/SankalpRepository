package com.ravijain.sankalp.fragments;

import android.view.View;
import android.widget.TextView;

import com.ravijain.sankalp.R;
import com.ravijain.sankalp.data.SpSankalp;
import com.ravijain.sankalp.support.SpDateUtils;
import com.ravijain.sankalp.support.SpUtils;

/**
 * Created by ravijain on 1/16/2017.
 */
public class SpDailySankalpDialog extends SpSimpleAlertDialog {

    private SpSankalp _randomSankalp = null;

    public void setRandomSankalp(SpSankalp s) {
        _randomSankalp = s;
    }

    @Override
    public void configureView(View view) {

        TextView sTypeValue = (TextView) view.findViewById(R.id.sTypeValue);
        TextView iNameValue = (TextView) view.findViewById(R.id.iNameValue);
        TextView cNameValue = (TextView) view.findViewById(R.id.cNameValue);
        TextView dateValue = (TextView) view.findViewById(R.id.dateValue);

        sTypeValue.setText(SpUtils.getSankalpString(getContext(), _randomSankalp.getSankalpType()));
        iNameValue.setText(_randomSankalp.getItem().getCategoryItemDisplayName(getContext()));
        cNameValue.setText(_randomSankalp.getCategory().getCategoryDisplayName(getContext()));
        dateValue.setText(SpDateUtils.getFriendlyPeriodString(_randomSankalp.getFromDate(), _randomSankalp.getToDate(), false));
    }
}
