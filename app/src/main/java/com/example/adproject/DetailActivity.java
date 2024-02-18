package com.example.adproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DetailActivity extends AppCompatActivity {
    Button shoppinglistbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);



        Intent intent = getIntent();
        Recipe recipe = intent.getParcelableExtra("Recipe");

        shoppinglistbtn = findViewById(R.id.addShoppingList);
        shoppinglistbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("user_pref", Context.MODE_PRIVATE);
                String username = sharedPreferences.getString("username", null);
                if (username == null) {
                    Toast.makeText(DetailActivity.this, "Please log in first", Toast.LENGTH_SHORT).show();
                } else {
                    List<String> ingredients = recipe.getIngredients();
                    String[] ingredientsArray = ingredients.toArray(new String[0]);
                    Intent intent = new Intent(DetailActivity.this, ShoppingListActivity.class);
                    intent.putExtra("INGREDIENTS_ARRAY", ingredientsArray);
                    Log.d("shoppinglist", "Ingredients sent to ShoppingListActivity: " + Arrays.toString(ingredientsArray));
                    startActivity(intent);
                }

            }
        });


        RecyclerView reviewsRecyclerView = findViewById(R.id.recycler_reviews);
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        Log.d("DetailActivity", "Recipe: " + recipe);


        RecyclerView stepsRecyclerView = findViewById(R.id.recycler_steps);

        stepsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        StepsAdapter stepsAdapter = new StepsAdapter(recipe.getSteps());
        stepsRecyclerView.setAdapter(stepsAdapter);

        RecyclerView ingredientsRecyclerView = findViewById(R.id.recycler_ingredients);
        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        IngredientsAdapter ingredientsAdapter = new IngredientsAdapter(recipe.getIngredients());
        //Log.d("drecipein", String.valueOf(recipe.getIngredients().size()));

        ingredientsRecyclerView.setAdapter(ingredientsAdapter);



        Log.d("DetailActivity", "Recipe: " + recipe);


        // 更新UI
        TextView nameTextView = findViewById(R.id.recipe_name);
        TextView descriptionTextView = findViewById(R.id.recipe_description);
        TextView ratingTextView = findViewById(R.id.recipe_rating);
        TextView numberOfRatingTextView = findViewById(R.id.recipe_numberOfRating);
        TextView numberOfSavedTextView = findViewById(R.id.recipe_numberOfSaved);
        TextView recipeIdTextView = findViewById(R.id.recipe_id);
        TextView submittedDateTextView = findViewById(R.id.recipe_submittedDate);
        TextView tagsTextView = findViewById(R.id.recipe_tags);
        TextView servingsTextView = findViewById(R.id.recipe_servings);
        TextView preparationTimeTextView = findViewById(R.id.recipe_preparationTime);
        ImageView imageView = findViewById(R.id.recipe_image);
        RatingBar ratingBar = findViewById(R.id.recipe_ratingbar);
        TextView healthScoreTextView = findViewById(R.id.health_score);
        TextView cal = findViewById(R.id.recipe_calories);
        TextView protein = findViewById(R.id.recipe_protein);
        TextView carbohydrate = findViewById(R.id.recipe_carbohydrate);
        TextView suger = findViewById(R.id.recipe_sugar);
        TextView sodium = findViewById(R.id.recipe_sodium);
        TextView fat = findViewById(R.id.recipe_fat);
        TextView saturatedfat = findViewById(R.id.recipe_saturatedFat);
        TextView memberUsername = findViewById(R.id.recipe_member_username);

        if (recipe != null) {
            nameTextView.setText(recipe.getName());
            memberUsername.setText(recipe.getUsername());
            descriptionTextView.setText(recipe.getDescription());
            ratingTextView.setText(String.valueOf(recipe.getRating()));
            numberOfRatingTextView.setText(recipe.getNumberOfRating() + " reviews");
            numberOfSavedTextView.setText(String.valueOf(recipe.getNumberOfSaved()) + " saved");
            recipeIdTextView.setText(String.valueOf(recipe.getId()));
            submittedDateTextView.setText(recipe.getSubmittedDate().toString());
            tagsTextView.setText(TextUtils.join(", ", recipe.getTags()));
            servingsTextView.setText(String.valueOf(recipe.getServings()));
            preparationTimeTextView.setText(recipe.getPreparationTime() + " mins");
            String imageUrl = "http://10.0.2.2:8080/images/" + recipe.getImage();
            Picasso.get().load(imageUrl).into(imageView);
            ratingBar.setRating(recipe.getRating().floatValue());
            int healthScore = recipe.getHealthScore();
            if (healthScore <= 1) {
                // 为0或1分设置红色
                healthScoreTextView.setTextColor(Color.RED);
            } else if (healthScore <= 3) {
                // 为2或3分设置橙色
                healthScoreTextView.setTextColor(Color.parseColor("#FFA500")); // 橙色
            } else {
                // 为4, 5, 6分设置绿色
                healthScoreTextView.setTextColor(Color.GREEN);
            }
            healthScoreTextView.setText(healthScore + " /6");
            cal.setText(String.valueOf(recipe.getCalories()) + " kcal");
            protein.setText(String.valueOf(recipe.getProtein()));
            carbohydrate.setText(String.valueOf(recipe.getCarbohydrate()));

            Log.d("recipeI", "Sugar Value: " + recipe.getSugar());
            Log.d("recipeI", "recipe name: " + recipe.getName());
            Log.d("recipeI", "recipe id: " + recipe.getId());
            Log.d("recipeI", "recipe Sodium: " + recipe.getSodium());

            suger.setText(String.valueOf(recipe.getSugar()));
            sodium.setText(String.valueOf(recipe.getSodium()));
            fat.setText(String.valueOf(recipe.getFat()));
            saturatedfat.setText(String.valueOf(recipe.getSaturatedFat()));
            getReview();
        }
    }

    private void getReview() {

        OkHttpClient client = new OkHttpClient();
        Intent intent = getIntent();
        Recipe recipe = intent.getParcelableExtra("Recipe");
        int recipeid = recipe.getId();
        String url = "http://10.0.2.2:8080/api/getReviewData?recipeid=" + recipeid;

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 请求失败的处理
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // 将响应体转换为字符串
                    String responseData = response.body().string();
                    // 将字符串转换为JSONArray
                    try {
                        JSONArray jsonArray = new JSONArray(responseData);
                        List<Review> reviews = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject reviewObject = jsonArray.getJSONObject(i);
                            // 用reviewObject填充Review对象
                            Review review = new Review();
                            review.setMemberId(reviewObject.optInt("memberId"));
                            review.setId(reviewObject.optInt("id"));
                            review.setComment(reviewObject.optString("comment"));
                            int rating = reviewObject.optInt("rating");
                            review.setRating(rating);
                            String dateStr = reviewObject.optString("reviewDate");
                            Log.d("comment", review.getComment());
                            if (!dateStr.isEmpty()) {
                                LocalDate reviewDate = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
                                review.setReviewDate(reviewDate);
                            }
                            reviews.add(review);
                        }

                        // 在UI线程中更新UI
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // 使用review更新RecyclerView的适配器
                                updateReviewRecyclerView(reviews);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // 请求失败的处理
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DetailActivity.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void updateReviewRecyclerView(List<Review> reviewDTOs) {
        RecyclerView reviewsRecyclerView = findViewById(R.id.recycler_reviews);

        ReviewAdapter adapter = (ReviewAdapter) reviewsRecyclerView.getAdapter();
        if (adapter == null) {

            adapter = new ReviewAdapter(reviewDTOs);
            reviewsRecyclerView.setAdapter(adapter);
        } else {
            adapter.updateData(reviewDTOs);
        }
    }
}