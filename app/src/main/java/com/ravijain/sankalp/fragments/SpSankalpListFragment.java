package com.ravijain.sankalp.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ravijain.sankalp.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class SpSankalpListFragment extends Fragment {

    //private ListViewAdapter tyagAdapter;
    private ListView tyagListView;

    public SpSankalpListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sp_sankalp_list, container, false);
    }
}
