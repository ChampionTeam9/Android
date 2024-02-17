package com.example.adproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyShoppingListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SelectedItemsAdapter adapter;
    private List<Item> shoppingListItems;
    private List<Item> selectedItems;
    private List<String> myShoppingList;
    private SharedPreferences sharedPreferences;
    private Button Btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shopping_list);

        Btn = findViewById(R.id.back_to_home);
        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MyShoppingListActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });


        // 初始化 RecyclerView
        recyclerView = findViewById(R.id.recycler_selected_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 初始化 SharedPreferences
        sharedPreferences = getSharedPreferences("SelectedItems", MODE_PRIVATE);

        // 从服务器获取购物清单列表
        getShoppingList();
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
                            Boolean isChecked = shoppingListItem.getBoolean("checked");
                            Integer id = shoppingListItem.getInt("id");
                            Log.d("itemname", itemName);
                            Log.d("id", id.toString());
                            Log.d("checked", isChecked.toString());
                            Item item = new Item(itemName, isChecked, id);
                            itemList.add(item);
                        }
                        // 在UI线程中更新UI，显示购物清单列表或进行其他操作
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // 创建适配器并设置到 RecyclerView
                                adapter = new SelectedItemsAdapter(itemList, sharedPreferences);
                                recyclerView.setAdapter(adapter);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // 请求失败的处理，例如显示错误消息
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MyShoppingListActivity.this, "Failed to retrieve shopping list", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

}
