package com.ravijain.sankalp.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.ravijain.sankalp.R;
import com.ravijain.sankalp.data.SpDataConstants;
import com.ravijain.sankalp.fragments.SpSankalpListFragment;
import com.ravijain.sankalp.support.SpDateUtils;

import java.util.Calendar;

public class SpSankalpList extends AppCompatActivity {

    private PagerAdapter _pagerAdapter;
    private ViewPager _viewPager;
    private int _intentListFilter;
    private int _sankalpType;
    private Calendar _launchDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sp_sankalp_list);

        _sankalpType = getIntent().getIntExtra(SpConstants.INTENT_KEY_SANKALP_TYPE, SpDataConstants.SANKALP_TYPE_BOTH);
        _intentListFilter = getIntent().getIntExtra(SpConstants.INTENT_KEY_SANKALP_LIST_FILTER, SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_CURRENT);
        long time = getIntent().getLongExtra(SpConstants.INTENT_KEY_SANKALP_LIST_FILTER_DATE_VALUE, -1);
        if (time > -1) {
            _launchDate = Calendar.getInstance();
            _launchDate.setTimeInMillis(time);
        }

        _viewPager = (ViewPager) findViewById(R.id.vpPager);
        if (_isStatic()) {
            _pagerAdapter =
                    new SpStaticPagerAdapter(
                            getSupportFragmentManager());
        } else {
            _pagerAdapter =
                    new SpDynamicPagerAdapter(
                            getSupportFragmentManager());

        }
        _viewPager.setAdapter(_pagerAdapter);


        ((ViewPager.LayoutParams) (findViewById(R.id.pager_header)).getLayoutParams()).isDecor = true;

    }

    private boolean _isStatic() {
        return _intentListFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_CURRENT ||
                _intentListFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_LIFETIME ||
                _intentListFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_UPCOMING ||
                _intentListFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_ALL;
    }

    private Bundle _createBundle(int sankalpType, int intentListFilter, long time)
    {
        Bundle args = new Bundle();
        args.putLong(SpConstants.INTENT_KEY_SANKALP_LIST_FILTER_DATE_VALUE, time);
        args.putInt(SpConstants.INTENT_KEY_SANKALP_TYPE, SpDataConstants.SANKALP_TYPE_BOTH);
        //args.putInt(SpConstants.INTENT_KEY_SANKALP_LIST_FILTER_SANKALP_TYPE, sankalpType);
        args.putInt(SpConstants.INTENT_KEY_SANKALP_LIST_FILTER, intentListFilter);
        return args;
    }

    private Fragment _getListFragment(int sankalpType, int intentListFilter, long time)
    {
        Fragment fragment = new SpSankalpListFragment();
        fragment.setArguments(_createBundle(sankalpType, intentListFilter, time));
        return fragment;
    }

    private class SpDynamicPagerAdapter extends FragmentStatePagerAdapter {
        public SpDynamicPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            long time = _getTime(i).getTimeInMillis();
            return _getListFragment(_sankalpType, _intentListFilter, time);
        }

        private Calendar _getTime(int position) {
            if (position == 0) {
                return _launchDate;
            } else {
                Calendar c = Calendar.getInstance();
                c.setTime(_launchDate.getTime());
                if (_intentListFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_DAY) {
                    c.add(Calendar.DAY_OF_YEAR, position);
                }
                else if (_intentListFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_MONTH) {
                    c.add(Calendar.MONTH, position);
                }
                else if (_intentListFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_YEAR) {
                    c.add(Calendar.YEAR, position);
                }
                return c;
            }
        }

        @Override
        public int getCount() {
            return 100;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            Calendar c = _getTime(position);
            if (_intentListFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_DAY) {
                return SpDateUtils.getFriendlyDateShortString(c.getTime());
            }
            else if (_intentListFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_MONTH) {
                return SpDateUtils.getMonthString(c);
            }
            else if (_intentListFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_YEAR) {
                return SpDateUtils.getYearString(c);
            }
            return String.valueOf(position);
        }
    }

    private class SpStaticPagerAdapter extends FragmentPagerAdapter {

        int[] _intentListFilter = {SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_CURRENT, SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_LIFETIME,
                SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_UPCOMING, SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_ALL};
        String[] _titles = {getString(R.string.current), getString(R.string.lifetime_db), getString(R.string.upcoming), getString(R.string.all)};

        public SpStaticPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return _getListFragment(_sankalpType, _intentListFilter[position], -1);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return _titles[position];
        }
    }

//    private class SpStaticPagerAdapter extends FragmentPagerAdapter {
//
//        String cs = getString(R.string.current);
//        String ls = getString(R.string.lifetime_db);
//        String us = getString(R.string.upcoming);
//        String as = getString(R.string.all);
//
//        public SpStaticPagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            int intentListFilter;
//            String title = _getTitle(position);
//            if (title.equals(cs)) {
//                intentListFilter = SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_CURRENT;
//            }
//            else if (title.equals(ls)) {
//                intentListFilter = SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_LIFETIME;
//            }
//            else if (title.equals(us)) {
//                intentListFilter = SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_UPCOMING;
//            }
//            else {
//                intentListFilter = SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_ALL;
//            }
//
//            return _getListFragment(_sankalpType, intentListFilter, -1);
//        }
//
//        @Override
//        public int getCount() {
//            return 4;
//        }
//
//        private String _getTitle(int position) {
//
//            if (_intentListFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_CURRENT) {
//                switch (position) {
//                    case 0:
//                        return cs;
//                    case 1:
//                        return ls;
//                    case 2:
//                        return us;
//                    case 3:
//                        return as;
//                }
//            } else if (_intentListFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_LIFETIME) {
//                switch (position) {
//                    case 0:
//                        return ls;
//                    case 1:
//                        return cs;
//                    case 2:
//                        return us;
//                    case 3:
//                        return as;
//                }
//            } else if (_intentListFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_UPCOMING) {
//                switch (position) {
//                    case 0:
//                        return us;
//                    case 1:
//                        return cs;
//                    case 2:
//                        return ls;
//                    case 3:
//                        return as;
//                }
//            } else {
//                switch (position) {
//                    case 0:
//                        return as;
//                    case 1:
//                        return cs;
//                    case 2:
//                        return ls;
//                    case 3:
//                        return us;
//                }
//            }
//            return "";
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return _getTitle(position);
//        }
//    }
}
