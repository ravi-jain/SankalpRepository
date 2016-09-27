package com.ravijain.sankalp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;

import com.ravijain.sankalp.data.SpContentProvider;
import com.ravijain.sankalp.data.SpUser;
import com.ravijain.sankalp.fragments.SpSettingsFragment;
import com.ravijain.sankalp.support.SpUtils;

public class SpSplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Intent intent;
        if (!sharedPreferences.getBoolean(SpSettingsFragment.KEY_USER_REGISTERED, false)) {
            intent = new Intent(this, SpUserSetupActivity.class);
        }
        else {
            intent = new Intent(this, SpMaterialDashboardActivity.class);
        }

        // Set the language
        SpUtils.updateLanguage(getApplicationContext(), sharedPreferences.getString(SpSettingsFragment.KEY_PREF_LANGUAGE, "en_US"));
        startActivity(intent);
        finish();
    }
}
