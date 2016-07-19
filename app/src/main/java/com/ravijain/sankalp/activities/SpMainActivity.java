package com.ravijain.sankalp.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ravijain.sankalp.R;
import com.ravijain.sankalp.data.SpUser;
import com.ravijain.sankalp.data.SpContentProvider;
import com.ravijain.sankalp.fragments.SpDashboardFragment;

public class SpMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SpContentProvider provider = SpContentProvider.getInstance(getApplicationContext());
        SpUser user = provider.getUser();
        Fragment fragment;
        boolean forceUserScreen = false;
        if (forceUserScreen || user == null) {
            Intent intent = new Intent(this, SpUserSetupActivity.class);
            startActivity(intent);
        }
        else {
            setContentView(R.layout.activity_main);
        }
    }
}
