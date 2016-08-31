package com.ravijain.sankalp.data;

import android.content.Context;

import com.ravijain.sankalp.R;

/**
 * Created by ravijain on 7/6/2016.
 */
public class SpExceptionOrTarget {

    public static final int EXCEPTION_OR_TARGET_UNDEFINED = -1;
    public static final int EXCEPTION_OR_TARGET_TOTAL = 0;
    public static final int EXCEPTION_OR_TARGET_YEARLY = 1;
    public static final int EXCEPTION_OR_TARGET_MONTHLY = 2;
    public static final int EXCEPTION_OR_TARGET_WEEKLY = 3;
    public static final int EXCEPTION_OR_TARGET_DAILY = 4;

    private int _id;
    private String _label;
    private int _exceptionOrTargetCount;
    private int _exceptionOrTargetCountCurrent = EXCEPTION_OR_TARGET_UNDEFINED;
    private Context _context;

    public SpExceptionOrTarget(int id, Context context)
    {
        _id = id;
        _context = context;
        _setLabel(id);
    }

    private void _setLabel(int id)
    {
        switch (id) {
            case EXCEPTION_OR_TARGET_YEARLY:
                _label = _context.getString(R.string.yearly);
                break;
            case EXCEPTION_OR_TARGET_MONTHLY:
                _label = _context.getString(R.string.monthly);
                break;
            case EXCEPTION_OR_TARGET_WEEKLY:
                _label = _context.getString(R.string.weekly);
                break;
            case EXCEPTION_OR_TARGET_DAILY:
                _label = _context.getString(R.string.daily);
                break;
            case EXCEPTION_OR_TARGET_TOTAL:
            default:
                _label = _context.getString(R.string.total2);
        }
    }

    public String getRepresentationalSummary()
    {
        StringBuilder s = new StringBuilder();
        if (getExceptionOrTargetCount() == 0) {
            s.append("None");
        }
        else {
            s.append(getLabel()).append(" ");
            if (getExceptionOrTargetCount() > 1) {
                s.append(String.valueOf(getExceptionOrTargetCount())).append(" times");
            }
            else if (getExceptionOrTargetCount() == 1) {
                s.append(" once");
            }
        }

        return s.toString();
    }

    public int getExceptionOrTargetCount() {
        return _exceptionOrTargetCount;
    }

    public void setExceptionOrTargetCount(int exceptionOrTargetCount) {
        this._exceptionOrTargetCount = exceptionOrTargetCount;
    }

    public int getExceptionOrTargetCountCurrent() {
        return _exceptionOrTargetCountCurrent;
    }

    public void setExceptionOrTargetCountCurrent(int exceptionOrTargetCountCurrent) {
        this._exceptionOrTargetCountCurrent = exceptionOrTargetCountCurrent;
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
        _setLabel(id);
    }

    public String getLabel() {
        return _label;
    }

}
