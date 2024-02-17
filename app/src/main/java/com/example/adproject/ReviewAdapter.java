package com.example.adproject;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private List<Review> reviewList;
    private OkHttpClient client;

    public ReviewAdapter(List<Review> reviewList) {
        this.reviewList = reviewList;
        this.client = new OkHttpClient();
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
            Log.d("review",review.getComment());
            holder.reviewReviewDate.setText(review.getReviewDate().toString());
            holder.reviewRatingBar.setRating(review.getRating());
            holder.reviewComment.setText(review.getComment());

            Integer memberId = review.getMemberId();
            if (memberId != null) {
                // memberId 不为 null，则设置对应的用户名
                Log.d("reviewmessage", "memberid is not null");
                // 发送请求获取用户名
                getMemberUsername(memberId, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        // 请求失败的处理
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String responseBodyString = response.body().string();

                            // 使用 Handler 在主线程更新 UI
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    // 更新 UI
                                    holder.memberUsername.setText(responseBodyString);
                                }
                            });
                        } else {
                            Log.e("Response", "Unsuccessful response: " + response.code());
                        }
                    }
                });
            } else {
                Log.d("memberid","member id is null");
                holder.memberUsername.setText("Unknown");
            }
        }
    }



    private void getMemberUsername(int memberId, Callback callback) {
        String apiUrl = "http://10.0.2.2:8080/api/getMemberUsername?id=" + memberId;

        // 创建请求对象
        Request request = new Request.Builder()
                .url(apiUrl)
                .build();

        // 发送 GET 请求
        client.newCall(request).enqueue(callback);
        Log.d("reviewsend","success");
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
