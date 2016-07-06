package com.ravijain.sankalp.fragments;


import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ravijain.sankalp.R;
import com.ravijain.sankalp.activities.SpAddSankalpActivity;
import com.ravijain.sankalp.activities.SpConstants;
import com.ravijain.sankalp.activities.SpSankalpList;
import com.ravijain.sankalp.data.SpCategory;
import com.ravijain.sankalp.data.SpCategoryItem;
import com.ravijain.sankalp.data.SpContentProvider;
import com.ravijain.sankalp.data.SpDataConstants;
import com.ravijain.sankalp.data.SpDateUtils;
import com.ravijain.sankalp.data.SpSankalp;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpDashboardFragment extends Fragment {


    private DashboardLoader _dashboardLoaderTask;

    private TextView _tyagTotal;
    private TextView _tyagCurrent;
    private TextView _tyagLifetime;
    private TextView _tyagUpcoming;
    private TextView _niyamTotal;
    private TextView _niyamCurrent;
    private TextView _niyamLifetime;
    private TextView _niyamUpcoming;

    private LinearLayout _tt_ll;
    private LinearLayout _tc_ll;
    private LinearLayout _tl_ll;
    private LinearLayout _tu_ll;

    private LinearLayout _nt_ll;
    private LinearLayout _nc_ll;
    private LinearLayout _nl_ll;
    private LinearLayout _nu_ll;

    public SpDashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_sankalp_dashboard, container, false);

        _tyagTotal = (TextView) rootView.findViewById(R.id.tyag_total_tv);
        _tyagCurrent = (TextView) rootView.findViewById(R.id.tyag_current_tv);
        _tyagLifetime = (TextView) rootView.findViewById(R.id.tyag_lifetime_tv);
        _tyagUpcoming = (TextView) rootView.findViewById(R.id.tyag_upcoming_tv);

        _niyamTotal = (TextView) rootView.findViewById(R.id.niyam_total_tv);
        _niyamCurrent = (TextView) rootView.findViewById(R.id.niyam_current_tv);
        _niyamLifetime = (TextView) rootView.findViewById(R.id.niyam_lifetime_tv);
        _niyamUpcoming = (TextView) rootView.findViewById(R.id.niyam_upcoming_tv);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _handleLinearLayoutClick(view);
            }
        };
        _tt_ll = (LinearLayout) rootView.findViewById(R.id.tt_ll);
        _tt_ll.setOnClickListener(listener);
        _tc_ll = (LinearLayout) rootView.findViewById(R.id.tc_ll);
        _tc_ll.setOnClickListener(listener);
        _tl_ll = (LinearLayout) rootView.findViewById(R.id.tl_ll);
        _tl_ll.setOnClickListener(listener);
        _tu_ll = (LinearLayout) rootView.findViewById(R.id.tu_ll);
        _tu_ll.setOnClickListener(listener);

        _nt_ll = (LinearLayout) rootView.findViewById(R.id.nt_ll);
        _nt_ll.setOnClickListener(listener);
        _nc_ll = (LinearLayout) rootView.findViewById(R.id.nc_ll);
        _nc_ll.setOnClickListener(listener);
        _nl_ll = (LinearLayout) rootView.findViewById(R.id.nl_ll);
        _nl_ll.setOnClickListener(listener);
        _nu_ll = (LinearLayout) rootView.findViewById(R.id.nu_ll);
        _nu_ll.setOnClickListener(listener);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.addSankalpButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SpAddSankalpActivity.class);
                startActivity(intent);
            }
        });

        _dashboardLoaderTask = new DashboardLoader();
        _dashboardLoaderTask.execute((Void) null);


        return rootView;
    }

    private void _handleLinearLayoutClick(View view) {
        int sankalpType = SpDataConstants.SANKALP_TYPE_TYAG;
        int filter = SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_ALL;

        if (view == _tt_ll) {
            sankalpType = SpDataConstants.SANKALP_TYPE_TYAG;
            filter = SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_ALL;
        }
        else if (view == _tc_ll) {
            sankalpType = SpDataConstants.SANKALP_TYPE_TYAG;
            filter = SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_CURRENT;
        }
        else if (view == _tl_ll) {
            sankalpType = SpDataConstants.SANKALP_TYPE_TYAG;
            filter = SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_LIFETIME;
        }
        else if (view == _tu_ll) {
            sankalpType = SpDataConstants.SANKALP_TYPE_TYAG;
            filter = SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_UPCOMING;
        }
        else if (view == _nt_ll) {
            sankalpType = SpDataConstants.SANKALP_TYPE_NIYAM;
            filter = SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_ALL;
        }
        else if (view == _nc_ll) {
            sankalpType = SpDataConstants.SANKALP_TYPE_NIYAM;
            filter = SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_CURRENT;
        }
        else if (view == _nl_ll) {
            sankalpType = SpDataConstants.SANKALP_TYPE_NIYAM;
            filter = SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_LIFETIME;
        }
        else if (view == _nu_ll) {
            sankalpType = SpDataConstants.SANKALP_TYPE_NIYAM;
            filter = SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_UPCOMING;
        }

        Intent intent = new Intent(getActivity(), SpSankalpList.class);
        intent.putExtra(SpConstants.INTENT_KEY_SANKALP_TYPE, sankalpType);
        intent.putExtra(SpConstants.INTENT_KEY_SANKALP_LIST_FILTER, filter);
        startActivity(intent);
    }

    private class DashboardLoader extends AsyncTask<Void, Void, Boolean>
    {
        private int _tt = 0;
        private int _tc = 0;
        private int _tl = 0;
        private int _tu = 0;
        private int _nt = 0;
        private int _nc = 0;
        private int _nl = 0;
        private int _nu = 0;
        @Override
        protected Boolean doInBackground(Void... params) {
            SpContentProvider provider = SpContentProvider.getInstance(getContext());
            ArrayList<SpSankalp> tyags = provider.getSankalps(SpDataConstants.SANKALP_TYPE_TYAG, SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_ALL);
            for (SpSankalp tyag : tyags) {
                _tt++;
                if (tyag.isLifetime() == SpDataConstants.SANKALP_IS_LIFTIME_TRUE) {
                    _tl++;
                }
                if (SpDateUtils.isUpcomingDate(tyag.getFromDate())) {
                    _tu++;
                }
                else if(SpDateUtils.isCurrentDate(tyag.getFromDate(), tyag.getToDate())) {
                    _tc++;
                }
            }

            ArrayList<SpSankalp> niyams = provider.getSankalps(SpDataConstants.SANKALP_TYPE_NIYAM, SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_ALL);
            for (SpSankalp niyam : niyams) {
                _nt++;
                if (niyam.isLifetime() == SpDataConstants.SANKALP_IS_LIFTIME_TRUE) {
                    _nl++;
                }
                if (SpDateUtils.isUpcomingDate(niyam.getFromDate())) {
                    _nu++;
                }
                else if(SpDateUtils.isCurrentDate(niyam.getFromDate(), niyam.getToDate())) {
                    _nc++;
                }
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            _dashboardLoaderTask = null;

            if (success) {
                _tyagTotal.setText(String.valueOf(_tt));
                _tyagCurrent.setText(String.valueOf(_tc));
                _tyagLifetime.setText(String.valueOf(_tl));
                _tyagUpcoming.setText(String.valueOf(_tu));

                _niyamTotal.setText(String.valueOf(_nt));
                _niyamCurrent.setText(String.valueOf(_nc));
                _niyamLifetime.setText(String.valueOf(_nl));
                _niyamUpcoming.setText(String.valueOf(_nu));
            } else {
                // Error
            }
        }
    }

}
