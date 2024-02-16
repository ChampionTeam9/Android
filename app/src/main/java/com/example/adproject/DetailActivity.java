package com.example.adproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

//        Button backButton = findViewById(R.id.back_button);
//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//             // 设置FLAG_ACTIVITY_CLEAR_TOP标志位
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//
//            }
//        });
        // 获取从MainActivity传递的食谱对象

        Intent intent = getIntent();
        Recipe recipe = intent.getParcelableExtra("Recipe");

        // 找到步骤RecyclerView
        RecyclerView stepsRecyclerView = findViewById(R.id.recycler_steps);
        // 设置布局管理器
        stepsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 创建并设置适配器
        StepsAdapter stepsAdapter = new StepsAdapter(recipe.getSteps());
        stepsRecyclerView.setAdapter(stepsAdapter);
        // 在获取对象之后立即打印日志
        Log.d("DetailActivity", "Recipe: " + recipe);

        if (recipe != null) {
            Log.d("DetailActivity", "Recipe name: " + recipe.getName());
            Log.d("DetailActivity", "Recipe description: " + recipe.getDescription());
            // 更新UI...
        } else {
            Log.d("DetailActivity", "Recipe object is null");
        }

        // 更新UI
        TextView nameTextView = findViewById(R.id.recipe_name);
        TextView descriptionTextView = findViewById(R.id.recipe_description);
        TextView ratingTextView=findViewById(R.id.recipe_rating);
        TextView numberOfRatingTextView=findViewById(R.id.recipe_numberOfRating);
        TextView numberOfSavedTextView=findViewById(R.id.recipe_numberOfSaved);
        TextView recipeIdTextView=findViewById(R.id.recipe_id);
        TextView submittedDateTextView=findViewById(R.id.recipe_submittedDate);
        TextView tagsTextView=findViewById(R.id.recipe_tags);
        TextView servingsTextView=findViewById(R.id.recipe_servings);
        TextView preparationTimeTextView=findViewById(R.id.recipe_preparationTime);
        ImageView imageView=findViewById(R.id.recipe_image);
        RatingBar ratingBar=findViewById(R.id.recipe_ratingbar);
        TextView healthScoreTextView=findViewById(R.id.health_score);
        TextView cal=findViewById(R.id.recipe_calories);
        TextView protein=findViewById(R.id.recipe_protein);
        TextView carbohydrate=findViewById(R.id.recipe_carbohydrate);
        TextView suger=findViewById(R.id.recipe_sugar);
        TextView sodium=findViewById(R.id.recipe_sodium);
        TextView fat=findViewById(R.id.recipe_fat);
        TextView saturatedfat=findViewById(R.id.recipe_saturatedFat);
        TextView memberUsername=findViewById(R.id.recipe_member_username);

        if (recipe != null) {
            nameTextView.setText(recipe.getName());
            memberUsername.setText(recipe.getUsername());
            descriptionTextView.setText(recipe.getDescription());
            ratingTextView.setText(String.valueOf(recipe.getRating()));
            numberOfRatingTextView.setText(recipe.getNumberOfRating() + " reviews");
            numberOfSavedTextView.setText(String.valueOf(recipe.getNumberOfSaved())+" saved");
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
            cal.setText(String.valueOf(recipe.getCalories())+" kcal");
            protein.setText(String.valueOf(recipe.getProtein()));
            carbohydrate.setText(String.valueOf(recipe.getCarbohydrate()));



            Log.d("YourTag", "Sugar Value: " + recipe.getSugar());
            Log.d("YourTag", "recipe name: " + recipe.getName());
            Log.d("YourTag", "recipe id: " + recipe.getId());
            Log.d("YourTag", "recipe Sodium: " + recipe.getSodium());

            suger.setText(String.valueOf(recipe.getSugar()));
            sodium.setText(String.valueOf(recipe.getSodium()));
            fat.setText(String.valueOf(recipe.getFat()));
            saturatedfat.setText(String.valueOf(recipe.getSaturatedFat()));



        }


    }
}