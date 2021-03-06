package com.ravijain.sankalp.support;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.ravijain.sankalp.R;
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
    public static final int SELECTION_MODE_EVENT = 3;

    public static final int DISPLAY_MODE_MONTH = 4;
    public static final int DISPLAY_MODE_WEEK = 5;

    public SpCalendarViewHandler(Context applicationContext, View view)
    {
        _applicationContext = applicationContext;
        _view = view;
    }

    public void constructCalendar(int viewId, int selectionMode)
    {
        constructCalendar(viewId, selectionMode, DISPLAY_MODE_MONTH);
    }
    public void constructCalendar(int viewId, int selectionMode, int displayMode)
    {
        _selectionMode = selectionMode;
        _widget = (MaterialCalendarView) _view.findViewById(viewId);
        _widget.setOnDateChangedListener(this);
        _widget.setDynamicHeightEnabled(true);

        if (selectionMode == SELECTION_MODE_EVENT) {
            _widget.setOnMonthChangedListener(this);
            loadCalendarEvents();
            _widget.setSelectedDate(new Date());
        }
        else if (selectionMode == SELECTION_MODE_RANGE){
            _widget.setSelectionMode(MaterialCalendarView.SELECTION_MODE_RANGE);
        }
        else if (selectionMode == SELECTION_MODE_SINGLE) {
            _widget.setSelectionMode(MaterialCalendarView.SELECTION_MODE_SINGLE);
            _widget.setSelectedDate(new Date());
        }
    }

    public void loadCalendarEvents()
    {
        EventsLoader l = new EventsLoader();
        l.execute(Calendar.getInstance());
    }

    public static void launchSankalpList(Date date, Context context)
    {
        Intent intent = new Intent(context, SpSankalpList.class);
        intent.putExtra(SpConstants.INTENT_KEY_SANKALP_TYPE, SpConstants.SANKALP_TYPE_BOTH);
        intent.putExtra(SpConstants.INTENT_KEY_SANKALP_LIST_FILTER, SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_DAY);
        intent.putExtra(SpConstants.INTENT_KEY_SANKALP_LIST_FILTER_DATE_VALUE, date.getTime());
        context.startActivity(intent);
    }

    @Override
    public void onDateSelected(MaterialCalendarView widget, CalendarDay date, boolean selected) {
        if (_selectionMode == SELECTION_MODE_EVENT) {
            widget.clearSelection();
            if (_events.contains(date)) {
                launchSankalpList(date.getDate(), _applicationContext);
            }
            else {
                Snackbar
                        .make(_view, _applicationContext.getString(R.string.EmptyList), Snackbar.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

        if (_selectionMode == SELECTION_MODE_EVENT) {
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
