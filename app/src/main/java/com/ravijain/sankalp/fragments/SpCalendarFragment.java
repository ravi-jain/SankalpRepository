package com.ravijain.sankalp.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.clans.fab.FloatingActionMenu;
import com.ravijain.sankalp.R;
import com.ravijain.sankalp.activities.SpAddSankalpActivity;
import com.ravijain.sankalp.activities.SpSankalpList;
import com.ravijain.sankalp.support.SpCalendarViewHandler;
import com.ravijain.sankalp.support.SpConstants;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpCalendarFragment extends SpFabBaseFragment{


    private SpCalendarViewHandler _calendarView;
    private Button _viewMonthDetailsButton;

    public SpCalendarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_sp_calendar, container, false);
        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.calendarHeader);

        _setUpCalendarCard(rootView);

        setupFAB(rootView);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_calendar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_showDashboard) {
            getFragmentManager().beginTransaction().replace(R.id.content_frame, new SpMainDashboard()).commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private void _setUpCalendarCard(View rootView) {

        _calendarView = new SpCalendarViewHandler(getContext(), rootView);
        _calendarView.constructCalendar(R.id.db_calendarView, SpCalendarViewHandler.SELECTION_MODE_EVENT);

        _viewMonthDetailsButton = (Button) rootView.findViewById(R.id.viewMonthDetails_button);
        _viewMonthDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SpSankalpList.class);
                intent.putExtra(SpConstants.INTENT_KEY_SANKALP_TYPE, SpConstants.SANKALP_TYPE_BOTH);
                intent.putExtra(SpConstants.INTENT_KEY_SANKALP_LIST_FILTER, SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_MONTH);
                intent.putExtra(SpConstants.INTENT_KEY_SANKALP_LIST_FILTER_DATE_VALUE, new Date().getTime());
                startActivity(intent);
            }
        });
    }

}
