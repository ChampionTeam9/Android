package com.example.adproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
// 设置FLAG_ACTIVITY_CLEAR_TOP标志位
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
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