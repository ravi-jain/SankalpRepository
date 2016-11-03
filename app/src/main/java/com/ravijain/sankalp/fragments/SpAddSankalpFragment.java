package com.ravijain.sankalp.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.ravijain.sankalp.R;
import com.ravijain.sankalp.support.SpConstants;
import com.ravijain.sankalp.data.SpCategoryItem;
import com.ravijain.sankalp.data.SpContentProvider;
import com.ravijain.sankalp.data.SpExceptionOrTarget;
import com.ravijain.sankalp.data.SpSankalp;
import com.ravijain.sankalp.support.SpColorGenerator;
import com.ravijain.sankalp.support.SpDateUtils;
import com.ravijain.sankalp.support.SpTextDrawable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ravijain on 9/8/2016.
 */
public class SpAddSankalpFragment extends Fragment implements View.OnClickListener, SpSimpleAlertDialog.SpSimpleAlertDialogListener {

    private int _sankalpType;
    private SpSankalp _originalSankalp;
    private SpSankalp _editedSankalp;
    private View _rootView;
    private boolean _isEditMode = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _rootView = inflater.inflate(R.layout.fragment_sp_add_sankalp_material, container, false);
        setHasOptionsMenu(true);

        _sankalpType = getActivity().getIntent().getIntExtra(SpConstants.INTENT_KEY_SANKALP_TYPE, SpConstants.SANKALP_TYPE_TYAG);


        int sankalpId = getActivity().getIntent().getIntExtra(SpConstants.INTENT_KEY_SANKALP_ID, -1);
        if (sankalpId > -1) {
            _isEditMode = true;
            DataLoaderTask dlt = new DataLoaderTask(sankalpId, DataLoaderTask.REQUEST_TYPE_SANKALP);
            dlt.execute((Void) null);
        } else {
            _loadView(null);
            _editedSankalp = new SpSankalp(_sankalpType);
        }

        if (_sankalpType == SpConstants.SANKALP_TYPE_TYAG) {
            getActivity().setTitle(R.string.tyag);
        } else {
            getActivity().setTitle(R.string.niyam);
        }

        return _rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_sp_add_sankalp, menu);
        if (_isEditMode) {
            menu.findItem(R.id.action_addSankalp).setTitle(getString(R.string.update));
            menu.findItem(R.id.action_deleteSankalp).setVisible(true);
            MenuItem item = menu.findItem(R.id.action_share).setVisible(true);

            ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, _editedSankalp.getSankalpSummary());
            sendIntent.setType("text/plain");
            shareActionProvider.setShareIntent(sendIntent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_addSankalp) {
            if (_isEditMode) {
                DataLoaderTask dlt = new DataLoaderTask(_editedSankalp.getId(), DataLoaderTask.REQUEST_TYPE_UPDATE);
                dlt.execute((Void) null);
            } else {
                if (_hasRequiredFields(_editedSankalp)) {
                    DataLoaderTask dlt = new DataLoaderTask(-1, DataLoaderTask.REQUEST_TYPE_ADD);
                    dlt.execute((Void) null);
                } else {
                    Toast.makeText(getContext(), "Can't add", Toast.LENGTH_SHORT).show();
                }
            }

            return true;
        } else if (id == R.id.action_deleteSankalp) {

            new AlertDialog.Builder(getContext())
                    //set message, title, and icon
                    .setTitle(getString(R.string.deleteSankalp))
                    .setMessage(getString(R.string.singleDeletePrompt))
                    .setIcon(R.drawable.ic_delete_black_24dp)

                    .setPositiveButton(R.string.deleteSankalp, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {

                            dialog.dismiss();
                            ArrayList<SpSankalp> sankalpsToBeDeleted = new ArrayList<SpSankalp>();
                            sankalpsToBeDeleted.add(_editedSankalp);
                            SpContentProvider provider = SpContentProvider.getInstance(getContext());
                            provider.deleteSankalps(sankalpsToBeDeleted);

                            getActivity().setResult(Activity.RESULT_OK);
                            NavUtils.navigateUpFromSameTask(getActivity());
                        }

                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                            NavUtils.navigateUpFromSameTask(getActivity());

                        }
                    })
                    .create().show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void _setSummaryTextView(SpSankalp editedSankalp) {

        View container = _rootView.findViewById(R.id.summary_ll);

        if (editedSankalp == null) {
            container.setVisibility(View.GONE);
        } else {
            if (_hasRequiredFields(editedSankalp)) {
                container.setVisibility(View.VISIBLE);
                TextView sankalpSummaryTV = (TextView) _rootView.findViewById(R.id.add_sankalpSummary_tv);
                sankalpSummaryTV.setText(editedSankalp.getSankalpSummary());
            }
        }
        container.setVisibility(View.GONE);
    }

    private boolean _hasRequiredFields(SpSankalp s) {
        if (s == null || /*s.getCategory() == null ||*/ s.getItem() == null || s.getFromDate() == null)
            return false;
        return true;
    }

    private void _loadView(SpSankalp s) {

        // Summary
        _setSummaryTextView(s);

        // Category
//        String value = s == null || s.getCategory() == null ? getString(R.string.categorySelectPrompt) : s.getCategory().getCategoryDisplayName();
//        _loadItemView(_rootView.findViewById(R.id.category_view), -1,
//                getString(R.string.sankalpCategory), value);

        // Item
        String value = s == null || s.getItem() == null ? getString(R.string.itemSelectPrompt) : s.getItem().getCategoryItemDisplayName(getContext());
        _loadItemView(_rootView.findViewById(R.id.item_view), R.drawable.ic_class_black_24dp,
                getString(R.string.sankalpItem), value);

        // Period
        value = s == null || s.getFromDate() == null ? getString(R.string.periodSelectPrompt) : _getPeriodString(s);
        _loadItemView(_rootView.findViewById(R.id.period_view), R.drawable.ic_date_range_color_24dp,
                getString(R.string.sankalpPeriod), value);

        String exTarLabel, exTarCurrentCountLabel;
        int icon;
        int sType = s == null ? _sankalpType : s.getSankalpType();
        if (sType == SpConstants.SANKALP_TYPE_TYAG) {
            exTarLabel = getString(R.string.tyagExceptions);
            exTarCurrentCountLabel = getString(R.string.exception_left_label);
            icon = R.drawable.ic_trending_down_black_24dp;
        } else {
            exTarLabel = getString(R.string.niyamFrequency);
            exTarCurrentCountLabel = getString(R.string.frequency_done_label);
            icon = R.drawable.ic_trending_up_black_24dp;
        }

        // Target or Exception
        value = (s == null || s.getExceptionOrTarget() == null) ? getString(R.string.exTarSelectPrompt) + " " + exTarLabel.toLowerCase() : s.getExceptionOrTarget().getRepresentationalSummary();
        _loadItemView(_rootView.findViewById(R.id.exTar_view), icon,
                exTarLabel, value);

        // Current Count

        View v = _rootView.findViewById(R.id.currentCount_view);
        if (s != null && s.getExceptionOrTarget() != null && s.getExceptionOrTarget().getExceptionOrTargetCountCurrent() > -1) {
            value = String.valueOf(s.getExceptionOrTarget().getExceptionOrTargetCountCurrent());
        } else {
            value = getString(R.string.exTarSelectPrompt) + " " + exTarCurrentCountLabel.toLowerCase();
        }
        _loadItemView(v, R.drawable.ic_trending_flat_black_24dp, exTarCurrentCountLabel, value);

        if (s != null && s.getExceptionOrTarget() != null && s.getExceptionOrTarget().getExceptionOrTargetCount() > 0) {
            v.setVisibility(View.VISIBLE);
        } else {
            v.setVisibility(View.GONE);
        }

        // Notification
        String notification;

        View notificationView = _rootView.findViewById(R.id.notification_view);
        SwitchCompat notifSwitch = (SwitchCompat) notificationView.findViewById(R.id.switch1);
        notifSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                _editedSankalp.setNotification(b == true ? 1 : 0);
                _updateView(R.id.notification_view, _editedSankalp);
            }
        });
        if (s == null) {
            notification = getString(R.string.notificationPrompy);
            notifSwitch.setChecked(false);
        } else {

            if (s.isNotificationOn() == SpConstants.SANKALP_IS_LIFTIME_TRUE) {
                notification = getString(R.string.showRemindersEnabled);
                notifSwitch.setChecked(true);
            } else {
                notification = getString(R.string.showRemindersDisabled);
                notifSwitch.setChecked(false);
            }
        }
        _loadItemView(notificationView, R.drawable.ic_alarm_black_24dp, getString(R.string.showReminders), notification);

        // Description
        String description = s != null ? s.getDescription() : getString(R.string.descriptionAddPrompt);
        if (description == null || TextUtils.isEmpty(description)) {
            description = getString(R.string.defaultDescriptionEmptyValue);
        }
        _loadItemView(_rootView.findViewById(R.id.description_view), R.drawable.ic_description_black_24dp,
                getString(R.string.description), description);

    }

    private String _getPeriodString(SpSankalp s) {
        if (s.isLifetime() == SpConstants.SANKALP_IS_LIFTIME_TRUE) {
            return getString(R.string.lifetime_db);
        } else {
            return SpDateUtils.getFriendlyPeriodString(s.getFromDate(), s.getToDate(), false);
        }
    }

    private void _loadItemView(View view, int imageResourceId, String label, String value) {

        view.setOnClickListener(this);

        ImageView iconView = (ImageView) view.findViewById(R.id.add_icon);
        if (imageResourceId > -1) {
            iconView.setImageResource(imageResourceId);
        } else {
            String letter = String.valueOf(label.toCharArray()[0]).toUpperCase();
            SpColorGenerator generator = SpColorGenerator.MATERIAL;
            SpTextDrawable drawable = SpTextDrawable.builder()
                    .buildRound(letter, generator.getRandomColor());
            iconView.setImageDrawable(drawable);
        }


        TextView labelView = (TextView) view.findViewById(R.id.add_label);
        labelView.setText(label);

        TextView valueView = (TextView) view.findViewById(R.id.add_value);
        valueView.setText(value);
    }

    private void _updateView(int viewId, SpSankalp s) {
        String value = "";
        boolean summaryChanged = false;
        switch (viewId) {
            case R.id.item_view:
                value = s.getItem().getCategoryItemDisplayName(getContext());
                summaryChanged = true;
                break;
            case R.id.period_view:
                value = _getPeriodString(s);
                summaryChanged = true;
                break;
            case R.id.exTar_view:
                value = s.getExceptionOrTarget().getRepresentationalSummary();
                summaryChanged = true;
                View v = _rootView.findViewById(R.id.currentCount_view);
                if (s.getExceptionOrTarget().getExceptionOrTargetCount() > 0) {
                    v.setVisibility(View.VISIBLE);
                } else {
                    v.setVisibility(View.GONE);
                }
                break;
            case R.id.currentCount_view:
                value = String.valueOf(s.getExceptionOrTarget().getExceptionOrTargetCountCurrent());
                break;

            case R.id.notification_view:
                value = s.isNotificationOn() == SpConstants.SANKALP_IS_LIFTIME_TRUE ?
                        getString(R.string.showRemindersEnabled) : getString(R.string.showRemindersDisabled);
                break;
            case R.id.description_view:
                value = s.getDescription();
                break;
        }
        View listItemView = _rootView.findViewById(viewId);
        TextView valueView = (TextView) listItemView.findViewById(R.id.add_value);
        valueView.setText(value);

        if (summaryChanged) _setSummaryTextView(s);
    }

    private boolean _hasSankalpExpired(SpSankalp s) {
        return s.getToDate() != null && s.getToDate().before(new Date());
    }

    @Override
    public void onClick(View view) {
        if (_isEditMode && _hasSankalpExpired(_editedSankalp)) {
            Toast.makeText(getContext(), "Sankalp is past", Toast.LENGTH_SHORT).show();
            return;
        }

        /*if (view.getId() == R.id.category_view) {
            _launchItemSelectionDialog(SpItemSelectionDialog.SIMPLE_DIALOG_ITEM_TYPE_CATEGORY, _sankalpType);
        } else*/
        if (view.getId() == R.id.item_view) {
//            if (_editedSankalp.getCategory() == null) {
//                Toast.makeText(getContext(), getString(R.string.selectCategoryFirst), Toast.LENGTH_SHORT).show();
//            } else {
            _launchItemSelectionDialog(_sankalpType);
//            }
        } else if (view.getId() == R.id.period_view) {

            PopupMenu popup = new PopupMenu(getContext(), view, Gravity.RIGHT | Gravity.TOP);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.menu_edit_period, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int intentFilter = -1;
                    switch (item.getItemId()) {

                        case R.id.action_day:
                            intentFilter = SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_DAY;
                            break;
                        case R.id.action_month:
                            intentFilter = SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_MONTH;
                            break;
                        case R.id.action_year:
                            intentFilter = SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_YEAR;
                            break;
                        case R.id.action_lifetime:
                            setPeriod(SpDateUtils.beginOfDate(Calendar.getInstance()), null);
                            break;
                        case R.id.action_range:
                            intentFilter = SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_RANGE;
                            break;
                    }
                    if (intentFilter > -1) {
                        _showDialog(intentFilter);
                    }
                    return false;
                }
            });
            popup.show();

        } else if (view.getId() == R.id.exTar_view) {
            String title = _editedSankalp.getSankalpType() == SpConstants.SANKALP_TYPE_TYAG ?
                    getString(R.string.tyagExceptions) : getString(R.string.niyamFrequency);
            _launchNumberPickerDialog(SpConstants.FRAGMENT_TAG_EXTAR, R.layout.fragment_two_number_picker_dialog, title);
        } else if (view.getId() == R.id.currentCount_view) {
            String title = _editedSankalp.getSankalpType() == SpConstants.SANKALP_TYPE_TYAG ?
                    getString(R.string.exception_left_label) : getString(R.string.frequency_done_label);
            _launchNumberPickerDialog(SpConstants.FRAGMENT_TAG_CURRENT_COUNT, R.layout.fragment_number_picker_dialog, title);
        } else if (view.getId() == R.id.notification_view) {
            SwitchCompat s = (SwitchCompat) view.findViewById(R.id.switch1);
            s.toggle();
        } else if (view.getId() == R.id.description_view) {
            _launchSimpleAlertDialog(null, SpConstants.FRAGMENT_TAG_DESCRIPTION, R.layout.description_view, getString(R.string.AddDescription));
        }
    }

    private void _showDialog(int periodKey) {
        SpPeriodDialog d = new SpPeriodDialog();
        d.setParentFragment(this);
        Bundle args = new Bundle();
        args.putInt(SpConstants.INTENT_KEY_SANKALP_PERIOD, periodKey);
        d.setArguments(args);
        d.show(getFragmentManager(), "SpPeriodDialog");
    }

    private void _launchNumberPickerDialog(String tag, int layoutId, String title) {
        _launchSimpleAlertDialog(new SpNumberPickerDialog(), tag, layoutId, title);
    }

    private void _launchSimpleAlertDialog(SpSimpleAlertDialog d, String tag, int layoutId, String title) {

        if (d == null) {
            d = new SpSimpleAlertDialog();
        }
        d.setTargetFragment(this, 300);
        Bundle args = new Bundle();
        args.putInt(SpSimpleAlertDialog.AD_LAYOUT_ID, layoutId);
        args.putString(SpSimpleAlertDialog.AD_TITLE, title);
        d.setArguments(args);
        d.show(getFragmentManager(), tag);
    }

    private void _launchItemSelectionDialog(int id) {
        SpItemSelectionDialog d = new SpItemSelectionDialog();
        d.setParentFragment(this);
        d.setTargetFragment(this, 300);
        Bundle b = new Bundle();
        b.putInt(SpConstants.INTENT_KEY_SANKALP_TYPE, id);
        //d.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
        d.setArguments(b);
        //d.show(getFragmentManager(), "SpItemSelectionDialog");
        // The device is smaller, so show the fragment fullscreen
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        // For a little polish, specify a transition animation
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        // To make it fullscreen, use the 'content' root view as the container
        // for the fragment, which is always the root view for the activity
        transaction.replace(android.R.id.content, d)
                .addToBackStack(null).commit();
    }

//    public void setCategory(SpCategory category) {
//        _editedSankalp.setCategory(category);
//        _editedSankalp.setCategoryID(category.getId());
//        _loadView(_editedSankalp);
//    }

    public void setCategoryItem(SpCategoryItem categoryItem) {
        _editedSankalp.setItem(categoryItem);
        _editedSankalp.setItemId(categoryItem.getId());
        _editedSankalp.setCategoryID(categoryItem.getCategoryId());
        _updateView(R.id.item_view, _editedSankalp);
    }

    public void setPeriod(Date fromDate, Date toDate) {
        if (fromDate == null && toDate == null) return;
        else if (fromDate != null && toDate == null) {
            _editedSankalp.setLifetime(SpConstants.SANKALP_IS_LIFTIME_TRUE);
            _editedSankalp.setFromDate(fromDate);
            _editedSankalp.setToDate(null);
        } else {
            _editedSankalp.setLifetime(SpConstants.SANKALP_IS_LIFTIME_FALSE);
            _editedSankalp.setFromDate(fromDate);
            _editedSankalp.setToDate(toDate);
        }
        _updateView(R.id.period_view, _editedSankalp);
    }

    public ArrayList<Integer> getExceptionFrequencyList() {
        if (_editedSankalp == null) return null;
        ArrayList<Integer> frequencies = new ArrayList<Integer>();
        frequencies.add(SpExceptionOrTarget.EXCEPTION_OR_TARGET_TOTAL);
        if (_editedSankalp.getFromDate() != null) {
            if (_editedSankalp.getToDate() == null) {
                frequencies.add(SpExceptionOrTarget.EXCEPTION_OR_TARGET_DAILY);
                frequencies.add(SpExceptionOrTarget.EXCEPTION_OR_TARGET_WEEKLY);
                frequencies.add(SpExceptionOrTarget.EXCEPTION_OR_TARGET_MONTHLY);
                frequencies.add(SpExceptionOrTarget.EXCEPTION_OR_TARGET_YEARLY);
            } else {
                long numDays = SpDateUtils.subtract(_editedSankalp.getToDate(), _editedSankalp.getFromDate(), Calendar.DATE);
                if (numDays > 0) {
                    frequencies.add(SpExceptionOrTarget.EXCEPTION_OR_TARGET_DAILY);
                }
                if (numDays > 7) {
                    frequencies.add(SpExceptionOrTarget.EXCEPTION_OR_TARGET_WEEKLY);
                }
                if (numDays > 31) {
                    frequencies.add(SpExceptionOrTarget.EXCEPTION_OR_TARGET_MONTHLY);
                }
                if (numDays > 366) {
                    frequencies.add(SpExceptionOrTarget.EXCEPTION_OR_TARGET_YEARLY);
                }
            }
        }
        return frequencies;
    }

    @Override
    public void onSimpleAlertDialogPositiveClick(AlertDialog dialog, String tag) {
        if (tag.equals(SpConstants.FRAGMENT_TAG_DESCRIPTION)) {
            EditText e = (EditText) dialog.findViewById(R.id.sankalpDescription);
            String desc = e.getText().toString();
            if (!TextUtils.isEmpty(desc)) {
                _editedSankalp.setDescription(desc);
                _updateView(R.id.description_view, _editedSankalp);
            }
        } else if (tag.equals(SpConstants.FRAGMENT_TAG_EXTAR)) {
            NumberPicker np = (NumberPicker) dialog.findViewById(R.id.number_picker);
            NumberPicker left = (NumberPicker) dialog.findViewById(R.id.number_picker_left);

            if (_editedSankalp.getExceptionOrTarget() == null) {
                SpExceptionOrTarget et = new SpExceptionOrTarget(left.getValue(), getContext());
                _editedSankalp.setExceptionOrTarget(et);
            }
            _editedSankalp.getExceptionOrTarget().setExceptionOrTargetCount(np.getValue());
            _editedSankalp.getExceptionOrTarget().setId(left.getValue());
            _updateView(R.id.exTar_view, _editedSankalp);
        } else if (tag.equals(SpConstants.FRAGMENT_TAG_CURRENT_COUNT)) {
            NumberPicker np = (NumberPicker) dialog.findViewById(R.id.number_picker);
            _editedSankalp.getExceptionOrTarget().setExceptionOrTargetCountCurrent(np.getValue());
            _updateView(R.id.currentCount_view, _editedSankalp);
        }
    }

    @Override
    public void onSimpleAlertDialogNegativeClick(AlertDialog dialog, String tag) {
        // Don't do anything
    }

    private class DataLoaderTask extends AsyncTask<Void, Void, Boolean> {

        final static int REQUEST_TYPE_UPDATE = 0;
        final static int REQUEST_TYPE_ADD = 1;
        static final int REQUEST_TYPE_SANKALP = 2;
        private int _id;
        private int _requestType;

        DataLoaderTask(int id, int requestType) {
            _id = id;
            _requestType = requestType;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            SpContentProvider provider = SpContentProvider.getInstance(getContext());
            if (_requestType == REQUEST_TYPE_UPDATE) {
                if (!_originalSankalp.isSame(_editedSankalp)) {
                    provider.updateSankalp(_editedSankalp);
                }
                int currentCount = _editedSankalp.getExceptionOrTarget().getExceptionOrTargetCountCurrent();
                if (_originalSankalp.getExceptionOrTarget().getExceptionOrTargetCountCurrent() != currentCount) {
                    provider.addExTarEntry(_originalSankalp.getId(), currentCount, new Date());
                }
            } else if (_requestType == REQUEST_TYPE_ADD) {
                provider.addSankalp(_editedSankalp);
            } else if (_requestType == REQUEST_TYPE_SANKALP) {
                _originalSankalp = provider.getSankalpById(_id);
                _editedSankalp = provider.getSankalpById(_id); // TODO: Add clone
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {
                if (_requestType == REQUEST_TYPE_UPDATE) {
                    getActivity().setResult(Activity.RESULT_OK);
                    NavUtils.navigateUpFromSameTask(getActivity());
                } else if (_requestType == REQUEST_TYPE_ADD) {
                    getActivity().setResult(Activity.RESULT_OK);
                    NavUtils.navigateUpFromSameTask(getActivity());
                } else if (_requestType == REQUEST_TYPE_SANKALP) {
                    _isEditMode = true;
                    _loadView(_editedSankalp);
                }
            } else {
                // Error
            }
        }
    }
}
