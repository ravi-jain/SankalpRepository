package com.ravijain.sankalp.fragments;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.ravijain.sankalp.R;
import com.ravijain.sankalp.activities.SpAddSankalpActivity;
import com.ravijain.sankalp.activities.SpConstants;
import com.ravijain.sankalp.activities.SpSankalpDetailsActivity;
import com.ravijain.sankalp.activities.SpSankalpList;
import com.ravijain.sankalp.data.SpContentProvider;
import com.ravijain.sankalp.data.SpDataConstants;
import com.ravijain.sankalp.support.SpDateUtils;
import com.ravijain.sankalp.data.SpSankalp;

import java.util.ArrayList;

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

    private ListView _recentList;
    private SpSankalpListAdapter _sankalpAdapter;

//    private LinearLayout _tt_ll;
//    private LinearLayout _tc_ll;
//    private LinearLayout _tl_ll;
//    private LinearLayout _tu_ll;
//
//    private LinearLayout _nt_ll;
//    private LinearLayout _nc_ll;
//    private LinearLayout _nl_ll;
//    private LinearLayout _nu_ll;

    /*private BarChart _chart;*/

    public SpDashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_sankalp_dashboard, container, false);

//        _handleChart(rootView);

        _tyagTotal = (TextView) rootView.findViewById(R.id.tyag_total_tv);
        _tyagCurrent = (TextView) rootView.findViewById(R.id.tyag_current_tv);
        _tyagLifetime = (TextView) rootView.findViewById(R.id.tyag_lifetime_tv);
        _tyagUpcoming = (TextView) rootView.findViewById(R.id.tyag_upcoming_tv);

        _niyamTotal = (TextView) rootView.findViewById(R.id.niyam_total_tv);
        _niyamCurrent = (TextView) rootView.findViewById(R.id.niyam_current_tv);
        _niyamLifetime = (TextView) rootView.findViewById(R.id.niyam_lifetime_tv);
        _niyamUpcoming = (TextView) rootView.findViewById(R.id.niyam_upcoming_tv);

        _recentList = (ListView) rootView.findViewById(R.id.card_recent_list);
        _sankalpAdapter = new SpSankalpListAdapter(getContext(), new ArrayList<SpSankalp>());
        _recentList.setAdapter(_sankalpAdapter);

        _recentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                SpSankalp sankalp = (SpSankalp)adapterView.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), SpSankalpDetailsActivity.class);
                intent.putExtra(SpConstants.INTENT_KEY_SANKALP_TYPE, sankalp.getSankalpType());
                intent.putExtra(SpConstants.INTENT_KEY_SANKALP_ID, sankalp.getId());
                startActivity(intent);
            }
        });

        ImageButton btn = (ImageButton) rootView.findViewById(R.id.viewAll_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SpSankalpList.class);
                intent.putExtra(SpConstants.INTENT_KEY_SANKALP_TYPE, SpDataConstants.SANKALP_TYPE_BOTH);
                intent.putExtra(SpConstants.INTENT_KEY_SANKALP_LIST_FILTER, SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_CURRENT);
                startActivity(intent);
            }
        });

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

    /*private void _handleChart(View rootView)*/ /*{

        _chart = (BarChart) rootView.findViewById(R.id.sankalp_chart);
        _chart.setDrawGridBackground(false);

        List<String> xVals = new ArrayList<String>();
        xVals.add(getString(R.string.total));
        xVals.add(getString(R.string.current));
        xVals.add(getString(R.string.Lifetime));
        xVals.add(getString(R.string.upcoming));

        Legend l = _chart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_INSIDE);
        //l.setTypeface(mTfLight);
        l.setYOffset(0f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);

        XAxis xl = _chart.getXAxis();
      //  xl.setTypeface(mTfLight);
        xl.setGranularity(1f);

        xl.setCenterAxisLabels(true);
        xl.setAvoidFirstLastClipping(true);
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setValueFormatter(new AxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int v = (int) value;
                switch (v) {
                    case 0 :
                        return getString(R.string.total);
                    case 1 :
                        return getString(R.string.current);
                    case 2 :
                        return getString(R.string.Lifetime);
                    case 3:
                        return getString(R.string.upcoming);
                    default:
                        return "";
                }
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });
        //xl.setDrawGridLines(false);

        YAxis leftAxis = _chart.getAxisLeft();
        //leftAxis.setTypeface(mTfLight);
        leftAxis.setValueFormatter(new AxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return "";
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(30f);
        leftAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)

        _chart.getAxisRight().setEnabled(false);

        float groupSpace = 0.1f;
        float barSpace = 0.1f; // x3 dataset
        float barWidth = 0.3f;



        ArrayList<BarEntry> tyagValues = new ArrayList<BarEntry>();
        tyagValues.add(new BarEntry(0, 24));
        tyagValues.add(new BarEntry(1, 14));
        tyagValues.add(new BarEntry(2, 7));
        tyagValues.add(new BarEntry(3, 10));

        ArrayList<BarEntry> niyamValues = new ArrayList<BarEntry>();
        niyamValues.add(new BarEntry(0, 32));
        niyamValues.add(new BarEntry(1, 22));
        niyamValues.add(new BarEntry(2, 12));
        niyamValues.add(new BarEntry(3, 10));

        BarDataSet set1 = new BarDataSet(tyagValues, "Tyag");
        set1.setColor(Color.BLUE);
        BarDataSet set2 = new BarDataSet(niyamValues, "Niyam");
        set2.setColor(Color.CYAN);

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);
        dataSets.add(set2);

        BarData data = new BarData(dataSets);
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return String.valueOf((int) value);
            }
        });
        data.setValueTextSize(8f);

        _chart.setData(data);
        _chart.getBarData().setBarWidth(barWidth);
        xl.setAxisMinValue(0f);
        xl.setAxisMaxValue((float)(xl.getAxisMaximum() * 1.3));
        _chart.groupBars(0, groupSpace, barSpace);
        _chart.invalidate();
        _chart.invalidate();

    }*/

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
        private ArrayList<SpSankalp> _recents;

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

            _recents = provider.getMostRecentSankalps(5);
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

                _sankalpAdapter.clear();
                _sankalpAdapter.addAll(_recents);
            } else {
                // Error
            }
        }
    }

}
