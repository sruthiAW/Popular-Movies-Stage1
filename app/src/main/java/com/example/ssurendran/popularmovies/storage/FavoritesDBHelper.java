package com.example.ssurendran.popularmovies.storage;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.ssurendran.popularmovies.models.MovieDetails;
import com.example.ssurendran.popularmovies.network.RequestsBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ssurendran on 3/14/18.
 */

public class FavoritesDBHelper {

    public void persist(Context context, String movieId, MovieDetails movieDetails) {
        ContentValues values = new ContentValues();
        values.put(MoviesContract.FavoriteMovieEntry.MOVIE_ID, Integer.valueOf(movieId));
        values.put(MoviesContract.FavoriteMovieEntry.MOVIE_NAME, movieDetails.getMovieName());
        values.put(MoviesContract.FavoriteMovieEntry.SYNOPSIS, movieDetails.getMoviePlot());
        values.put(MoviesContract.FavoriteMovieEntry.USER_RATING, movieDetails.getUserRating());
        values.put(MoviesContract.FavoriteMovieEntry.RELEASE_DATE, movieDetails.getReleaseDate());

        byte[] image = null;
        try {
            image = new RequestsBuilder(context).downloadPosterImage(movieDetails.getPosterPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        values.put(MoviesContract.FavoriteMovieEntry.MOVIE_POSTER, image);
        context.getContentResolver().insert(MoviesContract.FavoriteMovieEntry.CONTENT_URI, values);
    }

    public MovieDetails readData(Context context, String movieId) {
        MovieDetails movieDetails = null;

        Cursor cursor = context.getContentResolver().query(
                ContentUris.withAppendedId(MoviesContract.FavoriteMovieEntry.CONTENT_URI, Integer.valueOf(movieId)),
                null,
                null,
                null,
                null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToNext();
            movieDetails = new MovieDetails();
            movieDetails.setMovieName(cursor.getString(cursor.getColumnIndex(MoviesContract.FavoriteMovieEntry.MOVIE_NAME)));
            movieDetails.setUserRating(cursor.getString(cursor.getColumnIndex(MoviesContract.FavoriteMovieEntry.USER_RATING)));
            movieDetails.setMoviePlot(cursor.getString(cursor.getColumnIndex(MoviesContract.FavoriteMovieEntry.SYNOPSIS)));
            movieDetails.setReleaseDate(cursor.getString(cursor.getColumnIndex(MoviesContract.FavoriteMovieEntry.RELEASE_DATE)));

            byte[] bitmapdata = cursor.getBlob(cursor.getColumnIndex(MoviesContract.FavoriteMovieEntry.MOVIE_POSTER));
            Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
            movieDetails.setMoviePoster(bitmap);
        }

        return movieDetails;
    }

    public List<MovieDetails> getAllFavoriteMovies(Context context) {
        List<MovieDetails> movieList = new ArrayList<>();

        Cursor cursor = context.getContentResolver().query(
                MoviesContract.FavoriteMovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                MovieDetails movieDetails = new MovieDetails();
                movieDetails.setMovieId(cursor.getString(cursor.getColumnIndex(MoviesContract.FavoriteMovieEntry.MOVIE_ID)));
                movieDetails.setMovieName(cursor.getString(cursor.getColumnIndex(MoviesContract.FavoriteMovieEntry.MOVIE_NAME)));
                movieDetails.setUserRating(cursor.getString(cursor.getColumnIndex(MoviesContract.FavoriteMovieEntry.USER_RATING)));
                movieDetails.setMoviePlot(cursor.getString(cursor.getColumnIndex(MoviesContract.FavoriteMovieEntry.SYNOPSIS)));
                movieDetails.setReleaseDate(cursor.getString(cursor.getColumnIndex(MoviesContract.FavoriteMovieEntry.RELEASE_DATE)));

                byte[] bitmapdata = cursor.getBlob(cursor.getColumnIndex(MoviesContract.FavoriteMovieEntry.MOVIE_POSTER));
                Bitmap bitmap = ByteArrayToBitmap(bitmapdata);
                movieDetails.setMoviePoster(bitmap);

                movieList.add(movieDetails);
            }
        }

        return movieList;
    }

    public Bitmap ByteArrayToBitmap(byte[] byteArray) {
        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(byteArray);
        Bitmap bitmap = BitmapFactory.decodeStream(arrayInputStream);
        return bitmap;
    }
}
