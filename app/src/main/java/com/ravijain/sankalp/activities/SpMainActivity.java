package com.ravijain.sankalp.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.ravijain.sankalp.R;
import com.ravijain.sankalp.data.SpUser;
import com.ravijain.sankalp.data.SpContentProvider;
import com.ravijain.sankalp.fragments.SpCardDashboardFragment;
import com.ravijain.sankalp.fragments.SpChartCalendarDashboard;
import com.ravijain.sankalp.fragments.SpDashboardFragment;
import com.ravijain.sankalp.fragments.SpUserProfileFragment;
import com.ravijain.sankalp.support.SpCalendarViewHandler;
import com.roomorama.caldroid.CaldroidFragment;

import java.util.Calendar;

public class SpMainActivity extends AppCompatActivity implements ListView.OnItemClickListener {

    private DrawerLayout _drawerLayout;
    private ListView _drawerListView;
    private String[] _drawerList;
    private ActionBarDrawerToggle _drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SpContentProvider provider = SpContentProvider.getInstance(getApplicationContext());
        SpUser user = provider.getUser();
        boolean forceUserScreen = false;
        if (forceUserScreen || user == null) {
            Intent intent = new Intent(this, SpUserSetupActivity.class);
            startActivity(intent);
        } else {
            setContentView(R.layout.activity_main);
            _drawerList = getResources().getStringArray(R.array.drawerList);

            _drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            _drawerListView = (ListView) findViewById(R.id.navList);

            // Set the adapter for the list view
            _drawerListView.setAdapter(new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, _drawerList));
            // Set the list's click listener
            _drawerListView.setOnItemClickListener(this);

            _loadChartCalendarDashboardFragment();
            setupDrawer();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    private void _loadDashboardFragment() {
        Fragment f = new SpCardDashboardFragment();
        FragmentManager man = getSupportFragmentManager();
        man.beginTransaction().replace(R.id.content_frame, f).commit();
    }

    private void _loadChartCalendarDashboardFragment() {
        Fragment f = new SpChartCalendarDashboard();
        FragmentManager man = getSupportFragmentManager();
        man.beginTransaction().replace(R.id.content_frame, f).commit();
    }

    private void setupDrawer() {
        _drawerToggle = new ActionBarDrawerToggle(this, _drawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
//                getSupportActionBar().setTitle("Navigation!");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
//                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        _drawerToggle.setDrawerIndicatorEnabled(true);
        _drawerLayout.setDrawerListener(_drawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        if (_drawerToggle != null) {
            _drawerToggle.syncState();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (_drawerToggle != null) {
            _drawerToggle.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

//        getMenuInflater().inflate(R.menu.menu_user_profile, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Activate the navigation drawer toggle
        if (_drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);

        if (_drawerList[i].equals(getString(R.string.Dashboard1))) {
            if (!(currentFragment instanceof SpCardDashboardFragment)) {
                _loadDashboardFragment();
                _drawerListView.setItemChecked(i, true);
                setTitle(getString(R.string.title_activity_sp_sankalp_list));
                _drawerLayout.closeDrawer(_drawerListView);
            }
        }else if (_drawerList[i].equals(getString(R.string.Dashboard))) {
            if (!(currentFragment instanceof SpChartCalendarDashboard)) {
                _loadChartCalendarDashboardFragment();
                _drawerListView.setItemChecked(i, true);
                setTitle(getString(R.string.title_activity_sp_sankalp_list));
                _drawerLayout.closeDrawer(_drawerListView);
            }
        }

        else if (_drawerList[i].equals(getString(R.string.UpdateProfile))) {
            if (!(currentFragment instanceof SpUserProfileFragment)) {
                Fragment f = new SpUserProfileFragment();
                Bundle b = new Bundle();
                b.putBoolean(SpConstants.IS_USER_ALREADY_CREATED, true);
                f.setArguments(b);
                FragmentManager man = getSupportFragmentManager();
                man.beginTransaction().replace(R.id.content_frame, f).commit();

                _drawerListView.setItemChecked(i, true);
                setTitle(_drawerList[i]);
            }
            _drawerLayout.closeDrawer(_drawerListView);
        } else if (_drawerList[i].equals(getString(R.string.Dashboard2))) {
            Fragment f = new SpDashboardFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, f).commit();
            setTitle(getString(R.string.title_activity_sp_sankalp_list));
            _drawerLayout.closeDrawer(_drawerListView);
        } else if (_drawerList[i].equals(getString(R.string.CalendarView))) {
            SpCalendarViewHandler cal = new SpCalendarViewHandler(this, SpCalendarViewHandler.CONTEXT_FULL, getSupportFragmentManager(), R.id.content_frame);
            cal.constructCalendarView();

            // Attach to the activity
            //getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, calViewFragment).commit();

            setTitle(R.string.calendarViewLabel);
            _drawerLayout.closeDrawer(_drawerListView);
        } else {
            Toast.makeText(SpMainActivity.this, _drawerList[i], Toast.LENGTH_SHORT).show();
        }
    }


}
