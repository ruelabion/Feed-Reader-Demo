package com.spiceworx.android.feedreaderdemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by ruelabion on 11/1/15.
 */
public class FeedReaderContract {
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.FEED_ID + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.FEED_TITLE + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.FEED_SUBTITLE + TEXT_TYPE + " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;

    private final Context context;

    private FeedReaderDbHelper DBHelper;
    private SQLiteDatabase db;

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public FeedReaderContract(Context ctx) {
        this.context = ctx;
        DBHelper = new FeedReaderDbHelper(context);
    }


    //--open the database---
    public FeedReaderContract open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---close the database ---
    public void close() {
        DBHelper.close();
    }

    //----insert a record into the database---
    public long insertRecord(String entryid, String title, String subtitle) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(FeedEntry.FEED_ID, entryid);
        initialValues.put(FeedEntry.FEED_TITLE, title);
        initialValues.put(FeedEntry.FEED_SUBTITLE, subtitle);
        return  db.insert(FeedEntry.TABLE_NAME, null, initialValues);
    }

    //---deletes a particular record----
    public boolean deleteRecord(long rowId) {
        return db.delete(FeedEntry.TABLE_NAME, FeedEntry._ID + "=" + rowId,null) > 0;
    }

    //----retrieves all the records----
    public Cursor getAllRecords() {
        return db.query(FeedEntry.TABLE_NAME, new String[] {FeedEntry._ID, FeedEntry.FEED_ID, FeedEntry.FEED_TITLE,
                FeedEntry.FEED_SUBTITLE},null,null,null,null,null);
    }

    /* Inner class that defines the table contents */
    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "feeddemo";
        public static final String FEED_ID = "entryid";
        public static final String FEED_TITLE = "title";
        public static final String FEED_SUBTITLE = "subtitle";
    }

    private class FeedReaderDbHelper extends SQLiteOpenHelper {
        // If you change the database schema, you must increment the database version.
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "FeedReader.db";

        public FeedReaderDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }
}
