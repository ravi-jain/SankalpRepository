package com.ravijain.sankalp.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionMenu;
import com.github.clans.fab.FloatingActionButton;
import com.ravijain.sankalp.R;
import com.ravijain.sankalp.activities.SpAddSankalpActivity;
import com.ravijain.sankalp.support.SpConstants;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpFabBaseFragment extends Fragment implements View.OnClickListener{


    public SpFabBaseFragment() {
        // Required empty public constructor
    }


    public void setupFAB(View rootView)
    {
        FloatingActionButton fabTyag = (FloatingActionButton) rootView.findViewById(R.id.chartCalendarDb_addTyagButton);
        FloatingActionButton niyamTyag = (FloatingActionButton) rootView.findViewById(R.id.chartCalendarDb_addNiyamButton);
        fabTyag.setOnClickListener(this);
        niyamTyag.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), SpAddSankalpActivity.class);
        if (view.getId() == R.id.chartCalendarDb_addTyagButton) {
            intent.putExtra(SpConstants.INTENT_KEY_SANKALP_TYPE, SpConstants.SANKALP_TYPE_TYAG);
        } else if (view.getId() == R.id.chartCalendarDb_addNiyamButton) {
            intent.putExtra(SpConstants.INTENT_KEY_SANKALP_TYPE, SpConstants.SANKALP_TYPE_NIYAM);
        }
        FloatingActionMenu floatingActionsMenu = (FloatingActionMenu) getView().findViewById(R.id.right_labels);
        floatingActionsMenu.close(true);
        startActivityForResult(intent, SpConstants.ACTIVITY_REQUEST_CODE);
    }
}
