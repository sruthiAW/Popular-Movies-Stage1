package com.example.ssurendran.popularmovies;

import android.content.ContentUris;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ssurendran.popularmovies.adapters.TrailerListAdapter;
import com.example.ssurendran.popularmovies.models.MovieDetails;
import com.example.ssurendran.popularmovies.models.TrailerDetails;
import com.example.ssurendran.popularmovies.network.RequestsBuilder;
import com.example.ssurendran.popularmovies.storage.FavoritesDBHelper;
import com.example.ssurendran.popularmovies.storage.MoviesContract;
import com.example.ssurendran.popularmovies.utils.Constants;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    private static final String FAVORITE = "favorite";
    private static final String NORMAL = "normal";

    private ImageView moviePoster;
    private TextView movieName;
    private TextView releaseDate;
    private TextView userRating;
    private TextView plot;
    private TextView noContentTv;
    private TextView reviewLink;
    private ImageView favoriteIcon;
    private TextView noTrailerTv;
    private RecyclerView trailerRecyclerView;
    private RelativeLayout mainLayout;

    private RequestsBuilder requestsBuilder;
    private String movieId;
    private MovieDetails mMovieDetails;
    private MoviePref moviePref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        requestsBuilder = new RequestsBuilder(this);
        moviePref = new MoviePref(this);

        movieId = getIntent().getStringExtra(Constants.MOVIE_ID_EXTRA);

        initializeUI();
        fetchMovieDetails();
        fetchMovieTrailers();
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
        noTrailerTv = (TextView) findViewById(R.id.no_trailer_tv);
        trailerRecyclerView = (RecyclerView) findViewById(R.id.trailer_list);

        reviewLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reviewIntent = new Intent(DetailsActivity.this, ReviewsActivity.class);
                reviewIntent.putExtra(Constants.MOVIE_ID_EXTRA, movieId);
                startActivity(reviewIntent);
            }
        });

//        setUpFavoriteIcon();

        favoriteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFavoriteStatus();
            }
        });
    }

    private void setDetails(MovieDetails movieDetails) {
        mMovieDetails = movieDetails;
        movieName.setText(movieDetails.getMovieName());
        userRating.append(movieDetails.getUserRating());
        releaseDate.append(movieDetails.getReleaseDate());
        plot.setText(movieDetails.getMoviePlot());


        if (movieDetails.isFavorite()){
            favoriteIcon.setImageResource(R.drawable.ic_star_black_24dp);
            favoriteIcon.setTag(FAVORITE);
        } else {
            favoriteIcon.setImageResource(R.drawable.ic_star_border_black_24dp);
            favoriteIcon.setTag(NORMAL);
        }
        favoriteIcon.setColorFilter(ContextCompat.getColor(DetailsActivity.this, R.color.yellow), PorterDuff.Mode.SRC_IN);


        String poster_path = movieDetails.getPosterPath();
        if (poster_path != null) {
            Picasso.with(this).load(Constants.IMAGE_BASE_URL + Constants.IMAGE_FILE_SIZE + poster_path).into(moviePoster);
            return;
        }

        Bitmap posterBitmap = movieDetails.getMoviePoster();
        if (posterBitmap != null){
            moviePoster.setImageBitmap(posterBitmap);
        }

    }

    private void setUpTrailerList(List<TrailerDetails> trailerList){
        trailerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        trailerRecyclerView.setHasFixedSize(true);
        trailerRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        trailerRecyclerView.setAdapter(new TrailerListAdapter(this, trailerList));
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
                String sortOrder = moviePref.getSortOrder();
                if (!requestsBuilder.isNetworkAvailable() && !sortOrder.equalsIgnoreCase(getString(R.string.favorites_sort))) {
                    noContentTv.setText(R.string.no_internet_msg);
                    return null;
                }
                try {
                    if(sortOrder.equalsIgnoreCase(getString(R.string.favorites_sort))){
                        MovieDetails movieDetails = new FavoritesDBHelper().readData(DetailsActivity.this, movieId);
                        movieDetails.setFavorite(true);
                        return movieDetails;
                    } else {
                        MovieDetails movieDetails = requestsBuilder.makeMovieDetailsRequest(movieId);
                        movieDetails.setFavorite(false);
                        return movieDetails;
                    }
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
        new AsyncTask<Void, Void, List<TrailerDetails>>() {

            @Override
            protected void onPreExecute() {
                noTrailerTv.setVisibility(View.VISIBLE);
                trailerRecyclerView.setVisibility(View.GONE);

                noTrailerTv.setText(R.string.please_wait_while_we_load);
            }

            @Override
            protected List<TrailerDetails> doInBackground(Void... voids) {
                if (!requestsBuilder.isNetworkAvailable()) {
                    noTrailerTv.setText(R.string.no_internet_msg);
                    return null;
                }
                try {
                    return requestsBuilder.makeTrailerRequest(movieId);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<TrailerDetails> trailers) {
                if (trailers == null && requestsBuilder.isNetworkAvailable()) {
                    noTrailerTv.setText(R.string.error_try_again_msg);
                    return;
                } else if (trailers != null && trailers.size() == 0 && requestsBuilder.isNetworkAvailable()) {
                    noTrailerTv.setText(R.string.no_trailers_found);
                    return;
                } else if (trailers == null || (trailers != null && trailers.size() == 0)){
                    return;
                }
                noTrailerTv.setVisibility(View.GONE);
                trailerRecyclerView.setVisibility(View.VISIBLE);
                setUpTrailerList(trailers);
            }
        }.execute(null, null, null);
    }

    private void toggleFavoriteStatus(){
        new AsyncTask<Void, Void, Void>(){

            @Override
            protected void onPreExecute() {
                if (favoriteIcon.getTag().equals(FAVORITE)) {
                    favoriteIcon.setImageResource(R.drawable.ic_star_border_black_24dp);
                    Toast.makeText(DetailsActivity.this, R.string.removed_from_favorites, Toast.LENGTH_SHORT).show();
                } else {
                    favoriteIcon.setImageResource(R.drawable.ic_star_black_24dp);
                    Toast.makeText(DetailsActivity.this, R.string.added_to_favorites, Toast.LENGTH_SHORT).show();
                }

                favoriteIcon.setColorFilter(ContextCompat.getColor(DetailsActivity.this, R.color.yellow), PorterDuff.Mode.SRC_IN);
                favoriteIcon.setEnabled(false);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                if (favoriteIcon.getTag().equals(FAVORITE)){
                    int deletedCount = DetailsActivity.this.getContentResolver().delete(
                            ContentUris.withAppendedId(MoviesContract.FavoriteMovieEntry.CONTENT_URI, Long.valueOf(movieId)),
                            null,
                            null);

                } else{
                    new FavoritesDBHelper().persist(DetailsActivity.this, movieId, mMovieDetails);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (favoriteIcon.getTag().equals(FAVORITE)) {
                    favoriteIcon.setTag(NORMAL);
                } else {
                    favoriteIcon.setTag(FAVORITE);
                }
                favoriteIcon.setEnabled(true);
            }
        }.execute(null, null, null);
    }

}
