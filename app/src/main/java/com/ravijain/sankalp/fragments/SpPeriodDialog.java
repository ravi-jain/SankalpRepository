package com.ravijain.sankalp.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.ravijain.sankalp.R;
import com.ravijain.sankalp.activities.SpConstants;
import com.ravijain.sankalp.support.SpCalendarViewHandler;
import com.ravijain.sankalp.support.SpDateUtils;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ravijain on 9/1/2016.
 */
public class SpPeriodDialog extends DialogFragment implements View.OnClickListener{

    private Date _fromDate, _toDate;
    //private CaldroidFragment _calViewFragment;
    private SpCalendarViewHandler _calendarView;
    private int _periodKey = SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_DAY;

    private NumberPicker _monthPicker, _yearPicker;
    private SpAddSankalpActivityFragment _parentFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.period_dialog, container, false);
        v.findViewById(R.id.pd_ok_action).setOnClickListener(this);
        v.findViewById(R.id.pd_cancel_action).setOnClickListener(this);

        Bundle b = getArguments();

        if (b != null) {
            _periodKey = b.getInt(SpConstants.INTENT_KEY_SANKALP_PERIOD, SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_DAY);
        }
        String periodTitle = getString(R.string.selectPeriod);
        if (_periodKey == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_DAY) {
            periodTitle = getString(R.string.selectDay);
            _setUpCalendarForDay(v);
        }
        else if (_periodKey == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_MONTH) {
            periodTitle = getString(R.string.selectMonth);
            _setUpMonthYearPicker(v);
        }
        else if (_periodKey == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_YEAR) {
            periodTitle = getString(R.string.selectYear);
            _setUpYearPicker(v);
        }
        else if (_periodKey == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_RANGE) {
            periodTitle = getString(R.string.selectInterval);
            _setUpCalendarForRange(v);
        }
        ((TextView)v.findViewById(R.id.period_title)).setText(periodTitle);

        return v;
    }

    private void _setUpCalendarForDay(View v) {
        v.findViewById(R.id.period_calendarView).setVisibility(View.VISIBLE);
        _calendarView = new SpCalendarViewHandler(getContext(), v);
        _calendarView.constructCalendar(R.id.period_calendar, SpCalendarViewHandler.SELECTION_MODE_SINGLE);
    }
    private void _setUpCalendarForRange(View v) {
        v.findViewById(R.id.period_calendarView).setVisibility(View.VISIBLE);
        _calendarView = new SpCalendarViewHandler(getContext(), v);
        _calendarView.constructCalendar(R.id.period_calendar, SpCalendarViewHandler.SELECTION_MODE_RANGE);
    }

    private void _setUpYearPicker(View v) {

        v.findViewById(R.id.year_picker_ll).setVisibility(View.VISIBLE);
        Calendar c = Calendar.getInstance();
        _yearPicker = (NumberPicker) v.findViewById(R.id.year_picker_solo);
        _yearPicker.setWrapSelectorWheel(true);
        _yearPicker.setMaxValue(2100);
        _yearPicker.setMinValue(1900);
        _yearPicker.setValue(c.get(Calendar.YEAR));
    }

    private void _setUpMonthYearPicker(View v) {
        v.findViewById(R.id.month_year_picker).setVisibility(View.VISIBLE);
        Calendar c = Calendar.getInstance();
        _monthPicker = (NumberPicker) v.findViewById(R.id.month_picker);
        _monthPicker.setWrapSelectorWheel(true);
        _monthPicker.setMaxValue(11);
        _monthPicker.setMinValue(0);
        _monthPicker.setValue(c.get(Calendar.MONTH));
        _monthPicker.setDisplayedValues(SpDateUtils.getMonthStrings());

        _yearPicker = (NumberPicker) v.findViewById(R.id.year_picker);
        _yearPicker.setWrapSelectorWheel(true);
        _yearPicker.setMaxValue(2100);
        _yearPicker.setMinValue(1900);
        _yearPicker.setValue(c.get(Calendar.YEAR));
    }

    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

//    private void _setUpCalendarForDay(View v) {
//        v.findViewById(R.id.period_calendarView).setVisibility(View.VISIBLE);
//        _calViewFragment = new CaldroidFragment();
//        Bundle args = new Bundle();
//        Calendar cal = Calendar.getInstance();
//        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
//        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
//        args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
//        args.putBoolean(CaldroidFragment.SHOW_NAVIGATION_ARROWS, true);
//        args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);
//
//        _calViewFragment.setArguments(args);
//        _calViewFragment.setCaldroidListener(new CalendarEventListener());
//
//        // Attach to the activity
//        getChildFragmentManager().beginTransaction().replace(R.id.period_calendarView, _calViewFragment).commit();
//    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.pd_ok_action) {
            if (_periodKey == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_DAY || _periodKey == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_RANGE) {
                if (_calendarView != null) {
                    Date[] dates = _calendarView.getRangeDates();
                    _fromDate = SpDateUtils.beginOfDate(dates[0]);
                    _toDate = SpDateUtils.endOfDate(dates[1]);
                }
            }
            else if (_periodKey == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_MONTH) {
                if (_monthPicker != null && _yearPicker != null) {
                    int month = _monthPicker.getValue();
                    int year = _yearPicker.getValue();
                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.MONTH, month);
                    c.set(Calendar.YEAR, year);
                    _fromDate = SpDateUtils.beginOfMonth(c);
                    _toDate = SpDateUtils.endOfMonth(c);

                }
            }
            else if (_periodKey == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_YEAR) {

                if (_yearPicker != null) {
                    int year = _yearPicker.getValue();
                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.YEAR, year);
                    _fromDate = SpDateUtils.beginOfYear(c);
                    _toDate = SpDateUtils.endOfYear(c);
                }

            }

            _parentFragment.setDate(_fromDate, _toDate);
            dismiss();
        }
        else {
            dismiss();
        }

    }

    public void setParentFragment(SpAddSankalpActivityFragment parentFragment) {
        this._parentFragment = parentFragment;
    }

//    private class CalendarEventListener extends CaldroidListener
//    {
//
//        @Override
//        public void onSelectDate(Date date, View view) {
//
//
//            if (_periodKey == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_DAY) {
//                _calViewFragment.clearSelectedDates();
//                _calViewFragment.setSelectedDate(date);
//                _calViewFragment.refreshView();
//                _fromDate = SpDateUtils.beginOfDate(date);
//                _toDate = SpDateUtils.endOfDate(date);
//            }
//            else {
//
//            }
//        }
//    }
}
