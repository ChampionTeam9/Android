package com.example.adproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

        if (recipe != null) {
            nameTextView.setText(recipe.getName());
            descriptionTextView.setText(recipe.getDescription());
            ratingTextView.setText(String.valueOf(recipe.getRating()));
            numberOfRatingTextView.setText(String.valueOf(recipe.getNumberOfRating()));
            numberOfSavedTextView.setText(String.valueOf(recipe.getNumberOfSaved()));
            recipeIdTextView.setText(String.valueOf(recipe.getId()));
            submittedDateTextView.setText(recipe.getSubmittedDate().toString());
            tagsTextView.setText(TextUtils.join(", ", recipe.getTags()));
            servingsTextView.setText(String.valueOf(recipe.getServings()));
            preparationTimeTextView.setText(recipe.getPreparationTime() + " mins");
        }


    }
}