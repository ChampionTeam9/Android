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
import android.widget.EditText;
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
import java.util.Iterator;
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
    private SharedPreferences sharedPreferences;
    private Button clearAll;
    private Button clearSelectedButton;
    private Button addButton;
    private EditText addItemText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shopping_list);

        clearAll = findViewById(R.id.delete_all);
        clearSelectedButton = findViewById(R.id.clearSelectedButton);
        addButton = findViewById(R.id.addButton);
        addItemText = findViewById(R.id.addItemText);

        //getShoppingListItems();
        // 检查是否有传递的数据
        Intent intent = getIntent();
        ArrayList<Item> shoppingList = null;
//        if (intent != null && intent.hasExtra("shoppingList")) {

            shoppingList = intent.getParcelableArrayListExtra("shoppingList");


        clearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAll();
                shoppingListItems.clear();
                adapter.notifyDataSetChanged();
                finish();
            }
        });

        ArrayList<Item> finalShoppingList = shoppingList;
        clearSelectedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Item> itemsToClear = new ArrayList<>();

                for (Item item : finalShoppingList) {
                    if (item.isSelected()) {
                        itemsToClear.add(item);
                    }
                }
                Log.d("selectedItems", itemsToClear.toString()); // 打印选中的项目
                clearSelectedItemsInDb(itemsToClear);
                shoppingListItems.removeAll(itemsToClear); // 从列表中移除选中的项目
                adapter.notifyDataSetChanged();
                finish();
            }

        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newItem = String.valueOf(addItemText.getText());
                if (newItem.trim().isEmpty()) {
                    return;
                }
                addToShoppingListDB(newItem);
                adapter.notifyDataSetChanged();
                addItemText.setText("");
            }
        });

        shoppingListItems = new ArrayList<>();

        System.out.println("shoppingListItems.size(): " + shoppingListItems.size());

        // 初始化 RecyclerView
        recyclerView = findViewById(R.id.recycler_selected_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 初始化 SharedPreferences
        sharedPreferences = getSharedPreferences("SelectedItems", MODE_PRIVATE);

        // 创建适配器并设置到 RecyclerView
        adapter = new SelectedItemsAdapter(shoppingList, sharedPreferences);
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
            jsonObject.put("username", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("jsonSL", jsonObject.toString());
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

    public void addToShoppingListDB(String newItem) {
        System.out.print("addToShoppingListDB called!");
        OkHttpClient client = new OkHttpClient();
        String url = "http://10.0.2.2:8080/api/addItemToShoppingList";

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            SharedPreferences sharedPreferences = this.getSharedPreferences("user_pref", Context.MODE_PRIVATE);
            String username = sharedPreferences.getString("username", null);
            jsonObject.put("newItem", newItem);
            jsonObject.put("username", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("json!!!", jsonObject.toString());
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

                    Log.d("addToshoppingList", "succeeded");
                    // 将响应体转换为字符串
                    String responseData = response.body().string();
                    // 将字符串转换为JSONArray
                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        Item item = new Item();
                        item.setId(jsonObject.optInt("id"));
                        item.setItemName(jsonObject.optString("ingredientName"));
                        item.setSelected(jsonObject.optBoolean("isChecked"));
                        shoppingListItems.add(item);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.d("addToshoppingList", "failed");

                }
            }
        });
    }

    private void deleteAll() {
        OkHttpClient client = new OkHttpClient();
        String url = "http://10.0.2.2:8080/api/deleteAll";
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.d("deleteall", "successes");
                } else {
                    // 请求失败的处理
                    Log.d("deleteall", "failed");
                }
            }

        });
    }


    private void clearSelectedItemsInDb(List<Item> itemsToClear) {
        System.out.print("clearSelectedItemsInDb called!");
        OkHttpClient client = new OkHttpClient();
        String url = "http://10.0.2.2:8080/api/clearSelectedItemsInDb";

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String selectedItemsStr = "";
        for (Item item : itemsToClear) {
            selectedItemsStr += (item.getId() + ",");
        }
        // 在移除最后一个逗号前打印selectedItemsStr的内容
        System.out.println("Selected items IDs (before removing last comma): " + selectedItemsStr.toString());


        selectedItemsStr = selectedItemsStr.substring(0, selectedItemsStr.length() - 1);


        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("itemsToClear", selectedItemsStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("json", jsonObject.toString());
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
                    Log.d("clearedSelectedFromShoppingList", "succeeded");
                } else {
                    Log.d("clearedSelectedFromShoppingList", "failed");

                }
            }
        });
    }

}

