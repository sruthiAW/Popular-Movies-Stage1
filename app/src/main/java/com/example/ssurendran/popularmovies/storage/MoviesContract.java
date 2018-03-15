package com.example.ssurendran.popularmovies.storage;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ssurendran on 3/11/18.
 */

public class MoviesContract {

    public static final String AUTHORITY = "com.example.ssurendran.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_FAVORITE_MOVIES = "favoritemovies";

    public static final class FavoriteMovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE_MOVIES).build();

        public static final String TABLE_NAME = "favorite_movies";
        public static final String MOVIE_NAME = "movie_name";
        public static final String MOVIE_ID = "movie_id";
        public static final String MOVIE_POSTER = "poster_bitmap";
        public static final String MOVIE_POSTER_PATH = "poster_path";
        public static final String SYNOPSIS = "synopsis";
        public static final String USER_RATING = "user_rating";
        public static final String RELEASE_DATE = "release_date";

    }

}
