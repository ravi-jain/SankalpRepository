package com.ravijain.sankalp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ravijain on 7/1/2016.
 */
public class SpDBHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 9;

    static final String DATABASE_NAME = "sankalp.db";

    private Context _context;

    // Create a table to hold locations.  A location consists of the string supplied in the
    // location setting, the city name, and the latitude and longitude
    final String SQL_CREATE_USER_TABLE = "CREATE TABLE " + SpTableContract.SpUserTable.TABLE_NAME + " (" +
            SpTableContract.SpUserTable._ID + " INTEGER PRIMARY KEY," +
            SpTableContract.SpUserTable.COLUMN_USER_NAME + " TEXT NOT NULL, " +
            SpTableContract.SpUserTable.COLUMN_USER_MOBILE + " TEXT, " +
            SpTableContract.SpUserTable.COLUMN_USER_EMAIL + " TEXT NOT NULL " +
            " );";

    final String SQL_CREATE_CATEGORY_TABLE = "CREATE TABLE " + SpTableContract.SpCategoryTable.TABLE_NAME + " (" +
            SpTableContract.SpCategoryTable._ID + " INTEGER PRIMARY KEY," +
            SpTableContract.SpCategoryTable.COLUMN_CATEGORY_NAME + " TEXT NOT NULL, " +
            SpTableContract.SpCategoryTable.COLUMN_CATEGORY_DISPLAYNAME + " TEXT, " +
            SpTableContract.SpCategoryTable.COLUMN_CATEGORY_TYPE + " INTEGER NOT NULL " +
            " );";

    final String SQL_CREATE_ITEM_TABLE = "CREATE TABLE " + SpTableContract.SpItemTable.TABLE_NAME + " (" +
            SpTableContract.SpItemTable._ID + " INTEGER PRIMARY KEY," +
            SpTableContract.SpItemTable.COLUMN_ITEM_NAME + " TEXT NOT NULL, " +
            SpTableContract.SpItemTable.COLUMN_ITEM_DISPLAYNAME + " TEXT, " +
            SpTableContract.SpItemTable.COLUMN_ITEM_CATEGORY_ID + " INTEGER NOT NULL, " +
            // Set up the location column as a foreign key to location table.
            " FOREIGN KEY (" + SpTableContract.SpItemTable.COLUMN_ITEM_CATEGORY_ID + ") REFERENCES " +
            SpTableContract.SpCategoryTable.TABLE_NAME + " (" + SpTableContract.SpCategoryTable._ID + ")" +
            " );";

    final String PARTIAL_SQL_CREATE_SANKALP_TABLE = " (" +
            SpTableContract.SpSankalpTable._ID + " INTEGER PRIMARY KEY," +
            SpTableContract.SpSankalpTable.COLUMN_CREATION_DATE + " INTEGER NOT NULL, " +
            SpTableContract.SpSankalpTable.COLUMN_FROM_DATE + " INTEGER NOT NULL, " +
            SpTableContract.SpSankalpTable.COLUMN_TO_DATE + " INTEGER, " +
            SpTableContract.SpSankalpTable.COLUMN_ITEM_ID + " INTEGER NOT NULL, " +
            SpTableContract.SpSankalpTable.COLUMN_DESCRIPTION + " TEXT, " +
            SpTableContract.SpSankalpTable.COLUMN_CATEGORY_ID + " INTEGER NOT NULL, " +
            SpTableContract.SpSankalpTable.COLUMN_ISLIFETIME + " INTEGER NOT NULL, " +
            SpTableContract.SpSankalpTable.COLUMN_EXCEPTION_FREQUENCY_ID + " INTEGER, " +
            SpTableContract.SpSankalpTable.COLUMN_EXCEPTION_FREQUENCY_COUNT + " INTEGER, " +
            SpTableContract.SpSankalpTable.COLUMN_EXCEPTION_FREQUENCY_COUNT_FINISHED + " INTEGER, " +
            // Set up the location column as a foreign key to location table.
            " FOREIGN KEY (" + SpTableContract.SpSankalpTable.COLUMN_CATEGORY_ID + ") REFERENCES " +
            SpTableContract.SpCategoryTable.TABLE_NAME + " (" + SpTableContract.SpCategoryTable._ID + "), " +
            " FOREIGN KEY (" + SpTableContract.SpSankalpTable.COLUMN_ITEM_ID + ") REFERENCES " +
            SpTableContract.SpItemTable.TABLE_NAME + " (" + SpTableContract.SpItemTable._ID + ")" +
            " );";

    final String SQL_CREATE_TYAG_TABLE = "CREATE TABLE " + SpTableContract.SpTyagTable.TABLE_NAME + PARTIAL_SQL_CREATE_SANKALP_TABLE;
    final String SQL_CREATE_NIYAM_TABLE = "CREATE TABLE " + SpTableContract.SpNiyamTable.TABLE_NAME + PARTIAL_SQL_CREATE_SANKALP_TABLE;

    public SpDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        _context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //sqLiteDatabase.execSQL(SQL_CREATE_USER_TABLE);
        _createCategoryTable(sqLiteDatabase);
        _createItemTable(sqLiteDatabase);
        sqLiteDatabase.execSQL(SQL_CREATE_NIYAM_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TYAG_TABLE);
    }

    private void _createItemTable(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ITEM_TABLE);
        SpContentProvider.getInstance(_context).bulkInsertCategoryItems(sqLiteDatabase);
    }

    private void _createCategoryTable(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_CATEGORY_TABLE);
        SpContentProvider.getInstance(_context).bulkInsertCategories(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SpTableContract.SpUserTable.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SpTableContract.SpTyagTable.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SpTableContract.SpNiyamTable.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SpTableContract.SpCategoryTable.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SpTableContract.SpItemTable.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
