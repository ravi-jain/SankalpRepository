package com.ravijain.sankalp.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * Created by ravijain on 7/1/2016.
 */
public class SpContentProvider {

    private SpDBHelper _dbHelper;
    private static SpContentProvider instanceRef = null;

    private SpContentProvider(Context context) {
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

    public Hashtable<Integer, SpCategory> getAllCategories()
    {
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

    public void addSankalp(SpSankalp sankalp)
    {
        if (sankalp instanceof SpTyag) {
            addTyag((SpTyag)sankalp);
        }
        else if (sankalp instanceof SpNiyam) {
            addNiyam((SpNiyam)sankalp);
        }
    }

    public void addTyag(SpTyag tyag)
    {
        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SpTableContract.SpTyagTable.COLUMN_CREATION_DATE, new Date().getTime());
        values.put(SpTableContract.SpTyagTable.COLUMN_CATEGORY_ID, tyag.getCategoryID());
        values.put(SpTableContract.SpTyagTable.COLUMN_ITEM_ID, tyag.getItemId());
        values.put(SpTableContract.SpTyagTable.COLUMN_ISLIFETIME, tyag.isLifetime());
        if (tyag.isLifetime() == SpDataConstants.SANKALP_IS_LIFTIME_FALSE) {
            values.put(SpTableContract.SpTyagTable.COLUMN_FROM_DATE, tyag.getFromDate().getTime());
            values.put(SpTableContract.SpTyagTable.COLUMN_TO_DATE, tyag.getToDate().getTime());
        }
        values.put(SpTableContract.SpTyagTable.COLUMN_DESCRIPTION, tyag.getDescription());

        // Inserting Row
        db.insert(SpTableContract.SpTyagTable.TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    public void addNiyam(SpNiyam niyam)
    {
        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SpTableContract.SpNiyamTable.COLUMN_CREATION_DATE, new Date().getTime());
        values.put(SpTableContract.SpNiyamTable.COLUMN_CATEGORY_ID, niyam.getCategoryID());
        values.put(SpTableContract.SpNiyamTable.COLUMN_ITEM_ID, niyam.getItemId());
        values.put(SpTableContract.SpNiyamTable.COLUMN_ISLIFETIME, niyam.isLifetime());
        if (niyam.isLifetime() == SpDataConstants.SANKALP_IS_LIFTIME_FALSE) {
            values.put(SpTableContract.SpNiyamTable.COLUMN_FROM_DATE, niyam.getFromDate().getTime());
            values.put(SpTableContract.SpNiyamTable.COLUMN_TO_DATE, niyam.getToDate().getTime());
        }
        values.put(SpTableContract.SpNiyamTable.COLUMN_DESCRIPTION, niyam.getDescription());

        // Inserting Row
        db.insert(SpTableContract.SpNiyamTable.TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

}
