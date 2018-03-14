package com.example.ssurendran.popularmovies.network;

import com.example.ssurendran.popularmovies.models.MovieDetails;
import com.example.ssurendran.popularmovies.models.ReviewDetails;
import com.example.ssurendran.popularmovies.models.TrailerDetails;
import com.example.ssurendran.popularmovies.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.ssurendran.popularmovies.utils.Constants.TRAILER_TYPE;

/**
 * Created by ssurendran on 2/17/18.
 */

public class ResponseParser {


    public ResponseParser() {
    }

    public List<List<String>> parseMovieListResponse(String responseString) throws JSONException {
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

    public List<ReviewDetails> parseReviewResponse(String responseString) throws JSONException {
        List<ReviewDetails> reviews = new ArrayList<>();
        JSONObject mainJSONObject = new JSONObject(responseString);
        JSONArray jsonarray = mainJSONObject.getJSONArray("results");
        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject jsonobject = jsonarray.getJSONObject(i);
            ReviewDetails reviewDetails = new ReviewDetails();
            reviewDetails.setAuthor(jsonobject.getString("author"));
            reviewDetails.setReview(jsonobject.getString("content"));
            reviews.add(i, reviewDetails);
        }
        return reviews;
    }

    public List<TrailerDetails> parseTrailerResponse(String responseString) throws JSONException {
        List<TrailerDetails> trailers = new ArrayList<>();
        JSONObject mainJSONObject = new JSONObject(responseString);
        JSONArray jsonarray = mainJSONObject.getJSONArray("results");
        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject jsonobject = jsonarray.getJSONObject(i);
            if (jsonobject.getString("type").equalsIgnoreCase(TRAILER_TYPE)) {
                String trailerKey = jsonobject.getString("key");
                trailers.add(new TrailerDetails(Constants.MOVIE_TRAILER_BASE_URL + trailerKey));
            }
        }
        return trailers;
    }
}
