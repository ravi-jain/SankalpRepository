package com.ravijain.sankalp.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ravijain.sankalp.R;
import com.ravijain.sankalp.activities.SpAddSankalpActivity;
import com.ravijain.sankalp.activities.SpConstants;
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
 * A placeholder fragment containing a simple view.
 */
public class SpSankalpListFragment extends Fragment {

    private SankalpListAdapter _sankalpAdapter;
    private ListView _sankalpListView;
    private SankalpLoaderTask _loaderTask;
    private Hashtable<Integer, SpCategoryItem> _categoryItems;

    private int _sankalpType;
    private int _listFilter;

    public SpSankalpListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sp_sankalp_list, container, false);
        _sankalpType = getActivity().getIntent().getIntExtra(SpConstants.INTENT_KEY_SANKALP_TYPE, SpDataConstants.SANKALP_TYPE_TYAG);
        _listFilter = getActivity().getIntent().getIntExtra(SpConstants.INTENT_KEY_SANKALP_LIST_FILTER, SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_CURRENT);

        String title = getString(R.string.tyag);
        if (_sankalpType == SpDataConstants.SANKALP_TYPE_NIYAM) {
            title = getString(R.string.niyam);
        }
        getActivity().setTitle(title);

        _sankalpAdapter = new SankalpListAdapter(getContext(), new ArrayList<SpSankalp>());
        _sankalpListView = (ListView) rootView.findViewById(R.id.sankalpListview);
        _sankalpListView.setAdapter(_sankalpAdapter);

        SpMultiNodeChoiceListener listener = new SpMultiNodeChoiceListener();
        _sankalpListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        _sankalpListView.setMultiChoiceModeListener(listener);

//        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.addSankalpButton);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), SpAddSankalpActivity.class);
//                startActivity(intent);
//            }
//        });

        _loaderTask = new SankalpLoaderTask();
        _loaderTask.execute((Void) null);

        return rootView;
    }

    public class SankalpListAdapter extends ArrayAdapter<SpSankalp> {

        public SankalpListAdapter(Context context, ArrayList<SpSankalp> sankalps) {
            super(context, 0, sankalps);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            SpSankalp sankalp = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_dashboard, parent, false);
            }
            // Lookup view for data population
            TextView tvItem = (TextView) convertView.findViewById(R.id.list_item_item_textview);
            TextView tvPeriod = (TextView) convertView.findViewById(R.id.list_item_period_textview);
            // Populate the data into the template view using the data object
            int itemId = sankalp.getItemId();
            SpCategoryItem item = _categoryItems.get(itemId);
            int isLifetime = sankalp.isLifetime();

            String period;
            if (isLifetime == SpDataConstants.SANKALP_IS_LIFTIME_TRUE) {
                period = getString(R.string.Lifetime);
            }
            else {
                Date fromDate = sankalp.getFromDate();
                Date toDate = sankalp.getToDate();

                period = SpDateUtils.getFriendlyDateString(fromDate) + " - " + SpDateUtils.getFriendlyDateString(toDate);
            }


            // Populate fields with extracted properties
//            tvCategory.setText(category.getCategoryName());
            tvItem.setText(item.getCategoryItemName());
            tvPeriod.setText(String.valueOf(period));
            // Return the completed view to render on screen
            return convertView;
        }
    }

    private class SankalpLoaderTask extends AsyncTask<Void, Void, Boolean>
    {
        private ArrayList<SpSankalp> _sankalps = new ArrayList<SpSankalp>();
        @Override
        protected Boolean doInBackground(Void... params) {
            SpContentProvider provider = SpContentProvider.getInstance(getContext());
            _categoryItems = provider.getAllCategoryItems();
            ArrayList<SpSankalp> sankalps = provider.getSankalps(_sankalpType, SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_ALL);
            for (SpSankalp sankalp : sankalps) {
                if (_listFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_ALL) {
                    _sankalps.add(sankalp);
                }
                else if (_listFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_LIFETIME){
                    if (sankalp.isLifetime() == SpDataConstants.SANKALP_IS_LIFTIME_TRUE) {
                        _sankalps.add(sankalp);
                    }
                }else {
                    Date now = new Date();
                    if (_listFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_UPCOMING && SpDateUtils.isUpcomingDate(sankalp.getFromDate())) {
                        _sankalps.add(sankalp);
                    }
                    else if(_listFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_CURRENT && SpDateUtils.isCurrentDate(sankalp.getFromDate(), sankalp.getToDate())) {
                        _sankalps.add(sankalp);
                    }
                }

            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            _loaderTask = null;

            if (success) {
                _sankalpAdapter.clear();
                _sankalpAdapter.addAll(_sankalps);
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
            inflater.inflate(R.menu.menu_dashboard_context, menu);
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
                    provider.deleteSankalps(sankalpsToBeDeleted, _sankalpType);
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
