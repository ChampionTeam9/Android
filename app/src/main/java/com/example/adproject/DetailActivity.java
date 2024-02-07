package com.example.adproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // 获取从MainActivity传递的食谱对象
        Recipe recipe = (Recipe) getIntent().getSerializableExtra("Recipe");

        // 更新UI
        TextView nameTextView = findViewById(R.id.detail_recipe_name);
        TextView descriptionTextView = findViewById(R.id.detail_recipe_description);

        if (recipe != null) {
            nameTextView.setText(recipe.getName());
            descriptionTextView.setText(recipe.getDescription());
        }

    }
}