package com.example.adproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;

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
    private boolean isLoading = false;

    private List<Recipe> mAllRecipes = new ArrayList<>();
    private List<Recipe> mRecipes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavigationView navigationView = findViewById(R.id.navigation_view);
        View headerView = navigationView.getHeaderView(0);
        TextView usernameTextView = headerView.findViewById(R.id.nav_header_title); // 头部布局
        MenuItem loginMenuItem = navigationView.getMenu().findItem(R.id.nav_login); // 假设菜单项的ID为nav_login

        SharedPreferences prefs = getSharedPreferences("user_pref", Context.MODE_PRIVATE);
        boolean isLoggedIn;
        String username = prefs.getString("username", "Guest");
        if (username == "Guest") {
            isLoggedIn = false;
        } else {
            isLoggedIn = true;
        }

        // 更新标题和菜单项
        if (isLoggedIn) {
            // 用户已登录，更新标题为用户名，更改菜单项为"Logout"
            Log.d("Login", "successful");

            usernameTextView.setText(username);
            loginMenuItem.setTitle("Logout");
            loginMenuItem.setIcon(R.drawable.ic_logout); // 假设您有一个表示登出的图标资源
        } else {
            // 用户未登录，将标题设置为"Guest"，菜单项为"Login"
            Log.d("login", "fail");
            usernameTextView.setText("Guest");
            loginMenuItem.setTitle("Login");
            loginMenuItem.setIcon(R.drawable.ic_login); // 假设您有一个表示登录的图标资源
        }

        // 设置 NavigationView 的选择监听器

        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_login) {
                if (isLoggedIn) {
                    // 处理登出逻辑
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("isLoggedIn", false);
                    editor.remove("username");
                    editor.apply();
                    // 更新UI
                    usernameTextView.setText("Guest");
                    item.setTitle("Login");
                    item.setIcon(R.drawable.ic_login);
                    // 重新加载Activity以更新UI
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                } else {
                    // 启动登录Activity
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
            return true;
        });




        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null) {
                    int visibleItemCount = layoutManager.getChildCount(); // 当前在屏幕上可见的项数
                    int totalItemCount = layoutManager.getItemCount(); // 总数据项数
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition(); // 第一个可见项的位置

                    // 如果没有正在加载，并且可见项+第一个可见项的位置>=总项数，说明滑动到了底部
                    if (!isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0 && totalItemCount >= mPageSize) {
                        Log.d("MainActivity", "Loading more items");
                        loadRecipes();
                        isLoading = true;
                    }
                }
            }
        });

        // 在 MainActivity 类中找到底部表单的视图
        View bottomSheet = findViewById(R.id.bottom_sheet);

        // 使用 BottomSheetBehavior 进行设置
        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
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

    private int mCurrentPage = 0; // 当前页码
    private int mPageSize = 30; // 每页加载的数据量

    private void loadRecipes() {
        Log.d("MainActivity", "loadRecipes called, currentPage: " + mCurrentPage);
        if (isLoading) { // 如果当前正在加载，直接返回
            return;
        }
        isLoading = true; // 标记开始加载数据

        OkHttpClient client = new OkHttpClient();
        String url = "http://10.0.2.2:8080/api/getRecipeData?start=" + (mCurrentPage * mPageSize) + "&limit=" + mPageSize;

        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseData = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONArray jsonArray = new JSONArray(responseData);
                                Log.d("MainActivity", "Number of new items: " + jsonArray.length()); // 添加日志输出
                                List<Recipe> newRecipes = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonRecipe = jsonArray.getJSONObject(i);
                                    Recipe recipe = Recipe.fromJson(jsonRecipe);
                                    newRecipes.add(recipe);

                                }
                                Log.d("MainActivity", "Size of newRecipes after adding: " + newRecipes.size()); // 添加日志输出
                                mAdapter.appendRecipes(newRecipes);
                                mCurrentPage++;
                            } catch (JSONException e) {
                                Log.e("MainActivity", "JSON parsing error: " + e.getMessage());
                            } finally {
                                isLoading = false;
                            }
                        }
                    });
                } else {
                    isLoading = false;
                }
            }


            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("MainActivity", "Request failed: " + e.getMessage());
                isLoading = false; // 网络请求失败也需要重置加载状态
            }
        });
    }
}
