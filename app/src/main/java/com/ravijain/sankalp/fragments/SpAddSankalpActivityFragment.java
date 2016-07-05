package com.ravijain.sankalp.fragments;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.ravijain.sankalp.R;
import com.ravijain.sankalp.data.SpCategory;
import com.ravijain.sankalp.data.SpCategoryItem;
import com.ravijain.sankalp.data.SpContentProvider;
import com.ravijain.sankalp.data.SpDataConstants;
import com.ravijain.sankalp.data.SpDateUtils;
import com.ravijain.sankalp.data.SpSankalp;
import com.ravijain.sankalp.data.SpSankalpFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;

/**
 * A placeholder fragment containing a simple view.
 */
public class SpAddSankalpActivityFragment extends Fragment {

    private RadioGroup _sankalpRGView;
    private Spinner _categoriesSpinnerView;
    private Spinner _itemsSpinnerView;
    private Spinner _rangeLabelsSpinnerView;
    private EditText _fromDateTextView;
    private EditText _toDateTextView;
    private EditText _descriptionView;
    private TextView _rangeValueTextView;
    private View _fromToDateContainer;

    private DatePickerDialog _fromDatePickerDialog;
    private DatePickerDialog _toDatePickerDialog;

    private ArrayAdapter<SpCategory> _categoriesAdapter;
    private ArrayAdapter<SpCategoryItem> _itemsAdapter;

    private Hashtable<Integer, Hashtable<String, SpCategory>> _categoriesTable;
    private Hashtable<Integer, Hashtable<String, SpCategoryItem>> _categoryItemsTable;
    private Date _fromDate = null;
    private Date _toDate = null;

    public SpAddSankalpActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_sp_add_sankalp, container, false);
        setHasOptionsMenu(true);

        _categoriesTable = new Hashtable<Integer, Hashtable<String, SpCategory>>();
        _categoryItemsTable = new Hashtable<Integer, Hashtable<String, SpCategoryItem>>();

        Calendar today = Calendar.getInstance();
        _fromDatePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                _setDate(_fromDateTextView, view, year, monthOfYear, dayOfMonth);
            }


        }, today.get(Calendar.YEAR), today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH));

        _toDatePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                _setDate(_toDateTextView, view, year, monthOfYear, dayOfMonth);
            }


        }, today.get(Calendar.YEAR), today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH));


        _populateAndBindFormFields(fragmentView);
        _sankalpRGView.check(R.id.radio_tyag);
        //_populateData();

        return fragmentView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_sp_add_sankalp, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_addSankalp) {
            _addSankalp();
            return true;
        }
        else if (id == R.id.action_discardSankalp) {
            _discardSankalp();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void _populateData() {
        int sankalpType = _sankalpRGView.getCheckedRadioButtonId();
        if (sankalpType == R.id.radio_tyag) {
            _populateCategories(SpDataConstants.SANKALP_TYPE_TYAG);
        } else {
            _populateCategories(SpDataConstants.SANKALP_TYPE_NIYAM);
        }
    }

    private void _populateAndBindFormFields(View view) {
        _sankalpRGView = (RadioGroup) view.findViewById(R.id.radioGroup_sankalp);
        _sankalpRGView.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_tyag:
                        _populateCategories(SpDataConstants.SANKALP_TYPE_TYAG);
                        break;
                    case R.id.radio_niyam:
                        _populateCategories(SpDataConstants.SANKALP_TYPE_NIYAM);
                        break;
                }
            }
        });

        _categoriesSpinnerView = (Spinner) view.findViewById(R.id.categories_spinner);
        _categoriesAdapter = new ArrayAdapter<SpCategory>(getContext(), R.layout.spinner_item, new ArrayList<SpCategory>());
        _categoriesAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        _categoriesSpinnerView.setAdapter(_categoriesAdapter);
        _categoriesSpinnerView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                _populateItems(((SpCategory)parentView.getSelectedItem()).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here

            }

        });

        _itemsSpinnerView = (Spinner) view.findViewById(R.id.items_spinner);
        _itemsAdapter = new ArrayAdapter<SpCategoryItem>(getContext(), R.layout.spinner_item, new ArrayList<SpCategoryItem>());
        _itemsAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        _itemsSpinnerView.setAdapter(_itemsAdapter);
        _itemsSpinnerView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        _fromToDateContainer = view.findViewById(R.id.fromToDateContainer);
        _rangeValueTextView = (TextView) view.findViewById(R.id.rangeValue_textView);
        _rangeLabelsSpinnerView = (Spinner) view.findViewById(R.id.rangeLabels_spinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getContext(), R.array.rangeLabelsList, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        _rangeLabelsSpinnerView.setAdapter(adapter);
        _rangeLabelsSpinnerView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String label = (String) parentView.getSelectedItem();
                Calendar today = Calendar.getInstance();
                if (label.equals(getString(R.string.Range))) {
                    _togglePeriodViewVisibility(false);
                    _setDate(_fromDateTextView, SpDateUtils.beginOfDate(today));
                    _setDate(_toDateTextView, SpDateUtils.endOfDate(today));
                }
                else if (label.equals(getString(R.string.thisDay))) {
                    _setDate(_fromDateTextView, SpDateUtils.beginOfDate(today));
                    _setDate(_toDateTextView, SpDateUtils.endOfDate(today));
                    _togglePeriodViewVisibility(true);
                    _rangeValueTextView.setText(SpDateUtils.getDayString(today));

                }
                else if (label.equals(getString(R.string.tomorrow))) {
                    _togglePeriodViewVisibility(true);
                    Calendar nextDate = SpDateUtils.nextDate(today);
                    _setDate(_fromDateTextView, SpDateUtils.beginOfDate(nextDate));
                    _setDate(_toDateTextView, SpDateUtils.endOfDate(nextDate));
                    _rangeValueTextView.setText(SpDateUtils.getDayString(nextDate));
                }
                else if (label.equals(getString(R.string.thisMonth))) {
                    _togglePeriodViewVisibility(true);
                    _setDate(_fromDateTextView, SpDateUtils.beginOfMonth(today));
                    _setDate(_toDateTextView, SpDateUtils.endOfMonth(today));
                    _rangeValueTextView.setText(SpDateUtils.getMonthString(today));
                }
                else if (label.equals(getString(R.string.thisYear))) {
                    _togglePeriodViewVisibility(true);
                    _setDate(_fromDateTextView, SpDateUtils.beginOfYear(today));
                    _setDate(_toDateTextView, SpDateUtils.endOfYear(today));
                    _rangeValueTextView.setText(SpDateUtils.yearOfDate(today));
                }
                else if (label.equals(getString(R.string.Lifetime))) {
                    _togglePeriodViewVisibility(true);
                    _fromDateTextView.setText(getString(R.string.Lifetime));
                    _toDateTextView.setText(getString(R.string.Lifetime));
                    _rangeValueTextView.setText(R.string.Lifetime);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        _fromDateTextView = (EditText) view.findViewById(R.id.fromdate_textView);
        _toDateTextView = (EditText) view.findViewById(R.id.todate_textView);
        DateFieldClickListener clickListener = new DateFieldClickListener();
        _fromDateTextView.setOnClickListener(clickListener);
        _toDateTextView.setOnClickListener(clickListener);
        DateFieldFocusChangeListener focusChangeListener = new DateFieldFocusChangeListener();
        _fromDateTextView.setOnFocusChangeListener(focusChangeListener);
        _toDateTextView.setOnFocusChangeListener(focusChangeListener);

        _descriptionView = (EditText) view.findViewById(R.id.sankalpDescription);

    }

    private void _addSankalp()
    {
        int id = _sankalpRGView.getCheckedRadioButtonId();
        int sankalpType = -1;
        switch (id) {
            case R.id.radio_tyag:
                sankalpType = SpDataConstants.SANKALP_TYPE_TYAG;
                break;
            case R.id.radio_niyam:
                sankalpType = SpDataConstants.SANKALP_TYPE_NIYAM;
                break;
        }

        int categoryId = ((SpCategory)_categoriesSpinnerView.getSelectedItem()).getId();
        int itemId = ((SpCategoryItem)_itemsSpinnerView.getSelectedItem()).getId();

        SpSankalp sankalp = SpSankalpFactory.getNewSankalp(sankalpType, categoryId, itemId);
        if (_fromDate != null && _toDate != null) {
            sankalp.setFromDate(_fromDate);
            sankalp.setToDate(_toDate);
        }
        else if (_fromDateTextView.getText().equals(getString(R.string.Lifetime))) {
            sankalp.setLifetime(SpDataConstants.SANKALP_IS_LIFTIME_TRUE);
        }
        sankalp.setDescription(_descriptionView.getText().toString());

        SpContentProvider.getInstance(getContext()).addSankalp(sankalp);
        NavUtils.navigateUpFromSameTask(getActivity());
    }

    private void _discardSankalp()
    {
        NavUtils.navigateUpFromSameTask(getActivity());
    }

    private void _togglePeriodViewVisibility(boolean isLabelVisible)
    {
        if (isLabelVisible) {
            _rangeValueTextView.setVisibility(View.VISIBLE);
            _fromToDateContainer.setVisibility(View.INVISIBLE);
        }
        else {
            _rangeValueTextView.setVisibility(View.INVISIBLE);
            _fromToDateContainer.setVisibility(View.VISIBLE);
        }

    }

    private void _populateCategories(int sankalpType) {
        Hashtable<String, SpCategory> cats = _categoriesTable.get(sankalpType);
        if (cats == null) {
            // Database request
            DataLoaderTask dlt = new DataLoaderTask(sankalpType, DataLoaderTask.REQUEST_TYPE_CATEGORY);
            dlt.execute((Void) null);
        } else {
            _populateCategories(cats.values());
        }
    }

    private void _populateCategories(Collection<SpCategory> values) {
        _categoriesAdapter.clear();
        _categoriesAdapter.addAll(values);
    }

    private void _populateItems(int categoryId) {
        Hashtable<String, SpCategoryItem> items = _categoryItemsTable.get(categoryId);
        if (items == null) {
            // Database request
            DataLoaderTask dlt = new DataLoaderTask(categoryId, DataLoaderTask.REQUEST_TYPE_ITEM);
            dlt.execute((Void) null);
        } else {
            _populateItems(items.values());

        }
    }

    private void _populateItems(Collection<SpCategoryItem> values) {
        _itemsAdapter.clear();
        _itemsAdapter.addAll(values);
    }

    private void _setDate(EditText dateTextView, DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, monthOfYear, dayOfMonth);
        _setDate(dateTextView, cal.getTime());
    }

    private void _setDate(EditText dateTextView, Date date)
    {
        if (dateTextView == _fromDateTextView) {
            _fromDate = date;
        } else if (dateTextView == _toDateTextView) {
            _toDate = date;
        }
        dateTextView.setText(SpDateUtils.getFriendlyDateString(date));
    }

    private class DateFieldClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (view == _fromDateTextView) {
                _fromDatePickerDialog.show();
            } else if (view == _toDateTextView) {
                _toDatePickerDialog.show();
            }
        }
    }

    private class DateFieldFocusChangeListener implements View.OnFocusChangeListener {

        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if (hasFocus) {
                if (view == _fromDateTextView) {
                    _fromDatePickerDialog.show();
                } else if (view == _toDateTextView) {
                    _toDatePickerDialog.show();
                }
            }
        }
    }


    private class DataLoaderTask extends AsyncTask<Void, Void, Boolean> {
        private int _id;
        private int _requestType;
        final static int REQUEST_TYPE_CATEGORY = 0;
        final static int REQUEST_TYPE_ITEM = 1;
        private Hashtable<String, SpCategory> _cats = null;
        private Hashtable<String, SpCategoryItem> _items = null;

        DataLoaderTask(int id, int requestType) {
            _id = id;
            _requestType = requestType;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            SpContentProvider provider = SpContentProvider.getInstance(getContext());
            if (_requestType == REQUEST_TYPE_CATEGORY) {
                _cats = provider.getAllCategoriesBySankalpType(_id);
            } else if (_requestType == REQUEST_TYPE_ITEM) {
                _items = provider.getAllCategoryItemsByCategoryId(_id);
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {
                if (_requestType == REQUEST_TYPE_CATEGORY) {
                    if (_cats != null) {
                        _categoriesTable.put(_id, _cats);
                        _populateCategories(_cats.values());
                    }
                } else if (_requestType == REQUEST_TYPE_ITEM) {
                    if (_items != null) {
                        _categoryItemsTable.put(_id, _items);
                        _populateItems(_items.values());
                    }
                }
            } else {
                // Error
            }
        }
    }


}
