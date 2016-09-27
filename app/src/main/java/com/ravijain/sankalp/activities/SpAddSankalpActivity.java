package com.ravijain.sankalp.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.ravijain.sankalp.R;
import com.ravijain.sankalp.fragments.SpAddSankalpFragment;

public class SpAddSankalpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme(R.style.SankalpTheme);
        setContentView(R.layout.activity_sp_add_sankalp);

        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new SpAddSankalpFragment()).commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_clear_black_24dp);
        }
    }

}
