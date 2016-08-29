package com.ravijain.sankalp.support;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Toast;

import com.ravijain.sankalp.R;
import com.ravijain.sankalp.activities.SpConstants;
import com.ravijain.sankalp.activities.SpSankalpList;
import com.ravijain.sankalp.data.SpContentProvider;
import com.ravijain.sankalp.data.SpDataConstants;
import com.ravijain.sankalp.data.SpSankalp;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by ravijain on 8/25/2016.
 */
public class SpCalendarViewHandler {

    private CaldroidFragment _calViewFragment;
    private int _contentFrameId;
    private FragmentManager _fragmentManager;
    private String _context;
    private Context _applicationContext;

    public static final String CONTEXT_FULL = "CONTEXT_FULL";
    public static final String CONTEXT_LIMITED = "CONTEXT_LIMITED";

    public SpCalendarViewHandler(Context applicationContext, String context, FragmentManager manager, int contentFrameId)
    {
        _context = context;
        _contentFrameId = contentFrameId;
        _fragmentManager = manager;
        _applicationContext = applicationContext;
    }

    public void constructCalendarView()
    {
        _calViewFragment = new CaldroidFragment();

        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));

        boolean enableSwipe = false;
        boolean showNavigationArrows = false;
        if (_context == CONTEXT_FULL) {
            enableSwipe = true;
            showNavigationArrows = true;
        }

        args.putBoolean(CaldroidFragment.ENABLE_SWIPE, enableSwipe);
        args.putBoolean(CaldroidFragment.SHOW_NAVIGATION_ARROWS, showNavigationArrows);

        //args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, false);
        args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, false);

        //args.putInt(CaldroidFragment.THEME_RESOURCE, R.style.CaldroidDefaultDark1);

        _calViewFragment.setArguments(args);

        // Attach to the activity
        _fragmentManager.beginTransaction().replace(_contentFrameId, _calViewFragment).commit();

        // Setup Caldroid
        _calViewFragment.setCaldroidListener(new CalendarEventListener());

    }

    private void _launchSankalpList(Date date)
    {
        Intent intent = new Intent(_applicationContext, SpSankalpList.class);
        intent.putExtra(SpConstants.INTENT_KEY_SANKALP_TYPE, SpDataConstants.SANKALP_TYPE_BOTH);
        intent.putExtra(SpConstants.INTENT_KEY_SANKALP_LIST_FILTER, SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_DAY);
        intent.putExtra(SpConstants.INTENT_KEY_SANKALP_LIST_FILTER_DATE_VALUE, date.getTime());
        _applicationContext.startActivity(intent);
    }

    private class CalendarEventListener extends CaldroidListener
    {

        @Override
        public void onSelectDate(Date date, View view) {

            if (view.getBackground() instanceof LayerDrawable) {
                _launchSankalpList(date);
            }
            else {
                Toast.makeText(_applicationContext, _applicationContext.getString(R.string.EmptyList), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onChangeMonth(int month, int year) {
            Calendar cal = Calendar.getInstance();
            cal.set(year, month - 1, 1);
            // Add events
            EventsLoader l = new EventsLoader();
            l.execute(cal);
        }

        @Override
        public void onLongClickDate(Date date, View view) {
        }

        @Override
        public void onCaldroidViewCreated() {
        }


    }

    private class EventsLoader extends AsyncTask<Calendar, Void, Boolean> {

        ArrayList<SpSankalp> sankalps;

        @Override
        protected Boolean doInBackground(Calendar... calendars) {
            Calendar c = calendars[0];
            SpContentProvider p = SpContentProvider.getInstance(_applicationContext);
            sankalps = p.getSankalps(SpDataConstants.SANKALP_TYPE_BOTH, SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_MONTH, c);
            return true;
        }

        protected void onPostExecute(final Boolean success) {

            HashMap<Date, Drawable> events = new HashMap<Date, Drawable>();
            for (SpSankalp sankalp : sankalps) {

                if (events.get(sankalp.getToDate()) == null) {
                    events.put(sankalp.getToDate(), _applicationContext.getResources().getDrawable(R.drawable.niyam_calendar_drawable));
                }
//                if (sankalp.getSankalpType() == SpDataConstants.SANKALP_TYPE_TYAG) {
//                    _calViewFragment.setBackgroundDrawableForDate(_applicationContext.getResources().getDrawable(R.drawable.niyam_calendar_drawable), sankalp.getToDate());
//                }
//                else {
//                    _calViewFragment.setBackgroundDrawableForDate(_applicationContext.getResources().getDrawable(R.drawable.niyam_calendar_drawable), sankalp.getToDate());
//                }
            }
            if (events.size() > 0) {
                _calViewFragment.setBackgroundDrawableForDates(events);
                _calViewFragment.refreshView();
            }

        }
    }
}
