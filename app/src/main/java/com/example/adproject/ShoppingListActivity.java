package com.example.adproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
                    List<String> selectedItems = adapter.getSelectedItems();
                    Log.d("Selected Items", selectedItems.toString());
                    Intent intent = new Intent(ShoppingListActivity.this, MyShoppingListActivity.class);
                    intent.putStringArrayListExtra("SELECTED_ITEMS", (ArrayList<String>) selectedItems);
                    startActivity(intent);
                }
            });





        } else {
            Log.d("ShoppingListActivity", "No INGREDIENTS_ARRAY extra found in intent.");
        }





    }
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