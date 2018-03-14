package com.example.ssurendran.popularmovies;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ssurendran.popularmovies.adapters.MovieListAdapter;
import com.example.ssurendran.popularmovies.models.MovieDetails;
import com.example.ssurendran.popularmovies.network.RequestsBuilder;
import com.example.ssurendran.popularmovies.storage.FavoritesDBHelper;
import com.example.ssurendran.popularmovies.utils.ItemOffsetDecoration;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MovieListActivity extends AppCompatActivity {

    RecyclerView movieRecyclerView;
    MovieListAdapter movieListAdapter;
    TextView noContentTv;
    MoviePref moviePref;
    RequestsBuilder requestsBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        moviePref = new MoviePref(this);
        requestsBuilder = new RequestsBuilder(this);

        noContentTv = (TextView) findViewById(R.id.no_content);
        movieRecyclerView = (RecyclerView) findViewById(R.id.movie_recycler_view);
        setUpRecyclerView();

        fetchMovieList();

    }

    private void setUpRecyclerView() {
        movieListAdapter = new MovieListAdapter(this, new ArrayList<MovieDetails>());
        movieRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        movieRecyclerView.addItemDecoration(new ItemOffsetDecoration(this, R.dimen.grid_spacing));
        movieRecyclerView.setHasFixedSize(true);
        movieRecyclerView.setAdapter(movieListAdapter);
    }

    private void updateRecyclerView(List<MovieDetails> movieList){
        movieListAdapter.refreshData(movieList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(moviePref.getSortOrder().equalsIgnoreCase(getString(R.string.favorites_sort))){
            fetchMovieList();
        }
    }

    private void fetchMovieList() {
        new AsyncTask<Void, Void, List<MovieDetails>>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                noContentTv.setVisibility(View.VISIBLE);
                movieRecyclerView.setVisibility(View.GONE);

                noContentTv.setText(R.string.please_wait_while_we_load);
            }

            @Override
            protected List<MovieDetails> doInBackground(Void... voids) {
                String sortOrder = moviePref.getSortOrder();
                if (!requestsBuilder.isNetworkAvailable() && !sortOrder.equalsIgnoreCase(getString(R.string.favorites_sort))){
                    noContentTv.setText(R.string.no_internet_msg);
                    return null;
                }

                try {
                    if (sortOrder.equalsIgnoreCase(getString(R.string.popular_sort))) {
                        return requestsBuilder.makePopularMoviesRequest();
                    } else if (sortOrder.equalsIgnoreCase(getString(R.string.top_rated_sort))){
                        return requestsBuilder.makeTopRatingMoviesRequest();
                    } else {
                        return new FavoritesDBHelper().getAllFavoriteMovies(MovieListActivity.this);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<MovieDetails> movieList) {
                if (movieList == null && requestsBuilder.isNetworkAvailable()){
                    noContentTv.setText(R.string.error_try_again_msg);
                    return;
                }
                else if (movieList != null && movieList.size() == 0){
                    noContentTv.setText(R.string.no_movies_to_show_here);
                    return;
                }
                else if (movieList == null){
                    return;
                }
                movieRecyclerView.setVisibility(View.VISIBLE);
                noContentTv.setVisibility(View.GONE);
                updateRecyclerView(movieList);
            }
        }.execute(null, null, null);
    }

    private void sortMovieList() {
        new AsyncTask<Void, Void, List<MovieDetails>>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                noContentTv.setVisibility(View.VISIBLE);
                movieRecyclerView.setVisibility(View.GONE);

                noContentTv.setText(R.string.please_wait_while_we_load);
            }

            @Override
            protected List<MovieDetails> doInBackground(Void... voids) {
                String sortOrder = moviePref.getSortOrder();
                if (!requestsBuilder.isNetworkAvailable() && !sortOrder.equalsIgnoreCase(getString(R.string.favorites_sort))){
                    noContentTv.setText(R.string.no_internet_msg);
                    return null;
                }

                try {
                    if (sortOrder.equalsIgnoreCase(getString(R.string.popular_sort))) {
                        return requestsBuilder.makePopularMoviesRequest();
                    } else if (sortOrder.equalsIgnoreCase(getString(R.string.top_rated_sort))){
                        return requestsBuilder.makeTopRatingMoviesRequest();
                    } else{
                        return new FavoritesDBHelper().getAllFavoriteMovies(MovieListActivity.this);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<MovieDetails> movieList) {
                if (movieList == null && requestsBuilder.isNetworkAvailable()){
                    noContentTv.setText(R.string.error_try_again_msg);
                    return;
                }
                else if (movieList != null && movieList.size() == 0){
                    noContentTv.setText(R.string.no_movies_to_show_here);
                    return;
                }
                else if (movieList == null){
                    return;
                }
                movieRecyclerView.setVisibility(View.VISIBLE);
                noContentTv.setVisibility(View.GONE);
                updateRecyclerView(movieList);
            }
        }.execute(null, null, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movie_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sort) {
            launchSortDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    private void launchSortDialog() {

        final String[] sortList = new String[]{getString(R.string.popular_sort), getString(R.string.top_rated_sort), getString(R.string.favorites_sort)};

        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setTitle(R.string.sort_dialog_title);

        final int checkedItem = moviePref.getSortDialogCheckedItem();

        alt_bld.setSingleChoiceItems(sortList, checkedItem, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (checkedItem == item){
                    dialog.dismiss();
                    return;
                }
                if (requestsBuilder.isNetworkAvailable()) {
                    Toast.makeText(getApplicationContext(), R.string.sorting_load, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.no_network_connection_sort_dialog, Toast.LENGTH_SHORT).show();
                }
                moviePref.setSortOrder(sortList[item]);
                moviePref.setSortDialogCheckedItem(item);
                sortMovieList();
                dialog.dismiss();

            }
        });
        AlertDialog alert = alt_bld.create();
        alert.show();
    }

}
