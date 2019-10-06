package com.imamsutono.moviecatalogue.db;

import android.net.Uri;
import android.provider.BaseColumns;

public final class DatabaseContract {
    public static String TABLE_MOVIE = "movie";
    static String TABLE_TVSHOW = "tv_show";
    public static final String AUTHORITY = "com.imamsutono.moviecatalogue";
    private static final String SCHEME = "content";

    public static final class DbColumns implements BaseColumns {
        public static String POSTER = "poster";
        public static String TITLE = "title";
        public static String RELEASE_DATE = "release_date";
        public static String LANGUAGE = "language";
        public static String VOTERS = "voters";
        public static String SCORE = "score";
        public static String DESCRIPTION = "description";

        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_MOVIE)
                .build();
    }
}
