package com.example.yodenproject.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.yodenproject.Model.Review;
import com.example.yodenproject.R;
import com.github.abdularis.civ.CircleImageView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<Review> reviewsList;
    private MyReviewListener listener;

    public interface MyReviewListener{
        void onReviewClick(int position);
    }

    public void setListener(MyReviewListener listener){
        this.listener=listener;
    }

    public ReviewAdapter(List<Review> reviewsList) {
        this.reviewsList = reviewsList;
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder{

        TextView reviewerName;
        CircleImageView reviewerImage;
        TextView reviewInfo;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            reviewerName=itemView.findViewById(R.id.name_reviewer_tv);
            reviewInfo=itemView.findViewById(R.id.reviewer_info_tv);
            reviewerImage=itemView.findViewById(R.id.image_of_reviewer);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null)
                        listener.onReviewClick(getAdapterPosition());
                }
            });

        }
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.review_fragment,parent,false);
        ReviewViewHolder reviewViewHolder=new ReviewViewHolder(view);

        return reviewViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {

        Review review=reviewsList.get(position);
        holder.reviewInfo.setText(review.getReview());
        holder.reviewerName.setText(review.getNameClient());
        if(review.getClientImage().equals("default"))
            holder.reviewerImage.setImageResource(R.drawable.ic_user);
        else Glide.with(holder.reviewerImage.getContext()).load(review.getClientImage()).into(holder.reviewerImage);
    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }
}
