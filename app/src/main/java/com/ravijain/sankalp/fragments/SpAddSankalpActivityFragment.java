package com.ravijain.sankalp.fragments;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.ravijain.sankalp.R;
import com.ravijain.sankalp.activities.SpConstants;
import com.ravijain.sankalp.data.SpCategory;
import com.ravijain.sankalp.data.SpCategoryItem;
import com.ravijain.sankalp.data.SpContentProvider;
import com.ravijain.sankalp.data.SpDataConstants;
import com.ravijain.sankalp.support.SpCustomSpinner;
import com.ravijain.sankalp.support.SpDateUtils;
import com.ravijain.sankalp.data.SpExceptionOrTarget;
import com.ravijain.sankalp.data.SpSankalp;
import com.ravijain.sankalp.data.SpSankalpFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

/**
 * A placeholder fragment containing a simple view.
 */
public class SpAddSankalpActivityFragment extends Fragment {

    private TextView _sankalpSummaryTV;
    private Spinner _categoriesSpinnerView;
    private Spinner _itemsSpinnerView;
    private SpCustomSpinner _rangeLabelsSpinnerView;

//    private EditText _fromDateTextView;
//    private EditText _toDateTextView;
    private EditText _descriptionView;
    private EditText _exceptionFrequencyCount;
    private TextView _rangeValueTextView;
//    private View _fromToDateContainer;

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

    private Date _fromDate = null;
    private Date _toDate = null;
    private int _sankalpType;
    private SpSankalp _editedSankalp;

    private boolean _isDocumentReady = false;

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
        } else {
            getActivity().setTitle(R.string.title_activity_sp_add_niyam);
        }

        int sankalpId = getActivity().getIntent().getIntExtra(SpConstants.INTENT_KEY_SANKALP_ID, -1);
        if (sankalpId > -1) {
            DataLoaderTask dlt = new DataLoaderTask(sankalpId, DataLoaderTask.REQUEST_TYPE_SANKALP);
            dlt.execute((Void) null);
        }

//        _categoriesTable = new Hashtable<Integer, Hashtable<String, SpCategory>>();
//        _categoryItemsTable = new Hashtable<Integer, Hashtable<String, SpCategoryItem>>();

//        Calendar today = Calendar.getInstance();
//        _fromDatePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear,
//                                  int dayOfMonth) {
//                _setDate(_fromDateTextView, view, year, monthOfYear, dayOfMonth);
//            }
//
//
//        }, today.get(Calendar.YEAR), today.get(Calendar.MONTH),
//                today.get(Calendar.DAY_OF_MONTH));
//
//        //_hideDateItems();
//
//        _toDatePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear,
//                                  int dayOfMonth) {
//                _setDate(_toDateTextView, view, year, monthOfYear, dayOfMonth);
//            }
//
//
//        }, today.get(Calendar.YEAR), today.get(Calendar.MONTH),
//                today.get(Calendar.DAY_OF_MONTH));


        _populateAndBindFormFields(fragmentView);

        return fragmentView;
    }

//    private void _hideDateItems() {
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
//            int daySpinnerId = Resources.getSystem().getIdentifier("day", "id", "android");
//            if (daySpinnerId != 0)
//            {
//                View daySpinner = _fromDatePickerDialog.findViewById(daySpinnerId);
//                if (daySpinner != null)
//                {
//                    daySpinner.setVisibility(View.GONE);
//                }
//            }
//
//            int monthSpinnerId = Resources.getSystem().getIdentifier("month", "id", "android");
//            if (monthSpinnerId != 0)
//            {
//                View monthSpinner = _fromDatePickerDialog.findViewById(monthSpinnerId);
//                if (monthSpinner != null)
//                {
//                    monthSpinner.setVisibility(View.VISIBLE);
//                }
//            }
//
//            int yearSpinnerId = Resources.getSystem().getIdentifier("year", "id", "android");
//            if (yearSpinnerId != 0)
//            {
//                View yearSpinner = _fromDatePickerDialog.findViewById(yearSpinnerId);
//                if (yearSpinner != null)
//                {
//                    yearSpinner.setVisibility(View.GONE);
//                }
//            }
//        } else { //Older SDK versions
//            Field f[] = _fromDatePickerDialog.getClass().getDeclaredFields();
//            for (Field field : f)
//            {
//                if(field.getName().equals("mDayPicker") || field.getName().equals("mDaySpinner"))
//                {
//                    field.setAccessible(true);
//                    Object dayPicker = null;
//                    try {
//                        dayPicker = field.get(_fromDatePickerDialog);
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    }
//                    ((View) dayPicker).setVisibility(View.GONE);
//                }
//
//                if(field.getName().equals("mMonthPicker") || field.getName().equals("mMonthSpinner"))
//                {
//                    field.setAccessible(true);
//                    Object monthPicker = null;
//                    try {
//                        monthPicker = field.get(_fromDatePickerDialog);
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    }
//                    ((View) monthPicker).setVisibility(View.VISIBLE);
//                }
//
//                if(field.getName().equals("mYearPicker") || field.getName().equals("mYearSpinner"))
//                {
//                    field.setAccessible(true);
//                    Object yearPicker = null;
//                    try {
//                        yearPicker = field.get(_fromDatePickerDialog);
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    }
//                    ((View) yearPicker).setVisibility(View.GONE);
//                }
//            }
//        }
//    }

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

        _sankalpSummaryTV = (TextView) view.findViewById(R.id.add_sankalpSummary_tv);

        _exceptionOrTargetTitleTextView = (TextView) view.findViewById(R.id.exceptionOrTargetTitle);
        _exceptionOrTargetCurrentCount_ll = view.findViewById(R.id.exceptionOrTargetCurrentCount_ll);
        _exceptionOrTargetCurrentCount_label = (TextView) view.findViewById(R.id.exceptionOrTargetCurrentCount_label);
        _exceptionOrTargetCurrentCount_tv = (EditText) view.findViewById(R.id.exceptionOrTargetCurrentCount_tv);

        if (_sankalpType == SpDataConstants.SANKALP_TYPE_TYAG) {
            _exceptionOrTargetTitleTextView.setText(R.string.tyagExceptions);
            _exceptionOrTargetCurrentCount_label.setText(R.string.exception_left_label);
            _populateCategories(SpDataConstants.SANKALP_TYPE_TYAG);
        } else {
            _exceptionOrTargetTitleTextView.setText(R.string.niyamFrequency);
            _exceptionOrTargetCurrentCount_label.setText(R.string.frequency_done_label);
            _populateCategories(SpDataConstants.SANKALP_TYPE_NIYAM);
        }

        _categoriesSpinnerView = (Spinner) view.findViewById(R.id.categories_spinner);
        _categoriesAdapter = new ArrayAdapter<SpCategory>(getContext(), R.layout.spinner_item, new ArrayList<SpCategory>());
        _categoriesAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        _categoriesSpinnerView.setAdapter(_categoriesAdapter);
        _categoriesSpinnerView.setOnItemSelectedListener(new SpinnerItemSelectionListener());

        _itemsSpinnerView = (Spinner) view.findViewById(R.id.items_spinner);
        _itemsAdapter = new ArrayAdapter<SpCategoryItem>(getContext(), R.layout.spinner_item, new ArrayList<SpCategoryItem>());
        _itemsAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        _itemsSpinnerView.setAdapter(_itemsAdapter);
        _itemsSpinnerView.setOnItemSelectedListener(new SpinnerItemSelectionListener());


//        _fromToDateContainer = view.findViewById(R.id.fromToDateContainer);
        _rangeValueTextView = (TextView) view.findViewById(R.id.rangeValue_textView);
        _rangeLabelsSpinnerView = (SpCustomSpinner) view.findViewById(R.id.rangeLabels_spinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getContext(), R.array.periodLabelsList, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        _rangeLabelsSpinnerView.setAdapter(adapter);
        _rangeLabelsSpinnerView.setOnItemSelectedListener(new SpinnerItemSelectionListener());

        _exceptionsOrTargetSpinnerView = (Spinner) view.findViewById(R.id.exceptionOrTarget_spinner);
        _exceptionsFrequencyAdapter = new ArrayAdapter<SpExceptionOrTarget>(getContext(), R.layout.spinner_item,
                _getExceptionFrequencyList(null));
        _exceptionsFrequencyAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        _exceptionsOrTargetSpinnerView.setAdapter(_exceptionsFrequencyAdapter);
        _exceptionsOrTargetSpinnerView.setOnItemSelectedListener(new SpinnerItemSelectionListener());
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
                _updateSummary();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

//        _fromDateTextView = (EditText) view.findViewById(R.id.fromdate_textView);
//        _toDateTextView = (EditText) view.findViewById(R.id.todate_textView);
//        DateFieldClickListener clickListener = new DateFieldClickListener();
//        _fromDateTextView.setOnClickListener(clickListener);
//        _toDateTextView.setOnClickListener(clickListener);
//        DateFieldFocusChangeListener focusChangeListener = new DateFieldFocusChangeListener();
//        _fromDateTextView.setOnFocusChangeListener(focusChangeListener);
//        _toDateTextView.setOnFocusChangeListener(focusChangeListener);

        _descriptionView = (EditText) view.findViewById(R.id.sankalpDescription);

    }

    private void _setSummaryTextView(SpSankalp editedSankalp) {

        _sankalpSummaryTV.setText(editedSankalp.getSankalpSummary());
    }

    private void _updateSummary()
    {
        if (_isDocumentReady) {
            _editedSankalp = _getSankalpFromInput();
            _setSummaryTextView(_editedSankalp);
        }

    }

    private SpSankalp _getSankalpFromInput() {
        SpCategory category = ((SpCategory) _categoriesSpinnerView.getSelectedItem());
        SpCategoryItem item = ((SpCategoryItem) _itemsSpinnerView.getSelectedItem());

        SpSankalp sankalp = SpSankalpFactory.getNewSankalp(_sankalpType, category.getId(), item.getId());
        sankalp.setCategory(category);
        sankalp.setItem(item);
        sankalp.setFromDate(_fromDate);
        sankalp.setToDate(_toDate);
        if (_rangeValueTextView.getText().equals(getString(R.string.Lifetime))) {
            sankalp.setLifetime(SpDataConstants.SANKALP_IS_LIFTIME_TRUE);
        }

        SpExceptionOrTarget exceptionOrTarget = new SpExceptionOrTarget(SpExceptionOrTarget.EXCEPTION_OR_TARGET_UNDEFINED, getContext());
        String count = _exceptionFrequencyCount.getText().toString();
        if (!TextUtils.isEmpty(count)) {
            int countValue = Integer.valueOf(count);
            if (countValue == 0 && _sankalpType == SpDataConstants.SANKALP_TYPE_TYAG) {
                exceptionOrTarget.setId(SpExceptionOrTarget.EXCEPTION_OR_TARGET_TOTAL);
            } else {
                exceptionOrTarget.setExceptionOrTargetCount(countValue);
                exceptionOrTarget.setId(((SpExceptionOrTarget) _exceptionsOrTargetSpinnerView.getSelectedItem()).getId());
            }

            String currentCount = _exceptionOrTargetCurrentCount_tv.getText().toString();
            if (!TextUtils.isEmpty(currentCount)) {
                exceptionOrTarget.setExceptionOrTargetCountCurrent(Integer.valueOf(currentCount));
            }
        }

        sankalp.setExceptionOrTarget(exceptionOrTarget);
        sankalp.setDescription(_descriptionView.getText().toString());
        return sankalp;
    }

    private void _addSankalp() {
        SpSankalp sankalp = _getSankalpFromInput();
        SpContentProvider.getInstance(getContext()).addSankalp(sankalp);
        NavUtils.navigateUpFromSameTask(getActivity());
    }

    private void _discardSankalp() {
        NavUtils.navigateUpFromSameTask(getActivity());
    }

//    private void _togglePeriodViewVisibility(boolean isLabelVisible) {
//        if (isLabelVisible) {
//            _rangeValueTextView.setVisibility(View.VISIBLE);
//            _fromToDateContainer.setVisibility(View.GONE);
//        } else {
//            _rangeValueTextView.setVisibility(View.GONE);
//            _fromToDateContainer.setVisibility(View.VISIBLE);
//        }
//
//    }

    private ArrayList<SpExceptionOrTarget> _getExceptionFrequencyList(String range) {
        ArrayList<SpExceptionOrTarget> frequencies = new ArrayList<SpExceptionOrTarget>();
        frequencies.add(new SpExceptionOrTarget(SpExceptionOrTarget.EXCEPTION_OR_TARGET_TOTAL, getContext()));
        if (range != null) {
            if (range.equals(getString(R.string.Range))) {
                // Add date maths logic
            } else if (range.equals(getString(R.string.month))) {
                frequencies.add(new SpExceptionOrTarget(SpExceptionOrTarget.EXCEPTION_OR_TARGET_WEEKLY, getContext()));
                frequencies.add(new SpExceptionOrTarget(SpExceptionOrTarget.EXCEPTION_OR_TARGET_DAILY, getContext()));
            } else if (range.equals(getString(R.string.year))) {
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
        // Database request
        DataLoaderTask dlt = new DataLoaderTask(sankalpType, DataLoaderTask.REQUEST_TYPE_CATEGORY);
        dlt.execute((Void) null);
    }

    private void _populateCategories(Collection<SpCategory> values) {
        _categoriesAdapter.clear();
        _categoriesAdapter.addAll(values);
        _categoriesSpinnerView.setSelection(0);
    }

    private void _populateItems(int categoryId) {
        // Database request
        _isDocumentReady = false;
        DataLoaderTask dlt = new DataLoaderTask(categoryId, DataLoaderTask.REQUEST_TYPE_ITEM);
        dlt.execute((Void) null);
    }

    private void _populateItems(Collection<SpCategoryItem> values) {

        _itemsAdapter.clear();
        _itemsAdapter.addAll(values);
        _itemsSpinnerView.setSelection(0);
        _isDocumentReady = true;
        _updateSummary();
    }

//    private void _setDate(EditText dateTextView, DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//        Calendar cal = Calendar.getInstance();
//        cal.set(year, monthOfYear, dayOfMonth);
//        _setDate(dateTextView, cal.getTime());
//    }

//    private void _setDate(EditText dateTextView, Date date) {
//        if (dateTextView == _fromDateTextView) {
//            _fromDate = date;
//        } else if (dateTextView == _toDateTextView) {
//            _toDate = date;
//        }
//        dateTextView.setText(SpDateUtils.getFriendlyDateShortString(date));
//    }

    public void setFromDate(Date date) {

        _fromDate = date;
    }

    public void setToDate(Date date) {
        _toDate = date;
    }

    public void setDate(Date fromDate, Date toDate)
    {
        setFromDate(fromDate);
        setToDate(toDate);
        _rangeValueTextView.setText(SpDateUtils.getFriendlyPeriodString(fromDate, toDate, false));
        _updateSummary();
    }

    private void _showDialog(int periodKey)
    {
        SpPeriodDialog d = new SpPeriodDialog();
        d.setParentFragment(this);
        Bundle args = new Bundle();
        args.putInt(SpConstants.INTENT_KEY_SANKALP_PERIOD, periodKey);
        d.setArguments(args);
        d.show(getFragmentManager(), "SpPeriodDialog");
    }

    private class SpinnerItemSelectionListener implements AdapterView.OnItemSelectedListener
    {

        private boolean isFirstTime = true;
        @Override
        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

            if (parentView.getId() == R.id.rangeLabels_spinner) {
                _handleRangeLabelSpinnerSelection(parentView);
            }
            else if (parentView.getId() == R.id.categories_spinner) {
                _handleCategoriesSpinnerSelection(parentView);
            }
            else if (parentView.getId() == R.id.items_spinner) {
                _handleItemsSpinnerSelection(parentView);
            }
            else if (parentView.getId() == R.id.exceptionOrTarget_spinner) {
                _handleExTarSpinnerSelection(parentView);
            }

            if (isFirstTime) isFirstTime = false;
        }

        private void _handleExTarSpinnerSelection(AdapterView<?> parentView) {
            _updateSummary();
        }

        private void _handleItemsSpinnerSelection(AdapterView<?> parentView) {
            _updateSummary();
        }

        private void _handleCategoriesSpinnerSelection(AdapterView<?> parentView) {

            _populateItems(((SpCategory) parentView.getSelectedItem()).getId());
        }



        private void _handleRangeLabelSpinnerSelection(AdapterView<?> parentView)
        {
            String label = (String) parentView.getSelectedItem();
            Calendar today = Calendar.getInstance();
            if (label.equals(getString(R.string.Range))) {
                _showDialog(SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_RANGE);
            } else if (label.equals(getString(R.string.Day))) {

                if (!isFirstTime) {
                    _showDialog(SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_DAY);
                }
                else {
                    _fromDate = SpDateUtils.beginOfDate(new Date());
                    _toDate = SpDateUtils.endOfDate(new Date());
                }


            } else if (label.equals(getString(R.string.month))) {
                _showDialog(SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_MONTH);
            } else if (label.equals(getString(R.string.year))) {
                _showDialog(SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_YEAR);
            } else if (label.equals(getString(R.string.Lifetime))) {
//                _togglePeriodViewVisibility(true);
                _fromDate = null;
                _toDate = null;
                setFromDate(SpDateUtils.beginOfDate(today));
//                _toDateTextView.setText(getString(R.string.Lifetime));
                _rangeValueTextView.setText(R.string.Lifetime);
            }
            _exceptionsFrequencyAdapter.clear();
            _exceptionsFrequencyAdapter.addAll(_getExceptionFrequencyList(label));
        }
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            Log.i("Spinner", "nothing selected");

        }
    }

//    private class DateFieldClickListener implements View.OnClickListener {
//        @Override
//        public void onClick(View view) {
//            if (view == _fromDateTextView) {
//                _fromDatePickerDialog.show();
//            } else if (view == _toDateTextView) {
//                _toDatePickerDialog.show();
//            }
//        }
//    }
//
//    private class DateFieldFocusChangeListener implements View.OnFocusChangeListener {
//
//        @Override
//        public void onFocusChange(View view, boolean hasFocus) {
//            if (hasFocus) {
//                if (view == _fromDateTextView) {
//                    //_fromDatePickerDialog.requestWindowFeature(getTargetRequestCode());
//                    _fromDatePickerDialog.show();
//                } else if (view == _toDateTextView) {
//                    _toDatePickerDialog.show();
//                }
//            }
//        }
//    }


    private class DataLoaderTask extends AsyncTask<Void, Void, Boolean> {

        private int _id;
        private int _requestType;
        final static int REQUEST_TYPE_CATEGORY = 0;
        final static int REQUEST_TYPE_ITEM = 1;
        static final int REQUEST_TYPE_SANKALP = 2;
        private ArrayList<SpCategory> _cats = null;
        private ArrayList<SpCategoryItem> _items = null;

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
            } else if (_requestType == REQUEST_TYPE_SANKALP) {
                _editedSankalp = provider.getSankalpById(_id);
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {
                if (_requestType == REQUEST_TYPE_CATEGORY) {
                    if (_cats != null) {
                        _populateCategories(_cats);
                    }
                } else if (_requestType == REQUEST_TYPE_ITEM) {
                    if (_items != null) {
                        _populateItems(_items);
                    }
                } else if (_requestType == REQUEST_TYPE_SANKALP) {
                    _setSummaryTextView(_editedSankalp);
                }
            } else {
                // Error
            }
        }
    }

}
