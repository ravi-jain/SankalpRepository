package com.ravijain.sankalp.fragments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ravijain.sankalp.R;
import com.ravijain.sankalp.data.SpCategoryItem;
import com.ravijain.sankalp.data.SpContentProvider;
import com.ravijain.sankalp.data.SpExceptionOrTarget;
import com.ravijain.sankalp.data.SpSankalp;
import com.ravijain.sankalp.support.SpConstants;
import com.ravijain.sankalp.support.SpDateUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by ravijain on 1/6/2017.
 */
public class SpSankalpListRecyclerAdapter extends RecyclerView.Adapter<SpSankalpListRecyclerAdapter.SankalpListViewHolder> {

    private ArrayList<SpSankalp> _currentSankalps;
    private ArrayList<SpSankalp> _originalSankalps;
    private Context _context;
    private RecyclerViewClickListener _clickListener;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    public interface RecyclerViewClickListener {
        void onItemClicked(int position);
        boolean onItemLongClicked(int position);
    }

    public SpSankalpListRecyclerAdapter(Context context, ArrayList<SpSankalp> sankalpList, RecyclerViewClickListener listener)  {
        _originalSankalps = new ArrayList<SpSankalp>();
        loadAdapter(sankalpList);

        _context = context;
        _clickListener = listener;
    }

    public class SankalpListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener{

        public TextView tvItem, tvPeriod, title, currentCountLabel, count, currentCount;
        public View exceptionOrTargetContainer, listBar, container, rlContainer, selectedOverlay;
        public SankalpListViewHolder(View itemView) {
            super(itemView);
            tvItem = (TextView) itemView.findViewById(R.id.list_item_item_textview);
            tvPeriod = (TextView) itemView.findViewById(R.id.list_item_period_textview);
            exceptionOrTargetContainer = itemView.findViewById(R.id.exceptionOrTarget_rl_container);
            title = (TextView) itemView.findViewById(R.id.exceptionOrTarget_li_title);
            currentCountLabel = (TextView) itemView.findViewById(R.id.exceptionOrTargetCurrentCount_li_label);
            listBar = itemView.findViewById(R.id.listBar);
            count = (TextView) itemView.findViewById(R.id.exceptionOrTargetCount_li_textView);
            currentCount = (TextView) itemView.findViewById(R.id.exceptionOrTargetCurrentCount_li_tv);
            container = itemView.findViewById(R.id.exceptionOrTargetCurrent_li_container);
            rlContainer = itemView.findViewById(R.id.exceptionOrTarget_rl_container);
            //selectedOverlay = itemView.findViewById(R.id.selected_overlay);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            _clickListener.onItemClicked(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            return _clickListener.onItemLongClicked(getAdapterPosition());
        }
    }

    public SpSankalp getSankalpByPosition(int position)
    {
        return _currentSankalps.get(position);
    }

    @Override
    public SpSankalpListRecyclerAdapter.SankalpListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_dashboard_material, parent, false);

        return new SankalpListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SpSankalpListRecyclerAdapter.SankalpListViewHolder holder, int position) {
        SpSankalp sankalp = _currentSankalps.get(position);

        // Populate the data into the template view using the data object
        int itemId = sankalp.getItemId();
        int categoryId = sankalp.getCategoryID();
        SpContentProvider provider = SpContentProvider.getInstance(_context);
        SpCategoryItem item = provider.getAllCategoryItems().get(itemId);
        int isLifetime = sankalp.isLifetime();

        String period;
        if (isLifetime == SpConstants.SANKALP_IS_LIFTIME_TRUE) {
            period = _context.getString(R.string.Lifetime);
        }
        else {
            Date fromDate = sankalp.getFromDate();
            Date toDate = sankalp.getToDate();

            period = SpDateUtils.getFriendlyPeriodString(fromDate, toDate, true);
        }


        // Populate fields with extracted properties
        holder.tvItem.setText(item.getCategoryItemDisplayName(_context));
        holder.tvPeriod.setText(String.valueOf(period));

        SpExceptionOrTarget exceptionOrTarget = sankalp.getExceptionOrTarget();
        if (exceptionOrTarget.getId() == SpExceptionOrTarget.EXCEPTION_OR_TARGET_UNDEFINED) {
            holder.rlContainer.setVisibility(View.GONE);
        }
        else {

            if (sankalp.getSankalpType() == SpConstants.SANKALP_TYPE_TYAG) {
                holder.title.setText(R.string.tyagExceptions);
                holder.currentCountLabel.setText(R.string.exception_left_label);
                holder.listBar.setBackgroundColor(_context.getResources().getColor(R.color.sankalp_tyag));
            }
            else if (sankalp.getSankalpType() == SpConstants.SANKALP_TYPE_NIYAM) {
                holder.title.setText(R.string.niyamFrequency);
                holder.currentCountLabel.setText(R.string.frequency_done_label);
                holder.listBar.setBackgroundColor(_context.getResources().getColor(R.color.sankalp_niyam));
            }
            holder.count.setText(exceptionOrTarget.getRepresentationalSummary());

            if (exceptionOrTarget.getExceptionOrTargetCount() > 0) {
                holder.container.setVisibility(View.VISIBLE);
                holder.currentCount.setText(String.valueOf(exceptionOrTarget.getExceptionOrTargetCountCurrent()));

            }
            else {
                holder.container.setVisibility(View.GONE);
            }
        }

        holder.itemView.setBackgroundColor(isSelected(position) ? _context.getResources().getColor(R.color.selected_overlay) : _context.getResources().getColor(R.color.white));

    }

    @Override
    public int getItemCount() {
        return _currentSankalps.size();
    }

    public void clearAdapter() {
        _currentSankalps = null;
    }

    public void loadAdapter(ArrayList<SpSankalp> sankalps) {
        _currentSankalps = sankalps;
        _originalSankalps.addAll(_currentSankalps);
    }

    public void sortList(final int sortId, final int sortOrder) {

        Collections.sort(_currentSankalps, new Comparator<SpSankalp>() {
            @Override
            public int compare(SpSankalp spSankalp, SpSankalp t1) {
                return spSankalp.compareTo(sortId, t1) * sortOrder;
            }
        });
        notifyDataSetChanged();
    }

    public void filter(int sankalpType, int listFilter)
    {
        ArrayList<SpSankalp> filteredList = new ArrayList<SpSankalp>();
        for (int i = 0; i < _originalSankalps.size(); i++) {
            SpSankalp s = _originalSankalps.get(i);
            if (sankalpType == SpConstants.SANKALP_TYPE_BOTH || s.getSankalpType() == sankalpType) {
                if (listFilter == -1 || listFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_ALL ||
                        (listFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_CURRENT &&
                                SpDateUtils.isCurrentDate(s.getFromDate(), s.getToDate())) ||
                        (listFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_UPCOMING && SpDateUtils.isUpcomingDate(s.getFromDate()))) {
                    filteredList.add(s);
                }
            }
        }
        clearAdapter();
        _currentSankalps = filteredList;
        notifyDataSetChanged();
    }

    public void search(String query) {
//        getFilter().filter(query);
        clearAdapter();
        _currentSankalps = _getFilteredList(query);
        notifyDataSetChanged();
    }

    private ArrayList<SpSankalp> _getFilteredList(String query)
    {
        if (query == null || query.length() == 0) {
            return _originalSankalps;
        }
        else {
            ArrayList<SpSankalp> filteredList = new ArrayList<SpSankalp>();
            for (int i = 0; i < _originalSankalps.size(); i++) {
                SpSankalp s = _originalSankalps.get(i);
                if (s.isMatch(query)) {
                    filteredList.add(s);
                }
            }
            return filteredList;
        }
    }

    public ArrayList<SpSankalp> getSelectedSankalps() {
        ArrayList<SpSankalp> items = new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); ++i) {
            items.add(_currentSankalps.get(selectedItems.keyAt(i)));
        }
        return items;
    }

    public void removeItem(int position) {
       _remove(position);
        notifyItemRemoved(position);
    }

    private void _remove(int position)
    {
        SpSankalp s = _currentSankalps.get(position);
        _currentSankalps.remove(s);
        _originalSankalps.remove(s);
    }

    public void removeItems(List<Integer> positions) {
        // Reverse-sort the list
        Collections.sort(positions, new Comparator<Integer>() {
            @Override
            public int compare(Integer lhs, Integer rhs) {
                return rhs - lhs;
            }
        });

        // Split the list in ranges
        while (!positions.isEmpty()) {
            if (positions.size() == 1) {
                removeItem(positions.get(0));
                positions.remove(0);
            } else {
                int count = 1;
                while (positions.size() > count && positions.get(count).equals(positions.get(count - 1) - 1)) {
                    ++count;
                }

                if (count == 1) {
                    removeItem(positions.get(0));
                } else {
                    removeRange(positions.get(count - 1), count);
                }

                for (int i = 0; i < count; ++i) {
                    positions.remove(0);
                }
            }
        }
    }

    private void removeRange(int positionStart, int itemCount) {
        for (int i = 0; i < itemCount; ++i) {
            _remove(positionStart);
        }
        notifyItemRangeRemoved(positionStart, itemCount);
    }

    /**
     * Indicates if the item at position position is selected
     * @param position Position of the item to check
     * @return true if the item is selected, false otherwise
     */
    public boolean isSelected(int position) {
        return getSelectedItems().contains(position);
    }

    /**
     * Toggle the selection status of the item at a given position
     * @param position Position of the item to toggle the selection status for
     */
    public void toggleSelection(int position) {
        if (selectedItems.get(position, false)) {
            selectedItems.delete(position);
        } else {
            selectedItems.put(position, true);
        }
        notifyItemChanged(position);
    }

    /**
     * Clear the selection status for all items
     */
    public void clearSelection() {
        List<Integer> selection = getSelectedItems();
        selectedItems.clear();
        for (Integer i : selection) {
            notifyItemChanged(i);
        }
    }

    /**
     * Count the selected items
     * @return Selected items count
     */
    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    /**
     * Indicates the list of selected items
     * @return List of selected items ids
     */
    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); ++i) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }
}
