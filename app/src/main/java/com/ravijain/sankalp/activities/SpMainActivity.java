package com.ravijain.sankalp.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ravijain.sankalp.R;
import com.ravijain.sankalp.data.SpUser;
import com.ravijain.sankalp.data.SpContentProvider;
import com.ravijain.sankalp.fragments.SpDashboardFragment;
import com.ravijain.sankalp.fragments.SpUserSetupFragment;

public class SpMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SpContentProvider provider = SpContentProvider.getInstance(getApplicationContext());
        SpUser user = provider.getUser();
        Fragment fragment;
        if (user == null) {
            fragment = new SpUserSetupFragment();
        }
        else {
            fragment = new SpDashboardFragment();
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.main_container, fragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }
}
