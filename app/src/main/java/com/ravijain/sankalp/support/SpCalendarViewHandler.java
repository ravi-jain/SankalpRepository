package com.ravijain.sankalp.support;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.ravijain.sankalp.R;
import com.ravijain.sankalp.activities.SpConstants;
import com.ravijain.sankalp.activities.SpSankalpList;
import com.ravijain.sankalp.data.SpContentProvider;
import com.ravijain.sankalp.data.SpSankalp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by ravijain on 9/2/2016.
 */
public class SpCalendarViewHandler implements OnDateSelectedListener, OnMonthChangedListener {

    private View _view;
    private Context _applicationContext;
    private MaterialCalendarView _widget;
    private ArrayList<CalendarDay> _events;
    private int _selectionMode;

    public static final int SELECTION_MODE_NONE = 0;
    public static final int SELECTION_MODE_SINGLE = 1;
    public static final int SELECTION_MODE_RANGE = 2;

    public SpCalendarViewHandler(Context applicationContext, View view)
    {
        _applicationContext = applicationContext;
        _view = view;
    }

    public void constructCalendar(int viewId, int selectionMode)
    {
        _selectionMode = selectionMode;
        _widget = (MaterialCalendarView) _view.findViewById(viewId);
        _widget.setOnDateChangedListener(this);


        if (selectionMode == SELECTION_MODE_NONE) {
            _widget.setOnMonthChangedListener(this);
            EventsLoader l = new EventsLoader();
            l.execute(Calendar.getInstance());
        }
        else if (selectionMode == SELECTION_MODE_RANGE){
            _widget.setSelectionMode(MaterialCalendarView.SELECTION_MODE_RANGE);
        }
        else if (selectionMode == SELECTION_MODE_SINGLE) {
            _widget.setSelectionMode(MaterialCalendarView.SELECTION_MODE_SINGLE);
            _widget.setSelectedDate(new Date());
        }
    }

    private void _launchSankalpList(Date date)
    {
        Intent intent = new Intent(_applicationContext, SpSankalpList.class);
        intent.putExtra(SpConstants.INTENT_KEY_SANKALP_TYPE, SpConstants.SANKALP_TYPE_BOTH);
        intent.putExtra(SpConstants.INTENT_KEY_SANKALP_LIST_FILTER, SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_DAY);
        intent.putExtra(SpConstants.INTENT_KEY_SANKALP_LIST_FILTER_DATE_VALUE, date.getTime());
        _applicationContext.startActivity(intent);
    }

    @Override
    public void onDateSelected(MaterialCalendarView widget, CalendarDay date, boolean selected) {
        if (_selectionMode == SELECTION_MODE_NONE) {
            widget.clearSelection();
            if (_events.contains(date)) {
                _launchSankalpList(date.getDate());
            }
            else {
                Toast.makeText(_applicationContext, _applicationContext.getString(R.string.EmptyList), Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

        if (_selectionMode == SELECTION_MODE_NONE) {
            // Add events
            EventsLoader l = new EventsLoader();
            l.execute(date.getCalendar());
        }

    }

    public Date[] getRangeDates()
    {
        Date[] dates = new Date[2];
        List<CalendarDay> selectedDates = _widget.getSelectedDates();
        if (selectedDates.size() > 0) {
            Date fromDate = selectedDates.get(0).getDate();
            Date toDate = selectedDates.get(0).getDate();
            if (selectedDates.size() > 1) {
                for (int i = 1; i < selectedDates.size(); i++) {
                    Date d = selectedDates.get(i).getDate();
                    if (d.compareTo(fromDate) < 0) {
                        fromDate = d;
                        continue;
                    }
                    if (d.compareTo(toDate) > 0) {
                        toDate = d;
                        continue;
                    }
                }
            }
            dates[0] = fromDate;
            dates[1] = toDate;
        }
        return dates;
    }

    private class EventsLoader extends AsyncTask<Calendar, Void, Boolean> {

        ArrayList<SpSankalp> sankalps;

        @Override
        protected Boolean doInBackground(Calendar... calendars) {
            Calendar c = calendars[0];
            SpContentProvider p = SpContentProvider.getInstance(_applicationContext);
            sankalps = p.getSankalps(SpConstants.SANKALP_TYPE_BOTH, SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_MONTH, c);
            return true;
        }

        protected void onPostExecute(final Boolean success) {

            _events = new ArrayList<>();
            for (SpSankalp sankalp : sankalps) {
                _events.add(CalendarDay.from(sankalp.getToDate()));
            }
            if (_events.size() > 0) {
                _widget.addDecorator(new SpCalendarEventDecorator(_applicationContext.getResources().getColor(R.color.sankalp), _events));
            }

        }
    }
}
