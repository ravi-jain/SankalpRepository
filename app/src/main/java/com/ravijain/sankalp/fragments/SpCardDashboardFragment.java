package com.ravijain.sankalp.fragments;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ravijain.sankalp.R;
import com.ravijain.sankalp.activities.SpAddSankalpActivity;
import com.ravijain.sankalp.activities.SpConstants;
import com.ravijain.sankalp.activities.SpSankalpList;
import com.ravijain.sankalp.data.SpContentProvider;
import com.ravijain.sankalp.data.SpDataConstants;
import com.ravijain.sankalp.support.SpCaldroidCalendarViewHandler;
import com.ravijain.sankalp.support.SpDateUtils;
import com.ravijain.sankalp.data.SpSankalp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpCardDashboardFragment extends Fragment implements View.OnClickListener {


    private TextView _quoteTv;

    private CardView _todayCv;
    private TextView _tyagTodayTv;
    private TextView _niyamTodayTv;

    private CardView _tomorrowCv;
    private TextView _tyagTomTv;
    private TextView _niyamTomTv;

    private CardView _monthCv;
    private TextView _monthLabelTv;
    private TextView _tyagMonthTv;
    private TextView _niyamMonthTv;

    private CardView _yearCv;
    private TextView _yearLabelTv;
    private TextView _tyagYearTv;
    private TextView _niyamYearTv;

    private CardView _currentCv;
    private TextView _tyagCurrentTv;
    private TextView _niyamCurrentTv;

    private CardView _lifetimeCv;
    private TextView _tyagLifetimeTv;
    private TextView _niyamLifetimeTv;

    private CardView _upcomingCv;
    private TextView _tyagUpcomingTv;
    private TextView _niyamUpcomingTv;

    private CardView _allCv;
    private TextView _tyagAllTv;
    private TextView _niyamAllTv;

    public SpCardDashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_card_dashboard, container, false);

        _initializeViews(rootView);

        /*FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.cardDb_addSankalpButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SpAddSankalpActivity.class);
                startActivity(intent);
            }
        });*/

        _setUpCalendarView();

        com.getbase.floatingactionbutton.FloatingActionButton fabTyag = (com.getbase.floatingactionbutton.FloatingActionButton) rootView.findViewById(R.id.cardDb_addTyagButton);
        com.getbase.floatingactionbutton.FloatingActionButton niyamTyag = (com.getbase.floatingactionbutton.FloatingActionButton) rootView.findViewById(R.id.cardDb_addNiyamButton);
        fabTyag.setOnClickListener(this);
        niyamTyag.setOnClickListener(this);

        DashboardLoader dashboardLoaderTask = new DashboardLoader();
        dashboardLoaderTask.execute((Void) null);

        return rootView;
    }

    private void _setUpCalendarView() {

        SpCaldroidCalendarViewHandler cal = new SpCaldroidCalendarViewHandler(getActivity(), SpCaldroidCalendarViewHandler.CONTEXT_LIMITED, getFragmentManager(), R.id.db_calendarView);
        cal.constructCalendarView();

        // Attach to the activity
        //getFragmentManager().beginTransaction().replace(R.id.db_calendarView, _calViewFragment).commit();
    }

    private void _initializeViews(View rootView) {

        _quoteTv = (TextView) rootView.findViewById(R.id.db_quote_tv);

        _todayCv = (CardView) rootView.findViewById(R.id.db_card_today);
        _todayCv.setOnClickListener(this);
        _tyagTodayTv = (TextView) rootView.findViewById(R.id.db_tyag_today_tv);
        _niyamTodayTv = (TextView) rootView.findViewById(R.id.db_niyam_today_tv);

        _tomorrowCv = (CardView) rootView.findViewById(R.id.db_card_tomorrow);
        _tomorrowCv.setOnClickListener(this);
        _tyagTomTv = (TextView) rootView.findViewById(R.id.db_tyag_tomorrow_tv);
        _niyamTomTv = (TextView) rootView.findViewById(R.id.db_niyam_tomorrow_tv);

        _monthCv = (CardView) rootView.findViewById(R.id.db_card_month);
        _monthCv.setOnClickListener(this);
        _monthLabelTv = (TextView) rootView.findViewById(R.id.db_tv_month_label);
        _monthLabelTv.setText(SpDateUtils.getMonthString(Calendar.getInstance()));
        _tyagMonthTv = (TextView) rootView.findViewById(R.id.db_tyag_month_tv);
        _niyamMonthTv = (TextView) rootView.findViewById(R.id.db_niyam_month_tv);

        _yearCv = (CardView) rootView.findViewById(R.id.db_card_year);
        _yearCv.setOnClickListener(this);
        _yearLabelTv = (TextView) rootView.findViewById(R.id.db_tv_year_label);
        _yearLabelTv.setText(SpDateUtils.yearOfDate(Calendar.getInstance()));
        _tyagYearTv = (TextView) rootView.findViewById(R.id.db_tyag_year_tv);
        _niyamYearTv = (TextView) rootView.findViewById(R.id.db_niyam_year_tv);

        _currentCv = (CardView) rootView.findViewById(R.id.db_card_current);
        _currentCv.setOnClickListener(this);
        _tyagCurrentTv = (TextView) rootView.findViewById(R.id.db_tyag_current_tv);
        _niyamCurrentTv = (TextView) rootView.findViewById(R.id.db_niyam_current_tv);

        _lifetimeCv = (CardView) rootView.findViewById(R.id.db_card_lifetime);
        _lifetimeCv.setOnClickListener(this);
        _tyagLifetimeTv = (TextView) rootView.findViewById(R.id.db_tyag_lifetime_tv);
        _niyamLifetimeTv = (TextView) rootView.findViewById(R.id.db_niyam_lifetime_tv);

        _upcomingCv = (CardView) rootView.findViewById(R.id.db_card_upcoming);
        _upcomingCv.setOnClickListener(this);
        _tyagUpcomingTv = (TextView) rootView.findViewById(R.id.db_tyag_upcoming_tv);
        _niyamUpcomingTv = (TextView) rootView.findViewById(R.id.db_niyam_upcoming_tv);

        _allCv = (CardView) rootView.findViewById(R.id.db_card_all);
        _allCv.setOnClickListener(this);
        _tyagAllTv = (TextView) rootView.findViewById(R.id.db_tyag_all_tv);
        _niyamAllTv = (TextView) rootView.findViewById(R.id.db_niyam_all_tv);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.cardDb_addTyagButton) {
            Intent intent = new Intent(getActivity(), SpAddSankalpActivity.class);
            intent.putExtra(SpConstants.INTENT_KEY_SANKALP_TYPE, SpDataConstants.SANKALP_TYPE_TYAG);
            startActivity(intent);
        } else if (view.getId() == R.id.cardDb_addNiyamButton) {
            Intent intent = new Intent(getActivity(), SpAddSankalpActivity.class);
            intent.putExtra(SpConstants.INTENT_KEY_SANKALP_TYPE, SpDataConstants.SANKALP_TYPE_NIYAM);
            startActivity(intent);
        } else {
            int intentlistFilter = SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_CURRENT;
            switch (view.getId()) {
                case R.id.db_card_today:
                    intentlistFilter = SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_TODAY;
                    break;
                case R.id.db_card_tomorrow:
                    intentlistFilter = SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_TOMORROW;
                    break;
                case R.id.db_card_month:
                    intentlistFilter = SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_MONTH;
                    break;
                case R.id.db_card_year:
                    intentlistFilter = SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_YEAR;
                    break;
                case R.id.db_card_current:
                    intentlistFilter = SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_CURRENT;
                    break;
                case R.id.db_card_lifetime:
                    intentlistFilter = SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_LIFETIME;
                    break;
                case R.id.db_card_upcoming:
                    intentlistFilter = SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_UPCOMING;
                    break;
                case R.id.db_card_all:
                    intentlistFilter = SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_ALL;
                    break;
            }

            Intent intent = new Intent(getActivity(), SpSankalpList.class);
            intent.putExtra(SpConstants.INTENT_KEY_SANKALP_TYPE, SpDataConstants.SANKALP_TYPE_BOTH);
            intent.putExtra(SpConstants.INTENT_KEY_SANKALP_LIST_FILTER, intentlistFilter);
            startActivity(intent);
        }

    }

    private class DashboardLoader extends AsyncTask<Void, Void, Boolean> {

        private int ttod, ntod, ttom, ntom, tm, nm, ty, ny, tc, nc, tl, nl, tu, nu, ta, na = 0;

        @Override
        protected Boolean doInBackground(Void... voids) {
            SpContentProvider p = SpContentProvider.getInstance(getContext());
            ArrayList<SpSankalp> sankalps = p.getSankalps(SpDataConstants.SANKALP_TYPE_BOTH, SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_ALL);
            for (SpSankalp s : sankalps) {

                if (s.getSankalpType() == SpDataConstants.SANKALP_TYPE_TYAG) {
                    ta++;
                    Date d = s.getToDate();
                    if (s.isLifetime() == SpDataConstants.SANKALP_IS_LIFTIME_TRUE) {
                        tl++;
                        tc++;
                        continue;
                    }
                    if (SpDateUtils.isToday(d)) {
                        ttod++;
                        tm++;
                        ty++;
                        tc++;
                        continue;
                    }
                    if (SpDateUtils.isTomorrow(d)) {
                        ttom++;
                    }

                    if (SpDateUtils.isCurrentMonth(d)) {
                        tm++;
                        ty++;
                    } else if (SpDateUtils.isCurrentYear(d)) {
                        ty++;
                    }

                    if (SpDateUtils.isCurrentDate(s.getFromDate(), d)) {
                        tc++;
                    } else if (SpDateUtils.isUpcomingDate(s.getFromDate())) {
                        tu++;
                    }
                } else {
                    na++;
                    Date d = s.getToDate();
                    if (s.isLifetime() == SpDataConstants.SANKALP_IS_LIFTIME_TRUE) {
                        nl++;
                        nc++;
                        continue;
                    }
                    if (SpDateUtils.isToday(d)) {
                        ntod++;
                        nm++;
                        ny++;
                        nc++;
                        continue;
                    }
                    if (SpDateUtils.isTomorrow(d)) {
                        ntom++;
                    }

                    if (SpDateUtils.isCurrentMonth(d)) {
                        nm++;
                        ny++;
                    } else if (SpDateUtils.isCurrentYear(d)) {
                        ny++;
                    }

                    if (SpDateUtils.isCurrentDate(s.getFromDate(), d)) {
                        nc++;
                    } else if (SpDateUtils.isUpcomingDate(s.getFromDate())) {
                        nu++;
                    }
                }

            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            _tyagTodayTv.setText(String.valueOf(ttod));
            _niyamTodayTv.setText(String.valueOf(ntod));

            _tyagTomTv.setText(String.valueOf(ttom));
            _niyamTomTv.setText(String.valueOf(ntom));

            _tyagMonthTv.setText(String.valueOf(tm));
            _niyamMonthTv.setText(String.valueOf(nm));

            _tyagYearTv.setText(String.valueOf(ty));
            _niyamYearTv.setText(String.valueOf(ny));

            _tyagCurrentTv.setText(String.valueOf(tc));
            _niyamCurrentTv.setText(String.valueOf(nc));

            _tyagLifetimeTv.setText(String.valueOf(tl));
            _niyamLifetimeTv.setText(String.valueOf(nl));

            _tyagUpcomingTv.setText(String.valueOf(tu));
            _niyamUpcomingTv.setText(String.valueOf(nu));

            _tyagAllTv.setText(String.valueOf(ta));
            _niyamAllTv.setText(String.valueOf(na));
        }
    }
}
