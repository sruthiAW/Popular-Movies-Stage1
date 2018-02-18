package com.example.ssurendran.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ssurendran on 2/15/18.
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {
    private List<String > movieNames = new ArrayList<>();
    private List<String > movieIds = new ArrayList<>();
    private List<String > moviePosters = new ArrayList<>();
    private Context context;

    public MovieListAdapter(Context context, List<List<String >> bigList) {
        this.context = context;
        movieIds = bigList.get(0);
        movieNames = bigList.get(1);
        moviePosters = bigList.get(2);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Picasso.with(context).load(Constants.IMAGE_BASE_URL + context.getString(R.string.poster_size) + moviePosters.get(position)).into(holder.moviePoster);

        holder.moviePoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchDetailsActivity(position);
            }
        });
    }

    private void launchDetailsActivity(int position){
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(Constants.MOVIE_ID_EXTRA, movieIds.get(position));
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return movieNames.size();
    }

    public void refreshData(List<List<String>> bigList){
        movieIds = bigList.get(0);
        movieNames = bigList.get(1);
        moviePosters = bigList.get(2);
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView moviePoster;

        public ViewHolder(View itemView) {
            super(itemView);
            moviePoster = (ImageView) itemView.findViewById(R.id.movie_poster);
        }
    }

}
