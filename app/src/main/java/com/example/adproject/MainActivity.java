package com.example.adproject;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

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
    private List<Recipe> mRecipes = new ArrayList<Recipe>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Lookup the recyclerview in activity layout
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // Create adapter passing in the sample user data
        mAdapter = new RecipeAdapter(mRecipes);

        // Attach the adapter to the recyclerview to populate items
        mRecyclerView.setAdapter(mAdapter);

        // Set layout manager to position the items
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // That's all!
        loadRecipes(); // Assume you have this method to load recipes into mRecipes



        // 执行网络请求
        OkHttpClient client = new OkHttpClient();
        String url = "http://10.249.31.248:3000/getRecipeData";

        Request request = new Request.Builder()
                .url(url)
                .build();

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
                                    // 使用新的构造函数创建Recipe对象
                                    Recipe recipe = new Recipe(jsonArray.getJSONObject(i));
                                    recipes.add(recipe);
                                }
                                // 使用新数据更新适配器
                                mAdapter.updateRecipes(recipes);
                            } catch (JSONException e) {
                                Log.e("MainActivity", "JSON parsing error: " + e.getMessage());
                            }
                        }
                    });
                }


            }

            @Override
            public void onFailure(Call call, IOException e) {
                // 处理请求失败的情况
                Log.e("YourActivity", "Request failed: " + e.getMessage());
            }
            // 假设这个方法是用来加载和解析JSON数据的


        });
    }

    private void loadRecipes() {mAdapter.notifyDataSetChanged();
    }



}