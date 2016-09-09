package com.ravijain.sankalp.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ravijain.sankalp.R;
import com.ravijain.sankalp.activities.SpConstants;
import com.ravijain.sankalp.data.SpCategory;
import com.ravijain.sankalp.data.SpCategoryItem;
import com.ravijain.sankalp.data.SpContentProvider;
import com.ravijain.sankalp.support.SpColorGenerator;
import com.ravijain.sankalp.support.SpTextDrawable;

import java.util.ArrayList;

/**
 * Created by ravijain on 9/8/2016.
 */
public class SpSimpleDialog extends DialogFragment implements AdapterView.OnItemClickListener {

    public static final int SIMPLE_DIALOG_ITEM_TYPE_CATEGORY = 1;
    public static final int SIMPLE_DIALOG_ITEM_TYPE_ITEM = 2;

    private int _itemType, _itemTypeID;
    private View _rootView;

    private SpAddSankalpFragment _parentFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _rootView = inflater.inflate(R.layout.fragment_simple_dialog, container, false);

        Bundle b = getArguments();

        if (b != null) {
            _itemType = b.getInt(SpConstants.INTENT_KEY_SIMPLE_DIALOG_ITEM_TYPE);
            TextView titleView = (TextView) _rootView.findViewById(R.id.simple_dialog_title);
            if (_itemType == SIMPLE_DIALOG_ITEM_TYPE_CATEGORY) {
                titleView.setText(getString(R.string.selectCategoryTitle));
            }
            else if (_itemType == SIMPLE_DIALOG_ITEM_TYPE_ITEM) {
                titleView.setText(getString(R.string.selectItemTitle));
            }

            _itemTypeID = b.getInt(SpConstants.INTENT_KEY_SIMPLE_DIALOG_ITEM_TYPE_ID);
            ItemLoaderTask t = new ItemLoaderTask();
            t.execute();
        }
        return _rootView;
    }

    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
    public void setParentFragment(SpAddSankalpFragment parentFragment) {
        this._parentFragment = parentFragment;
    }


    private void _loadListView(ArrayList items)
    {
        SimpleDialogAdapter itemsAdapter =
                new SimpleDialogAdapter(getContext(), items);
        ListView lv = (ListView)_rootView.findViewById(R.id.simple_dialog_list_view);
        lv.setAdapter(itemsAdapter);
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lv.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Object item = adapterView.getItemAtPosition(i);
        if (item instanceof SpCategory) {
            _parentFragment.setCategory((SpCategory)item);
        }
        else if (item instanceof SpCategoryItem){
            _parentFragment.setCategoryItem((SpCategoryItem)item);
        }
        dismiss();
    }

    private class ItemLoaderTask extends AsyncTask<Void, Void, Boolean> {

        private ArrayList items;
        @Override
        protected Boolean doInBackground(Void... params) {
            if (_itemType == SIMPLE_DIALOG_ITEM_TYPE_CATEGORY) {
                items = SpContentProvider.getInstance(getContext()).getAllCategoriesBySankalpType(_itemTypeID);
            }
            else if (_itemType == SIMPLE_DIALOG_ITEM_TYPE_ITEM) {
                items = SpContentProvider.getInstance(getContext()).getAllCategoryItemsByCategoryId(_itemTypeID);
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (items != null) _loadListView(items);
        }
    }

    private class SimpleDialogAdapter extends ArrayAdapter {
        // View lookup cache
        private class ViewHolder {
            ImageView icon;
            TextView label;
        }

        public SimpleDialogAdapter(Context context, ArrayList items) {
            super(context, R.layout.icon_one_line_list_item_layout, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Object item = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            ViewHolder viewHolder; // view lookup cache stored in tag
            if (convertView == null) {
                // If there's no view to re-use, inflate a brand new view for row
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.icon_one_line_list_item_layout, parent, false);
                viewHolder.icon = (ImageView) convertView.findViewById(R.id.add_icon);
                viewHolder.label = (TextView) convertView.findViewById(R.id.add_label);
                // Cache the viewHolder object inside the fresh view
                convertView.setTag(viewHolder);
            } else {
                // View is being recycled, retrieve the viewHolder object from tag
                viewHolder = (ViewHolder) convertView.getTag();
            }
            // Populate the data into the template view using the data object
            String label = item instanceof SpCategory ? ((SpCategory)item).getCategoryDisplayName() : ((SpCategoryItem)item).getCategoryItemDisplayName();
            viewHolder.label.setText(label);

            String letter = String.valueOf(label.toCharArray()[0]).toUpperCase();
            SpColorGenerator generator = SpColorGenerator.MATERIAL;
            SpTextDrawable drawable = SpTextDrawable.builder()
                    .buildRound(letter, generator.getRandomColor());
            viewHolder.icon.setImageDrawable(drawable);
            // Return the completed view to render on screen
            return convertView;
        }
    }
}
