package com.imamsutono.moviecatalogue.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.imamsutono.moviecatalogue.model.Movie;

import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.imamsutono.moviecatalogue.db.DatabaseContract.DbColumns.LANGUAGE;
import static com.imamsutono.moviecatalogue.db.DatabaseContract.DbColumns.POSTER;
import static com.imamsutono.moviecatalogue.db.DatabaseContract.DbColumns.RELEASE_DATE;
import static com.imamsutono.moviecatalogue.db.DatabaseContract.DbColumns.TITLE;
import static com.imamsutono.moviecatalogue.db.DatabaseContract.TABLE_MOVIE;

public class MovieHelper {
    private static final String DATABASE_TABLE = TABLE_MOVIE;
    private static DatabaseHelper databaseHelper;
    private static MovieHelper INSTANCE;

    private static SQLiteDatabase database;

    private MovieHelper(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public static MovieHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MovieHelper(context);
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

    public List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE, null,
                null,
                null,
                null,
                null,
                _ID + " ASC",
                null);
        cursor.moveToFirst();

        Movie movie;

        if (cursor.getCount() > 0) {
            do {
                movie = new Movie();
                movie.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                movie.setPoster(cursor.getString(cursor.getColumnIndexOrThrow(POSTER)));
                movie.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                movie.setYear(cursor.getString(cursor.getColumnIndexOrThrow(RELEASE_DATE)));
                movie.setLanguage(cursor.getString(cursor.getColumnIndexOrThrow(LANGUAGE)));

                movies.add(movie);
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return movies;
    }

    public int getMovie(String title, String year) {
        String query = "SELECT * FROM " + TABLE_MOVIE +
                " WHERE " + TITLE + " = '" + title + "'" +
                " AND " + RELEASE_DATE + " = '" + year + "'";
        Log.d("query", query);
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

    public long insertMovie(Movie movie) {
        ContentValues args = new ContentValues();
        args.put(POSTER, movie.getPoster());
        args.put(TITLE, movie.getTitle());
        args.put(RELEASE_DATE, movie.getYear());
        args.put(LANGUAGE, movie.getLanguage());

        return database.insert(DATABASE_TABLE, null, args);
    }

    public int deleteMovie(String title, String year) {
        return database.delete(
                TABLE_MOVIE,
                TITLE + " = '" + title + "' AND " + RELEASE_DATE + " = '" + year + "'",
                null
        );
    }
}
