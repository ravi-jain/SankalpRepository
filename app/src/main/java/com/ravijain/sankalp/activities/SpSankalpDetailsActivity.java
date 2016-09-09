package com.ravijain.sankalp.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ravijain.sankalp.R;
import com.ravijain.sankalp.data.SpContentProvider;
import com.ravijain.sankalp.support.SpDateUtils;
import com.ravijain.sankalp.data.SpSankalp;

import java.util.Date;

public class SpSankalpDetailsActivity extends AppCompatActivity {

    private TextView _sankalpSummaryTV;
    private TextView _sankalpTypeTV;
    private TextView _sankalpCategoryTV;
    private TextView _sankalpItemTV;
    private TextView _sankalpPeriodTV;
    private TextView _exceptionOrTargetTitleTV;
    private TextView _sankalpExceptionOrTargetTV;
    private View _exTarLL;
    private TextView _exTarCurrentLabelTV;
    private EditText _exTarCurrentCountET;
    private TextView _sankalpDescriptionTV;
    private ShareActionProvider mShareActionProvider;
    private SpSankalp _sankalp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp_sankalp_details);

        final int id = getIntent().getIntExtra(SpConstants.INTENT_KEY_SANKALP_ID, -1);

        if (id > -1) {
            _sankalpSummaryTV = (TextView) findViewById(R.id.sankalpSummary_v_tv);
            _sankalpTypeTV = (TextView) findViewById(R.id.sankalpType_v_tv);
            _sankalpCategoryTV = (TextView) findViewById(R.id.category_v_tv);
            _sankalpItemTV = (TextView) findViewById(R.id.item_v_tv);
            _sankalpPeriodTV = (TextView) findViewById(R.id.sankalpPeriod_v_tv);
            _exceptionOrTargetTitleTV = (TextView) findViewById(R.id.exceptionOrTargetTitle);
            _sankalpExceptionOrTargetTV = (TextView) findViewById(R.id.sankalpExceptionOrTarget_v_tv);
            _exTarLL = findViewById(R.id.exTarCurrent_ll);
            _exTarCurrentLabelTV = (TextView) findViewById(R.id.exTarCurrentLabel_tv);
            _exTarCurrentCountET = (EditText) findViewById(R.id.exTarCurrentCount_tv);
            _sankalpDescriptionTV = (TextView) findViewById(R.id.sankalpDescription_v_tv);

            _exTarCurrentCountET.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String s = charSequence.toString();
                    if (!TextUtils.isEmpty(s)) {
                        int count = Integer.valueOf(s);
                        UpdateExTarCountTask t = new UpdateExTarCountTask(id, count, new Date());
                        t.execute((Void) null);
                    }

                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
        }

        DetailsLoaderTask loaderTask = new DetailsLoaderTask(id);
        loaderTask.execute((Void) null);
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
        sendIntent.putExtra(Intent.EXTRA_TEXT, _sankalp.getSankalpSummary());
        sendIntent.setType("text/plain");
        setShareIntent(sendIntent);
        // Return true to display menu
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
//        if (id == R.id.details_menu_action_share) {
//            Intent sendIntent = new Intent();
//            sendIntent.setAction(Intent.ACTION_SEND);
//            sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
//            sendIntent.setType("text/plain");
//            setShareIntent(sendIntent);
//            return true;
//        }
        if (id == R.id.details_menu_action_edit) {
            Intent intent = new Intent(getApplicationContext(), SpAddSankalpActivity.class);
            intent.putExtra(SpConstants.INTENT_KEY_SANKALP_ID, _sankalp.getId());
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    private class UpdateExTarCountTask extends AsyncTask<Void, Void, Boolean>
    {

        private Date _date;
        private int _count;
        private int _id;

        UpdateExTarCountTask(int sankalpId, int count, Date date)
        {
            _id = sankalpId;
            _count = count;
            _date = date;
        }
        @Override
        protected Boolean doInBackground(Void... integers) {
            SpContentProvider p = SpContentProvider.getInstance(getApplicationContext());
            p.addExTarEntry(_id, _count, _date);
            return true;
        }
    }

    private class DetailsLoaderTask extends AsyncTask<Void, Void, Boolean> {
        private int _id;


        DetailsLoaderTask(int id) {
            _id = id;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            SpContentProvider provider = SpContentProvider.getInstance(getApplicationContext());
            _sankalp = provider.getSankalpById(_id);
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {

                _sankalpSummaryTV.setText(_sankalp.getSankalpSummary());
                String sankalpType;
                String exTarCurrentLabel;

                if (_sankalp.getSankalpType() == SpConstants.SANKALP_TYPE_TYAG) {
                    sankalpType = getString(R.string.tyag);
                    exTarCurrentLabel = getString(R.string.exception_left_label);
                    _exceptionOrTargetTitleTV.setText(getString(R.string.tyagExceptions));
                } else {
                    sankalpType = getString(R.string.niyam);
                    exTarCurrentLabel = getString(R.string.frequency_done_label);
                    _exceptionOrTargetTitleTV.setText(getString(R.string.niyamFrequency));
                }
                String category = _sankalp.getCategory().getCategoryName();
                String item = _sankalp.getItem().getCategoryItemName();
                int isLifetime = _sankalp.isLifetime();

                String period;
                if (isLifetime == SpConstants.SANKALP_IS_LIFTIME_TRUE) {
                    period = getString(R.string.Lifetime);
                } else {
                    Date fromDate = _sankalp.getFromDate();
                    Date toDate = _sankalp.getToDate();

                    period = SpDateUtils.getFriendlyPeriodString(fromDate, toDate, false);//SpDateUtils.getFriendlyDateString(fromDate) + " - " + SpDateUtils.getFriendlyDateString(toDate);
                }

                _sankalpTypeTV.setText(sankalpType);
                _sankalpCategoryTV.setText(category);
                _sankalpItemTV.setText(item);
                _sankalpPeriodTV.setText(period);

                String description = _sankalp.getDescription();
                if (description == null || TextUtils.isEmpty(description)) {
                    description = getString(R.string.defaultDescriptionEmptyValue);
                }
                _sankalpDescriptionTV.setText(description);

                String eOrT = _sankalp.getExceptionOrTarget().getRepresentationalSummary();
                _sankalpExceptionOrTargetTV.setText(eOrT);
                if (_sankalp.getExceptionOrTarget().getExceptionOrTargetCountCurrent() > -1) {
                    _exTarLL.setVisibility(View.VISIBLE);
                    _exTarCurrentLabelTV.setText(exTarCurrentLabel);
                    _exTarCurrentCountET.setText(String.valueOf(_sankalp.getExceptionOrTarget().getExceptionOrTargetCountCurrent()));
                }

            } else {
                // Error
            }
        }
    }

}
