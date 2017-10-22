package com.ravijain.sankalp.fragments;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ravijain.sankalp.R;
import com.ravijain.sankalp.activities.SpSankalpList;
import com.ravijain.sankalp.data.SpSankalp;
import com.ravijain.sankalp.support.SpConstants;
import com.ravijain.sankalp.support.SpUtils;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ravijain on 5/22/2017.
 */
public class SpSwitchViewDialog extends SpSimpleAlertDialog {

    private int _listFilter;

    protected boolean hasOkButton() {
        return false;
    }

    public void configureView(View view) {
        // Do Nothing
        ListView lv = (ListView) view.findViewById(R.id.switchViewLVId);
        String[] options = new String[]{getResources().getString(R.string.status),
                getResources().getString(R.string.year),
                getResources().getString(R.string.month),
                getResources().getString(R.string.Day)};
        lv.setAdapter(new SwitchViewListAdapter(getContext(), options));

        _listFilter = getArguments().getInt(SpConstants.INTENT_KEY_SANKALP_LIST_FILTER, SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_ALL);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // selected item
                TextView tv = (TextView) view.findViewById(R.id.add_label);
                String option = tv.getText().toString();
                int listFilter = -1;
                if (option.equals(getResources().getString(R.string.status))) {
                    listFilter = SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_CURRENT;

                } else if (option.equals(getResources().getString(R.string.year))) {
                    listFilter = SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_YEAR;
                } else if (option.equals(getResources().getString(R.string.month))) {
                    listFilter = SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_MONTH;
                } else if (option.equals(getResources().getString(R.string.Day))) {
                    listFilter = SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_DAY;
                }
                if (listFilter > -1 && _listFilter != listFilter) {
                    if (_listFilter <= SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_ALL && listFilter <= SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_ALL) {

                    } else {
                        dismiss();
                        _launchSankalpList(listFilter);
                    }

                }

            }
        });
    }

    private void _launchSankalpList(int listFilter) {
        Intent intent = new Intent(getContext(), SpSankalpList.class);
        intent.putExtra(SpConstants.INTENT_KEY_SANKALP_TYPE, SpConstants.SANKALP_TYPE_BOTH);
        intent.putExtra(SpConstants.INTENT_KEY_SANKALP_LIST_FILTER, listFilter);
        intent.putExtra(SpConstants.INTENT_KEY_SANKALP_LIST_FILTER_DATE_VALUE, new Date().getTime());
        startActivity(intent);
    }

    private class SwitchViewListAdapter extends ArrayAdapter<String> {
        String[] _options;
        public SwitchViewListAdapter(Context context, String[] options) {
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
            if ((position == 0 && _listFilter <= SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_ALL) ||
                    (_listFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_YEAR && position == 1) ||
                    (_listFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_MONTH && position == 2) ||
                    (_listFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_DAY && position == 3)) {
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
