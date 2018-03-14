package com.example.ssurendran.popularmovies.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.ssurendran.popularmovies.models.ReviewDetails;
import com.example.ssurendran.popularmovies.models.TrailerDetails;
import com.example.ssurendran.popularmovies.utils.Constants;
import com.example.ssurendran.popularmovies.models.MovieDetails;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by ssurendran on 2/17/18.
 */

public class RequestsBuilder {

    Context context;

    public RequestsBuilder(Context context) {
        this.context = context;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public List<List<String>> makePopularMoviesRequest() throws IOException, JSONException {
        String base_url = "https://api.themoviedb.org/3/movie/popular?api_key=" + Constants.MOVIE_DB_API_KEY + "&language=en-U";
        try {
            return makeNetworkRequestAndParseResponse(new URL(base_url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<List<String>> makeTopRatingMoviesRequest() throws IOException, JSONException {
        String base_url = "https://api.themoviedb.org/3/movie/top_rated?api_key=" + Constants.MOVIE_DB_API_KEY + "&language=en-U";
        try {
            return makeNetworkRequestAndParseResponse(new URL(base_url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public MovieDetails makeMovieDetailsRequest(String movieId) throws IOException, JSONException {
        String base_url = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + Constants.MOVIE_DB_API_KEY + "&language=en-U";
        try {
            String response = makeNetworkCall(new URL(base_url));
            return new ResponseParser().parseDetailsResponse(response);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return new MovieDetails();
    }

    public List<ReviewDetails> makeReviewRequest(String movieId) throws JSONException, IOException {
        String base_url = "https://api.themoviedb.org/3/movie/" + movieId + "/reviews?api_key=" + Constants.MOVIE_DB_API_KEY + "&language=en-US&page=1";
        try {
            String response = makeNetworkCall(new URL(base_url));
            return new ResponseParser().parseReviewResponse(response);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<TrailerDetails> makeTrailerRequest(String movieId) throws JSONException, IOException {
        String base_url = "https://api.themoviedb.org/3/movie/" + movieId + "/videos?api_key=" + Constants.MOVIE_DB_API_KEY + "&language=en-US";
        try {
            String response = makeNetworkCall(new URL(base_url));
            return new ResponseParser().parseTrailerResponse(response);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] downloadPosterImage(String posterPath) throws IOException {
        String base_url = Constants.IMAGE_BASE_URL + Constants.IMAGE_FILE_SIZE + posterPath;
        try{
            String response = makeNetworkCall(new URL(base_url));
            return response.getBytes();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<List<String>> makeNetworkRequestAndParseResponse(final URL url) throws IOException, JSONException {
        String response = makeNetworkCall(url);
        return new ResponseParser().parseMovieListResponse(response);
    }

    private String makeNetworkCall(URL url) throws IOException {
        InputStream stream = null;
        HttpsURLConnection connection = null;
        String result = null;
        try {
            connection = (HttpsURLConnection) url.openConnection();
            connection.setReadTimeout(3000);
            connection.setConnectTimeout(3000);
            connection.setRequestMethod("GET");
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpsURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + responseCode);
            }
            // Retrieve the response body as an InputStream.
            stream = connection.getInputStream();
            if (stream != null) {
                result = readResponseString(stream);
            }
        } finally {
            // Close Stream and disconnect HTTPS connection.
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;
    }

    private String readResponseString(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return "";
        }
        String responseString = "";
        BufferedReader reader = null;
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);
            responseString = readResponse(reader);

        } finally {
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
            inputStream.close();
        }
        return responseString;
    }

    private String readResponse(BufferedReader reader) throws IOException {
        if (reader == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        String line = "";
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        return builder.toString();
    }

}
