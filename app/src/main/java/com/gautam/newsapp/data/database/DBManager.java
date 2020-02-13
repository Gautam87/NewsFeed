package com.gautam.newsapp.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.gautam.newsapp.data.model.Article;

public class DBManager {

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = DatabaseHelper.getInstance(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(Article article) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.TITLE, article.getTitle());
        contentValue.put(DatabaseHelper.DESC, article.getDescription());
        contentValue.put(DatabaseHelper.URL, article.getUrl());
        contentValue.put(DatabaseHelper.URL_TO_IMAGE, article.getUrlToImage());
        contentValue.put(DatabaseHelper.CONTENT, article.getContent());
        contentValue.put(DatabaseHelper.SOURCE_NAME, article.getSource().getName());
        contentValue.put(DatabaseHelper.AUTHOR, article.getAuthor());
        contentValue.put(DatabaseHelper.IS_BOOKMARKED, article.isBookmarked()?1:0);
        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
    }

    public Cursor fetch() {
        String[] columns = new String[] { DatabaseHelper._ID,
                DatabaseHelper.TITLE,
                DatabaseHelper.DESC,
                DatabaseHelper.URL,
                DatabaseHelper.URL_TO_IMAGE,
                DatabaseHelper.PUBLISHED_AT,
                DatabaseHelper.CONTENT,
                DatabaseHelper.SOURCE_NAME,
                DatabaseHelper.AUTHOR,
                DatabaseHelper.IS_BOOKMARKED,
        };
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long _id, String title, String desc) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.TITLE, title);
        contentValues.put(DatabaseHelper.DESC, desc);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + _id, null);
        return i;
    }

    public void delete(long _id) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID + "=" + _id, null);
    }

}