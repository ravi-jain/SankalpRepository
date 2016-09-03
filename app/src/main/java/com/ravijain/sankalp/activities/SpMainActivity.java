package com.ravijain.sankalp.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ravijain.sankalp.R;
import com.ravijain.sankalp.data.SpUser;
import com.ravijain.sankalp.data.SpContentProvider;
import com.ravijain.sankalp.fragments.SpCardDashboardFragment;
import com.ravijain.sankalp.fragments.SpChartCalendarDashboard;
import com.ravijain.sankalp.fragments.SpDashboardFragment;
import com.ravijain.sankalp.fragments.SpSettingsFragment;
import com.ravijain.sankalp.fragments.SpUserProfileFragment;
import com.ravijain.sankalp.support.SpAlarmReceiver;
import com.ravijain.sankalp.support.SpCaldroidCalendarViewHandler;
import com.ravijain.sankalp.support.SpUtils;

import java.util.Calendar;

public class SpMainActivity extends AppCompatActivity implements ListView.OnItemClickListener {

    private DrawerLayout _drawerLayout;
    private ListView _drawerListView;
    private String[] _drawerList;
    private ActionBarDrawerToggle _drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        setLangRecreate(prefs.getString(SpSettingsFragment.KEY_PREF_LANGUAGE, ""));

        SpContentProvider provider = SpContentProvider.getInstance(getApplicationContext());
        SpUser user = provider.getUser();
        boolean forceUserScreen = false;
        if (forceUserScreen || user == null) {
            Intent intent = new Intent(this, SpUserSetupActivity.class);
            startActivity(intent);
        } else {

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

            setContentView(R.layout.activity_main);
            _drawerList = getResources().getStringArray(R.array.drawerList);

            _drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            _drawerListView = (ListView) findViewById(R.id.navList);

            // Set the adapter for the list view
            _drawerListView.setAdapter(new DrawerAdapter(this,
                    _drawerList));
            // Set the list's click listener
            _drawerListView.setOnItemClickListener(this);

            _loadChartCalendarDashboardFragment();
            setupDrawer();


            SpUtils.startAlarm(getApplicationContext());

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
//        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.edittextbox));
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
        } else if (_drawerList[i].equals(getString(R.string.Dashboard))) {
            if (!(currentFragment instanceof SpChartCalendarDashboard)) {
                _loadChartCalendarDashboardFragment();
                _drawerListView.setItemChecked(i, true);
                setTitle(getString(R.string.title_activity_sp_sankalp_list));

                _drawerLayout.closeDrawer(_drawerListView);
            }
        } else if (_drawerList[i].equals(getString(R.string.UpdateProfile))) {
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
            SpCaldroidCalendarViewHandler cal = new SpCaldroidCalendarViewHandler(this, SpCaldroidCalendarViewHandler.CONTEXT_FULL, getSupportFragmentManager(), R.id.content_frame);
            cal.constructCalendarView();

            // Attach to the activity
            //getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, calViewFragment).commit();

            setTitle(R.string.calendarViewLabel);
            _drawerLayout.closeDrawer(_drawerListView);
        } else if (_drawerList[i].equals(getString(R.string.settings))) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new SpSettingsFragment())
                    .commit();
            setTitle(getString(R.string.settings));
            _drawerLayout.closeDrawer(_drawerListView);
        } else {
            Toast.makeText(SpMainActivity.this, _drawerList[i], Toast.LENGTH_SHORT).show();
        }
    }

    private class DrawerAdapter extends ArrayAdapter<String> {
        int[] icons = {R.drawable.ic_dashboard_black_24dp, R.drawable.ic_person_black_24dp, R.drawable.ic_description_black_24dp, R.drawable.ic_settings_black_24dp, R.drawable.ic_contact_mail_black_24dp};

        DrawerAdapter(Context context, String[] items) {
            super(context, 0, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String item = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_drawer, parent, false);
            }
            TextView title = (TextView) convertView.findViewById(R.id.drawer_list_tv);
            title.setText(item);
            ImageView iconView = (ImageView) convertView.findViewById(R.id.drawer_list_icon);
            iconView.setImageResource(icons[position]);


            return convertView;
        }
    }

}
