package com.ravijain.sankalp.fragments;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ravijain.sankalp.R;
import com.ravijain.sankalp.activities.SpConstants;
import com.ravijain.sankalp.data.SpCategory;
import com.ravijain.sankalp.data.SpCategoryItem;
import com.ravijain.sankalp.data.SpContentProvider;
import com.ravijain.sankalp.data.SpDataConstants;
import com.ravijain.sankalp.data.SpDateUtils;
import com.ravijain.sankalp.data.SpExceptionOrTarget;
import com.ravijain.sankalp.data.SpSankalp;
import com.ravijain.sankalp.data.SpSankalpFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;

/**
 * A placeholder fragment containing a simple view.
 */
public class SpAddSankalpActivityFragment extends Fragment {

//    private RadioGroup _sankalpRGView;
    private Spinner _categoriesSpinnerView;
    private Spinner _itemsSpinnerView;
    private Spinner _rangeLabelsSpinnerView;

    private EditText _fromDateTextView;
    private EditText _toDateTextView;
    private EditText _descriptionView;
    private EditText _exceptionFrequencyCount;
    private TextView _rangeValueTextView;
    private View _fromToDateContainer;

    private Spinner _exceptionsOrTargetSpinnerView;
    private TextView _exceptionOrTargetTitleTextView;
    private View _exceptionOrTargetCurrentCount_ll;
    private TextView _exceptionOrTargetCurrentCount_label;
    private EditText _exceptionOrTargetCurrentCount_tv;

    private DatePickerDialog _fromDatePickerDialog;
    private DatePickerDialog _toDatePickerDialog;

    private ArrayAdapter<SpCategory> _categoriesAdapter;
    private ArrayAdapter<SpCategoryItem> _itemsAdapter;
    private ArrayAdapter<SpExceptionOrTarget> _exceptionsFrequencyAdapter;

    private Hashtable<Integer, Hashtable<String, SpCategory>> _categoriesTable;
    private Hashtable<Integer, Hashtable<String, SpCategoryItem>> _categoryItemsTable;
    private Date _fromDate = null;
    private Date _toDate = null;
    private int _sankalpType;

    public SpAddSankalpActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_sp_add_sankalp, container, false);
        setHasOptionsMenu(true);

        _sankalpType = getActivity().getIntent().getIntExtra(SpConstants.INTENT_KEY_SANKALP_TYPE, SpDataConstants.SANKALP_TYPE_TYAG);
        if (_sankalpType == SpDataConstants.SANKALP_TYPE_TYAG) {
            getActivity().setTitle(R.string.title_activity_sp_add_tyag);
        }
        else {
            getActivity().setTitle(R.string.title_activity_sp_add_niyam);
        }

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
//        _sankalpRGView.check(R.id.radio_tyag);
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
        } /*else if (id == R.id.action_discardSankalp) {
            _discardSankalp();
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    private void _populateAndBindFormFields(View view) {

        _exceptionOrTargetTitleTextView = (TextView) view.findViewById(R.id.exceptionOrTargetTitle);
        _exceptionOrTargetCurrentCount_ll = view.findViewById(R.id.exceptionOrTargetCurrentCount_ll);
        _exceptionOrTargetCurrentCount_label = (TextView) view.findViewById(R.id.exceptionOrTargetCurrentCount_label);
        _exceptionOrTargetCurrentCount_tv = (EditText) view.findViewById(R.id.exceptionOrTargetCurrentCount_tv);

        if (_sankalpType == SpDataConstants.SANKALP_TYPE_TYAG) {
            _exceptionOrTargetTitleTextView.setText(R.string.tyagExceptions);
            _exceptionOrTargetCurrentCount_label.setText(R.string.exception_left_label);
            _populateCategories(SpDataConstants.SANKALP_TYPE_TYAG);
        }
        else {
            _exceptionOrTargetTitleTextView.setText(R.string.niyamFrequency);
            _exceptionOrTargetCurrentCount_label.setText(R.string.frequency_done_label);
            _populateCategories(SpDataConstants.SANKALP_TYPE_NIYAM);
        }

        _categoriesSpinnerView = (Spinner) view.findViewById(R.id.categories_spinner);
        _categoriesAdapter = new ArrayAdapter<SpCategory>(getContext(), R.layout.spinner_item, new ArrayList<SpCategory>());
        _categoriesAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        _categoriesSpinnerView.setAdapter(_categoriesAdapter);
        _categoriesSpinnerView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                _populateItems(((SpCategory) parentView.getSelectedItem()).getId());
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
                Toast.makeText(getActivity(), ((SpCategoryItem)parentView.getSelectedItem()).getCategoryItemName(), Toast.LENGTH_SHORT).show();
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
                } else if (label.equals(getString(R.string.thisDay))) {
                    _setDate(_fromDateTextView, SpDateUtils.beginOfDate(today));
                    _setDate(_toDateTextView, SpDateUtils.endOfDate(today));
                    _togglePeriodViewVisibility(true);
                    _rangeValueTextView.setText(SpDateUtils.getDayString(today));

                } else if (label.equals(getString(R.string.tomorrow))) {
                    _togglePeriodViewVisibility(true);
                    Calendar nextDate = SpDateUtils.nextDate(today);
                    _setDate(_fromDateTextView, SpDateUtils.beginOfDate(nextDate));
                    _setDate(_toDateTextView, SpDateUtils.endOfDate(nextDate));
                    _rangeValueTextView.setText(SpDateUtils.getDayString(nextDate));
                } else if (label.equals(getString(R.string.thisMonth))) {
                    _togglePeriodViewVisibility(true);
                    _setDate(_fromDateTextView, SpDateUtils.beginOfMonth(today));
                    _setDate(_toDateTextView, SpDateUtils.endOfMonth(today));
                    _rangeValueTextView.setText(SpDateUtils.getMonthString(today));
                } else if (label.equals(getString(R.string.thisYear))) {
                    _togglePeriodViewVisibility(true);
                    _setDate(_fromDateTextView, SpDateUtils.beginOfYear(today));
                    _setDate(_toDateTextView, SpDateUtils.endOfYear(today));
                    _rangeValueTextView.setText(SpDateUtils.yearOfDate(today));
                } else if (label.equals(getString(R.string.Lifetime))) {
                    _togglePeriodViewVisibility(true);
                    _fromDate = null;
                    _toDate = null;
                    _setDate(_fromDateTextView, SpDateUtils.beginOfDate(today));
                    _toDateTextView.setText(getString(R.string.Lifetime));
                    _rangeValueTextView.setText(R.string.Lifetime);
                }
                _exceptionsFrequencyAdapter.clear();
                _exceptionsFrequencyAdapter.addAll(_getExceptionFrequencyList(label));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        _exceptionsOrTargetSpinnerView = (Spinner) view.findViewById(R.id.exceptionOrTarget_spinner);
        _exceptionsFrequencyAdapter = new ArrayAdapter<SpExceptionOrTarget>(getContext(), R.layout.spinner_item,
                _getExceptionFrequencyList(null));
        _exceptionsFrequencyAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        _exceptionsOrTargetSpinnerView.setAdapter(_exceptionsFrequencyAdapter);
        _exceptionsOrTargetSpinnerView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });
        _exceptionFrequencyCount = (EditText) view.findViewById(R.id.exceptionOrTargetCount_textView);
        _exceptionFrequencyCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String s = charSequence.toString();
                int newCount;
                if (s.equals("")) {
                    newCount = 0;
                } else {
                    newCount = Integer.valueOf(charSequence.toString());
                }
                if (newCount > 0) {
                    _exceptionOrTargetCurrentCount_ll.setVisibility(View.VISIBLE);
                    _exceptionOrTargetCurrentCount_tv.setText(String.valueOf(0));
                } else {
                    _exceptionOrTargetCurrentCount_ll.setVisibility(View.GONE);
                    _exceptionOrTargetCurrentCount_tv.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

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

    private void _addSankalp() {

        int categoryId = ((SpCategory) _categoriesSpinnerView.getSelectedItem()).getId();
        int itemId = ((SpCategoryItem) _itemsSpinnerView.getSelectedItem()).getId();

        SpSankalp sankalp = SpSankalpFactory.getNewSankalp(_sankalpType, categoryId, itemId);
        sankalp.setFromDate(_fromDate);
        sankalp.setToDate(_toDate);
        if (_rangeValueTextView.getText().equals(getString(R.string.Lifetime))) {
            sankalp.setLifetime(SpDataConstants.SANKALP_IS_LIFTIME_TRUE);
        }

        SpExceptionOrTarget exceptionOrTarget = new SpExceptionOrTarget(SpExceptionOrTarget.EXCEPTION_OR_TARGET_UNDEFINED, getContext());
        String count = _exceptionFrequencyCount.getText().toString();
        if (count != "") {
            int countValue = Integer.valueOf(count);
            if (countValue == 0 && _sankalpType == SpDataConstants.SANKALP_TYPE_TYAG) {
                exceptionOrTarget.setId(SpExceptionOrTarget.EXCEPTION_OR_TARGET_TOTAL);
            }
            else {
                exceptionOrTarget.setExceptionOrTargetCount(countValue);
                exceptionOrTarget.setId(((SpExceptionOrTarget) _exceptionsOrTargetSpinnerView.getSelectedItem()).getId());
            }

            String currentCount = _exceptionOrTargetCurrentCount_tv.getText().toString();
            if (!currentCount.equals("")) {
                exceptionOrTarget.setExceptionOrTargetCountCurrent(Integer.valueOf(currentCount));
            }
        }

        sankalp.setExceptionOrTarget(exceptionOrTarget);
        sankalp.setDescription(_descriptionView.getText().toString());

        SpContentProvider.getInstance(getContext()).addSankalp(sankalp);
        NavUtils.navigateUpFromSameTask(getActivity());
    }

    private void _discardSankalp() {
        NavUtils.navigateUpFromSameTask(getActivity());
    }

    private void _togglePeriodViewVisibility(boolean isLabelVisible) {
        if (isLabelVisible) {
            _rangeValueTextView.setVisibility(View.VISIBLE);
            _fromToDateContainer.setVisibility(View.GONE);
        } else {
            _rangeValueTextView.setVisibility(View.GONE);
            _fromToDateContainer.setVisibility(View.VISIBLE);
        }

    }

    private ArrayList<SpExceptionOrTarget> _getExceptionFrequencyList(String range) {
        ArrayList<SpExceptionOrTarget> frequencies = new ArrayList<SpExceptionOrTarget>();
        frequencies.add(new SpExceptionOrTarget(SpExceptionOrTarget.EXCEPTION_OR_TARGET_TOTAL, getContext()));
        if (range != null) {
            if (range.equals(getString(R.string.Range))) {
                // Add date maths logic
            } else if (range.equals(getString(R.string.thisMonth))) {
                frequencies.add(new SpExceptionOrTarget(SpExceptionOrTarget.EXCEPTION_OR_TARGET_WEEKLY, getContext()));
                frequencies.add(new SpExceptionOrTarget(SpExceptionOrTarget.EXCEPTION_OR_TARGET_DAILY, getContext()));
            } else if (range.equals(getString(R.string.thisYear))) {
                frequencies.add(new SpExceptionOrTarget(SpExceptionOrTarget.EXCEPTION_OR_TARGET_MONTHLY, getContext()));
                frequencies.add(new SpExceptionOrTarget(SpExceptionOrTarget.EXCEPTION_OR_TARGET_WEEKLY, getContext()));
                frequencies.add(new SpExceptionOrTarget(SpExceptionOrTarget.EXCEPTION_OR_TARGET_DAILY, getContext()));
            } else if (range.equals(getString(R.string.Lifetime))) {
                frequencies.add(new SpExceptionOrTarget(SpExceptionOrTarget.EXCEPTION_OR_TARGET_YEARLY, getContext()));
                frequencies.add(new SpExceptionOrTarget(SpExceptionOrTarget.EXCEPTION_OR_TARGET_MONTHLY, getContext()));
                frequencies.add(new SpExceptionOrTarget(SpExceptionOrTarget.EXCEPTION_OR_TARGET_WEEKLY, getContext()));
                frequencies.add(new SpExceptionOrTarget(SpExceptionOrTarget.EXCEPTION_OR_TARGET_DAILY, getContext()));
            }
        }
        return frequencies;
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
        _categoriesSpinnerView.setSelection(0);
//        Object o = _categoriesSpinnerView.getSelectedItem();
//        if (o != null && o instanceof SpCategory) {
//            _populateItems(((SpCategory) o).getId());
//        }
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

    private void _setDate(EditText dateTextView, Date date) {
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
