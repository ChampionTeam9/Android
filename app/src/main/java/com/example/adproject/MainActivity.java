package com.example.adproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecipeAdapter mAdapter;
    private EditText searchEditText;
    private boolean isLoading = false;
    TextView noResultsText;
    private List<Recipe> mAllRecipes;
    private List<Recipe> mRecipes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavigationView navigationView = findViewById(R.id.navigation_view);
        View headerView = navigationView.getHeaderView(0);
        TextView usernameTextView = headerView.findViewById(R.id.nav_header_title);
        MenuItem loginMenuItem = navigationView.getMenu().findItem(R.id.nav_login);

        noResultsText = findViewById(R.id.noResultsText);
        mAllRecipes = new ArrayList<>();

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
            loginMenuItem.setIcon(R.drawable.ic_logout);
        } else {
            // 用户未登录，将标题设置为"Guest"，菜单项为"Login"
            Log.d("login", "fail");
            usernameTextView.setText("Guest");
            loginMenuItem.setTitle("Login");
            loginMenuItem.setIcon(R.drawable.ic_login);
        }

        // 设置 NavigationView 的选择监听器
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_login) {
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
                return true;
            } else if (id == R.id.nav_saved) {
                getSavedRecipes(); // 调用获取保存的食谱的方法
                return true;
            } else if (id==R.id.shoppingList) {
                getShoppingList();
            }
            return false;
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
                noResultsText.setVisibility(View.INVISIBLE);
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


        if (filteredRecipes.size() == 0){
            noResultsText.setVisibility(View.VISIBLE);
        }


        mAdapter.updateRecipes(filteredRecipes);
    }
    private void getSavedRecipes() {

        OkHttpClient client = new OkHttpClient();
        String url = "http://10.0.2.2:8080/api/getMySavedRecipes";

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            // 获取 SharedPreferences 对象
            SharedPreferences sharedPreferences = this.getSharedPreferences("user_pref", Context.MODE_PRIVATE);
            // 从 SharedPreferences 中读取用户名，默认值为 null
            String username = sharedPreferences.getString("username", null);
            jsonObject.put("username", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
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
                        // 创建一个列表来存储解析后的食谱对象
                        List<Recipe> recipes = new ArrayList<>();
                        // 遍历JSON数组中的每个对象
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject recipeObject = jsonArray.getJSONObject(i);
                            // 从每个JSONObject中获取食谱的详细信息
                            String recipeName = recipeObject.optString("name", "");
                            String description = recipeObject.optString("description", "");
                            String image = recipeObject.optString("image", "");
                            double rating = recipeObject.optDouble("rating", 0.0);
                            int numberOfRating = recipeObject.optInt("numberOfRating", 0);
                            int numberOfSaved = recipeObject.optInt("numberOfSaved", 0);
                            String submittedDate = recipeObject.optString("submittedDate", "");
                            JSONArray tagsArray = recipeObject.optJSONArray("tags");
                            List<String> tagsList = new ArrayList<>();
                            if (tagsArray != null) {
                                for (int j = 0; j < tagsArray.length(); j++) {
                                    tagsList.add(tagsArray.optString(j, ""));
                                }
                            }
                            int servings = recipeObject.optInt("servings", 0);
                            int preparationTime = recipeObject.optInt("preparationTime", 0);
                            JSONArray stepsArray = recipeObject.optJSONArray("steps");
                            List<String> stepsList = new ArrayList<>();
                            if (stepsArray != null) {
                                for (int j = 0; j < stepsArray.length(); j++) {
                                    stepsList.add(stepsArray.optString(j, ""));
                                }
                            }
                            int healthScore = recipeObject.optInt("healthScore", 0);
                            double calories = recipeObject.optDouble("calories", 0.0);
                            double protein = recipeObject.optDouble("protein", 0.0);
                            double carbohydrate = recipeObject.optDouble("carbohydrate", 0.0);
                            double sugar = recipeObject.optDouble("sugar", 0.0);
                            double sodium = recipeObject.optDouble("sodium", 0.0);
                            double fat = recipeObject.optDouble("fat", 0.0);
                            double saturatedFat = recipeObject.optDouble("saturatedFat", 0.0);
                            String username = recipeObject.optString("username", "");
                            JSONArray ingredientsArray = recipeObject.optJSONArray("ingredients");
                            List<String> ingredientsList = new ArrayList<>();
                            if (ingredientsArray != null) {
                                for (int j = 0; j < ingredientsArray.length(); j++) {
                                    ingredientsList.add(ingredientsArray.optString(j, ""));
                                }
                            }
                            // 创建 Recipe 对象并设置其属性
                            Recipe recipe = new Recipe();
                            recipe.setName(recipeName);
                            recipe.setDescription(description);
                            recipe.setImage(image);
                            recipe.setRating(rating);
                            recipe.setNumberOfRating(numberOfRating);
                            recipe.setNumberOfSaved(numberOfSaved);
                            recipe.setSubmittedDate(LocalDate.parse(submittedDate));
                            recipe.setTags(tagsList);
                            recipe.setServings(servings);
                            recipe.setPreparationTime(preparationTime);
                            recipe.setSteps(stepsList);
                            recipe.setHealthScore(healthScore);
                            recipe.setCalories(calories);
                            recipe.setProtein(protein);
                            recipe.setCarbohydrate(carbohydrate);
                            recipe.setSugar(sugar);
                            recipe.setSodium(sodium);
                            recipe.setFat(fat);
                            recipe.setSaturatedFat(saturatedFat);
                            recipe.setUsername(username);
                            recipe.setIngredients(ingredientsList);
                            recipes.add(recipe);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(MainActivity.this, MyRecipeActivity.class);
                                intent.putExtra("recipeList", (Serializable) recipes);
                                startActivity(intent);
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
                            Toast.makeText(MainActivity.this, "Please log in first", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

        });




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
                                // save recipes list into mAllRecipes
                                mAllRecipes = newRecipes;
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
    private void getShoppingList() {
        OkHttpClient client = new OkHttpClient();
        String url = "http://10.0.2.2:8080/api/getShoppingList";

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            // 获取 SharedPreferences 对象
            SharedPreferences sharedPreferences = this.getSharedPreferences("user_pref", Context.MODE_PRIVATE);
            String username = sharedPreferences.getString("username", null);
            jsonObject.put("username", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
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
                        List<Item> itemList = new ArrayList<>();
                        // 遍历JSON数组中的每个对象
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject shoppingListItem = jsonArray.getJSONObject(i);
                            // 从每个JSONObject中获取购物清单项的详细信息
                            String itemName = shoppingListItem.getString("ingredientName");
                            Boolean isChecked=shoppingListItem.getBoolean("checked");
                            Integer id=shoppingListItem.getInt("id");
                            Log.d("itemname",itemName);
                            Log.d("id",id.toString());
                            Log.d("checked",isChecked.toString());
                            Item item=new Item(itemName,isChecked,id);
                            itemList.add(item);
                        }
                        // 在UI线程中更新UI
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(MainActivity.this, MyShoppingListActivity.class);
                                intent.putParcelableArrayListExtra("shoppingList", new ArrayList<>(itemList)); // 将响应数据作为JSON字符串传递
                                Log.d("shoppingList", responseData);
                                startActivity(intent);
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
                            Toast.makeText(MainActivity.this, "Failed to retrieve shopping list", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

}
