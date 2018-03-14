package com.example.ssurendran.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.ssurendran.popularmovies.DetailsActivity;
import com.example.ssurendran.popularmovies.MoviePref;
import com.example.ssurendran.popularmovies.R;
import com.example.ssurendran.popularmovies.models.MovieDetails;
import com.example.ssurendran.popularmovies.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.example.ssurendran.popularmovies.utils.Constants.MOVIE_OBJECT_EXTRA;

/**
 * Created by ssurendran on 2/15/18.
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {
    private List<MovieDetails> movielist = new ArrayList<>();
    /*private List<String > movieNames = new ArrayList<>();
    private List<String > movieIds = new ArrayList<>();
    private List<String > moviePosters = new ArrayList<>();*/
    private Context context;
    private MoviePref moviePref;

    public MovieListAdapter(Context context, List<MovieDetails> movieList) {
        this.context = context;
        this.movielist = movieList;
        moviePref = new MoviePref(context);
        /*movieIds = bigList.get(0);
        movieNames = bigList.get(1);
        moviePosters = bigList.get(2);*/
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        if (moviePref.getSortOrder().equalsIgnoreCase(context.getString(R.string.favorites_sort))){
            holder.moviePoster.setImageBitmap(movielist.get(position).getMoviePoster());
        } else {
            Picasso.with(context).load(Constants.IMAGE_BASE_URL + context.getString(R.string.poster_size) + movielist.get(position).getPosterPath()).into(holder.moviePoster);
        }

        holder.moviePoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchDetailsActivity(position);
            }
        });
    }

    private void launchDetailsActivity(int position) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(Constants.MOVIE_ID_EXTRA, movielist.get(position).getMovieId());
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return movielist.size();
    }

    public void refreshData(List<MovieDetails> movielist) {
        this.movielist = movielist;
        /*movieIds = bigList.get(0);
        movieNames = bigList.get(1);
        moviePosters = bigList.get(2);*/
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView moviePoster;

        public ViewHolder(View itemView) {
            super(itemView);
            moviePoster = (ImageView) itemView.findViewById(R.id.movie_poster);
        }
    }

}
