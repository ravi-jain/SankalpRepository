package com.ravijain.sankalp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.ravijain.sankalp.R;
import com.ravijain.sankalp.support.SpConstants;
import com.ravijain.sankalp.support.SpDateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * Created by ravijain on 7/1/2016.
 */
public class SpContentProvider {

    private SpDBHelper _dbHelper;
    private Context _context;
    private static SpContentProvider instanceRef = null;

    private Hashtable<Integer, SpCategory> _categories = null;
    private Hashtable<Integer, SpCategoryItem> _categoryItems = null;

    private SpContentProvider(Context context) {
        _context = context;

        _dbHelper = new SpDBHelper(context);
    }

    public static SpContentProvider getInstance(Context context) {
        if (instanceRef == null) {
            instanceRef = new SpContentProvider(context);
        }
        return instanceRef;
    }

    public SpUser getUser() {
        SQLiteDatabase db = _dbHelper.getReadableDatabase();
        SpUser user = null;
        Cursor cursor = getUserCursor();
        if (cursor != null) {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            user = new SpUser(cursor.getInt(0));
            user.setProperties(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
            cursor.close();
        }
        return user;
    }

    public void addUser(SpUser user) {
        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SpTableContract.SpUserTable.COLUMN_USER_NAME, user.getName());
        values.put(SpTableContract.SpUserTable.COLUMN_USER_MOBILE, user.getMobile());
        values.put(SpTableContract.SpUserTable.COLUMN_USER_EMAIL, user.getEmail());
        values.put(SpTableContract.SpUserTable.COLUMN_USER_CITY, user.getCity());

        // Inserting Row
        db.insert(SpTableContract.SpUserTable.TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    public void updateUser(SpUser user) {

        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SpTableContract.SpUserTable.COLUMN_USER_NAME, user.getName());
        values.put(SpTableContract.SpUserTable.COLUMN_USER_MOBILE, user.getMobile());
        values.put(SpTableContract.SpUserTable.COLUMN_USER_EMAIL, user.getEmail());
        values.put(SpTableContract.SpUserTable.COLUMN_USER_CITY, user.getCity());
        /*String selection = SpTableContract.SpUserTable._ID + " = ?";
        String[] args = {String.valueOf(user.getId())};*/
        db.update(SpTableContract.SpUserTable.TABLE_NAME, values, SpTableContract.SpUserTable._ID + " = " + String.valueOf(user.getId()), null);
        db.close();
    }

    public Cursor getUserCursor() {
        SQLiteDatabase db = _dbHelper.getReadableDatabase();
        String userQuery = "SELECT  * FROM " + SpTableContract.SpUserTable.TABLE_NAME;

        Cursor cursor = db.rawQuery(userQuery, null);
        //db.close();
        return cursor;
    }

    /*public Cursor getTyagCursor() {
        SQLiteDatabase db = _dbHelper.getReadableDatabase();
        String userQuery = "SELECT  * FROM " + SpTableContract.SpTyagTable.TABLE_NAME;

        Cursor cursor = db.rawQuery(userQuery, null);
        //db.close();
        return cursor;
    }

    public Cursor getNiyamCursor() {
        SQLiteDatabase db = _dbHelper.getReadableDatabase();
        String userQuery = "SELECT  * FROM " + SpTableContract.SpNiyamTable.TABLE_NAME;

        Cursor cursor = db.rawQuery(userQuery, null);
        //db.close();
        return cursor;
    }*/

    public Hashtable<Integer, SpCategory> getAllCategories() {
        if (_categories == null) {
            _categories = new Hashtable<Integer, SpCategory>();
            SQLiteDatabase db = _dbHelper.getReadableDatabase();
            String query = "SELECT * FROM " + SpTableContract.SpCategoryTable.TABLE_NAME;
            Cursor cursor = db.rawQuery(query, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    SpCategory category = new SpCategory(cursor.getInt(cursor.getColumnIndexOrThrow(SpTableContract.SpCategoryTable._ID)),
                            cursor.getString(cursor.getColumnIndexOrThrow(SpTableContract.SpCategoryTable.COLUMN_CATEGORY_NAME)),
                            cursor.getString(cursor.getColumnIndexOrThrow(SpTableContract.SpCategoryTable.COLUMN_SUB_CATEGORY)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(SpTableContract.SpCategoryTable.COLUMN_CATEGORY_TYPE)));
                    _categories.put(category.getId(), category);
                    cursor.moveToNext();
                }
                cursor.close();
            }
            db.close();
        }

        return _categories;
    }

    public Hashtable<Integer, SpCategoryItem> getAllCategoryItems() {
        if (_categoryItems == null) {
            _categoryItems = new Hashtable<Integer, SpCategoryItem>();
            SQLiteDatabase db = _dbHelper.getReadableDatabase();
            String query = "SELECT * FROM " + SpTableContract.SpItemTable.TABLE_NAME;

            Cursor cursor = db.rawQuery(query, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    SpCategoryItem item = new SpCategoryItem(cursor.getInt(cursor.getColumnIndexOrThrow(SpTableContract.SpItemTable._ID)),
                            cursor.getString(cursor.getColumnIndexOrThrow(SpTableContract.SpItemTable.COLUMN_ITEM_NAME)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(SpTableContract.SpItemTable.COLUMN_ITEM_CATEGORY_ID)));
                    _categoryItems.put(item.getId(), item);
                    cursor.moveToNext();
                }
                cursor.close();
            }
            db.close();
        }

        return _categoryItems;
    }

    /*public Hashtable<String, SpCategory> getAllCategoriesBySankalpType(int type) {
        Hashtable<String, SpCategory> categories = new Hashtable<String, SpCategory>();
        SQLiteDatabase db = _dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + SpTableContract.SpCategoryTable.TABLE_NAME + " WHERE "
                + SpTableContract.SpCategoryTable.COLUMN_CATEGORY_TYPE + " =" + type;
        if (type != SpDataConstants.SANKALP_TYPE_BOTH) {
            query += " OR " + SpTableContract.SpCategoryTable.COLUMN_CATEGORY_TYPE + " =" + SpDataConstants.SANKALP_TYPE_BOTH;
        }
        query += " ORDER BY " + SpTableContract.SpCategoryTable.COLUMN_CATEGORY_NAME + " ASC";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                SpCategory category = new SpCategory(cursor.getInt(cursor.getColumnIndexOrThrow(SpTableContract.SpCategoryTable._ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(SpTableContract.SpCategoryTable.COLUMN_CATEGORY_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(SpTableContract.SpCategoryTable.COLUMN_SUB_CATEGORY)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(SpTableContract.SpCategoryTable.COLUMN_CATEGORY_TYPE)));
                categories.put(category.getCategoryName(), category);
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return categories;
    }*/

    public ArrayList<SpCategory> getAllCategoriesBySankalpType(int type) {
        ArrayList<SpCategory> categories = new ArrayList<SpCategory>();
        SQLiteDatabase db = _dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + SpTableContract.SpCategoryTable.TABLE_NAME + " WHERE "
                + SpTableContract.SpCategoryTable.COLUMN_CATEGORY_TYPE + " =" + type;
        if (type != SpConstants.SANKALP_TYPE_BOTH) {
            query += " OR " + SpTableContract.SpCategoryTable.COLUMN_CATEGORY_TYPE + " =" + SpConstants.SANKALP_TYPE_BOTH;
        }
        query += " ORDER BY " + SpTableContract.SpCategoryTable.COLUMN_CATEGORY_NAME;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                SpCategory category = new SpCategory(cursor.getInt(cursor.getColumnIndexOrThrow(SpTableContract.SpCategoryTable._ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(SpTableContract.SpCategoryTable.COLUMN_CATEGORY_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(SpTableContract.SpCategoryTable.COLUMN_SUB_CATEGORY)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(SpTableContract.SpCategoryTable.COLUMN_CATEGORY_TYPE)));
                categories.add(category);
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return categories;
    }

//    public Hashtable<String, SpCategoryItem> getAllCategoryItemsByCategoryId(int id) {
//        Hashtable<String, SpCategoryItem> items = new Hashtable<String, SpCategoryItem>();
//        SQLiteDatabase db = _dbHelper.getReadableDatabase();
//        String query = "SELECT * FROM " + SpTableContract.SpItemTable.TABLE_NAME + " WHERE "
//                + SpTableContract.SpItemTable.COLUMN_ITEM_CATEGORY_ID + " =" + id;
//        query += " ORDER BY " + SpTableContract.SpItemTable.COLUMN_ITEM_NAME + " DESC";
//
//        Cursor cursor = db.rawQuery(query, null);
//        if (cursor != null) {
//            cursor.moveToFirst();
//            while (cursor.isAfterLast() == false) {
//                SpCategoryItem item = new SpCategoryItem(cursor.getInt(cursor.getColumnIndexOrThrow(SpTableContract.SpItemTable._ID)),
//                        cursor.getString(cursor.getColumnIndexOrThrow(SpTableContract.SpItemTable.COLUMN_ITEM_NAME)),
//                        cursor.getString(cursor.getColumnIndexOrThrow(SpTableContract.SpItemTable.COLUMN_ITEM_DISPLAYNAME)),
//                        cursor.getInt(cursor.getColumnIndexOrThrow(SpTableContract.SpItemTable.COLUMN_ITEM_CATEGORY_ID)));
//                items.put(item.getCategoryItemName(), item);
//                cursor.moveToNext();
//            }
//            cursor.close();
//        }
//        db.close();
//        return items;
//    }

    public ArrayList<SpCategoryItem> getAllCategoryItemsByCategoryId(int id) {
        ArrayList<SpCategoryItem> items = new ArrayList<SpCategoryItem>();
        SQLiteDatabase db = _dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + SpTableContract.SpItemTable.TABLE_NAME + " WHERE "
                + SpTableContract.SpItemTable.COLUMN_ITEM_CATEGORY_ID + " =" + id;
        query += " ORDER BY " + SpTableContract.SpItemTable.COLUMN_ITEM_NAME;

        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                SpCategoryItem item = new SpCategoryItem(cursor.getInt(cursor.getColumnIndexOrThrow(SpTableContract.SpItemTable._ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(SpTableContract.SpItemTable.COLUMN_ITEM_NAME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(SpTableContract.SpItemTable.COLUMN_ITEM_CATEGORY_ID)));
                items.add(item);
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return items;
    }

    public void bulkInsertCategories(SQLiteDatabase db) {
        //SQLiteDatabase db = _dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            ArrayList<SpCategory> defaultCategories = SpCategory.getDefaultCategories();

            for(SpCategory category : defaultCategories) {
                addCategory(category, db);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

    }

    public long addCategory(SpCategory category, SQLiteDatabase db)
    {
        boolean closeDblocally = false;
        if (db == null) {
            db = _dbHelper.getWritableDatabase();
            closeDblocally = true;
        }
        ContentValues values = new ContentValues();
        if (category.getId() > -1) {
            values.put(SpTableContract.SpCategoryTable._ID, category.getId());
        }
        values.put(SpTableContract.SpCategoryTable.COLUMN_CATEGORY_NAME, category.getCategoryName());
        values.put(SpTableContract.SpCategoryTable.COLUMN_SUB_CATEGORY, category.getSubCategoryName());
        values.put(SpTableContract.SpCategoryTable.COLUMN_CATEGORY_TYPE, category.getSankalpType());
        long id = db.insert(SpTableContract.SpCategoryTable.TABLE_NAME, null, values);

        if (closeDblocally) {
            db.close();
        }

        return id;
    }

    public void bulkInsertCategoryItems(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            ArrayList<SpCategoryItem> defaultCategoryItems = SpCategoryItem.getDefaultCategoryItems();
            for (SpCategoryItem item : defaultCategoryItems) {
                addCategoryItem(item, db);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

    }

    public void addCategoryItem(SpCategoryItem item, SQLiteDatabase db)
    {
        boolean closeDblocally = false;
        if (db == null) {
            db = _dbHelper.getWritableDatabase();
            closeDblocally = true;
        }
        ContentValues values = new ContentValues();
        if (item.getId() > -1) {
            values.put(SpTableContract.SpItemTable._ID, item.getId());
        }

        values.put(SpTableContract.SpItemTable.COLUMN_ITEM_NAME, item.getCategoryItemName());
        values.put(SpTableContract.SpItemTable.COLUMN_ITEM_CATEGORY_ID, item.getCategoryId());
        db.insert(SpTableContract.SpItemTable.TABLE_NAME, null, values);

        if (closeDblocally) {
            db.close();
        }
    }

    public void addSankalp(SpSankalp sankalp) {
        ContentValues values = new ContentValues();
        values.put(SpTableContract.SpSankalpTable.COLUMN_CREATION_DATE, new Date().getTime());
        values.put(SpTableContract.SpSankalpTable.COLUMN_SANKALP_TYPE, sankalp.getSankalpType());
        values.put(SpTableContract.SpSankalpTable.COLUMN_CATEGORY_ID, sankalp.getCategoryID());
        values.put(SpTableContract.SpSankalpTable.COLUMN_ITEM_ID, sankalp.getItemId());
        values.put(SpTableContract.SpSankalpTable.COLUMN_ISLIFETIME, sankalp.isLifetime());
        values.put(SpTableContract.SpSankalpTable.COLUMN_ISNOTIFICATION_ON, sankalp.isNotificationOn());
        values.put(SpTableContract.SpSankalpTable.COLUMN_FROM_DATE, sankalp.getFromDate().getTime());
        if (sankalp.getToDate() != null) {
            values.put(SpTableContract.SpSankalpTable.COLUMN_TO_DATE, sankalp.getToDate().getTime());
        }

        SpExceptionOrTarget exceptionOrTarget = sankalp.getExceptionOrTarget();
        if (exceptionOrTarget != null && exceptionOrTarget.getExceptionOrTargetCount() > 0) {
            values.put(SpTableContract.SpSankalpTable.COLUMN_EXCEPTION_TARGET_ID, sankalp.getExceptionOrTarget().getId());
            values.put(SpTableContract.SpSankalpTable.COLUMN_EXCEPTION_TARGET_COUNT, sankalp.getExceptionOrTarget().getExceptionOrTargetCount());
        }

//        values.put(SpTableContract.SpSankalpTable.COLUMN_EXCEPTION_TARGET_CURRENT_COUNT, sankalp.getExceptionOrTarget().getExceptionOrTargetCountCurrent());
        values.put(SpTableContract.SpSankalpTable.COLUMN_DESCRIPTION, sankalp.getDescription());

        String tableName = SpTableContract.SpSankalpTable.TABLE_NAME;
//        if (sankalp.getSankalpType() == SpDataConstants.SANKALP_TYPE_TYAG) {
//            addAdditionalTyagValues((SpTyag) sankalp, values);
//            tableName = SpTableContract.SpTyagTable.TABLE_NAME;
//        } else if (sankalp instanceof SpNiyam) {
//            addAdditionalNiyamValues((SpNiyam) sankalp, values);
//            tableName = SpTableContract.SpNiyamTable.TABLE_NAME;
//        }

        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        // Inserting Row
        long sankalpId = db.insert(tableName, null, values);
        db.close(); // Closing database connection

        if (sankalpId > -1 && exceptionOrTarget != null) {
            int currentCount = exceptionOrTarget.getExceptionOrTargetCountCurrent();
            if (exceptionOrTarget.getExceptionOrTargetCount() > 0 && currentCount >= 0) {
                // Add in the ExTar Table
                Date today = SpDateUtils.getToday();
                addExTarEntry(sankalpId, currentCount, today);
            }

        }
    }

    public void addExTarEntry(long sankalpId, int currentCount, Date d) {
        String tableName = SpTableContract.SpExTarTable.TABLE_NAME;
        ContentValues values = new ContentValues();
        values.put(SpTableContract.SpExTarTable.COLUMN_SANKALP_ID, sankalpId);
        values.put(SpTableContract.SpExTarTable.COLUMN_CURRENT_COUNT, currentCount);
        values.put(SpTableContract.SpExTarTable.COLUMN_UPDATED_ON, d.getTime());

        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        db.insert(tableName, null, values);
        db.close();
    }

    public ArrayList<SpSankalp> getSankalps(int sankalpType, int listFilter) {
        return getSankalps(sankalpType, listFilter, null);
    }

    public ArrayList<SpSankalp> getSankalps(int sankalpType, int listFilter, Calendar day) {
        String tableName = SpTableContract.SpSankalpTable.TABLE_NAME;
        String userQuery = "SELECT  * FROM " + tableName;
        userQuery += _prepareWhereClause(sankalpType, listFilter, day);
        userQuery += " ORDER BY " + SpTableContract.SpSankalpTable.COLUMN_TO_DATE + " IS NULL";
        return _runSankalpQuery(userQuery);
    }

    private String _prepareWhereClause(int sankalpType, int listFilter, Calendar day) {
        String whereClause = "";
        boolean isWhereClauseAdded = false;
        if (sankalpType != SpConstants.SANKALP_TYPE_BOTH) {
            whereClause += " WHERE " + SpTableContract.SpSankalpTable.COLUMN_SANKALP_TYPE + " = " + sankalpType;
            isWhereClauseAdded = true;
        }
        if (listFilter != SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_ALL) {
            if (!isWhereClauseAdded) {
                whereClause += " WHERE ";
            } else {
                whereClause += " AND ";
            }

            if (listFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_LIFETIME) {
                whereClause += SpTableContract.SpSankalpTable.COLUMN_ISLIFETIME + " = " + SpConstants.SANKALP_IS_LIFTIME_TRUE;
            } else if (listFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_CURRENT) {
                long time = new Date().getTime();
                whereClause += "(" + SpTableContract.SpSankalpTable.COLUMN_ISLIFETIME + " = " + SpConstants.SANKALP_IS_LIFTIME_TRUE
                        + " OR " + time + " BETWEEN " + SpTableContract.SpSankalpTable.COLUMN_FROM_DATE + " AND " + SpTableContract.SpSankalpTable.COLUMN_TO_DATE + ")";
            } else if (listFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_UPCOMING) {
                long time = new Date().getTime();
                whereClause += SpTableContract.SpSankalpTable.COLUMN_FROM_DATE + " > " + time;
            } else if (listFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_TODAY) {
                Calendar today = Calendar.getInstance();
                long begin = SpDateUtils.beginOfDate(today).getTime();
                long end = SpDateUtils.endOfDate(today).getTime();
                whereClause += SpTableContract.SpSankalpTable.COLUMN_TO_DATE + " BETWEEN " + begin + " AND " + end;
            } else if (listFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_TOMORROW) {
                Calendar tomorrow = SpDateUtils.nextDate(Calendar.getInstance());
                long begin = SpDateUtils.beginOfDate(tomorrow).getTime();
                long end = SpDateUtils.endOfDate(tomorrow).getTime();
                whereClause += SpTableContract.SpSankalpTable.COLUMN_TO_DATE + " BETWEEN " + begin + " AND " + end;
            } else if (listFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_DAY) {
                if (day == null) {
                    day = Calendar.getInstance();
                }

                long begin = SpDateUtils.beginOfDate(day).getTime();
                long end = SpDateUtils.endOfDate(day).getTime();
                whereClause += SpTableContract.SpSankalpTable.COLUMN_TO_DATE + " BETWEEN " + begin + " AND " + end;
            } else if (listFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_MONTH) {
                if (day == null) {
                    day = Calendar.getInstance();
                }
                long begin = SpDateUtils.beginOfMonth(day).getTime();
                long end = SpDateUtils.endOfMonth(day).getTime();
                whereClause += SpTableContract.SpSankalpTable.COLUMN_TO_DATE + " BETWEEN " + begin + " AND " + end;
            } else if (listFilter == SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_YEAR) {
                if (day == null) {
                    day = Calendar.getInstance();
                }
                long begin = SpDateUtils.beginOfYear(day).getTime();
                long end = SpDateUtils.endOfYear(day).getTime();
                whereClause += SpTableContract.SpSankalpTable.COLUMN_TO_DATE + " BETWEEN " + begin + " AND " + end;
            }
        }

        return whereClause;
    }

    public int getSankalpsCount(int sankalpType, int listFilter, Calendar day) {
        SQLiteDatabase db = _dbHelper.getReadableDatabase();
        int count = getSankalpsCount(db, sankalpType, listFilter, day);
        db.close();

        return count;
    }

    public int getSankalpsCount(SQLiteDatabase db, int sankalpType, int listFilter, Calendar day) {
        String tableName = SpTableContract.SpSankalpTable.TABLE_NAME;
        String userQuery = "SELECT  COUNT(*) FROM " + tableName;
        userQuery += _prepareWhereClause(sankalpType, listFilter, day);

        if (db != null) {
            Cursor cursor = db.rawQuery(userQuery, null);
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            cursor.close();

            return count;
        }
        return -1;
    }

    public SpSankalpCountData getSankalpCountData() {
        SpSankalpCountData data = new SpSankalpCountData();
        SQLiteDatabase db = _dbHelper.getReadableDatabase();
//        db.beginTransaction();
//        try {
        // Current Tyags
        data.setCurrentTyags(getSankalpsCount(db, SpConstants.SANKALP_TYPE_TYAG, SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_CURRENT, null));
        data.setCurrentNiyams(getSankalpsCount(db, SpConstants.SANKALP_TYPE_NIYAM, SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_CURRENT, null));
        data.setLifetimeTyags(getSankalpsCount(db, SpConstants.SANKALP_TYPE_TYAG, SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_LIFETIME, null));
        data.setLifetimeNiyams(getSankalpsCount(db, SpConstants.SANKALP_TYPE_NIYAM, SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_LIFETIME, null));
        data.setUpcomingTyags(getSankalpsCount(db, SpConstants.SANKALP_TYPE_TYAG, SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_UPCOMING, null));
        data.setUpcomingNiyams(getSankalpsCount(db, SpConstants.SANKALP_TYPE_NIYAM, SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_UPCOMING, null));
        data.setAllTyags(getSankalpsCount(db, SpConstants.SANKALP_TYPE_TYAG, SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_ALL, null));
        data.setAllNiyams(getSankalpsCount(db, SpConstants.SANKALP_TYPE_NIYAM, SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_ALL, null));

//            db.setTransactionSuccessful();
//        }
//        finally {
//            db.endTransaction();
//            db.close();
//        }
        db.close();
        return data;
    }

    public ArrayList<SpSankalp> getMostRecentSankalps(int recentCount) {
        String tableName = SpTableContract.SpSankalpTable.TABLE_NAME;
        String userQuery = "SELECT  * FROM "
                + "( SELECT * FROM " + tableName + " ORDER BY " + SpTableContract.SpSankalpTable.COLUMN_CREATION_DATE
                + " DESC LIMIT " + String.valueOf(recentCount) + ") ORDER BY "
                + SpTableContract.SpSankalpTable.COLUMN_CREATION_DATE;

        return _runSankalpQuery(userQuery);
    }

    private ArrayList<SpSankalp> _runSankalpQuery(String query) {
        SQLiteDatabase db = _dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        ArrayList<SpSankalp> sankalps = new ArrayList<SpSankalp>();
        if (cursor != null) {
            try {
                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    SpSankalp sankalp = createSankalpByCursor(cursor);
                    if (sankalp != null) {
                        sankalps.add(sankalp);
                    }
                    cursor.moveToNext();
                }
            } finally {
                cursor.close();
            }
        }

        db.close();
        return sankalps;
    }

    public SpSankalp createSankalpByCursor(Cursor cursor) {
        int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow(SpTableContract.SpSankalpTable.COLUMN_CATEGORY_ID));
        int itemId = cursor.getInt(cursor.getColumnIndexOrThrow(SpTableContract.SpSankalpTable.COLUMN_ITEM_ID));
        int sankalpType = cursor.getInt(cursor.getColumnIndexOrThrow(SpTableContract.SpSankalpTable.COLUMN_SANKALP_TYPE));
        SpSankalp sankalp = SpSankalpFactory.getNewSankalp(sankalpType, categoryId, itemId);
        sankalp.setCategory(getAllCategories().get(categoryId));
        sankalp.setItem(getAllCategoryItems().get(itemId));

        int id = cursor.getInt(cursor.getColumnIndexOrThrow(SpTableContract.SpSankalpTable._ID));
        sankalp.setId(id);
        int isLifetime = cursor.getInt(cursor.getColumnIndexOrThrow(SpTableContract.SpSankalpTable.COLUMN_ISLIFETIME));
        sankalp.setLifetime(isLifetime);

        int isNotificationOn = cursor.getInt(cursor.getColumnIndexOrThrow(SpTableContract.SpSankalpTable.COLUMN_ISNOTIFICATION_ON));
        sankalp.setNotification(isNotificationOn);

        long creationDate = cursor.getLong(cursor.getColumnIndexOrThrow(SpTableContract.SpSankalpTable.COLUMN_CREATION_DATE));
        sankalp.setCreationDate(new Date(creationDate));

        long fromDate = cursor.getLong(cursor.getColumnIndexOrThrow(SpTableContract.SpSankalpTable.COLUMN_FROM_DATE));
        sankalp.setFromDate(new Date(fromDate));
        int toDateColIndex = cursor.getColumnIndexOrThrow(SpTableContract.SpSankalpTable.COLUMN_TO_DATE);
        if (!cursor.isNull(toDateColIndex)) {
            long toDate = cursor.getLong(toDateColIndex);
            sankalp.setToDate(new Date(toDate));
        }

        String description = cursor.getString(cursor.getColumnIndexOrThrow(SpTableContract.SpSankalpTable.COLUMN_DESCRIPTION));
        sankalp.setDescription(description);

        int exceptionOrTargetid = cursor.getInt(cursor.getColumnIndexOrThrow(SpTableContract.SpSankalpTable.COLUMN_EXCEPTION_TARGET_ID));
        int exceptionOrTargetCount = cursor.getInt(cursor.getColumnIndexOrThrow(SpTableContract.SpSankalpTable.COLUMN_EXCEPTION_TARGET_COUNT));
        //int exceptionOrTargetCurrentCount = cursor.getInt(cursor.getColumnIndexOrThrow(SpTableContract.SpSankalpTable.COLUMN_EXCEPTION_TARGET_CURRENT_COUNT));
        SpExceptionOrTarget exceptionOrTarget = new SpExceptionOrTarget(exceptionOrTargetid, _context);
        exceptionOrTarget.setExceptionOrTargetCount(exceptionOrTargetCount);
        //exceptionOrTarget.setExceptionOrTargetCountCurrent(exceptionOrTargetCurrentCount);
        if (exceptionOrTargetCount > 0) {
            int currentCount = getExTarCurrentCount(exceptionOrTargetid, id);
            exceptionOrTarget.setExceptionOrTargetCountCurrent(currentCount);
        }
        sankalp.setExceptionOrTarget(exceptionOrTarget);

        return sankalp;
    }

    public void updateSankalp(SpSankalp sankalp) {
        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SpTableContract.SpSankalpTable.COLUMN_SANKALP_TYPE, sankalp.getSankalpType());
        values.put(SpTableContract.SpSankalpTable.COLUMN_CATEGORY_ID, sankalp.getCategoryID());
        values.put(SpTableContract.SpSankalpTable.COLUMN_ITEM_ID, sankalp.getItemId());
        values.put(SpTableContract.SpSankalpTable.COLUMN_ISLIFETIME, sankalp.isLifetime());
        values.put(SpTableContract.SpSankalpTable.COLUMN_ISNOTIFICATION_ON, sankalp.isNotificationOn());
        values.put(SpTableContract.SpSankalpTable.COLUMN_FROM_DATE, sankalp.getFromDate().getTime());
        if (sankalp.getToDate() != null) {
            values.put(SpTableContract.SpSankalpTable.COLUMN_TO_DATE, sankalp.getToDate().getTime());
        }

        int targetCount = sankalp.getExceptionOrTarget().getExceptionOrTargetCount();
        values.put(SpTableContract.SpSankalpTable.COLUMN_EXCEPTION_TARGET_ID, sankalp.getExceptionOrTarget().getId());
        values.put(SpTableContract.SpSankalpTable.COLUMN_EXCEPTION_TARGET_COUNT, targetCount);

        values.put(SpTableContract.SpSankalpTable.COLUMN_DESCRIPTION, sankalp.getDescription());
        db.update(SpTableContract.SpSankalpTable.TABLE_NAME, values, SpTableContract.SpSankalpTable._ID + " = " + String.valueOf(sankalp.getId()), null);
        db.close();
    }

    public void deleteSankalps(ArrayList<SpSankalp> sankalpsToBeDeleted) {
        int size = sankalpsToBeDeleted.size();
        if (size > 0) {
            String[] ids = new String[size];
            for (int i = 0; i < size; i++) {
                ids[i] = String.valueOf(sankalpsToBeDeleted.get(i).getId());
            }
            String args = TextUtils.join(" , ", ids);
            String tableName = SpTableContract.SpSankalpTable.TABLE_NAME;
            SQLiteDatabase db = _dbHelper.getWritableDatabase();
            db.execSQL(String.format("DELETE FROM " + tableName + " WHERE _ID IN (%s);", args));
            db.close();
        }
    }

    public SpSankalp getSankalpById(int id) {
        SpSankalp sankalp = null;
        SQLiteDatabase db = _dbHelper.getReadableDatabase();
        String tableName = SpTableContract.SpSankalpTable.TABLE_NAME;
        String userQuery = "SELECT  * FROM " + tableName + " WHERE _ID = " + id;
        Cursor cursor = db.rawQuery(userQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            sankalp = createSankalpByCursor(cursor);
            cursor.close();
        }

        db.close();
        return sankalp;
    }

    public int getExTarCurrentCount(int exceptionOrTargetid, int sankalpId) {

        int count = 0;
        String userQuery = "SELECT " + SpTableContract.SpExTarTable.COLUMN_CURRENT_COUNT + " FROM " + SpTableContract.SpExTarTable.TABLE_NAME
                + " WHERE " + SpTableContract.SpExTarTable.COLUMN_SANKALP_ID + " = " + sankalpId;
        Calendar today = Calendar.getInstance();
        long begin = 0;
        long end = 0;
        if (exceptionOrTargetid == SpExceptionOrTarget.EXCEPTION_OR_TARGET_DAILY) {
            begin = SpDateUtils.beginOfDate(today).getTime();
            end = SpDateUtils.endOfDate(today).getTime();
        } else if (exceptionOrTargetid == SpExceptionOrTarget.EXCEPTION_OR_TARGET_MONTHLY) {
            begin = SpDateUtils.beginOfMonth(today).getTime();
            end = SpDateUtils.endOfMonth(today).getTime();
        } else if (exceptionOrTargetid == SpExceptionOrTarget.EXCEPTION_OR_TARGET_YEARLY) {
            begin = SpDateUtils.beginOfYear(today).getTime();
            end = SpDateUtils.endOfYear(today).getTime();
        }
        if (begin > 0 && end > 0) {
            userQuery += " AND " + SpTableContract.SpExTarTable.COLUMN_UPDATED_ON + " BETWEEN " + begin + " AND " + end;
        }

        userQuery += " ORDER BY " + SpTableContract.SpExTarTable.COLUMN_UPDATED_ON + " DESC LIMIT 1";
        SQLiteDatabase db = _dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(userQuery, null);
        if (c != null && c.getCount() > 0) {
            try {
                c.moveToFirst();
                count = c.getInt(c.getColumnIndexOrThrow(SpTableContract.SpExTarTable.COLUMN_CURRENT_COUNT));
            } finally {
                c.close();
            }
        }

        db.close();
        return count;
    }

    public Cursor getExTarCursor(int sankalpId){
        String userQuery = "SELECT * FROM " + SpTableContract.SpExTarTable.TABLE_NAME
                + " WHERE " + SpTableContract.SpExTarTable.COLUMN_SANKALP_ID + " = " + sankalpId;
        userQuery += " ORDER BY " + SpTableContract.SpExTarTable.COLUMN_UPDATED_ON + " DESC";
        SQLiteDatabase db = _dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(userQuery, null);
        return c;
    }

    public ArrayList<SpExceptionOrTarget> getExTarEntries(SpSankalp s) {
        int sankalpId = s.getId();
        String userQuery = "SELECT * FROM " + SpTableContract.SpExTarTable.TABLE_NAME
                + " WHERE " + SpTableContract.SpExTarTable.COLUMN_SANKALP_ID + " = " + sankalpId;
        userQuery += " ORDER BY " + SpTableContract.SpExTarTable.COLUMN_UPDATED_ON + " DESC";
        SQLiteDatabase db = _dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(userQuery, null);
        ArrayList<SpExceptionOrTarget> entries = new ArrayList<>();
        if (c != null && c.getCount() > 0) {
            try {
                c.moveToFirst();
                while (c.isAfterLast() == false) {
                    long updatedOn = c.getLong(c.getColumnIndexOrThrow(SpTableContract.SpExTarTable.COLUMN_UPDATED_ON));
                    int count = c.getInt(c.getColumnIndexOrThrow(SpTableContract.SpExTarTable.COLUMN_CURRENT_COUNT));
                    SpExceptionOrTarget e = s.getExceptionOrTarget().clone();
                    e.setLastUpdatedOn(updatedOn);
                    e.setExceptionOrTargetCountCurrent(count);
                    entries.add(e);
                    c.moveToNext();
                }

            } finally {
                c.close();
            }
        }
        db.close();
        return entries;
    }
}
