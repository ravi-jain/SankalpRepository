package com.ravijain.sankalp.support;

/**
 * Created by ravijain on 7/5/2016.
 */
public interface SpConstants {


    public static final int ACTIVITY_REQUEST_CODE = 100;
    public static final String INTENT_KEY_SANKALP_TYPE = "SANKALP_TYPE";
    public static final String INTENT_KEY_SANKALP_LIST_FILTER = "SANKALP_LIST_FILTER";
    public static final String INTENT_KEY_SANKALP_LIST_FILTER_DATE_VALUE = "SANKALP_LIST_FILTER_DATE_VALUE";
    public static final String INTENT_KEY_SANKALP_PERIOD = "INTENT_KEY_SANKALP_PERIOD";
    public static final String INTENT_KEY_SIMPLE_DIALOG_ITEM_TYPE = "INTENT_KEY_SIMPLE_DIALOG_ITEM_TYPE";
    public static final String INTENT_KEY_SIMPLE_DIALOG_ITEM_TYPE_ID = "INTENT_KEY_SIMPLE_DIALOG_ITEM_TYPE_ID";
    public static final String INTENT_KEY_NUMBER_PICKER_TITLE = "INTENT_KEY_NUMBER_PICKER_TITLE";
    public static final String INTENT_KEY_NUMBER_PICKER_TYPE = "INTENT_KEY_NUMBER_PICKER_TYPE";
    //public static final String INTENT_KEY_SANKALP_LIST_FILTER_SANKALP_TYPE = "SANKALP_TYPE_FILTER";

    public static final int INTENT_VALUE_SANKALP_LIST_FILTER_CURRENT = 0;
    public static final int INTENT_VALUE_SANKALP_LIST_FILTER_UPCOMING = 2;
    public static final int INTENT_VALUE_SANKALP_LIST_FILTER_ALL = 3;
    public static final int INTENT_VALUE_SANKALP_LIST_FILTER_LIFETIME = 1;
    public static final int INTENT_VALUE_SANKALP_LIST_FILTER_TODAY = 4;
    public static final int INTENT_VALUE_SANKALP_LIST_FILTER_TOMORROW = 5;
    public static final int INTENT_VALUE_SANKALP_LIST_FILTER_MONTH = 6;
    public static final int INTENT_VALUE_SANKALP_LIST_FILTER_YEAR = 7;
    public static final int INTENT_VALUE_SANKALP_LIST_FILTER_DAY = 8;
    public static final int INTENT_VALUE_SANKALP_LIST_FILTER_RANGE = 9;

    public static final String INTENT_KEY_SANKALP_ID = "SANKALP_ID";
    public static final String IS_USER_ALREADY_CREATED = "IS_USER_ALREADY_CREATED";

    public static final int SANKALP_TYPE_TYAG = 0;
    public static final int SANKALP_TYPE_NIYAM = 1;
    public static final int SANKALP_TYPE_BOTH = 2;
    public static final int SANKALP_IS_LIFTIME_FALSE = 0;
    public static final int SANKALP_IS_LIFTIME_TRUE = 1;

    public static final String FRAGMENT_TAG_DESCRIPTION = "description";
    public static final String FRAGMENT_TAG_EXTAR = "exTar";
    public static final String FRAGMENT_TAG_CURRENT_COUNT = "currentCount";
    public static final String FRAGMENT_TAG_ITEM = "item";
    public static final String FRAGMENT_TAG_CATEGORY = "category";
    public static final String FRAGMENT_TAG_FILTER = "filter";
    public static final String FRAGMENT_TAG_INTERVAL = "interval";
    public static final String FRAGMENT_TAG_SORT = "sort";
    String FRAGMENT_TAG_DAILY_SANKALP = "dailySankalp";

    public static final String INTENT_SORTID = "INTENT_SORTID";
    public static final int SORT_ORDER_ASCENDING = 1;
    public static final int SORT_ORDER_DESCENDING = -1;
    String FRAGMENT_TAG_USER_PROFILE = "user_profile";
}
