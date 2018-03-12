package com.example.ssurendran.popularmovies.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ssurendran.popularmovies.R;
import com.example.ssurendran.popularmovies.models.ReviewDetails;

import java.util.List;

/**
 * Created by ssurendran on 3/11/18.
 */

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ViewHolder> {

    Context context;
    List<ReviewDetails> reviewDetailsList;

    public ReviewListAdapter(Context context, List<ReviewDetails> reviewDetailsList) {
        this.context = context;
        this.reviewDetailsList = reviewDetailsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.review_list_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ReviewDetails reviewDetails = reviewDetailsList.get(position);
        holder.author.setText(reviewDetails.getAuthor());
        holder.review.setText(reviewDetails.getReview());
    }

    @Override
    public int getItemCount() {
        return reviewDetailsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView avatar;
        TextView author;
        TextView review;

        public ViewHolder(View itemView) {
            super(itemView);
            avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
            author = (TextView) itemView.findViewById(R.id.tv_author);
            review = (TextView) itemView.findViewById(R.id.tv_review);

            avatar.setColorFilter(ContextCompat.getColor(context, R.color.black_60), android.graphics.PorterDuff.Mode.SRC_IN);
        }
    }

}
