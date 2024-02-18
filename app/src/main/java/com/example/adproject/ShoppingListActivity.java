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

            addselectedBtn=findViewById(R.id.button_add_selected);
            addselectedBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<String> checkedItems = adapter.getCheckedItemsWithText();
                    Log.d("Checked Items", checkedItems.toString());
                    addToShoppingListDB(checkedItems);
                    Intent intent = new Intent(ShoppingListActivity.this, MyShoppingListActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            Log.d("ShoppingListActivity", "No INGREDIENTS_ARRAY extra found in intent.");
        }
    }

    public void addToShoppingListDB(List<String> selectedItems)
    {
        System.out.print("addToShoppingListDB called!");
        OkHttpClient client = new OkHttpClient();
        String url = "http://10.0.2.2:8080/api/addToShoppingList";

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        String selectedItemsStr = String.join(",", selectedItems);
        try {
            SharedPreferences sharedPreferences = this.getSharedPreferences("user_pref", Context.MODE_PRIVATE);
            String username = sharedPreferences.getString("username", null);
            jsonObject.put("selectedItems", selectedItemsStr);
            jsonObject.put("username", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("json!!!",jsonObject.toString());
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
                    Log.d("addToshoppingList","succeeded");
                } else {
                    Log.d("addToshoppingList","failed");

                }
            }
        });
    }


}