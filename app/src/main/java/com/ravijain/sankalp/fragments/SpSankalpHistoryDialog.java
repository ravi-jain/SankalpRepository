package com.ravijain.sankalp.fragments;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ravijain.sankalp.R;
import com.ravijain.sankalp.data.SpContentProvider;
import com.ravijain.sankalp.data.SpExceptionOrTarget;
import com.ravijain.sankalp.data.SpSankalp;
import com.ravijain.sankalp.data.SpTableContract;
import com.ravijain.sankalp.support.SpConstants;
import com.ravijain.sankalp.support.SpDateUtils;
import com.ravijain.sankalp.support.SpExpandableListAdapter;
import com.ravijain.sankalp.support.SpUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ravijain on 2/26/2017.
 */
public class SpSankalpHistoryDialog extends SpSimpleAlertDialog {

    private SpSankalp _sankalp;

    public void setSankalp(SpSankalp s) {
        _sankalp = s;
    }

    @Override
    public void configureView(View view) {
        ExpandableListView lvItems = (ExpandableListView) view.findViewById(R.id.hisotry_lvExp);
        // Setup cursor adapter using cursor from last step
        ArrayList<SpExceptionOrTarget> entries = SpContentProvider.getInstance(getContext()).getExTarEntries(_sankalp);
        ArrayList groups = new ArrayList();
        HashMap childrenMap = new HashMap<Integer, List>();
        SpExceptionOrTarget exTar = _sankalp.getExceptionOrTarget();

        if (SpExceptionOrTarget.EXCEPTION_OR_TARGET_TOTAL == exTar.getId()) {
            for (SpExceptionOrTarget s : entries) {
                GroupViewHolder holder = new GroupViewHolder();
                int count = s.getExceptionOrTargetCountCurrent();
                int icon = R.drawable.ic_more_horiz_black_24dp;
                int colorFilter = R.color.orange_primary;
                holder.count = count;
                holder.updatedOn = SpDateUtils.getFriendlyDateString(s.getLastUpdatedOn());
                holder.period = holder.updatedOn;

                if (_sankalp.getSankalpType() == SpConstants.SANKALP_TYPE_NIYAM) {
                    if (count >= exTar.getExceptionOrTargetCount()) {
                        icon = R.drawable.ic_check_circle_black_24dp;
                        colorFilter = R.color.green_primary;
                    } else if (SpUtils.isSankalpPast(_sankalp)) {
                        icon = R.drawable.ic_remove_circle_black_24dp;
                        colorFilter = R.color.red_primary;
                    }
                } else {
                    if (count >= exTar.getExceptionOrTargetCount()) {
                        icon = R.drawable.ic_remove_circle_black_24dp;
                        colorFilter = R.color.red_primary;
                    } else if (SpUtils.isSankalpPast(_sankalp)) {
                        icon = R.drawable.ic_check_circle_black_24dp;
                        colorFilter = R.color.green_primary;
                    }
                }
                holder.icon = icon;
                holder.colorFilter = colorFilter;
                groups.add(holder);
            }
        }
        else {
            String currentPeriod = null;
            for (SpExceptionOrTarget s : entries) {

                int count = s.getExceptionOrTargetCountCurrent();

                int icon = R.drawable.ic_more_horiz_black_24dp;
                int colorFilter = R.color.orange_primary;
                if (!SpDateUtils.isSamePeriod(s.getLastUpdatedOn(), currentPeriod, exTar.getId())) {
                    // Children


                    GroupViewHolder holder = new GroupViewHolder();
                    holder.count = count;
                    // groups
                    holder.updatedOn = SpDateUtils.getPeriodString(s.getLastUpdatedOn(), exTar.getId());
                    holder.period = holder.updatedOn;
                    currentPeriod = holder.period;
                    if (_sankalp.getSankalpType() == SpConstants.SANKALP_TYPE_NIYAM) {
                        if (count >= exTar.getExceptionOrTargetCount()) {
                            icon = R.drawable.ic_check_circle_black_24dp;
                            colorFilter = R.color.green_primary;
                        } else if (!SpDateUtils.isSamePeriod(new Date().getTime(), currentPeriod, exTar.getId())) {
                            icon = R.drawable.ic_remove_circle_black_24dp;
                            colorFilter = R.color.red_primary;
                        }
                    } else {
                        if (count >= exTar.getExceptionOrTargetCount()) {
                            icon = R.drawable.ic_remove_circle_black_24dp;
                            colorFilter = R.color.red_primary;
                        } else if (!SpDateUtils.isSamePeriod(new Date().getTime(), currentPeriod, exTar.getId())) {
                            icon = R.drawable.ic_check_circle_black_24dp;
                            colorFilter = R.color.green_primary;
                        }
                    }
                    holder.icon = icon;
                    holder.colorFilter = colorFilter;
                    groups.add(holder);
                    childrenMap.put(groups.size() - 1, new ArrayList());
                }
                GroupViewHolder h1 = new GroupViewHolder();
                h1.count = count;
                h1.updatedOn = SpDateUtils.getFriendlyDateString(s.getLastUpdatedOn());
                ((List)childrenMap.get(groups.size() - 1)).add(h1);
            }
        }

        HistoryCursorAdapter todoAdapter = new HistoryCursorAdapter(getContext(), groups, childrenMap);
        // Attach cursor adapter to the ListView
        lvItems.setAdapter(todoAdapter);
    }

    private class GroupViewHolder {
        int icon, colorFilter, count;
        String updatedOn, period;
    }

    public class HistoryCursorAdapter extends SpExpandableListAdapter {

        public HistoryCursorAdapter(Context context, ArrayList groups, HashMap children) {
            super(context, groups, children);
        }

        @Override
        public int getGroupLayout() {
            return R.layout.history_group_list_item_layout;
        }

        @Override
        public void populateGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup parent) {
            GroupViewHolder holder = (GroupViewHolder) getGroup(groupPosition);
            TextView tvPeriod = (TextView) view.findViewById(R.id.period);
            tvPeriod.setText(holder.period);
            TextView tvCount = (TextView) view.findViewById(R.id.count);
            tvCount.setText(String.valueOf(holder.count));
            ImageView iconView = (ImageView) view.findViewById(R.id.status_icon);
            iconView.setImageDrawable(getResources().getDrawable(holder.icon));
            iconView.setColorFilter(getContext().getResources().getColor(holder.colorFilter));
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            final GroupViewHolder item = (GroupViewHolder) getChild(groupPosition, childPosition);
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.history_child_list_item_layout, parent, false);
            }

            // Populate the data into the template view using the data object
            TextView tvPeriod = (TextView) convertView.findViewById(R.id.updatedOn);
            tvPeriod.setText(item.updatedOn);
            TextView tvCount = (TextView) convertView.findViewById(R.id.count);
            tvCount.setText(String.valueOf(item.count));

            // Return the completed view to render on screen
            return convertView;
        }
    }
}