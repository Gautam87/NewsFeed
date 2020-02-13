package com.gautam.newsapp.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper mInstance = null;

    // Table Name
    public static final String TABLE_NAME = "ARTICLES";

    // Table columns
    public static final String _ID = "_id";
    public static final String TITLE = "title";
    public static final String DESC = "description";
    public static final String URL = "url";
    public static final String URL_TO_IMAGE = "url_to_image";
    public static final String PUBLISHED_AT = "published_at";
    public static final String CONTENT = "content";
    public static final String SOURCE_NAME = "source_name";
    public static final String AUTHOR = "author";
    public static final String IS_BOOKMARKED = "is_bookmarked";

    // Database Information
    static final String DB_NAME = "News_Articles.db";

    // database version
    static final int DB_VERSION = 1;

    private Context mContext;

    // Creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TITLE + " TEXT NOT NULL, "+ DESC + " TEXT, "
            + URL + " TEXT, "
            + URL_TO_IMAGE + " TEXT, "
            + PUBLISHED_AT + " TEXT, "
            + CONTENT + " TEXT, "
            + SOURCE_NAME + " TEXT, "
            + AUTHOR + " TEXT, "
            + IS_BOOKMARKED + " INTEGER DEFAULT 0);";

    public static DatabaseHelper getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new DatabaseHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }

    private DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}