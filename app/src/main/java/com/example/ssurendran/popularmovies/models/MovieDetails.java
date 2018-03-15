package com.example.ssurendran.popularmovies.models;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by ssurendran on 2/17/18.
 */

public class MovieDetails implements Serializable{

    private String movieId;
    private String movieName;
    private String userRating;
    private String releaseDate;
    private String posterPath;
    private String moviePlot;
    private Bitmap moviePoster;
    private boolean isFavorite;

    public MovieDetails() {
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getUserRating() {
        return userRating;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getMoviePlot() {
        return moviePlot;
    }

    public void setMoviePlot(String moviePlot) {
        this.moviePlot = moviePlot;
    }

    public Bitmap getMoviePoster() {
        return moviePoster;
    }

    public void setMoviePoster(Bitmap moviePoster) {
        this.moviePoster = moviePoster;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public void update(MovieDetails other){
        if (other.movieId != null) this.movieId = other.movieId;
        if (other.movieName != null) this.movieName = other.movieName;
        if (other.userRating != null) this.userRating = other.userRating;
        if (other.releaseDate != null) this.releaseDate = other.releaseDate;
        if (other.posterPath != null) this.posterPath = other.posterPath;
        if (other.moviePlot != null) this.moviePlot = other.moviePlot;
        if (other.moviePoster != null) this.moviePoster = other.moviePoster;
    }
}
