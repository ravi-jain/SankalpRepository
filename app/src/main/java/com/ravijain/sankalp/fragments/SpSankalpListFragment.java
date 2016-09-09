package com.ravijain.sankalp.fragments;

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
import android.widget.TextView;

import com.ravijain.sankalp.R;
import com.ravijain.sankalp.activities.SpAddSankalpActivity;
import com.ravijain.sankalp.activities.SpConstants;
import com.ravijain.sankalp.activities.SpSankalpDetailsActivity;
import com.ravijain.sankalp.data.SpContentProvider;
import com.ravijain.sankalp.data.SpSankalp;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A placeholder fragment containing a simple view.
 */
public class SpSankalpListFragment extends Fragment implements SearchView.OnQueryTextListener {

    private SpSankalpListAdapter _sankalpAdapter;
    private ListView _sankalpListView;
    private SankalpLoaderTask _loaderTask;
    private TextView _listSummaryTv;

    //private int _sankalpType;
    private int _listFilter;
    //private Calendar _day = null;

    public SpSankalpListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sp_sankalp_list, container, false);
        setHasOptionsMenu(true);

        Bundle b = getArguments();
        int sankalpType = SpConstants.SANKALP_TYPE_BOTH;
        Calendar day = null;
        if (b != null) {
            sankalpType = b.getInt(SpConstants.INTENT_KEY_SANKALP_TYPE, SpConstants.SANKALP_TYPE_BOTH);
            _listFilter = b.getInt(SpConstants.INTENT_KEY_SANKALP_LIST_FILTER, SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_ALL);
            long time = getArguments().getLong(SpConstants.INTENT_KEY_SANKALP_LIST_FILTER_DATE_VALUE, -1);
            if (time > -1)
            {
                day = Calendar.getInstance();
                day.setTimeInMillis(time);
            }
        }

        String title = getString(R.string.title_activity_sp_sankalp_list);
        if (sankalpType == SpConstants.SANKALP_TYPE_NIYAM) {
            title = getString(R.string.niyam);
        }
        else if (sankalpType == SpConstants.SANKALP_TYPE_TYAG) {
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
                Intent intent = new Intent(getActivity(), SpAddSankalpActivity.class);
                intent.putExtra(SpConstants.INTENT_KEY_SANKALP_TYPE, sankalp.getSankalpType());
                intent.putExtra(SpConstants.INTENT_KEY_SANKALP_ID, sankalp.getId());
                startActivity(intent);
            }
        });
        _sankalpListView.setEmptyView(rootView.findViewById(R.id.emptyListText));
        _listSummaryTv = ((TextView)rootView.findViewById(R.id.listViewShowSummary));
        _refreshView(sankalpType, _listFilter, day);
        return rootView;
    }

    private void _refreshView(int sankalpType, int intentFilter, Calendar day)
    {

        _listSummaryTv.setText(_getListSummary(sankalpType, intentFilter));
        _loaderTask = new SankalpLoaderTask(sankalpType, intentFilter, day);
        _loaderTask.execute();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_list, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.list_search).getActionView();
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_list_filter) {
            //_addSankalp();
            SpFilterDialogFragment d = new SpFilterDialogFragment();
            d.setListFilter(_listFilter);
            d.setParentFragment(this);
            d.show(getFragmentManager(), "SpFilterDialogFragment");
            return true;
        }
        else if (id == R.id.action_list_period) {
            SpIntervalDialogFragment d = new SpIntervalDialogFragment();
            d.show(getFragmentManager(), "SpIntervalDialogFragment");
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
            sankalpType = SpConstants.SANKALP_TYPE_TYAG;
        }
        else if (sankalpTypeid == R.id.rb_st_niyam) {
            sankalpType = SpConstants.SANKALP_TYPE_NIYAM;
        }
        else {
            sankalpType = SpConstants.SANKALP_TYPE_BOTH;
        }

        _filterList(sankalpType, listFilter);

    }

    private void _filterList(int sankalpType, int listFilter)
    {
        _listSummaryTv.setText(_getListSummary(sankalpType, listFilter));
        _sankalpAdapter.filter(sankalpType, listFilter);
    }

    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    private String _getListSummary(int sankalpType, int listFilter)
    {
        String sankalpTypeLabel;
        if (sankalpType == SpConstants.SANKALP_TYPE_BOTH) {
            sankalpTypeLabel = "sankalps";
        }
        else if (sankalpType == SpConstants.SANKALP_TYPE_TYAG) {
            sankalpTypeLabel = "tyags";
        }
        else {
            sankalpTypeLabel = "niyams";
        }

        String pd = null;
        if (listFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_LIFETIME) {
            pd = "lifetime";
        }
        else if (listFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_CURRENT) {
            pd = "current";
        }
        else if (listFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_UPCOMING) {
            pd = "upcoming";
        }
//        else if (_listFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_TODAY) {
//            pd = "today's";
//        }
//        else if (_listFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_TOMORROW) {
//            pd = "tomorrow's";
//        }
//        else if (_listFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_MONTH) {
//            pd = "this month's";
//        }
//        else if (_listFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_YEAR) {
//            pd = "this year's";
//        }
        else if (listFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_ALL){
            pd = "all";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Showing ");
        if (pd != null) sb.append(pd).append(" ");
        sb.append(sankalpTypeLabel).append(".");
        return sb.toString();
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


    private class SankalpLoaderTask extends AsyncTask<Void, Void, Boolean>
    {
        private ArrayList<SpSankalp> _sankalps = new ArrayList<SpSankalp>();

        private int _sType, _lFilter;
        private Calendar _dayFilter = null;

        SankalpLoaderTask(int sankalpType, int listFilter, Calendar day)
        {
            _sType = sankalpType;
            _lFilter = listFilter;
            _dayFilter = day;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            SpContentProvider provider = SpContentProvider.getInstance(getContext());
            _sankalps = provider.getSankalps(_sType, _lFilter, _dayFilter);

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            _loaderTask = null;

            if (success) {
                _sankalpAdapter.clearAdapter();
                _sankalpAdapter.addItems(_sankalps);

//                if (_sankalpTypeFilter != -1) {
//                    int f = _lFilter;
//                    if (_lFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_TOMORROW ||
//                            _lFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_MONTH ||
//                            _lFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_YEAR) {
//                        f = -1;
//                    }
//                    _filterList(_sankalpTypeFilter, f);
//                }

            } else {
                // Error
            }
        }

    }

    private class SpMultiNodeChoiceListener implements AbsListView.MultiChoiceModeListener
    {

        @Override
        public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
            int count = _sankalpListView.getCheckedItemCount();
            String label = String.valueOf(count) + " ";
            if (count == 1) {
                label += getResources().getString(R.string.listSelectedItemLabel);
            }
            else if (count > 1) {
                label += getResources().getString(R.string.listSelectedItemsLabel);
            }
            actionMode.setTitle(label);
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
