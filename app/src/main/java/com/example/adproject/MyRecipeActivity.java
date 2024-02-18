package com.example.adproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adproject.DetailActivity;
import com.example.adproject.Recipe;
import com.example.adproject.RecipeAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyRecipeActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecipeAdapter mAdapter;
    private List<Recipe> mRecipes = new ArrayList<>();
    private Button backbutton;
    private TextView noRecipesText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipe);

        // 初始化RecyclerView
        mRecyclerView = findViewById(R.id.my_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        noRecipesText = findViewById(R.id.noRecipesText);

        // 从Intent中获取传递的JSON字符串
        List<Recipe> recipes = (List<Recipe>) getIntent().getSerializableExtra("recipeList");


        // 解析JSON数据并更新RecyclerView
        parseRecipes(recipes);
    }

    private void parseRecipes(List<Recipe> recipes) {


        // 创建并设置适配器
        mAdapter = new RecipeAdapter(recipes, new RecipeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Recipe recipe) {
                // 处理RecyclerView中的项目点击事件
                // 这里可以打开一个新的Activity，显示食谱的详细信息
                Intent detailIntent = new Intent(MyRecipeActivity.this, DetailActivity.class);
                detailIntent.putExtra("Recipe", recipe); // 确保Recipe类实现了Parcelable接口
                startActivity(detailIntent);
            }
        });

        // 将适配器设置到RecyclerView中
        mRecyclerView.setAdapter(mAdapter);

        if (mRecipes.size() == 0){
            noRecipesText.setVisibility(View.VISIBLE);
        }

    }
}
