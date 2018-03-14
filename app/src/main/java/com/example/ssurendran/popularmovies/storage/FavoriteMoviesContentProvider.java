package com.example.ssurendran.popularmovies.storage;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.example.ssurendran.popularmovies.storage.MoviesContract.FavoriteMovieEntry.TABLE_NAME;

/**
 * Created by ssurendran on 3/11/18.
 */

public class FavoriteMoviesContentProvider extends ContentProvider {

    private static final int FAVORITE_MOVIES = 100;
    private static final int FAVORITE_MOVIES_WITH_ID = 101;

    private MoviesDBSQLiteOpenHelper mMoviesDBSQLiteOpenHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(MoviesContract.AUTHORITY, MoviesContract.PATH_FAVORITE_MOVIES, FAVORITE_MOVIES);
        uriMatcher.addURI(MoviesContract.AUTHORITY, MoviesContract.PATH_FAVORITE_MOVIES + "/#", FAVORITE_MOVIES_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mMoviesDBSQLiteOpenHelper = new MoviesDBSQLiteOpenHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase db = mMoviesDBSQLiteOpenHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match) {
            case FAVORITE_MOVIES:
                retCursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case FAVORITE_MOVIES_WITH_ID:
                String movie_id = uri.getPathSegments().get(1);
                selection = MoviesContract.FavoriteMovieEntry.MOVIE_ID + "=?";
                selectionArgs = new String[]{movie_id};
                retCursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final SQLiteDatabase db = mMoviesDBSQLiteOpenHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case FAVORITE_MOVIES:
                long id = db.insert(TABLE_NAME, null, values);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(MoviesContract.FavoriteMovieEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mMoviesDBSQLiteOpenHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int tasksDeleted;

        switch (match) {
            case FAVORITE_MOVIES_WITH_ID:
                String movie_id = uri.getPathSegments().get(1);
                selection = MoviesContract.FavoriteMovieEntry.MOVIE_ID + "=?";
                selectionArgs = new String[]{movie_id};
                tasksDeleted = db.delete(TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (tasksDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return tasksDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mMoviesDBSQLiteOpenHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int tasksUpdated;

        switch (match) {
            case FAVORITE_MOVIES_WITH_ID:
                String movie_id = uri.getPathSegments().get(1);
                selection = MoviesContract.FavoriteMovieEntry.MOVIE_ID + "=?";
                selectionArgs = new String[]{movie_id};
                tasksUpdated = db.update(TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (tasksUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return tasksUpdated;
    }
}
