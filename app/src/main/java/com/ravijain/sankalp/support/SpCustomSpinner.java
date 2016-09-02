package com.ravijain.sankalp.support;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.Spinner;

/**
 * Created by ravijain on 9/2/2016.
 */
public class SpCustomSpinner extends Spinner {

    private int prevPos = -1;
    public SpCustomSpinner(Context context) {
        super(context);
    }

    public SpCustomSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SpCustomSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SpCustomSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
    }

    @Override
    public void setSelection(int position) {
        super.setSelection(position);
        if (position == getSelectedItemPosition() && prevPos == position) {
            getOnItemSelectedListener().onItemSelected(this, null, position, 0);
        }
        prevPos = position;
    }
}
