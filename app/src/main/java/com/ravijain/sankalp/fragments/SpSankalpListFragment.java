package com.ravijain.sankalp.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionMenu;
import com.github.clans.fab.FloatingActionButton;
import com.ravijain.sankalp.R;
import com.ravijain.sankalp.activities.SpAddSankalpActivity;
import com.ravijain.sankalp.activities.SpSankalpList;
import com.ravijain.sankalp.support.SpConstants;
import com.ravijain.sankalp.data.SpContentProvider;
import com.ravijain.sankalp.data.SpSankalp;
import com.ravijain.sankalp.support.SpDividerItemDecoration;
import com.ravijain.sankalp.support.SpUtils;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A placeholder fragment containing a simple view.
 */
public class SpSankalpListFragment extends SpFabBaseFragment implements SearchView.OnQueryTextListener, SpSimpleAlertDialog.SpSimpleAlertDialogListener {

    private SpSankalpListRecyclerAdapter _sankalpAdapter;
    private ListView _sankalpListView;
    private SankalpLoaderTask _loaderTask;
    private TextView _listSummaryTv;

    private ActionModeCallback actionModeCallback = new ActionModeCallback();
    private ActionMode actionMode;

    private int _sankalpType;
    private int _listFilter;
    private Calendar _day = null;

    private int _sortId = R.string.endDate;
    private int _sortOrder = SpConstants.SORT_ORDER_ASCENDING;

    public SpSankalpListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sp_sankalp_list, container, false);
        setHasOptionsMenu(true);
        setupFAB(rootView);
        _setupNavigationMenu(rootView);

        Bundle b = getArguments();
        if (b != null) {
            _sankalpType = b.getInt(SpConstants.INTENT_KEY_SANKALP_TYPE, SpConstants.SANKALP_TYPE_BOTH);
            _listFilter = b.getInt(SpConstants.INTENT_KEY_SANKALP_LIST_FILTER, SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_ALL);
            long time = getArguments().getLong(SpConstants.INTENT_KEY_SANKALP_LIST_FILTER_DATE_VALUE, -1);
            if (time > -1) {
                _day = Calendar.getInstance();
                _day.setTimeInMillis(time);
            }
        }

        String title = getString(R.string.title_activity_sp_sankalp_list);
        if (_sankalpType == SpConstants.SANKALP_TYPE_NIYAM) {
            title = getString(R.string.niyam);
        } else if (_sankalpType == SpConstants.SANKALP_TYPE_TYAG) {
            title = getString(R.string.tyag);
        }
        getActivity().setTitle(title);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.sankalpListview);

        _sankalpAdapter = new SpSankalpListRecyclerAdapter(getContext(), new ArrayList<SpSankalp>(), new SpSankalpListRecyclerAdapter.RecyclerViewClickListener() {
            @Override
            public void onItemClicked(int position) {
                if (actionMode != null) {
                    toggleSelection(position);
                } else {
                    SpSankalp sankalp = _sankalpAdapter.getSankalpByPosition(position);
                    Intent intent = new Intent(getActivity(), SpAddSankalpActivity.class);
                    intent.putExtra(SpConstants.INTENT_KEY_SANKALP_TYPE, sankalp.getSankalpType());
                    intent.putExtra(SpConstants.INTENT_KEY_SANKALP_ID, sankalp.getId());
                    startActivityForResult(intent, SpConstants.ACTIVITY_REQUEST_CODE);
                }

            }

            @Override
            public boolean onItemLongClicked(int position) {
                if (actionMode == null) {
                    actionMode = getActivity().startActionMode(actionModeCallback);//startSupportActionMode(actionModeCallback);
                }

                toggleSelection(position);

                return true;
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(_sankalpAdapter);
        recyclerView.addItemDecoration(new SpDividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            FloatingActionMenu fab = (FloatingActionMenu) getActivity().findViewById(R.id.right_labels);
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if (dy > 0 ||dy<0 && fab.isShown())
                {
                    fab.hideMenu(false);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                {
                    fab.showMenu(false);
                }

                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        _refreshView(_sankalpType, _listFilter, _day);
        return rootView;
    }

    public void setupFAB(View rootView) {
        FloatingActionButton fabTyag = (FloatingActionButton) getActivity().findViewById(R.id.chartCalendarDb_addTyagButton);
        FloatingActionButton niyamTyag = (FloatingActionButton) getActivity().findViewById(R.id.chartCalendarDb_addNiyamButton);
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
        FloatingActionMenu floatingActionsMenu = (FloatingActionMenu) getActivity().findViewById(R.id.right_labels);
        //floatingActionsMenu.collapseImmediately();
        floatingActionsMenu.close(true);
        startActivityForResult(intent, SpConstants.ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SpConstants.ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            _refreshView(_sankalpType, _listFilter, _day);
        }
    }

    private void _refreshView(int sankalpType, int intentFilter, Calendar day) {
        _loaderTask = new SankalpLoaderTask(sankalpType, intentFilter, day);
        _loaderTask.execute();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_list, menu);

        MenuItem searchItem = menu.findItem(R.id.list_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);

        boolean searchActivated = getActivity().getIntent().getBooleanExtra(SpConstants.INTENT_KEY_SANKALP_LIST_FILTER_SEARCH_ACTIVATED, false);
        if (searchActivated) {
            searchItem.expandActionView();
        }
    }

    private void _setupNavigationMenu(View rootView) {

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                getActivity().findViewById(R.id.bottom_navigation);
        final SpSankalpListFragment that = this;
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_list_filter:
                                SpFilterDialog d = new SpFilterDialog();
                                d.setTargetFragment(that, 300);
                                Bundle args = new Bundle();
                                args.putInt(SpSimpleAlertDialog.AD_LAYOUT_ID, R.layout.filter_dialog);
                                args.putString(SpSimpleAlertDialog.AD_TITLE, getString(R.string.filterList));
                                args.putInt(SpConstants.INTENT_KEY_SANKALP_LIST_FILTER, _listFilter);
                                d.setArguments(args);
                                d.show(getFragmentManager(), SpConstants.FRAGMENT_TAG_FILTER);
                                break;
                            case R.id.action_list_sort:
                                SpSortDialog d1 = new SpSortDialog();
                                d1.setTargetFragment(that, 300);
                                Bundle args1 = new Bundle();
                                args1.putInt(SpSimpleAlertDialog.AD_LAYOUT_ID, R.layout.sort_dialog);
                                args1.putInt(SpConstants.INTENT_SORTID, _sortId);
                                args1.putString(SpSimpleAlertDialog.AD_TITLE, getString(R.string.sort));
                                d1.setArguments(args1);
                                d1.show(getFragmentManager(), SpConstants.FRAGMENT_TAG_SORT);
                                break;
                            case R.id.action_list_switch:
                                SpSwitchViewDialog d2 = new SpSwitchViewDialog();
                                d2.setTargetFragment(that, 300);
                                Bundle args2 = new Bundle();
                                args2.putInt(SpSimpleAlertDialog.AD_LAYOUT_ID, R.layout.switch_view_dialog);
                                args2.putString(SpSimpleAlertDialog.AD_TITLE, getString(R.string.switchView));
                                args2.putInt(SpConstants.INTENT_KEY_SANKALP_LIST_FILTER, _listFilter);
                                d2.setArguments(args2);
                                d2.show(getFragmentManager(), SpConstants.FRAGMENT_TAG_SWITCH_VIEW);
                                break;
                        }
                        return false;
                    }
                });
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_list_filter) {
//
//            SpFilterDialog d = new SpFilterDialog();
//            d.setTargetFragment(this, 300);
//            Bundle args = new Bundle();
//            args.putInt(SpSimpleAlertDialog.AD_LAYOUT_ID, R.layout.filter_dialog);
//            args.putString(SpSimpleAlertDialog.AD_TITLE, getString(R.string.filterList));
//            args.putInt(SpConstants.INTENT_KEY_SANKALP_LIST_FILTER, _listFilter);
//            d.setArguments(args);
//            d.show(getFragmentManager(), SpConstants.FRAGMENT_TAG_FILTER);
//            return true;
//        } else if (id == R.id.action_list_sort) {
//            SpSortDialog d = new SpSortDialog();
//            d.setTargetFragment(this, 300);
//            Bundle args = new Bundle();
//            args.putInt(SpSimpleAlertDialog.AD_LAYOUT_ID, R.layout.sort_dialog);
//            args.putInt(SpConstants.INTENT_SORTID, _sortId);
//            args.putString(SpSimpleAlertDialog.AD_TITLE, getString(R.string.sort));
//            d.setArguments(args);
//            d.show(getFragmentManager(), SpConstants.FRAGMENT_TAG_SORT);
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    private void _filterList(int sankalpType, int listFilter) {
        //_listSummaryTv.setText(_getListSummary(sankalpType, listFilter));
        _sankalpAdapter.filter(sankalpType, listFilter);
    }

    public void sortList(int sortId) {
        if (sortId == _sortId) _sortOrder = _sortOrder * -1;
        else {
            _sortOrder = SpConstants.SORT_ORDER_ASCENDING;
        }
        _sortId = sortId;
        _sankalpAdapter.sortList(sortId, _sortOrder);
    }

    private String _getListSummary(int sankalpType, int listFilter) {
        String sankalpTypeLabel;
        if (sankalpType == SpConstants.SANKALP_TYPE_BOTH) {
            sankalpTypeLabel = "sankalps";
        } else if (sankalpType == SpConstants.SANKALP_TYPE_TYAG) {
            sankalpTypeLabel = "tyags";
        } else {
            sankalpTypeLabel = "niyams";
        }

        String pd = null;
        if (listFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_LIFETIME) {
            pd = "lifetime";
        } else if (listFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_CURRENT) {
            pd = "current";
        } else if (listFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_UPCOMING) {
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
        else if (listFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_ALL) {
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

    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            searchList("");
        } else {
            searchList(newText);
        }
        return true;
    }

    @Override
    public void onSimpleAlertDialogPositiveClick(AlertDialog dialog, String tag) {
        /*if (tag.equals(SpConstants.FRAGMENT_TAG_SORT)) {
            RadioGroup sTRG = (RadioGroup) dialog.findViewById(R.id.rg_sort);
            if (sTRG == null) return;
            int id = sTRG.getCheckedRadioButtonId();
            sortList(id);
        } else */if (tag.equals(SpConstants.FRAGMENT_TAG_FILTER)) {
            RadioGroup sTRG = (RadioGroup) dialog.findViewById(R.id.rg_sankalpType);

            int sankalpTypeRB = sTRG.getCheckedRadioButtonId();
            int sankalpType;

            if (sankalpTypeRB == R.id.rb_st_tyag) {
                sankalpType = SpConstants.SANKALP_TYPE_TYAG;
            } else if (sankalpTypeRB == R.id.rb_st_niyam) {
                sankalpType = SpConstants.SANKALP_TYPE_NIYAM;
            } else {
                sankalpType = SpConstants.SANKALP_TYPE_BOTH;
            }

            int listFilter = -1;
            RadioGroup sPRG = (RadioGroup) dialog.findViewById(R.id.rg_sankalpPeriod);
            if (sPRG != null && sPRG.getVisibility() == View.VISIBLE) {
                int period = sPRG.getCheckedRadioButtonId();
                if (period == R.id.rb_sp_current) {
                    listFilter = SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_CURRENT;
                } else if (period == R.id.rb_sp_upcoming) {
                    listFilter = SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_UPCOMING;
                } else if (period == R.id.rb_sp_all) {
                    listFilter = SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_ALL;
                } else {
                    listFilter = -1;
                }
            }
            listFilter = SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_ALL;
            _filterList(sankalpType, listFilter);
        }
    }

    @Override
    public void onSimpleAlertDialogNegativeClick(AlertDialog dialog, String tag) {
    }

    /**
     * Toggle the selection state of an item.
     * <p/>
     * If the item was the last one in the selection and is unselected, the selection is stopped.
     * Note that the selection must already be started (actionMode must not be null).
     *
     * @param position Position of the item to toggle the selection state
     */
    private void toggleSelection(int position) {
        _sankalpAdapter.toggleSelection(position);
        int count = _sankalpAdapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            String label = String.valueOf(count) + " ";
            if (count == 1) {
                label += getResources().getString(R.string.listSelectedItemLabel);
            } else if (count > 1) {
                label += getResources().getString(R.string.listSelectedItemsLabel);
            }
            actionMode.setTitle(label);
            actionMode.invalidate();
        }
    }

    private class ActionModeCallback implements ActionMode.Callback {
        @SuppressWarnings("unused")
        private final String TAG = ActionModeCallback.class.getSimpleName();

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_list_context, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_db_deleteSankalp:
                    getDeleteWarningDialog(actionMode).show();
//                    mode.finish();
                    return true;

                default:
                    return false;
            }
        }

        private AlertDialog getDeleteWarningDialog(final ActionMode actionMode) {
            AlertDialog dialog = new AlertDialog.Builder(getContext())
                    //set message, title, and icon
                    .setTitle(getString(R.string.deleteSankalp))
                    .setMessage(getString(R.string.multiDeletePrompt))
                    .setIcon(R.drawable.ic_delete_black_24dp)

                    .setPositiveButton(R.string.deleteSankalp, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            //your deleting code

                            dialog.dismiss();
                            ArrayList<SpSankalp> sankalpsToBeDeleted = _sankalpAdapter.getSelectedSankalps();
                            _sankalpAdapter.removeItems(_sankalpAdapter.getSelectedItems());
                            SpContentProvider provider = SpContentProvider.getInstance(getContext());
                            provider.deleteSankalps(sankalpsToBeDeleted);
                            actionMode.finish(); // Action picked, so close the CAB
                        }

                    })


                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                            actionMode.finish(); // Action picked, so close the CAB

                        }
                    })
                    .create();
            return dialog;

        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            _sankalpAdapter.clearSelection();
            actionMode = null;
        }
    }

    private class SankalpLoaderTask extends AsyncTask<Void, Void, Boolean> {
        private ArrayList<SpSankalp> _sankalps = new ArrayList<SpSankalp>();

        private int _sType, _lFilter;
        private Calendar _dayFilter = null;

        SankalpLoaderTask(int sankalpType, int listFilter, Calendar day) {
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
                _sankalpAdapter.loadAdapter(_sankalps);
                _sankalpAdapter.notifyDataSetChanged();

            }
        }

    }
}
