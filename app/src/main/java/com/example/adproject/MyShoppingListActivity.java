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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
                finish();
            }
        });
        shoppingListItems = new ArrayList<>();
        getShoppingListItems();
        System.out.println("shoppingListItems.size(): " + shoppingListItems.size());

        // 初始化 RecyclerView
        recyclerView = findViewById(R.id.recycler_selected_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 初始化 SharedPreferences
        sharedPreferences = getSharedPreferences("SelectedItems", MODE_PRIVATE);

        // 创建适配器并设置到 RecyclerView
        adapter = new SelectedItemsAdapter(shoppingListItems, sharedPreferences);
        recyclerView.setAdapter(adapter);
    }

    private void getShoppingListItems() {
        System.out.println("getShoppingListItems called");
        OkHttpClient client = new OkHttpClient();
        String url = "http://10.0.2.2:8080/api/getShoppingList";

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            SharedPreferences sharedPreferences = this.getSharedPreferences("user_pref", Context.MODE_PRIVATE);
            String username = sharedPreferences.getString("username", null);
            jsonObject.put("username",username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("jsonSL",jsonObject.toString());
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
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject itemObject = jsonArray.getJSONObject(i);
                            Item item = new Item();
                            item.setId(itemObject.optInt("id"));
                            item.setItemName(itemObject.optString("ingredientName"));
                            item.setSelected(itemObject.optBoolean("isChecked"));
                            shoppingListItems.add(item);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // 请求失败的处理
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MyShoppingListActivity.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

        });
    }

//    private Map<String, Boolean> loadSelectedItems() {
//        // 从 SharedPreferences 中加载保存的选中状态
//        Map<String, Boolean> selectedItemsMap = new HashMap<>();
//        Map<String, ?> allEntries = sharedPreferences.getAll();
//        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
//            selectedItemsMap.put(entry.getKey(), (Boolean) entry.getValue());
//        }
//        return selectedItemsMap;
//    }

//    private void saveSelectedItems(Map<String, Boolean> selectedItemsMap) {
//        // 保存选中状态到 SharedPreferences
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        for (Map.Entry<String, Boolean> entry : selectedItemsMap.entrySet()) {
//            editor.putBoolean(entry.getKey(), entry.getValue());
//        }
//        editor.apply();
//    }

//    private List<Item> generateShoppingListItems(Map<String, Boolean> selectedItemsMap) {
//        // 生成购物清单列表项
//        List<Item> shoppingListItems = new ArrayList<>();
//
//        // 根据您的具体需求，初始化 selectedItems 变量
//        if (selectedItemsMap != null) {
//            for (Map.Entry<String, Boolean> entry : selectedItemsMap.entrySet()) {
//                String itemName = entry.getKey();
//                boolean isSelected = entry.getValue();
//
//                // 这里需要替换为您的 Item 类构造函数的参数
//                // 如果您的 Item 类需要 id 参数，请确保在这里提供一个 id
//                Item item = new Item(itemName, isSelected,id);
//                shoppingListItems.add(item);
//            }
//        }
//        return shoppingListItems;
//    }
}
