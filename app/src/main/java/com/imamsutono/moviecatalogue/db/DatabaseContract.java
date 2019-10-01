package com.imamsutono.moviecatalogue.db;

import android.provider.BaseColumns;

class DatabaseContract {
    static String TABLE_MOVIE = "movie";
    static String TABLE_TVSHOW = "tv_show";

    static final class DbColumns implements BaseColumns {
        static String POSTER = "poster";
        static String TITLE = "title";
        static String RELEASE_DATE = "release_date";
        static String LANGUAGE = "language";
    }
}
