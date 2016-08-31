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

import java.util.Locale;

/**
 * Created by ravijain on 8/30/2016.
 */
public class SpSettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{

    public static final String KEY_PREF_LANGUAGE = "pref_language";

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        // Load the preferences from an XML resource
//        addPreferencesFromResource(R.xml.sankalp_preferences);
//    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.sankalp_preferences);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        _updateSummary(sharedPreferences, KEY_PREF_LANGUAGE);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        if (key.equals(KEY_PREF_LANGUAGE)) {
            setLangRecreate(sharedPreferences.getString(key, ""));
            _updateSummary(sharedPreferences, key);
        }
    }

    public void setLangRecreate(String langval) {
        Configuration config = getActivity().getResources().getConfiguration();
        Locale locale = new Locale(langval);
        Locale.setDefault(locale);
        config.locale = locale;
        getActivity().getResources().updateConfiguration(config, getActivity().getResources().getDisplayMetrics());
        getActivity().recreate();
    }

    private void _updateSummary(SharedPreferences sharedPreferences,
                                String key)
    {
        if (key.equals(KEY_PREF_LANGUAGE)) {
            ListPreference listPreference = (ListPreference) findPreference(key);
            // Set summary to be the user-description for the selected value
            int prefIndex = listPreference.findIndexOfValue(sharedPreferences.getString(key, ""));
            if (prefIndex >= 0) {
                listPreference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        }
    }
}
