package com.imamsutono.moviecatalogue.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.imamsutono.moviecatalogue.model.TvShow;

import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.imamsutono.moviecatalogue.db.DatabaseContract.DbColumns.DESCRIPTION;
import static com.imamsutono.moviecatalogue.db.DatabaseContract.DbColumns.LANGUAGE;
import static com.imamsutono.moviecatalogue.db.DatabaseContract.DbColumns.POSTER;
import static com.imamsutono.moviecatalogue.db.DatabaseContract.DbColumns.RELEASE_DATE;
import static com.imamsutono.moviecatalogue.db.DatabaseContract.DbColumns.SCORE;
import static com.imamsutono.moviecatalogue.db.DatabaseContract.DbColumns.TITLE;
import static com.imamsutono.moviecatalogue.db.DatabaseContract.DbColumns.VOTERS;
import static com.imamsutono.moviecatalogue.db.DatabaseContract.TABLE_TVSHOW;

public class TvShowHelper {
    private static final String DATABASE_TABLE = TABLE_TVSHOW;
    private static DatabaseHelper databaseHelper;
    private static TvShowHelper INSTANCE;

    private static SQLiteDatabase database;

    private TvShowHelper(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public static TvShowHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TvShowHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
        database = databaseHelper.getWritableDatabase();
    }

    public void close() {
        databaseHelper.close();

        if (database.isOpen())
            database.close();
    }

    public List<TvShow> getAllTvShow() {
        List<TvShow> tvShows = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE, null,
                null,
                null,
                null,
                null,
                _ID + " ASC",
                null);
        cursor.moveToFirst();
        TvShow tvShow;

        if (cursor.getCount() > 0) {
            do {
                tvShow = new TvShow();
                tvShow.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                tvShow.setPoster(cursor.getString(cursor.getColumnIndexOrThrow(POSTER)));
                tvShow.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                tvShow.setYear(cursor.getString(cursor.getColumnIndexOrThrow(RELEASE_DATE)));
                tvShow.setLanguage(cursor.getString(cursor.getColumnIndexOrThrow(LANGUAGE)));
                tvShow.setVoters(cursor.getString(cursor.getColumnIndexOrThrow(VOTERS)));
                tvShow.setScore(cursor.getString(cursor.getColumnIndexOrThrow(SCORE)));
                tvShow.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION)));

                tvShows.add(tvShow);
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }

        cursor.close();
        return tvShows;
    }

    public int getTvShow(String title, String year) {
        String query = "SELECT * FROM " + TABLE_TVSHOW +
                " WHERE " + TITLE + " = '" + title.replace("'", "''") + "'" +
                " AND " + RELEASE_DATE + " = '" + year + "'";
        Cursor cursor = null;

        try {
            cursor = database.rawQuery(query, null);
            cursor.moveToFirst();
        } catch (Exception e) {
            Log.e(MovieHelper.class.getSimpleName(), e.toString());
        }

        if (cursor != null)
            cursor.close();

        return cursor != null ? cursor.getCount() : 0;
    }

    public long insertTvShow(TvShow tvShow) {
        ContentValues args = new ContentValues();
        args.put(POSTER, tvShow.getPoster());
        args.put(TITLE, tvShow.getTitle());
        args.put(RELEASE_DATE, tvShow.getYear());
        args.put(LANGUAGE, tvShow.getLanguage());
        args.put(VOTERS, tvShow.getVoters());
        args.put(SCORE, tvShow.getScore());
        args.put(DESCRIPTION, tvShow.getDescription());

        return database.insert(DATABASE_TABLE, null, args);
    }

    public int deleteTvShow(String title, String year) {
        return database.delete(
                TABLE_TVSHOW,
                TITLE + " = '" + title.replace("'", "''") + "' AND " + RELEASE_DATE + " = '" + year + "'",
                null
        );
    }
}
