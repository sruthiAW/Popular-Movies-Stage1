package com.example.ssurendran.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ssurendran.popularmovies.R;
import com.example.ssurendran.popularmovies.models.TrailerDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ssurendran on 3/14/18.
 */

public class TrailerListAdapter extends RecyclerView.Adapter<TrailerListAdapter.ViewHolder> {

    private Context context;
    private List<TrailerDetails> trailers = new ArrayList<>();

    public TrailerListAdapter(Context context, List<TrailerDetails> trailers) {
        this.context = context;
        this.trailers = trailers;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_list_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.trailerName.append(position+1 + "");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(trailers.get(position).getTrailerUrl()));
                context.startActivity(Intent.createChooser(intent, "Open with"));
            }
        });

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent share = new Intent(android.content.Intent.ACTION_SEND);
                share.setType("text/plain");
                context.startActivity(Intent.createChooser(share, "Share with"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView trailerName;
        ImageView share;

        public ViewHolder(View itemView) {
            super(itemView);
            trailerName = (TextView) itemView.findViewById(R.id.trailerName_tv);
            share = (ImageView) itemView.findViewById(R.id.share_iv);
        }
    }

}
