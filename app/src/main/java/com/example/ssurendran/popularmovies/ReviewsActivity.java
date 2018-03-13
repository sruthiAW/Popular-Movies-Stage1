package com.example.ssurendran.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.ssurendran.popularmovies.adapters.ReviewListAdapter;
import com.example.ssurendran.popularmovies.models.ReviewDetails;
import com.example.ssurendran.popularmovies.network.RequestsBuilder;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import static com.example.ssurendran.popularmovies.utils.Constants.MOVIE_ID_EXTRA;

public class ReviewsActivity extends AppCompatActivity {

    private RecyclerView reviewRecyclerView;
    private TextView noContentTv;
    private RequestsBuilder requestsBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        requestsBuilder = new RequestsBuilder(this);

        reviewRecyclerView = (RecyclerView) findViewById(R.id.rv_reviews_list);
        noContentTv = (TextView) findViewById(R.id.tv_no_content);

        fetchReviews();
    }

    private void setUpRecyclerView(List<ReviewDetails> reviews){
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviewRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        reviewRecyclerView.setHasFixedSize(true);
        reviewRecyclerView.setAdapter(new ReviewListAdapter(this, reviews));
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

    private void fetchReviews(){
        new AsyncTask<Void, Void, List<ReviewDetails>>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                noContentTv.setVisibility(View.VISIBLE);
                reviewRecyclerView.setVisibility(View.GONE);

                noContentTv.setText(R.string.please_wait_while_we_load);
            }

            @Override
            protected List<ReviewDetails> doInBackground(Void... voids) {
                if (!requestsBuilder.isNetworkAvailable()){
                    noContentTv.setText(R.string.no_internet_msg);
                    return null;
                }

                try {
                    return requestsBuilder.makeReviewRequest(getIntent().getStringExtra(MOVIE_ID_EXTRA));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<ReviewDetails> reviews) {
                if (reviews == null && requestsBuilder.isNetworkAvailable()){
                    noContentTv.setText(R.string.error_try_again_msg);
                    return;
                }
                else if (reviews != null && reviews.size() == 0 && requestsBuilder.isNetworkAvailable()){
                    noContentTv.setText(R.string.no_reviews);
                    return;
                }
                else if (reviews == null ||(reviews != null && reviews.size() == 0)){
                    return;
                }
                reviewRecyclerView.setVisibility(View.VISIBLE);
                noContentTv.setVisibility(View.GONE);
                setUpRecyclerView(reviews);
            }
        }.execute(null, null, null);
    }

}
