package com.ravijain.sankalp.fragments;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;

import com.ravijain.sankalp.R;
import com.ravijain.sankalp.support.SpUtils;

import java.util.Locale;

/**
 * Created by ravijain on 8/30/2016.
 */
public class SpSettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{

    public static final String KEY_PREF_LANGUAGE = "pref_language";
    public static final String KEY_PREF_REMINDERS = "pref_reminders";
    public static final String KEY_PREF_ALARM_REGISTERED = "pref_alarm_registered";
    public static final String KEY_USER_REGISTERED = "pref_user_registered";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.sankalp_preferences);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        _updateSummary(sharedPreferences, KEY_PREF_LANGUAGE);
        _updateSummary(sharedPreferences, KEY_PREF_REMINDERS);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        if (key.equals(KEY_PREF_LANGUAGE)) {
            SpUtils.updateLanguage(getContext(), sharedPreferences.getString(key, "en_US"));
        }
        else if (key.equals(KEY_PREF_REMINDERS)) {
            if (sharedPreferences.getBoolean(SpSettingsFragment.KEY_PREF_REMINDERS, true)) {
                SpUtils.startAlarm(getContext());
            }
            else {
                SpUtils.stopAlarm(getContext());
            }
        }
        _updateSummary(sharedPreferences, key);
    }

    private void _updateSummary(SharedPreferences sharedPreferences,
                                String key)
    {
        if (key.equals(KEY_PREF_LANGUAGE)) {
            ListPreference listPreference = (ListPreference) findPreference(key);
            // Set summary to be the user-description for the selected value
            int prefIndex = listPreference.findIndexOfValue(sharedPreferences.getString(key, "en_US"));
            if (prefIndex >= 0) {
                listPreference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        }
        else if (key.equals(KEY_PREF_REMINDERS)) {
            if (sharedPreferences.getBoolean(key, true)) {
                findPreference(key).setSummary(getString(R.string.showRemindersEnabled));
            }
            else {
                findPreference(key).setSummary(getString(R.string.showRemindersDisabled));
            }
        }
    }
}
