package com.ravijain.sankalp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ravijain.sankalp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpMaterialDashboard extends Fragment {


    public SpMaterialDashboard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sp_material_dashboard, container, false);
    }

}
