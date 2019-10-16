package com.imamsutono.moviefavorite.helper;

import android.database.Cursor;
import android.provider.BaseColumns;

import com.imamsutono.moviefavorite.model.Movie;

import java.util.ArrayList;
import java.util.List;

import static com.imamsutono.moviefavorite.db.DatabaseContract.DbColumns.DESCRIPTION;
import static com.imamsutono.moviefavorite.db.DatabaseContract.DbColumns.LANGUAGE;
import static com.imamsutono.moviefavorite.db.DatabaseContract.DbColumns.POSTER;
import static com.imamsutono.moviefavorite.db.DatabaseContract.DbColumns.RELEASE_DATE;
import static com.imamsutono.moviefavorite.db.DatabaseContract.DbColumns.SCORE;
import static com.imamsutono.moviefavorite.db.DatabaseContract.DbColumns.TITLE;
import static com.imamsutono.moviefavorite.db.DatabaseContract.DbColumns.VOTERS;

public class MappingHelper {

    public static List<Movie> mapCursorToList(Cursor cursor) {
        List<Movie> movies = new ArrayList<>();

        while (cursor.moveToNext()) {
            Movie movie = new Movie();
            movie.setId(cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID)));
            movie.setPoster(cursor.getString(cursor.getColumnIndexOrThrow(POSTER)));
            movie.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
            movie.setYear(cursor.getString(cursor.getColumnIndexOrThrow(RELEASE_DATE)));
            movie.setLanguage(cursor.getString(cursor.getColumnIndexOrThrow(LANGUAGE)));
            movie.setVoters(cursor.getString(cursor.getColumnIndexOrThrow(VOTERS)));
            movie.setScore(cursor.getString(cursor.getColumnIndexOrThrow(SCORE)));
            movie.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION)));

            movies.add(movie);
        }

        return movies;
    }

    public static Movie mapCursorToObject(Cursor cursor) {
        cursor.moveToFirst();

        Movie movie = new Movie();
        movie.setId(cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID)));
        movie.setPoster(cursor.getString(cursor.getColumnIndexOrThrow(POSTER)));
        movie.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
        movie.setYear(cursor.getString(cursor.getColumnIndexOrThrow(RELEASE_DATE)));
        movie.setLanguage(cursor.getString(cursor.getColumnIndexOrThrow(LANGUAGE)));
        movie.setVoters(cursor.getString(cursor.getColumnIndexOrThrow(VOTERS)));
        movie.setScore(cursor.getString(cursor.getColumnIndexOrThrow(SCORE)));
        movie.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION)));

        return movie;
    }
}
