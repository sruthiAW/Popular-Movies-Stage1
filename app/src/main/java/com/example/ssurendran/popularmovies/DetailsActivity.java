package com.example.ssurendran.popularmovies;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ssurendran.popularmovies.models.MovieDetails;
import com.example.ssurendran.popularmovies.network.RequestsBuilder;
import com.example.ssurendran.popularmovies.utils.Constants;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;

public class DetailsActivity extends AppCompatActivity {

    private ImageView moviePoster;
    private TextView movieName;
    private TextView releaseDate;
    private TextView userRating;
    private TextView plot;
    private TextView noContentTv;
    private TextView reviewLink;
    private ImageView favoriteIcon;
    private RelativeLayout mainLayout;
    private RequestsBuilder requestsBuilder;
    private String movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        requestsBuilder = new RequestsBuilder(this);

        movieId = getIntent().getStringExtra(Constants.MOVIE_ID_EXTRA);

        initializeUI();
        fetchMovieDetails();
    }

    private void initializeUI() {
        moviePoster = (ImageView) findViewById(R.id.movie_poster);
        movieName = (TextView) findViewById(R.id.movie_name);
        releaseDate = (TextView) findViewById(R.id.release_date);
        userRating = (TextView) findViewById(R.id.user_rating);
        plot = (TextView) findViewById(R.id.plot);
        noContentTv = (TextView) findViewById(R.id.no_content);
        mainLayout = (RelativeLayout) findViewById(R.id.main_details_rl);
        reviewLink = (TextView) findViewById(R.id.reviews_link);
        favoriteIcon = (ImageView) findViewById(R.id.favorite_icon);

        reviewLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reviewIntent = new Intent(DetailsActivity.this, ReviewsActivity.class);
                reviewIntent.putExtra(Constants.MOVIE_ID_EXTRA, movieId);
                startActivity(reviewIntent);
            }
        });

        favoriteIcon.setImageResource(R.drawable.ic_star_border_black_24dp);
        favoriteIcon.setColorFilter(ContextCompat.getColor(this, R.color.yellow), PorterDuff.Mode.SRC_IN);

        favoriteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void setDetails(MovieDetails movieDetails) {

        movieName.setText(movieDetails.getMovieName());
        userRating.append(movieDetails.getUserRating());
        releaseDate.append(movieDetails.getReleaseDate());
        plot.setText(movieDetails.getMoviePlot());

        String poster_path = movieDetails.getPosterPath();
        Picasso.with(this).load(Constants.IMAGE_BASE_URL + Constants.IMAGE_FILE_SIZE + poster_path).into(moviePoster);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    private void fetchMovieDetails() {
        new AsyncTask<Void, Void, MovieDetails>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                noContentTv.setVisibility(View.VISIBLE);
                mainLayout.setVisibility(View.GONE);

                noContentTv.setText(R.string.please_wait_while_we_load);
            }

            @Override
            protected MovieDetails doInBackground(Void... voids) {
                if (!requestsBuilder.isNetworkAvailable()) {
                    noContentTv.setText(R.string.no_internet_msg);
                    return null;
                }
                try {
                    return requestsBuilder.makeMovieDetailsRequest(movieId);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(MovieDetails movieDetails) {
                if (movieDetails == null && requestsBuilder.isNetworkAvailable()) {
                    noContentTv.setText(R.string.error_try_again_msg);
                    return;
                } else if (movieDetails == null) {
                    return;
                }
                noContentTv.setVisibility(View.GONE);
                mainLayout.setVisibility(View.VISIBLE);
                setDetails(movieDetails);
            }
        }.execute(null, null, null);
    }

    private void fetchMovieTrailers() {
        new AsyncTask<Void, Void, MovieDetails>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                noContentTv.setVisibility(View.VISIBLE);
                mainLayout.setVisibility(View.GONE);

                noContentTv.setText(R.string.please_wait_while_we_load);
            }

            @Override
            protected MovieDetails doInBackground(Void... voids) {
                if (!requestsBuilder.isNetworkAvailable()) {
                    noContentTv.setText(R.string.no_internet_msg);
                    return null;
                }
                try {
                    return requestsBuilder.makeMovieDetailsRequest(movieId);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(MovieDetails movieDetails) {
                if (movieDetails == null && requestsBuilder.isNetworkAvailable()) {
                    noContentTv.setText(R.string.error_try_again_msg);
                    return;
                } else if (movieDetails == null) {
                    return;
                }
                noContentTv.setVisibility(View.GONE);
                mainLayout.setVisibility(View.VISIBLE);
                setDetails(movieDetails);
            }
        }.execute(null, null, null);
    }
}
