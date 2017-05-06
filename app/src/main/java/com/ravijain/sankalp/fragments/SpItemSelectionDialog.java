package com.ravijain.sankalp.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ravijain.sankalp.R;
import com.ravijain.sankalp.data.SpCategory;
import com.ravijain.sankalp.data.SpCategoryItem;
import com.ravijain.sankalp.data.SpContentProvider;
import com.ravijain.sankalp.support.SpColorGenerator;
import com.ravijain.sankalp.support.SpConstants;
import com.ravijain.sankalp.support.SpExpandableListAdapter;
import com.ravijain.sankalp.support.SpTextDrawable;
import com.ravijain.sankalp.support.SpUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by ravijain on 9/8/2016.
 */
public class SpItemSelectionDialog extends DialogFragment implements SearchView.OnQueryTextListener, SpSimpleAlertDialog.SpSimpleAlertDialogListener {

    private View _rootView;
    private ExpandableListView _listView;

    private SpAddSankalpFragment _parentFragment;
    private ExpandableListAdapter _adapter;
    private List<SpCategory> _originalGroups;
    private HashMap<Integer, List<SpCategoryItem>> _originalChildren;
    private int _sankalpType;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _rootView = inflater.inflate(R.layout.fragment_simple_dialog, container, false);

        Toolbar toolbar = (Toolbar) _rootView.findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.selectItemTitle));

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_clear_black_24dp);
        }

        Bundle b = getArguments();

        if (b != null) {
            _sankalpType = b.getInt(SpConstants.INTENT_KEY_SANKALP_TYPE);
            _loadData();
        }
        setHasOptionsMenu(true);
        return _rootView;
    }

    private void _loadData() {
        ItemLoaderTask t = new ItemLoaderTask(ItemLoaderTask.OPERATION_FETCH_ITEMS, _sankalpType);
        t.execute();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_item_dialog, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.item_search).getActionView();
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // handle close button click here
            dismiss();
            return true;
        } else if (id == R.id.item_toggleAll) {
            if (item.getTitle().equals(getString(R.string.collapseAll))) {
                _collapseAll();
                item.setTitle(getString(R.string.expandAll));
                item.setIcon(R.drawable.ic_fullscreen_black_24dp);
            } else {
                _expandAll();
                item.setTitle(getString(R.string.collapseAll));
                item.setIcon(R.drawable.ic_fullscreen_exit_black_24dp);
            }
        } else if (id == R.id.item_add_item) {
            SpItemCreationDialog d = new SpItemCreationDialog();
            d.setTargetFragment(this, 300);
            Bundle args = new Bundle();
            args.putInt(SpSimpleAlertDialog.AD_LAYOUT_ID, R.layout.fragment_new_item);
            args.putString(SpSimpleAlertDialog.AD_TITLE, getString(R.string.addItem));
            args.putInt(SpSimpleAlertDialog.AD_OK_RESOURCE_ID, R.string.addSankalp);
            args.putInt(SpConstants.INTENT_KEY_SANKALP_TYPE, _sankalpType);
            d.setArguments(args);
            d.show(getFragmentManager(), SpConstants.FRAGMENT_TAG_ITEM);
        } else if (id == R.id.item_add_category) {
            SpSimpleAlertDialog d = new SpSimpleAlertDialog();
            d.setTargetFragment(this, 300);
            Bundle args = new Bundle();
            args.putInt(SpSimpleAlertDialog.AD_LAYOUT_ID, R.layout.fragment_new_category);
            args.putString(SpSimpleAlertDialog.AD_TITLE, getString(R.string.addCategory));
            args.putInt(SpSimpleAlertDialog.AD_OK_RESOURCE_ID, R.string.addSankalp);
            d.setArguments(args);
            d.show(getFragmentManager(), SpConstants.FRAGMENT_TAG_CATEGORY);
        }

        return super.onOptionsItemSelected(item);
    }

    public void setParentFragment(SpAddSankalpFragment parentFragment) {
        this._parentFragment = parentFragment;
    }


    private void _loadListView(List<SpCategory> groups, HashMap<Integer, List<SpCategoryItem>> children) {
        _originalGroups = groups;
        _originalChildren = children;

        ArrayList<Object> g = new ArrayList<Object>();
        g.addAll(groups);

        HashMap<Integer, List> c = new HashMap<>();
        c.putAll(children);
        _adapter =
                new ExpandableListAdapter(g, c);
        _listView = (ExpandableListView) _rootView.findViewById(R.id.lvExp);
        _listView.setAdapter(_adapter);

        //_listView.expandGroup(0);

        _listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                onItemClick(expandableListView.getExpandableListAdapter().getChild(groupPosition, childPosition));
                return true;
            }
        });
    }

    private void _expandAll() {
        int count = _adapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            _listView.expandGroup(i);
        }
    }

    private void _collapseAll() {
        int count = _adapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            _listView.collapseGroup(i);
        }
    }

    public void onItemClick(Object item) {
        _parentFragment.setCategoryItem((SpCategoryItem) item);
        dismiss();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;//onQueryTextChange(query);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (_adapter != null) {
            _adapter.filter(newText);
            return true;
        }
        return false;
    }

    @Override
    public void onSimpleAlertDialogPositiveClick(AlertDialog dialog, String tag) {
        if (tag.equals(SpConstants.FRAGMENT_TAG_CATEGORY)) {

            String name = ((TextView) dialog.findViewById(R.id.categoryName)).getText().toString();
            SpCategory c = new SpCategory(name, _sankalpType);
            ItemLoaderTask t = new ItemLoaderTask(ItemLoaderTask.OPERATION_ADD_CATEGORY, c);
            t.execute();
        } else if (tag.equals(SpConstants.FRAGMENT_TAG_ITEM)) {
            AppCompatSpinner spinner = (AppCompatSpinner) dialog.findViewById(R.id.categories_spinner);
            SpCategory cat = (SpCategory) spinner.getSelectedItem();
            String name = ((EditText) dialog.findViewById(R.id.itemName)).getText().toString();

            SpCategoryItem item = new SpCategoryItem(name, cat.getId());
            ItemLoaderTask t = new ItemLoaderTask(ItemLoaderTask.OPERATION_ADD_ITEM, item);
            t.execute();
        }
    }

    @Override
    public void onSimpleAlertDialogNegativeClick(AlertDialog dialog, String tag) {
    }

    private class ItemLoaderTask extends AsyncTask<Void, Void, Boolean> {

        static final int OPERATION_FETCH_ITEMS = 1;
        static final int OPERATION_ADD_CATEGORY = 2;
        static final int OPERATION_ADD_ITEM = 3;
        int operationID;
        Object param;

        private List<SpCategory> _groups = new ArrayList(); // header titles
        private HashMap<Integer, List<SpCategoryItem>> _children = new HashMap<>();

        ItemLoaderTask(int operationID, Object param) {
            this.operationID = operationID;
            this.param = param;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            SpContentProvider provider = SpContentProvider.getInstance(getContext());
            if (operationID == OPERATION_ADD_CATEGORY && param instanceof SpCategory) {
                provider.addCategory((SpCategory) param, null);
            } else if (operationID == OPERATION_ADD_ITEM && param instanceof SpCategoryItem) {
                provider.addCategoryItem((SpCategoryItem) param, null);
            } else if (operationID == OPERATION_FETCH_ITEMS && param instanceof Integer) {
                _groups = provider.getAllCategoriesBySankalpType((Integer) param);
                int i = 0;
                for (SpCategory c : _groups) {
                    _children.put(i++, provider.getAllCategoryItemsByCategoryId(c.getId()));
                }
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                if (operationID == OPERATION_ADD_CATEGORY || operationID == OPERATION_ADD_ITEM) {
                    _loadData();
                } else if (operationID == OPERATION_FETCH_ITEMS) {
                    if (_groups != null) {
                        _loadListView(_groups, _children);
                    }
                }
            }

        }
    }


    private class ExpandableListAdapter extends SpExpandableListAdapter {


        public ExpandableListAdapter(List groups, HashMap children) {
            super(getContext(), groups, children);
        }

        @Override
        public View getChildView(int groupPosition, final int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {

            final SpCategoryItem item = (SpCategoryItem) getChild(groupPosition, childPosition);
            ChildViewHolder viewHolder; // view lookup cache stored in tag
            if (convertView == null) {
                viewHolder = new ChildViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.expandable_list_child, parent, false);
                //viewHolder.icon = (ImageView) convertView.findViewById(R.id.add_icon);
                viewHolder.label = (TextView) convertView.findViewById(android.R.id.text1);
                // Cache the viewHolder object inside the fresh view
                convertView.setTag(viewHolder);
            } else {
                // View is being recycled, retrieve the viewHolder object from tag
                viewHolder = (ChildViewHolder) convertView.getTag();
            }

            // Populate the data into the template view using the data object
            String label = item.getCategoryItemDisplayName(getContext());
            viewHolder.label.setText(label);

            String letter = String.valueOf(label.toCharArray()[0]).toUpperCase();
            SpColorGenerator generator = SpColorGenerator.MATERIAL;
            SpTextDrawable drawable = SpTextDrawable.builder()
                    .buildRound(letter, generator.getRandomColor());
            //viewHolder.icon.setImageDrawable(drawable);
            // Return the completed view to render on screen
            return convertView;
        }


        public int getGroupLayout()
        {
            return R.layout.expandable_list_group;
        }

        @Override
        public void populateGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup parent) {
            SpCategory itemHeader = (SpCategory) getGroup(groupPosition);
            ImageView icon = (ImageView) view.findViewById(R.id.header_icon);
            icon.setImageDrawable(SpUtils.getIconDrawable(itemHeader, getContext()));

            TextView textTitle = (TextView) view.findViewById(R.id.headerTv);
            textTitle.setText(" " + itemHeader.getCategoryDisplayName(getContext()));
        }

        public void filter(String query) {

            HashMap children = new HashMap<>();
            List<Object> groups = new ArrayList();

            if (TextUtils.isEmpty(query)) {
                groups.addAll(_originalGroups);
                children.putAll(_originalChildren);
            } else {

                for (int i = 0; i < getGroups().size();i++) {

                    SpCategory category = (SpCategory) getGroups().get(i);
                    ArrayList<Object> filterItemsList = new ArrayList<Object>();
                    List<Object> l = getChildren().get(i);

                    if (category.getCategoryName().toLowerCase().contains(query.toLowerCase())) {
                        filterItemsList.addAll(l);
                    } else {
                        for (Object item : l) {
                            if (((SpCategoryItem)item).getCategoryItemName().toLowerCase().contains(query.toLowerCase())) {
                                filterItemsList.add(item);
                            }
                        }
                    }

                    if (filterItemsList.size() > 0) {
                        groups.add(category);
                        children.put(i, filterItemsList);
                    }
                }
            }
            reloadData(groups, children);
//            _expandAll();
        }

        private void reloadData(List<Object> groups, HashMap<Integer, List<Object>> children) {
            getChildren().clear();
            getGroups().clear();
            setGroups(groups);
            setChildren(children);
            notifyDataSetChanged();
        }

        private class ChildViewHolder {
            ImageView icon;
            TextView label;
        }
    }
}
