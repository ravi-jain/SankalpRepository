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
import android.widget.TextView;

import com.ravijain.sankalp.R;
import com.ravijain.sankalp.support.SpConstants;
import com.ravijain.sankalp.support.SpUtils;

/**
 * Created by ravijain on 8/2/2016.
 */
public class SpFilterDialog extends SpSimpleAlertDialog {

    private int _sankalpType;

    protected boolean hasOkButton() {
        return false;
    }

    private boolean _isVisible(int listFilter) {
        return listFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_TOMORROW ||
                listFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_MONTH ||
                listFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_YEAR;
    }

//    public void configureView(View view) {
//        Bundle b = getArguments();
//        if (b != null) {
//            int listFilter = b.getInt(SpConstants.INTENT_KEY_SANKALP_LIST_FILTER);
//            if (_isVisible(listFilter)) {
//                view.findViewById(R.id.filterDialog_periodView).setVisibility(View.VISIBLE);
//            }
//        }
//    }

    @Override
    public void configureView(View view) {

        Bundle b = getArguments();
        if (b != null) {
            _sankalpType = b.getInt(SpConstants.INTENT_KEY_SANKALP_TYPE);
        }

        ListView lv = (ListView) view.findViewById(R.id.filterLVId);
        String[] options = new String[]{
                getResources().getString(R.string.tyag),
                getResources().getString(R.string.niyam),
                getResources().getString(R.string.all)};
        lv.setAdapter(new FilterViewListAdapter(getContext(), options));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // selected item
                if (position != _sankalpType) {
                    Fragment f = getTargetFragment();
                    if (f != null && f instanceof SpSankalpListFragment) {
                        dismiss();
                        ((SpSankalpListFragment) f).filterList(position);
                    }
                }

            }
        });

    }

    private class FilterViewListAdapter extends ArrayAdapter<String> {

        String[] _options;

        public FilterViewListAdapter(Context context, String[] options) {
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

            if (position == _sankalpType) {
                tv.setTextColor(SpUtils.getPrimaryColor(getContext()));
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

