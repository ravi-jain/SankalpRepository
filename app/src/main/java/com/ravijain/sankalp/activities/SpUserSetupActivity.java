package com.ravijain.sankalp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.ravijain.sankalp.R;
import com.ravijain.sankalp.fragments.SpUserProfileFragment;
import com.ravijain.sankalp.support.SpConstants;

public class SpUserSetupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme(R.style.SankalpTheme);
        setContentView(R.layout.activity_user_setup);

        getSupportFragmentManager().beginTransaction().replace(R.id.contentId, new SpUserProfileFragment(), SpConstants.FRAGMENT_TAG_USER_PROFILE).commit();

        findViewById(R.id.userRegister_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SpUserProfileFragment f = (SpUserProfileFragment) getSupportFragmentManager().findFragmentByTag(SpConstants.FRAGMENT_TAG_USER_PROFILE);
                f.registerUser();
            }
        });

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
    }
}
