package com.ravijain.sankalp.activities;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.ravijain.sankalp.R;
import com.ravijain.sankalp.data.SpCategoryItem;
import com.ravijain.sankalp.data.SpContentProvider;
import com.ravijain.sankalp.data.SpDataConstants;
import com.ravijain.sankalp.data.SpSankalp;
import com.ravijain.sankalp.fragments.SpFilterDialogFragment;
import com.ravijain.sankalp.fragments.SpSankalpListAdapter;
import com.ravijain.sankalp.fragments.SpSankalpListFragment;

import java.util.ArrayList;
import java.util.Hashtable;

public class SpSankalpList extends AppCompatActivity implements SpFilterDialogFragment.NoticeDialogListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
//        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//            String query = intent.getStringExtra(SearchManager.QUERY);
//            fragment.searchList(query);
//        }
//        else {
            setContentView(R.layout.activity_sp_sankalp_list);
//            fragment = _getFragment();
//        }
    }

    private SpSankalpListFragment _getFragment()
    {
        return (SpSankalpListFragment) getSupportFragmentManager().findFragmentById(R.id.listfragment);
    }

    @Override
    public void onDialogPositiveClick(int sankalpType, int period) {

        _getFragment().onDialogPositiveClick(sankalpType, period);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }
}
