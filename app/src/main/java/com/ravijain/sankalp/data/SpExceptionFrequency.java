package com.ravijain.sankalp.data;

import android.content.Context;

import com.ravijain.sankalp.R;

/**
 * Created by ravijain on 7/6/2016.
 */
public class SpExceptionFrequency {

    public static final int FREQUENCY_UNDEFINED = 100;
    public static final int FREQUENCY_TOTAL = 0;
    public static final int FREQUENCY_YEARLY = 1;
    public static final int FREQUENCY_MONTHLY = 2;
    public static final int FREQUENCY_WEEKLY = 3;
    public static final int FREQUENCY_DAILY = 4;

    private int _id;
    private String _label;

    public SpExceptionFrequency(int id, Context context)
    {
        _id = id;
        switch (id) {
            case FREQUENCY_YEARLY:
                _label = context.getString(R.string.yearly);
                break;
            case FREQUENCY_MONTHLY:
                _label = context.getString(R.string.monthly);
                break;
            case FREQUENCY_WEEKLY:
                _label = context.getString(R.string.weekly);
                break;
            case FREQUENCY_DAILY:
                _label = context.getString(R.string.daily);
                break;
            case FREQUENCY_TOTAL:
            default:
                _label = context.getString(R.string.total);
        }
    }

    public String toString()
    {
        return _label;
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public String getLabel() {
        return _label;
    }

    public void setLabel(String label) {
        this._label = label;
    }
}
