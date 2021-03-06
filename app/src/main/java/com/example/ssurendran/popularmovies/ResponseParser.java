package com.example.ssurendran.popularmovies;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ssurendran on 2/17/18.
 */

public class ResponseParser {


    public ResponseParser() {
    }

    public List<List<String>> parseResponse(String responseString) throws JSONException {
        List<List<String>> bigList = new ArrayList<>();
        List<String > movieNames = new ArrayList<>();
        List<String > movieIds = new ArrayList<>();
        List<String > posterPaths = new ArrayList<>();

        JSONObject mainJSONObject = new JSONObject(responseString);
        JSONArray jsonarray = mainJSONObject.getJSONArray("results");
        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject jsonobject = jsonarray.getJSONObject(i);
            String movie_id = jsonobject.getString("id");
            movieIds.add(i, movie_id);
            String poster_path = jsonobject.getString("poster_path");
            posterPaths.add(i, poster_path);
            String movie_name = jsonobject.getString("original_title");
            movieNames.add(i, movie_name);
        }

        bigList.add(movieIds);
        bigList.add(movieNames);
        bigList.add(posterPaths);

        return bigList;
    }

    public MovieDetails parseDetailsResponse(String responseString) throws JSONException {
        JSONObject mainJSONObject = new JSONObject(responseString);
        MovieDetails movieDetails = new MovieDetails();
        movieDetails.setMovieName(mainJSONObject.getString("original_title"));
        movieDetails.setReleaseDate(mainJSONObject.getString("release_date"));
        movieDetails.setMoviePlot(mainJSONObject.getString("overview"));
        movieDetails.setUserRating(mainJSONObject.getString("vote_average"));
        movieDetails.setPosterPath(mainJSONObject.getString("poster_path"));
        return movieDetails;
    }
}
