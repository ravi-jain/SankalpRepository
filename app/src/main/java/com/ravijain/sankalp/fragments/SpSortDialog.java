package com.ravijain.sankalp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ravijain.sankalp.R;
import com.ravijain.sankalp.support.SpConstants;
import com.ravijain.sankalp.support.SpUtils;

/**
 * Created by ravijain on 10/7/2016.
 */
public class SpSortDialog extends SpSimpleAlertDialog {

    private int _sortId;

    @Override
    public void configureView(View view) {

        Bundle b = getArguments();
        if (b != null) {
            _sortId = b.getInt(SpConstants.INTENT_SORTID);
        }

        ListView lv = (ListView) view.findViewById(R.id.sortLVId);
        String[] options = new String[]{getResources().getString(R.string.endDate),
                getResources().getString(R.string.startDate),
                getResources().getString(R.string.creationDate),
                getResources().getString(R.string.itemName)};
        lv.setAdapter(new SortViewListAdapter(getContext(), options));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // selected item
                String option = ((TextView) view).getText().toString();
                int sortId = -1;
                if (option.equals(getResources().getString(R.string.endDate))) {
                    sortId = R.string.endDate;

                } else if (option.equals(getResources().getString(R.string.startDate))) {
                    sortId = R.string.startDate;
                } else if (option.equals(getResources().getString(R.string.creationDate))) {
                    sortId = R.string.creationDate;
                } else if (option.equals(getResources().getString(R.string.itemName))) {
                    sortId = R.string.itemName;
                }
                if (sortId > -1) {
                    Fragment f = getTargetFragment();
                    if (f != null && f instanceof SpSankalpListFragment) {
                        dismiss();
                        ((SpSankalpListFragment)f).sortList(sortId);
                    }
                }

            }
        });

    }

    private class SortViewListAdapter extends ArrayAdapter<String> {
        public SortViewListAdapter(Context context, String[] options) {
            super(context, android.R.layout.simple_list_item_1, options);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = super.getView(position, convertView, parent);

            if (getResources().getString(_sortId).equals(((TextView) convertView).getText().toString())) {
                ((TextView) convertView).setTextColor(SpUtils.getPrimaryColor(getContext()));
            } else {
                ((TextView) convertView).setTextColor(getResources().getColor(R.color.black));
            }

            return convertView;
        }
    }
}
