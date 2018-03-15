package com.example.ssurendran.popularmovies.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ssurendran on 3/11/18.
 */

public class MoviesDBSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favoritemovie.db";
    private static final int DATABASE_VERSION = 1;

    public MoviesDBSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FAVORITE_MOVIE_TABLE = "CREATE TABLE " + MoviesContract.FavoriteMovieEntry.TABLE_NAME + " (" +
                MoviesContract.FavoriteMovieEntry.MOVIE_NAME + " TEXT NOT NULL, " +
                MoviesContract.FavoriteMovieEntry.MOVIE_ID + " INTEGER NOT NULL, " +
                MoviesContract.FavoriteMovieEntry.MOVIE_POSTER + " BLOB, " +
                MoviesContract.FavoriteMovieEntry.MOVIE_POSTER_PATH + " TEXT NOT NULL, " +
                MoviesContract.FavoriteMovieEntry.SYNOPSIS + " TEXT NOT NULL, " +
                MoviesContract.FavoriteMovieEntry.USER_RATING + " TEXT NOT NULL, " +
                MoviesContract.FavoriteMovieEntry.RELEASE_DATE + " TEXT NOT NULL" +
                "); ";

        db.execSQL(SQL_CREATE_FAVORITE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.FavoriteMovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
