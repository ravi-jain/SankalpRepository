package com.ravijain.sankalp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
    private int _sortOrder;

    protected boolean hasOkButton() {
        return false;
    }

    @Override
    public void configureView(View view) {

        Bundle b = getArguments();
        if (b != null) {
            _sortId = b.getInt(SpConstants.INTENT_SORTID);
            _sortOrder = b.getInt(SpConstants.INTENT_SORT_ORDER);
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
                TextView tv = (TextView) view.findViewById(R.id.add_label);
                String option = tv.getText().toString();
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
                        ((SpSankalpListFragment) f).sortList(sortId);
                    }
                }

            }
        });

    }

    private class SortViewListAdapter extends ArrayAdapter<String> {

        String[] _options;

        public SortViewListAdapter(Context context, String[] options) {
            super(context, 0, options);
            _options = options;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.icon_right_one_line_list_item_layout, parent, false);
            }

            TextView tv = (TextView) convertView.findViewById(R.id.add_label);
            tv.setText(_options[position]);
            ImageView icon = (ImageView) convertView.findViewById(R.id.add_icon);

            if (getResources().getString(_sortId).equals(tv.getText().toString())) {
                tv.setTextColor(SpUtils.getPrimaryColor(getContext()));
                int drawable = _sortOrder == SpConstants.SORT_ORDER_ASCENDING ? R.drawable.ic_arrow_upward_black_24dp : R.drawable.ic_arrow_downward_black_24dp;
                icon.setImageDrawable(getResources().getDrawable(drawable));
                icon.setVisibility(View.VISIBLE);
                icon.setColorFilter(SpUtils.getPrimaryColor(getContext()));
            } else {
                tv.setTextColor(getResources().getColor(R.color.black));
                icon.setVisibility(View.GONE);
            }

            return convertView;
        }
    }
}
