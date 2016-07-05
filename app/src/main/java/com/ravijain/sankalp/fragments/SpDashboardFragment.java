package com.ravijain.sankalp.fragments;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.ravijain.sankalp.R;
import com.ravijain.sankalp.activities.SpAddSankalpActivity;
import com.ravijain.sankalp.data.SpCategory;
import com.ravijain.sankalp.data.SpCategoryItem;
import com.ravijain.sankalp.data.SpDataConstants;
import com.ravijain.sankalp.data.SpDateUtils;
import com.ravijain.sankalp.data.SpTableContract;
import com.ravijain.sankalp.data.SpContentProvider;

import java.util.Hashtable;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpDashboardFragment extends Fragment {


    private ListViewAdapter tyagAdapter;
    private ListView tyagListView;
    private ListViewAdapter niyamAdapter;
    private ListView niyamListView;
    private CursorLoaderTask cursorLoaderTask;
    private Hashtable<Integer, SpCategory> _categories;
    private Hashtable<Integer, SpCategoryItem> _categoryItems;

    public SpDashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_sankalp_dashboard, container, false);

        tyagAdapter = new ListViewAdapter(getContext(), null, 0);
        tyagListView = (ListView) rootView.findViewById(R.id.tyagListview_dashboard);
        tyagListView.setAdapter(tyagAdapter);

        niyamAdapter = new ListViewAdapter(getContext(), null, 0);
        niyamListView = (ListView) rootView.findViewById(R.id.niyamListview_dashboard);
        niyamListView.setAdapter(niyamAdapter);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.addSankalpButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SpAddSankalpActivity.class);
                startActivity(intent);
            }
        });

        cursorLoaderTask = new CursorLoaderTask();
        cursorLoaderTask.execute((Void) null);


        return rootView;
    }

    private class ListViewAdapter extends CursorAdapter
    {
        ListViewAdapter(Context context, Cursor cursor, int flags) {
            super(context, cursor, 0);
        }

        // The newView method is used to inflate a new view and return it,
        // you don't bind any data to the view at this point.
        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.list_item_dashboard, parent, false);
        }

        // The bindView method is used to bind all data to a given view
        // such as setting the text on a TextView.
        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            // Find fields to populate in inflated template
            TextView tvCategory = (TextView) view.findViewById(R.id.list_item_category_textview);
            TextView tvItem = (TextView) view.findViewById(R.id.list_item_item_textview);
            TextView tvPeriod = (TextView) view.findViewById(R.id.list_item_period_textview);
            // Extract properties from cursor
            int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow(SpTableContract.SpSankalpTable.COLUMN_CATEGORY_ID));
            SpCategory category = _categories.get(categoryId);
            int itemId = cursor.getInt(cursor.getColumnIndexOrThrow(SpTableContract.SpSankalpTable.COLUMN_ITEM_ID));
            SpCategoryItem item = _categoryItems.get(itemId);
            int isLifetime = cursor.getInt(cursor.getColumnIndexOrThrow(SpTableContract.SpSankalpTable.COLUMN_ISLIFETIME));

            String period;
            if (isLifetime == SpDataConstants.SANKALP_IS_LIFTIME_TRUE) {
                period = getString(R.string.Lifetime);
            }
            else {
                long fromDate = cursor.getLong(cursor.getColumnIndexOrThrow(SpTableContract.SpSankalpTable.COLUMN_FROM_DATE));
                long toDate = cursor.getLong(cursor.getColumnIndexOrThrow(SpTableContract.SpSankalpTable.COLUMN_TO_DATE));

                period = SpDateUtils.getFriendlyDateString(fromDate) + " - " + SpDateUtils.getFriendlyDateString(toDate);
            }


            // Populate fields with extracted properties
            tvCategory.setText(category.getCategoryName());
            tvItem.setText(item.getCategoryItemName());
            tvPeriod.setText(String.valueOf(period));
        }

    }

    private class CursorLoaderTask extends AsyncTask<Void, Void, Boolean>
    {
        private Cursor _tyagCursor;
        private Cursor _niyamCursor;
        @Override
        protected Boolean doInBackground(Void... params) {
            SpContentProvider provider = SpContentProvider.getInstance(getContext());
            _categories = provider.getAllCategories();
            _categoryItems = provider.getAllCategoryItems();
            _tyagCursor = provider.getTyagCursor();
            _niyamCursor = provider.getNiyamCursor();

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            cursorLoaderTask = null;

            if (success) {
                tyagAdapter.swapCursor(_tyagCursor);
                niyamAdapter.swapCursor(_niyamCursor);
            } else {
                // Error
            }
        }
    }

}
