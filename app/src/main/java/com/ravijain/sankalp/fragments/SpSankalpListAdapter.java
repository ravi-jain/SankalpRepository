package com.ravijain.sankalp.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ravijain.sankalp.R;
import com.ravijain.sankalp.support.SpConstants;
import com.ravijain.sankalp.data.SpCategory;
import com.ravijain.sankalp.data.SpCategoryItem;
import com.ravijain.sankalp.data.SpContentProvider;
import com.ravijain.sankalp.support.SpDateUtils;
import com.ravijain.sankalp.data.SpExceptionOrTarget;
import com.ravijain.sankalp.data.SpSankalp;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ravijain on 7/31/2016.
 */
public class SpSankalpListAdapter extends ArrayAdapter<SpSankalp>{

    private ArrayList<SpSankalp> _sankalps;
    public SpSankalpListAdapter(Context context, ArrayList<SpSankalp> sankalps) {
        super(context, 0, sankalps);
        _sankalps = sankalps;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        SpSankalp sankalp = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_dashboard_material, parent, false);
        }


        // Lookup view for data population
        TextView tvItem = (TextView) convertView.findViewById(R.id.list_item_item_textview);
        TextView tvPeriod = (TextView) convertView.findViewById(R.id.list_item_period_textview);
        // Populate the data into the template view using the data object
        int itemId = sankalp.getItemId();
        int categoryId = sankalp.getCategoryID();
        SpContentProvider provider = SpContentProvider.getInstance(getContext());
        SpCategory cat = provider.getAllCategories().get(categoryId);
        SpCategoryItem item = provider.getAllCategoryItems().get(itemId);
        int isLifetime = sankalp.isLifetime();

        String period;
        if (isLifetime == SpConstants.SANKALP_IS_LIFTIME_TRUE) {
            period = getContext().getString(R.string.Lifetime);
        }
        else {
            Date fromDate = sankalp.getFromDate();
            Date toDate = sankalp.getToDate();

            period = SpDateUtils.getFriendlyPeriodString(fromDate, toDate, true);
        }


        // Populate fields with extracted properties
//            tvCategory.setText(category.getCategoryName());
        tvItem.setText(item.getCategoryItemDisplayName());
        tvPeriod.setText(String.valueOf(period));

        SpExceptionOrTarget exceptionOrTarget = sankalp.getExceptionOrTarget();
        if (exceptionOrTarget.getId() == SpExceptionOrTarget.EXCEPTION_OR_TARGET_UNDEFINED) {
            View exceptionOrTargetContainer = convertView.findViewById(R.id.exceptionOrTarget_rl_container);
            exceptionOrTargetContainer.setVisibility(View.GONE);
        }
        else {
            TextView title = (TextView) convertView.findViewById(R.id.exceptionOrTarget_li_title);
            TextView currentCountLabel = (TextView) convertView.findViewById(R.id.exceptionOrTargetCurrentCount_li_label);

            if (sankalp.getSankalpType() == SpConstants.SANKALP_TYPE_TYAG) {
                title.setText(R.string.tyagExceptions);
                currentCountLabel.setText(R.string.exception_left_label);
                //convertView.findViewById(R.id.listBarNiyam).setVisibility(View.GONE);
                //convertView.findViewById(R.id.listBar).setVisibility(View.VISIBLE);
                convertView.findViewById(R.id.listBar).setBackgroundColor(getContext().getResources().getColor(R.color.sankalp_tyag));
            }
            else if (sankalp.getSankalpType() == SpConstants.SANKALP_TYPE_NIYAM) {
//                title.setText(R.string.niyamFrequency);
                title.setText(R.string.niyamFrequency);
                currentCountLabel.setText(R.string.frequency_done_label);
//                convertView.findViewById(R.id.listBar).setVisibility(View.GONE);
//                convertView.findViewById(R.id.listBarNiyam).setVisibility(View.VISIBLE);
                convertView.findViewById(R.id.listBar).setBackgroundColor(getContext().getResources().getColor(R.color.sankalp_niyam));
                //View listBar = convertView.findViewById(R.id.listBar);
//                listBarNiyam.setVisibility(View.VISIBLE);
//                convertView.findViewById(R.id.listBar).setVisibility(View.GONE);
                //listBar.setBackgroundColor(getContext().getResources().getColor(R.color.sankalp_niyam));
            }
//            TextView spinnerLabel = (TextView) convertView.findViewById(R.id.exceptionOrTarget_spinner_li_label);
//            spinnerLabel.setText(exceptionOrTarget.getLabel());
            TextView count = (TextView) convertView.findViewById(R.id.exceptionOrTargetCount_li_textView);
            count.setText(exceptionOrTarget.getRepresentationalSummary());

            if (exceptionOrTarget.getExceptionOrTargetCount() > 0) {
                convertView.findViewById(R.id.exceptionOrTargetCurrent_li_container).setVisibility(View.VISIBLE);
                TextView currentCount = (TextView) convertView.findViewById(R.id.exceptionOrTargetCurrentCount_li_tv);
                currentCount.setText(String.valueOf(exceptionOrTarget.getExceptionOrTargetCountCurrent()));

            }
            else {
                convertView.findViewById(R.id.exceptionOrTargetCurrent_li_container).setVisibility(View.GONE);
            }
        }

        // Return the completed view to render on screen
        return convertView;
    }

    public void search(String query) {
//        getFilter().filter(query);
        clear();
        addAll(_getFilteredList(query));
    }

    public void filter(int sankalpType, int listFilter)
    {
        ArrayList<SpSankalp> filteredList = new ArrayList<SpSankalp>();
        for (int i = 0; i < _sankalps.size(); i++) {
            SpSankalp s = _sankalps.get(i);
            if (sankalpType == SpConstants.SANKALP_TYPE_BOTH || s.getSankalpType() == sankalpType) {
                if (listFilter == -1 || listFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_ALL ||
                        (listFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_CURRENT &&
                                SpDateUtils.isCurrentDate(s.getFromDate(), s.getToDate())) ||
                        (listFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_UPCOMING && SpDateUtils.isUpcomingDate(s.getFromDate()))) {
                    filteredList.add(s);
                }
            }
        }
        clear();
        addAll(filteredList);
    }

    private ArrayList<SpSankalp> _getFilteredList(String query)
    {
        if (query == null || query.length() == 0) {
            return _sankalps;
        }
        else {
            ArrayList<SpSankalp> filteredList = new ArrayList<SpSankalp>();
            for (int i = 0; i < _sankalps.size(); i++) {
                SpSankalp s = _sankalps.get(i);
                if (s.isMatch(query)) {
                    filteredList.add(s);
                }
            }
            return filteredList;
        }
    }

    public void clearAdapter() {
        _sankalps = null;
        clear();
    }

    public void addItems(ArrayList<SpSankalp> sankalps) {
        _sankalps = sankalps;
        addAll(sankalps);
    }

    public void removeItems(ArrayList<SpSankalp> sankalpsToBeDeleted)
    {
        for (SpSankalp s: sankalpsToBeDeleted) {
            remove(s);
            _sankalps.remove(s);
        }
    }
}
