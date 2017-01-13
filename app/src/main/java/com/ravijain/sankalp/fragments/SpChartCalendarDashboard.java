package com.ravijain.sankalp.fragments;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.ravijain.sankalp.R;
import com.ravijain.sankalp.activities.SpAddSankalpActivity;
import com.ravijain.sankalp.support.SpConstants;
import com.ravijain.sankalp.activities.SpSankalpList;
import com.ravijain.sankalp.data.SpContentProvider;
import com.ravijain.sankalp.data.SpSankalpCountData;
import com.ravijain.sankalp.support.SpCalendarViewHandler;

import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpChartCalendarDashboard extends SpFabBaseFragment {


    private PieChart mChart;
    private ImageView _menuView;
    private Button _viewDetailsButton;

    private String[] _labels;
    private int _intentListFilter;
    private SpSankalpCountData _currentData;


    public SpChartCalendarDashboard() {
        // Required empty public constructor
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SpConstants.ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // referesh
            //_calendarView.loadCalendarEvents();
            _loadChartData();

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_sp_chart_calendar_dashboard, container, false);

        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.title_activity_sp_sankalp_list);
        _viewDetailsButton = (Button) rootView.findViewById(R.id.viewDetails_button);
        mChart = (PieChart) rootView.findViewById(R.id.db_pieChart);
        _setUpChartCard();

        setupFAB(rootView);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_dashboard, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_showCalendar) {
            getFragmentManager().beginTransaction().replace(R.id.content_frame, new SpCalendarFragment()).commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void _setUpViewButton() {
        _viewDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SpSankalpList.class);
                intent.putExtra(SpConstants.INTENT_KEY_SANKALP_TYPE, SpConstants.SANKALP_TYPE_BOTH);
                intent.putExtra(SpConstants.INTENT_KEY_SANKALP_LIST_FILTER, SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_CURRENT);
                startActivity(intent);
            }
        });
    }



    private void _setUpChartCard() {

        _setUpViewButton();

        _labels = new String[]{getString(R.string.current), getString(R.string.lifetime_db), getString(R.string.upcoming), getString(R.string.all)};

        mChart.setUsePercentValues(false);
        mChart.setDescription("");
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);

        //mChart.setCenterTextTypeface(mTfLight);
        mChart.setCenterText(generateCenterSpannableText(getString(R.string.title_activity_sp_sankalp_list)));

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(getResources().getColor(R.color.sankalp));

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(false);
        mChart.setHighlightPerTapEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        mChart.setTouchEnabled(true);
        // add a selection listener
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e instanceof PieEntry) {
                    PieEntry pe = (PieEntry) e;
                    int intentListFilter = SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_CURRENT;
                    if (pe.getLabel().equals(_labels[0])) {
                        // tyag;
                        intentListFilter = SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_CURRENT;
                    } else if (pe.getLabel().equals(_labels[1])) {
                        // tyag;
                        intentListFilter = SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_LIFETIME;
                    } else if (pe.getLabel().equals(_labels[2])) {
                        // tyag;
                        intentListFilter = SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_UPCOMING;
                    } else if (pe.getLabel().equals(_labels[3])) {
                        // tyag;
                        intentListFilter = SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_ALL;
                    }

                    Intent intent = new Intent(getContext(), SpSankalpList.class);
                    intent.putExtra(SpConstants.INTENT_KEY_SANKALP_TYPE, SpConstants.SANKALP_TYPE_BOTH);
                    intent.putExtra(SpConstants.INTENT_KEY_SANKALP_LIST_FILTER, intentListFilter);
                    startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected() {

                Log.i("chart", "nothing sle");

            }
        });

        //setData(3, 100);

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);


        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(7f);
        l.setYOffset(25f);

        // entry label styling
        mChart.setEntryLabelColor(Color.DKGRAY);
//        mChart.setEntryLabelTypeface(mTfRegular);
        mChart.setEntryLabelTextSize(12f);

        mChart.setNoDataText("No Sankalps found.");
        mChart.setNoDataTextDescription("Use the '+' button at the bottom of the screen to add.");
        mChart.getPaint(Chart.PAINT_INFO).setTextSize(40f);

        _loadChartData();
    }

    private void _loadChartData() {
        DashboardLoaderTask t = new DashboardLoaderTask();
        t.execute(DashboardLoaderTask.COMMAND_CHART_DATA);
    }

    private void setData() {


        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.

//        for (int i = 0; i < 3; i++) {
//            entries.add(new PieEntry((float) ((Math.random() * mult) + mult / 5), labels[i]));
//        }
        if (_currentData.getCurrentSankalps() > 0) {
            entries.add(new PieEntry(_currentData.getCurrentSankalps(), _labels[0]));
        }
        if (_currentData.getLifetimeSankalps() > 0) {
            entries.add(new PieEntry(_currentData.getLifetimeSankalps(), _labels[1]));
        }
        if (_currentData.getUpcomingSankalps() > 0) {
            entries.add(new PieEntry(_currentData.getUpcomingSankalps(), _labels[2]));
        }
        if (_currentData.getAllSankalps() > 0) {
            entries.add(new PieEntry(_currentData.getAllSankalps(), _labels[3]));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

//        for (int c : ColorTemplate.JOYFUL_COLORS)
//            colors.add(c);

//        for (int c : ColorTemplate.COLORFUL_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.LIBERTY_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.PASTEL_COLORS)
//            colors.add(c);
//
//        colors.add(ColorTemplate.getHoloBlue());

//        colors.add(ColorTemplate.rgb("#f05858"));
//        colors.add(ColorTemplate.rgb("#32b181"));
        //colors.add(ColorTemplate.rgb("#303F9F"));

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

//        dataSet.setValueLinePart1OffsetPercentage(80.f);
//        dataSet.setValueLinePart1Length(0.6f);
//        dataSet.setValueLinePart2Length(0.6f);
        //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
//        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return String.valueOf((int) value);
            }
        });
        data.setValueTextSize(15f);
        data.setValueTextColor(Color.DKGRAY);
        //data.setValueTypeface(mTfLight);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }


    private SpannableString generateCenterSpannableText(String text) {

        SpannableString s = new SpannableString(text);
        s.setSpan(new RelativeSizeSpan(1.7f), 0, s.length(), 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), 0, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(Color.DKGRAY), 0, s.length(), 0);
//        s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
//        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
//        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
        return s;
    }

    private class DashboardLoaderTask extends AsyncTask<Integer, Integer, Boolean> {

        static final int COMMAND_CHART_DATA = 0;

        @Override
        protected Boolean doInBackground(Integer... integers) {
            int command = integers[0];
            //int intent = integers[1];
            SpContentProvider p = SpContentProvider.getInstance(getContext());
            _currentData = p.getSankalpCountData();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {

                if (_currentData.getAllSankalps() > 0) {
                    setData();
                } else {
                    mChart.setData(null);
                    mChart.invalidate();
                }

            }
        }
    }

}
