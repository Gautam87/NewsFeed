package com.gautam.newsapp.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.gautam.newsapp.data.model.Article;
import com.gautam.newsapp.data.model.Source;

import java.util.LinkedList;
import java.util.List;

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
        contentValue.put(DatabaseHelper.URL, article.getUrl());
        contentValue.put(DatabaseHelper.TITLE, article.getTitle());
        contentValue.put(DatabaseHelper.DESC, article.getDescription());
        contentValue.put(DatabaseHelper.URL_TO_IMAGE, article.getUrlToImage());
        contentValue.put(DatabaseHelper.CONTENT, article.getContent());
        contentValue.put(DatabaseHelper.SOURCE_NAME, article.getSource().getName());
        contentValue.put(DatabaseHelper.AUTHOR, article.getAuthor());
        contentValue.put(DatabaseHelper.IS_BOOKMARKED, article.isBookmarked() ? 1 : 0);
//        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
        database.insertWithOnConflict(DatabaseHelper.TABLE_NAME, null, contentValue, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public Boolean isArticleBookmarked(String url) {
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, // a. table
                new String[]{DatabaseHelper.IS_BOOKMARKED}, // b. column names
                " " + DatabaseHelper.URL + " = ?", // c. selections
                new String[]{url}, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        if (cursor != null)
            cursor.moveToFirst();

        return Integer.parseInt(cursor.getString(0)) == 1;
    }


    public List<Article> fetch() {
        String[] columns = new String[]{DatabaseHelper.URL,
                DatabaseHelper.TITLE,
                DatabaseHelper.DESC,
                DatabaseHelper.URL_TO_IMAGE,
                DatabaseHelper.PUBLISHED_AT,
                DatabaseHelper.CONTENT,
                DatabaseHelper.SOURCE_NAME,
                DatabaseHelper.AUTHOR,
                DatabaseHelper.IS_BOOKMARKED,
        };
        List<Article> players = new LinkedList<Article>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Article player = new Article();
                    player.setUrl(cursor.getString(0));
                    player.setTitle(cursor.getString(1));
                    player.setDescription(cursor.getString(2));
                    player.setUrlToImage(cursor.getString(3));
                    player.setPublishedAt(cursor.getString(4));
                    player.setContent(cursor.getString(5));
                    player.setSource(new Source(null, cursor.getString(6)));
                    player.setAuthor(cursor.getString(7));
                    player.setBookmarked(Integer.parseInt(cursor.getString(8)) == 1);
                    players.add(player);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return players;
    }

    public int update(String url, String title, String desc) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.TITLE, title);
        contentValues.put(DatabaseHelper.DESC, desc);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper.URL + " = " + url, null);
        return i;
    }

    public void delete(String url) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.URL + "=" + url, null);
    }

    public int updateBookmark(String url, boolean isBookmarked) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.IS_BOOKMARKED, isBookmarked ? 1 : 0);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper.URL + " = ?", new String[]{url});
        return i;
    }
}