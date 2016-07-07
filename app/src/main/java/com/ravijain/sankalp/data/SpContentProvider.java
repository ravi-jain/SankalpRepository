package com.ravijain.sankalp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.ArrayList;
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
            user = new SpUser(cursor.getString(1), cursor.getString(2), cursor.getString(3));
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

        // Inserting Row
        db.insert(SpTableContract.SpUserTable.TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    public Cursor getUserCursor() {
        SQLiteDatabase db = _dbHelper.getReadableDatabase();
        String userQuery = "SELECT  * FROM " + SpTableContract.SpUserTable.TABLE_NAME;

        Cursor cursor = db.rawQuery(userQuery, null);
        //db.close();
        return cursor;
    }

    public Cursor getTyagCursor() {
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
    }

    public Hashtable<Integer, SpCategory> getAllCategories() {
        Hashtable<Integer, SpCategory> categories = new Hashtable<Integer, SpCategory>();
        SQLiteDatabase db = _dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + SpTableContract.SpCategoryTable.TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                SpCategory category = new SpCategory(cursor.getInt(cursor.getColumnIndexOrThrow(SpTableContract.SpCategoryTable._ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(SpTableContract.SpCategoryTable.COLUMN_CATEGORY_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(SpTableContract.SpCategoryTable.COLUMN_CATEGORY_DISPLAYNAME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(SpTableContract.SpCategoryTable.COLUMN_CATEGORY_TYPE)));
                categories.put(category.getId(), category);
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return categories;
    }

    public Hashtable<Integer, SpCategoryItem> getAllCategoryItems() {
        Hashtable<Integer, SpCategoryItem> items = new Hashtable<Integer, SpCategoryItem>();
        SQLiteDatabase db = _dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + SpTableContract.SpItemTable.TABLE_NAME;

        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                SpCategoryItem item = new SpCategoryItem(cursor.getInt(cursor.getColumnIndexOrThrow(SpTableContract.SpItemTable._ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(SpTableContract.SpItemTable.COLUMN_ITEM_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(SpTableContract.SpItemTable.COLUMN_ITEM_DISPLAYNAME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(SpTableContract.SpItemTable.COLUMN_ITEM_CATEGORY_ID)));
                items.put(item.getId(), item);
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return items;
    }

    public Hashtable<String, SpCategory> getAllCategoriesBySankalpType(int type) {
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
                        cursor.getString(cursor.getColumnIndexOrThrow(SpTableContract.SpCategoryTable.COLUMN_CATEGORY_DISPLAYNAME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(SpTableContract.SpCategoryTable.COLUMN_CATEGORY_TYPE)));
                categories.put(category.getCategoryName(), category);
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return categories;
    }

    public Hashtable<String, SpCategoryItem> getAllCategoryItemsByCategoryId(int id) {
        Hashtable<String, SpCategoryItem> items = new Hashtable<String, SpCategoryItem>();
        SQLiteDatabase db = _dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + SpTableContract.SpItemTable.TABLE_NAME + " WHERE "
                + SpTableContract.SpItemTable.COLUMN_ITEM_CATEGORY_ID + " =" + id;
        query += " ORDER BY " + SpTableContract.SpItemTable.COLUMN_ITEM_NAME + " ASC";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                SpCategoryItem item = new SpCategoryItem(cursor.getInt(cursor.getColumnIndexOrThrow(SpTableContract.SpItemTable._ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(SpTableContract.SpItemTable.COLUMN_ITEM_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(SpTableContract.SpItemTable.COLUMN_ITEM_DISPLAYNAME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(SpTableContract.SpItemTable.COLUMN_ITEM_CATEGORY_ID)));
                items.put(item.getCategoryItemName(), item);
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
            Hashtable<String, SpCategory> defaultCategories = SpCategory.getDefaultCategories();
            Iterator<SpCategory> iterator = defaultCategories.values().iterator();
            while (iterator.hasNext()) {
                SpCategory category = iterator.next();
                ContentValues values = new ContentValues();
                values.put(SpTableContract.SpCategoryTable._ID, category.getId());
                values.put(SpTableContract.SpCategoryTable.COLUMN_CATEGORY_NAME, category.getCategoryName());
                values.put(SpTableContract.SpCategoryTable.COLUMN_CATEGORY_DISPLAYNAME, category.getCategoryDisplayName());
                values.put(SpTableContract.SpCategoryTable.COLUMN_CATEGORY_TYPE, category.getSankalpType());
                db.insert(SpTableContract.SpCategoryTable.TABLE_NAME, null, values);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

    }

    public void bulkInsertCategoryItems(SQLiteDatabase db) {
        //SQLiteDatabase db = _dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            Hashtable<String, SpCategoryItem> defaultCategoryItems = SpCategoryItem.getDefaultCategoryItems();
            Iterator<SpCategoryItem> iterator = defaultCategoryItems.values().iterator();
            while (iterator.hasNext()) {
                SpCategoryItem item = iterator.next();
                ContentValues values = new ContentValues();
                values.put(SpTableContract.SpItemTable._ID, item.getId());
                values.put(SpTableContract.SpItemTable.COLUMN_ITEM_NAME, item.getCategoryItemName());
                values.put(SpTableContract.SpItemTable.COLUMN_ITEM_DISPLAYNAME, item.getCategoryItemDisplayName());
                values.put(SpTableContract.SpItemTable.COLUMN_ITEM_CATEGORY_ID, item.getCategoryId());
                db.insert(SpTableContract.SpItemTable.TABLE_NAME, null, values);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

    }

    public void addSankalp(SpSankalp sankalp) {
        ContentValues values = new ContentValues();
        values.put(SpTableContract.SpSankalpTable.COLUMN_CREATION_DATE, new Date().getTime());
        values.put(SpTableContract.SpSankalpTable.COLUMN_CATEGORY_ID, sankalp.getCategoryID());
        values.put(SpTableContract.SpSankalpTable.COLUMN_ITEM_ID, sankalp.getItemId());
        values.put(SpTableContract.SpSankalpTable.COLUMN_ISLIFETIME, sankalp.isLifetime());
        values.put(SpTableContract.SpSankalpTable.COLUMN_FROM_DATE, sankalp.getFromDate().getTime());
        if (sankalp.getToDate() != null) {
            values.put(SpTableContract.SpSankalpTable.COLUMN_TO_DATE, sankalp.getToDate().getTime());
        }
        values.put(SpTableContract.SpSankalpTable.COLUMN_EXCEPTION_TARGET_ID, sankalp.getExceptionOrTarget().getId());
        values.put(SpTableContract.SpSankalpTable.COLUMN_EXCEPTION_TARGET_COUNT, sankalp.getExceptionOrTarget().getExceptionOrTargetCount());
        values.put(SpTableContract.SpSankalpTable.COLUMN_EXCEPTION_TARGET_CURRENT_COUNT, sankalp.getExceptionOrTarget().getExceptionOrTargetCountCurrent());
        values.put(SpTableContract.SpSankalpTable.COLUMN_DESCRIPTION, sankalp.getDescription());

        String tableName = null;
        if (sankalp instanceof SpTyag) {
            addAdditionalTyagValues((SpTyag) sankalp, values);
            tableName = SpTableContract.SpTyagTable.TABLE_NAME;
        } else if (sankalp instanceof SpNiyam) {
            addAdditionalNiyamValues((SpNiyam) sankalp, values);
            tableName = SpTableContract.SpNiyamTable.TABLE_NAME;
        }

        if (tableName != null) {
            SQLiteDatabase db = _dbHelper.getWritableDatabase();
            // Inserting Row
            db.insert(tableName, null, values);
            db.close(); // Closing database connection
        }
    }

    public void addAdditionalTyagValues(SpTyag tyag, ContentValues values) {
    }

    public void addAdditionalNiyamValues(SpNiyam niyam, ContentValues values) {
    }

    public ArrayList<SpSankalp> getSankalps(int sankalpType, int listFilter) {
        SQLiteDatabase db = _dbHelper.getReadableDatabase();
        String tableName = sankalpType == SpDataConstants.SANKALP_TYPE_NIYAM ? SpTableContract.SpNiyamTable.TABLE_NAME : SpTableContract.SpTyagTable.TABLE_NAME;
        String userQuery = "SELECT  * FROM " + tableName;

        Cursor cursor = db.rawQuery(userQuery, null);

        ArrayList<SpSankalp> sankalps = new ArrayList<SpSankalp>();
        if (cursor != null) {
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow(SpTableContract.SpSankalpTable.COLUMN_CATEGORY_ID));
                int itemId = cursor.getInt(cursor.getColumnIndexOrThrow(SpTableContract.SpSankalpTable.COLUMN_ITEM_ID));
                SpSankalp sankalp = SpSankalpFactory.getNewSankalp(sankalpType, categoryId, itemId);

                int id = cursor.getInt(cursor.getColumnIndexOrThrow(SpTableContract.SpSankalpTable._ID));
                sankalp.setId(id);
                int isLifetime = cursor.getInt(cursor.getColumnIndexOrThrow(SpTableContract.SpSankalpTable.COLUMN_ISLIFETIME));
                sankalp.setLifetime(isLifetime);

                long fromDate = cursor.getLong(cursor.getColumnIndexOrThrow(SpTableContract.SpSankalpTable.COLUMN_FROM_DATE));
                sankalp.setFromDate(new Date(fromDate));
                int toDateColIndex = cursor.getColumnIndexOrThrow(SpTableContract.SpSankalpTable.COLUMN_TO_DATE);
                if (!cursor.isNull(toDateColIndex)) {
                    long toDate = cursor.getLong(toDateColIndex);
                    sankalp.setToDate(new Date(toDate));
                }

                int exceptionOrTargetid = cursor.getInt(cursor.getColumnIndexOrThrow(SpTableContract.SpSankalpTable.COLUMN_EXCEPTION_TARGET_ID));
                int exceptionOrTargetCount = cursor.getInt(cursor.getColumnIndexOrThrow(SpTableContract.SpSankalpTable.COLUMN_EXCEPTION_TARGET_COUNT));
                int exceptionOrTargetCurrentCount = cursor.getInt(cursor.getColumnIndexOrThrow(SpTableContract.SpSankalpTable.COLUMN_EXCEPTION_TARGET_CURRENT_COUNT));
                SpExceptionOrTarget exceptionOrTarget = new SpExceptionOrTarget(exceptionOrTargetid, _context);
                exceptionOrTarget.setExceptionOrTargetCount(exceptionOrTargetCount);
                exceptionOrTarget.setExceptionOrTargetCountCurrent(exceptionOrTargetCurrentCount);
                sankalp.setExceptionOrTarget(exceptionOrTarget);

                String description = cursor.getString(cursor.getColumnIndexOrThrow(SpTableContract.SpSankalpTable.COLUMN_DESCRIPTION));
                sankalp.setDescription(description);

                sankalps.add(sankalp);
                cursor.moveToNext();
            }
            cursor.close();
        }

        cursor.close();
        db.close();
        return sankalps;
    }

    public void deleteSankalps(ArrayList<SpSankalp> sankalpsToBeDeleted, int sankalpType) {
        int size = sankalpsToBeDeleted.size();
        if (size > 0) {
            String[] ids = new String[size];
            for (int i = 0; i < size; i++) {
                ids[i] = String.valueOf(sankalpsToBeDeleted.get(i).getId());
            }
            String args = TextUtils.join(" , ", ids);
            String tableName = sankalpType == SpDataConstants.SANKALP_TYPE_NIYAM ? SpTableContract.SpNiyamTable.TABLE_NAME : SpTableContract.SpTyagTable.TABLE_NAME;
            SQLiteDatabase db = _dbHelper.getWritableDatabase();
            db.execSQL(String.format("DELETE FROM " + tableName + " WHERE _ID IN (%s);", args));
            db.close();
        }
    }
}
