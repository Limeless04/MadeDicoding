package com.imamsutono.moviecatalogue.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.imamsutono.moviecatalogue.db.DatabaseContract.DbColumns;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static String DATABASE_NAME = "dbmoviecatalogue";
    private static final int DATABASE_VERSION = 2;

    private static final String SQL_CREATE_TABLE_MOVIE = String.format("CREATE TABLE %s" +
            " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
            DatabaseContract.TABLE_MOVIE,
            DbColumns._ID,
            DbColumns.POSTER,
            DbColumns.TITLE,
            DbColumns.RELEASE_DATE,
            DbColumns.LANGUAGE,
            DbColumns.VOTERS,
            DbColumns.SCORE,
            DbColumns.DESCRIPTION
    );

    private static final String SQL_CREATE_TABLE_TVSHOW = String.format("CREATE TABLE %s" +
            " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
            DatabaseContract.TABLE_TVSHOW,
            DbColumns._ID,
            DbColumns.POSTER,
            DbColumns.TITLE,
            DbColumns.RELEASE_DATE,
            DbColumns.LANGUAGE,
            DbColumns.VOTERS,
            DbColumns.SCORE,
            DbColumns.DESCRIPTION
    );

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_MOVIE);
        db.execSQL(SQL_CREATE_TABLE_TVSHOW);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_MOVIE);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_TVSHOW);
        onCreate(db);
    }
}
