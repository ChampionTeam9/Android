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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ShoppingListActivity extends AppCompatActivity {
    Button addselectedBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);


        Intent intent = getIntent();
        String[] receivedIngredientsArray = intent.getStringArrayExtra("INGREDIENTS_ARRAY");

        if (receivedIngredientsArray != null) {
            List<String> receivedIngredientsList = Arrays.asList(receivedIngredientsArray);
            Log.d("ShoppingListActivity", "Received INGREDIENTS_ARRAY: " + receivedIngredientsList.toString());

            RecyclerView recyclerView = findViewById(R.id.recycler_shoppingListItem);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

            ItemAdapter adapter = new ItemAdapter(receivedIngredientsList);
            recyclerView.setAdapter(adapter);


            addselectedBtn = findViewById(R.id.button_add_selected);
            addselectedBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<String> selectedItems = adapter.getSelectedItems();
                    Log.d("Selected Items", selectedItems.toString());
                    SharedPreferences prefs = getSharedPreferences("user_pref", Context.MODE_PRIVATE);
                    String username = prefs.getString("username", "Guest");
                    for (String item : selectedItems) {
                        saveItem(false, username, item);
                    }
                    Intent intent = new Intent(ShoppingListActivity.this, MyShoppingListActivity.class);
                    startActivity(intent);


                }
            });
        } else {
            Log.d("ShoppingListActivity", "No INGREDIENTS_ARRAY extra found in intent.");
        }


    }

    public void saveItem(Boolean ischecked, String username, String itemName) {
        OkHttpClient client = new OkHttpClient();
        String url = "http://10.0.2.2:8080/api/saveShoppingList";

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("isChecked", ischecked);
            jsonObject.put("username", username);
            jsonObject.put("ingredientName", itemName);

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
                    Log.d("saveShoppingList", "successes");
                } else {
                    Log.d("saveShoppingList", "failed");

                }
            }
        });


//    private void sendSelectedItemsToServer(List<String> selectedItems) {
//        // 创建 Retrofit 实例
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://your-server-url.com/") // 替换为您的服务器 URL
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        // 创建服务接口实例
//        ApiService apiService = retrofit.create(ApiService.class);
//
//        // 创建请求体
//        SelectedItemsRequestBody requestBody = new SelectedItemsRequestBody(selectedItems);
//
//        // 发送网络请求
//        Call<Void> call = apiService.sendSelectedItems(requestBody);
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                if (response.isSuccessful()) {
//                    // 请求成功，处理响应
//                    Log.d("Send Selected Items", "Successfully sent selected items to server");
//                } else {
//                    // 请求失败，处理错误
//                    Log.e("Send Selected Items", "Failed to send selected items to server");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                // 请求失败，处理错误
//                Log.e("Send Selected Items", "Failed to send selected items to server: " + t.getMessage());
//            }
//        });
//    }

    }
}