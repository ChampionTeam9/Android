package com.example.adproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private List<Review> reviewList;

    public ReviewAdapter(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviewList.get(position);
        if (review != null) {
            if (review.getMemberId() != null) {
                holder.memberUsername.setText(review.getMemberId());
            }
            if (review.getReviewDate() != null) {
                holder.reviewReviewDate.setText(review.getReviewDate().toString());
            }
            holder.reviewRatingBar.setRating(review.getRating());
            if (review.getComment() != null) {
                holder.reviewComment.setText(review.getComment());
            }
        }
    }


    @Override
    public int getItemCount() {
        return reviewList != null ? reviewList.size() : 0;
    }
    public void updateData(List<Review> reviewDTOs) {
        this.reviewList = reviewDTOs;
        notifyDataSetChanged(); // 通知数据变更，刷新UI
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView memberUsername;
        TextView reviewReviewDate;
        RatingBar reviewRatingBar;
        TextView reviewComment;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            memberUsername = itemView.findViewById(R.id.member_username);
            reviewReviewDate = itemView.findViewById(R.id.review_reviewDate);
            reviewRatingBar = itemView.findViewById(R.id.review_ratingbar);
            reviewComment = itemView.findViewById(R.id.review_comment);
        }
    }
}
