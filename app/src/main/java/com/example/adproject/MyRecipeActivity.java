package com.example.adproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.adproject.DetailActivity;
import com.example.adproject.Recipe;
import com.example.adproject.RecipeAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyRecipeActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecipeAdapter mAdapter;
    private List<Recipe> mRecipes = new ArrayList<>();
    private Button backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipe);



        // 初始化RecyclerView
        mRecyclerView = findViewById(R.id.my_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 从Intent中获取传递的JSON字符串
        String recipeListJson = getIntent().getStringExtra("recipeList");

        // 解析JSON数据并更新RecyclerView
        parseRecipes(recipeListJson);
    }

    private void parseRecipes(String recipeListJson) {
        try {
            // 将JSON字符串转换为JSONArray
            JSONArray jsonArray = new JSONArray(recipeListJson);

            // 遍历JSONArray，解析每个JSONObject并创建Recipe对象
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Recipe recipe = new Recipe();

                // 从JSONObject中提取Recipe对象的属性值
                recipe.setName(jsonObject.getString("name"));
                recipe.setDescription(jsonObject.getString("description"));
                // 继续提取其他属性...

                // 将Recipe对象添加到mRecipes列表中
                mRecipes.add(recipe);
            }

            // 创建并设置适配器
            mAdapter = new RecipeAdapter(mRecipes, new RecipeAdapter.OnItemClickListener() {
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

        } catch (JSONException e) {
            // JSON解析失败，显示错误消息
            Toast.makeText(this, "Failed to parse recipes", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
