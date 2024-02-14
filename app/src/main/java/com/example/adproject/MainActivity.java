package com.example.adproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adproject.DetailActivity;
import com.example.adproject.LoginFragment;
import com.example.adproject.Recipe;
import com.example.adproject.RecipeAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecipeAdapter mAdapter;
    private EditText searchEditText;
    private List<Recipe> mAllRecipes = new ArrayList<>();
    private List<Recipe> mRecipes = new ArrayList<>();
    private BottomSheetBehavior<View> bottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化Fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new LoginFragment())
                    .commit();
        }

        // 在 MainActivity 类中找到底部表单的视图
        View bottomSheet = findViewById(R.id.bottom_sheet);

// 使用 BottomSheetBehavior 进行设置
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setDraggable(true);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);


        // 初始化RecyclerView和搜索框
        mRecyclerView = findViewById(R.id.recyclerView);
        searchEditText = findViewById(R.id.searchEditText);
        Button searchButton = findViewById(R.id.searchButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchString = searchEditText.getText().toString();
                searchRecipes(searchString);
            }
        });

        // 初始化Adapter
        mAdapter = new RecipeAdapter(mRecipes, new RecipeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Recipe recipe) {
                Intent detailIntent = new Intent(MainActivity.this, DetailActivity.class);
                detailIntent.putExtra("Recipe", recipe); // 使用Parcelable传递对象
                startActivity(detailIntent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadRecipes(); // 加载食谱数据
    }

    // 其他方法保持不变...

    private void searchRecipes(String query) {
        List<Recipe> filteredRecipes = new ArrayList<>();
        for (Recipe recipe : mAllRecipes) { // 应该使用mAllRecipes进行搜索
            if (recipe.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredRecipes.add(recipe);
            }
        }
        // 更新适配器数据，但不修改mRecipes或mAllRecipes
        mAdapter.updateRecipes(filteredRecipes);
    }


    private void loadRecipes() {
        OkHttpClient client = new OkHttpClient();
        String url = "http://10.0.2.2:3000/getRecipeData";

        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // 这是在MainActivity类中的网络请求回调内
                if (response.isSuccessful()) {
                    final String responseData = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONArray jsonArray = new JSONArray(responseData);
                                List<Recipe> recipes = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonRecipe = jsonArray.getJSONObject(i);
                                    Recipe recipe = Recipe.fromJson(jsonRecipe); // 使用静态工厂方法
                                    recipes.add(recipe);
                                }
                                // 更新完整的食谱列表和当前显示的食谱列表
                                mAllRecipes.clear();
                                mAllRecipes.addAll(recipes);
                                mAdapter.updateRecipes(recipes); // 更新适配器数据
                                mRecipes.clear();
                                mRecipes.addAll(recipes); // 保持mRecipes为最新数据
                            } catch (JSONException e) {
                                Log.e("MainActivity", "JSON parsing error: " + e.getMessage());
                            }
                        }
                    });

                }

            }

            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("MainActivity", "Request failed: " + e.getMessage());
            }



        });
    }



}
