package com.ravijain.sankalp.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.ravijain.sankalp.R;
import com.ravijain.sankalp.data.SpCategory;
import com.ravijain.sankalp.data.SpCategoryItem;
import com.ravijain.sankalp.data.SpContentProvider;
import com.ravijain.sankalp.data.SpDataConstants;
import com.ravijain.sankalp.data.SpDateUtils;
import com.ravijain.sankalp.data.SpSankalp;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

public class SpSankalpDetailsActivity extends AppCompatActivity {

    private TextView _sankalpTypeTV;
    private TextView _sankalpCategoryTV;
    private TextView _sankalpItemTV;
    private TextView _sankalpPeriodTV;
    private TextView _exceptionOrTargetTitleTV;
    private TextView _sankalpExceptionOrTargetTV;
    private TextView _sankalpDescriptionTV;
    private ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp_sankalp_details);


        int sankalpType = getIntent().getIntExtra(SpConstants.INTENT_KEY_SANKALP_TYPE, SpDataConstants.SANKALP_TYPE_TYAG);
        int id = getIntent().getIntExtra(SpConstants.INTENT_KEY_SANKALP_ID, -1);

        if (id > -1) {
            _sankalpTypeTV = (TextView) findViewById(R.id.sankalpType_v_tv);
            _sankalpCategoryTV = (TextView) findViewById(R.id.category_v_tv);
            _sankalpItemTV = (TextView) findViewById(R.id.item_v_tv);
            _sankalpPeriodTV = (TextView) findViewById(R.id.sankalpPeriod_v_tv);
            _exceptionOrTargetTitleTV = (TextView) findViewById(R.id.exceptionOrTargetTitle);
            _sankalpExceptionOrTargetTV = (TextView) findViewById(R.id.sankalpExceptionOrTarget_v_tv);
            _sankalpDescriptionTV = (TextView) findViewById(R.id.sankalpDescription_v_tv);

            DetailsLoaderTask loaderTask = new DetailsLoaderTask(id, sankalpType);
            loaderTask.execute((Void) null);
        }


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu resource file.
        getMenuInflater().inflate(R.menu.menu_details, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.details_menu_action_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        sendIntent.setType("text/plain");
        setShareIntent(sendIntent);
        // Return true to display menu
        return true;

    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.details_menu_item_share) {
//            Intent sendIntent = new Intent();
//            sendIntent.setAction(Intent.ACTION_SEND);
//            sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
//            sendIntent.setType("text/plain");
//            setShareIntent(sendIntent);
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    private class DetailsLoaderTask extends AsyncTask<Void, Void, Boolean> {
        private int _sankalpType;
        private int _id;
        private SpSankalp _sankalp;
        Hashtable<Integer, SpCategory> _categories;
        Hashtable<Integer, SpCategoryItem> _categoryItems;

        DetailsLoaderTask(int id, int sankalpType) {
            _id = id;
            _sankalpType = sankalpType;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            SpContentProvider provider = SpContentProvider.getInstance(getApplicationContext());
            _sankalp = provider.getSankalpById(_id, _sankalpType);
            _categories = provider.getAllCategories();
            _categoryItems = provider.getAllCategoryItems();
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {
                String sankalpType;
                if (_sankalpType == SpDataConstants.SANKALP_TYPE_TYAG) {
                    sankalpType = getString(R.string.tyag);
                    _exceptionOrTargetTitleTV.setText(getString(R.string.tyagExceptions));
                } else {
                    sankalpType = getString(R.string.niyam);
                    _exceptionOrTargetTitleTV.setText(getString(R.string.niyamFrequency));
                }
                String category = _categories.get(_sankalp.getCategoryID()).getCategoryDisplayName();
                String item = _categoryItems.get(_sankalp.getItemId()).getCategoryItemName();
                int isLifetime = _sankalp.isLifetime();

                String period;
                if (isLifetime == SpDataConstants.SANKALP_IS_LIFTIME_TRUE) {
                    period = getString(R.string.Lifetime);
                } else {
                    Date fromDate = _sankalp.getFromDate();
                    Date toDate = _sankalp.getToDate();

                    period = SpDateUtils.getFriendlyDateString(fromDate) + " - " + SpDateUtils.getFriendlyDateString(toDate);
                }

                _sankalpTypeTV.setText(sankalpType);
                _sankalpCategoryTV.setText(category);
                _sankalpItemTV.setText(item);
                _sankalpPeriodTV.setText(period);

                String description = _sankalp.getDescription();
                if (description != null && description.length() > 0) {
                    _sankalpDescriptionTV.setText(_sankalp.getDescription());
                }

                String eOrT = _sankalp.getExceptionOrTarget().getLabel() + " " + String.valueOf(_sankalp.getExceptionOrTarget().getExceptionOrTargetCount()) + " times";
                _sankalpExceptionOrTargetTV.setText(eOrT);

            } else {
                // Error
            }
        }
    }

}
