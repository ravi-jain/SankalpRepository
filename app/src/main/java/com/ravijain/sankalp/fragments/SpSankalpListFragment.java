package com.ravijain.sankalp.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ravijain.sankalp.R;
import com.ravijain.sankalp.activities.SpConstants;
import com.ravijain.sankalp.activities.SpSankalpDetailsActivity;
import com.ravijain.sankalp.data.SpCategoryItem;
import com.ravijain.sankalp.data.SpContentProvider;
import com.ravijain.sankalp.data.SpDataConstants;
import com.ravijain.sankalp.data.SpDateUtils;
import com.ravijain.sankalp.data.SpSankalp;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

/**
 * A placeholder fragment containing a simple view.
 */
public class SpSankalpListFragment extends Fragment implements SearchView.OnQueryTextListener {

    private SpSankalpListAdapter _sankalpAdapter;
    private ListView _sankalpListView;
    private SankalpLoaderTask _loaderTask;
    private Hashtable<Integer, SpCategoryItem> _categoryItems;
    private TextView _listSummaryTv;

    private int _sankalpType;
    private int _listFilter;

    public SpSankalpListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sp_sankalp_list, container, false);
        setHasOptionsMenu(true);
        _sankalpType = getActivity().getIntent().getIntExtra(SpConstants.INTENT_KEY_SANKALP_TYPE, SpDataConstants.SANKALP_TYPE_BOTH);
        _listFilter = getActivity().getIntent().getIntExtra(SpConstants.INTENT_KEY_SANKALP_LIST_FILTER, SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_CURRENT);

        String title = getString(R.string.title_activity_sp_sankalp_list);
        if (_sankalpType == SpDataConstants.SANKALP_TYPE_NIYAM) {
            title = getString(R.string.niyam);
        }
        else if (_sankalpType == SpDataConstants.SANKALP_TYPE_TYAG) {
            title = getString(R.string.tyag);
        }
        getActivity().setTitle(title);

        _sankalpAdapter = new SpSankalpListAdapter(getContext(), new ArrayList<SpSankalp>());
        _sankalpListView = (ListView) rootView.findViewById(R.id.sankalpListview);
        _sankalpListView.setAdapter(_sankalpAdapter);

        SpMultiNodeChoiceListener listener = new SpMultiNodeChoiceListener();
        _sankalpListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        _sankalpListView.setMultiChoiceModeListener(listener);
        _sankalpListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                SpSankalp sankalp = (SpSankalp)adapterView.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), SpSankalpDetailsActivity.class);
                intent.putExtra(SpConstants.INTENT_KEY_SANKALP_TYPE, sankalp.getSankalpType());
                intent.putExtra(SpConstants.INTENT_KEY_SANKALP_ID, sankalp.getId());
                startActivity(intent);
            }
        });
        _sankalpListView.setEmptyView(rootView.findViewById(R.id.emptyListText));

        _listSummaryTv = ((TextView)rootView.findViewById(R.id.listViewShowSummary));
        _listSummaryTv.setText(_getListSummary());
        _loaderTask = new SankalpLoaderTask();
        _loaderTask.execute(_sankalpType, _listFilter);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_list, menu);

        // Get the SearchView and set the searchable configuration
//        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.list_search).getActionView();
        searchView.setOnQueryTextListener(this);
        // Assumes current activity is the searchable activity
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
//        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_list_filter) {
            //_addSankalp();
            SpFilterDialogFragment d = new SpFilterDialogFragment();
            d.setListFilter(_listFilter);
            d.show(getFragmentManager(), "SpFilterDialogFragment");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onDialogPositiveClick(int sankalpTypeid, int period) {

        int sankalpType, listFilter;
        if (period == R.id.rb_sp_current) {
            listFilter = SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_CURRENT;
        }
        else if (period == R.id.rb_sp_upcoming) {
            listFilter = SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_UPCOMING;
        }
        else if (period == R.id.rb_sp_all) {
            listFilter = SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_ALL;
        }
        else {
            listFilter = -1;
        }
        if (sankalpTypeid == R.id.rb_st_tyag) {
            sankalpType = SpDataConstants.SANKALP_TYPE_TYAG;
        }
        else if (sankalpTypeid == R.id.rb_st_niyam) {
            sankalpType = SpDataConstants.SANKALP_TYPE_NIYAM;
        }
        else {
            sankalpType = SpDataConstants.SANKALP_TYPE_BOTH;
        }

        _listSummaryTv.setText(_getListSummary());

        _sankalpAdapter.filter(sankalpType, listFilter);

//        _loaderTask = new SankalpLoaderTask();
//        _loaderTask.execute(_sankalpType, _listFilter);
    }

    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    private String _getListSummary()
    {
        String sankalpType;
        if (_sankalpType == SpDataConstants.SANKALP_TYPE_BOTH) {
            sankalpType = "sankalps";
        }
        else if (_sankalpType == SpDataConstants.SANKALP_TYPE_TYAG) {
            sankalpType = "tyags";
        }
        else {
            sankalpType = "niyams";
        }

        String pd;
        if (_listFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_LIFETIME) {
            pd = "lifetime";
        }
        else if (_listFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_CURRENT) {
            pd = "current";
        }
        else if (_listFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_UPCOMING) {
            pd = "upcoming";
        }
        else if (_listFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_TODAY) {
            pd = "today's";
        }
        else if (_listFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_TOMORROW) {
            pd = "tomorrow's";
        }
        else if (_listFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_MONTH) {
            pd = "this month's";
        }
        else if (_listFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_YEAR) {
            pd = "this year's";
        }
        else {
            pd = "all";
        }

        return "Showing " + pd + " " + sankalpType + ".";
    }

    public void searchList(String query) {
        _sankalpAdapter.search(query);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchList(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            searchList("");
            return true;
        }
        return false;
    }


    private class SankalpLoaderTask extends AsyncTask<Integer, Integer, Boolean>
    {
        private ArrayList<SpSankalp> _sankalps = new ArrayList<SpSankalp>();
        @Override
        protected Boolean doInBackground(Integer... integers) {
            int sankalpType = integers[0];
            int listFilter = integers[1];
            SpContentProvider provider = SpContentProvider.getInstance(getContext());
            _sankalps = provider.getSankalps(_sankalpType, _listFilter);

//            _sankalpType = sankalpType;
//            _listFilter = listFilter;

            /*for (SpSankalp sankalp : sankalps) {
                if (listFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_ALL) {
                    _sankalps.add(sankalp);
                }
                else if (listFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_LIFETIME){
                    if (sankalp.isLifetime() == SpDataConstants.SANKALP_IS_LIFTIME_TRUE) {
                        _sankalps.add(sankalp);
                    }
                }else {
                    if (listFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_UPCOMING && SpDateUtils.isUpcomingDate(sankalp.getFromDate())) {
                        _sankalps.add(sankalp);
                    }
                    else if(listFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_CURRENT && SpDateUtils.isCurrentDate(sankalp.getFromDate(), sankalp.getToDate())) {
                        _sankalps.add(sankalp);
                    }
                }

            }*/

            return true;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            _loaderTask = null;

            if (success) {
                _sankalpAdapter.clearAdapter();
                _sankalpAdapter.addItems(_sankalps);
            } else {
                // Error
            }
        }
    }

    private class SpMultiNodeChoiceListener implements AbsListView.MultiChoiceModeListener
    {

        @Override
        public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {

        }

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            MenuInflater inflater = actionMode.getMenuInflater();
            inflater.inflate(R.menu.menu_list_context, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {

            switch (menuItem.getItemId()) {
                case R.id.action_db_deleteSankalp:
                    SparseBooleanArray selected = _sankalpListView.getCheckedItemPositions();
                    ArrayList<SpSankalp> sankalpsToBeDeleted = new ArrayList<SpSankalp>();
                    // Captures all selected ids with a loop
                    for (int i = (selected.size() - 1); i >= 0; i--) {
                        if (selected.valueAt(i)) {
                            SpSankalp selecteditem = _sankalpAdapter
                                    .getItem(selected.keyAt(i));
                            // Remove selected items following the ids
                            sankalpsToBeDeleted.add(selecteditem);
                            _sankalpAdapter.remove(selecteditem);

                        }
                    }
                    SpContentProvider provider = SpContentProvider.getInstance(getContext());
                    provider.deleteSankalps(sankalpsToBeDeleted);
                    actionMode.finish(); // Action picked, so close the CAB
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {

        }
    }
}
