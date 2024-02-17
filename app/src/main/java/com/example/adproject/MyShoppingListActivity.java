package com.example.adproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        // 获取传递过来的已选项目列表
        selectedItems = getIntent().getParcelableArrayListExtra("shoppingList");//SELECTED_ITEMS

        if (selectedItems != null) {
            for (Item item : selectedItems) {
                Log.d("Selected Items", String.valueOf(item));
            }
        } else {
            Log.d("Selected Items", "No items selected.");
        }

        // 初始化 RecyclerView
        recyclerView = findViewById(R.id.recycler_selected_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 初始化 SharedPreferences
        sharedPreferences = getSharedPreferences("SelectedItems", MODE_PRIVATE);

        // 创建适配器并设置到 RecyclerView
        adapter = new SelectedItemsAdapter(selectedItems, sharedPreferences);
        recyclerView.setAdapter(adapter);
    }

    private Map<String, Boolean> loadSelectedItems() {
        // 从 SharedPreferences 中加载保存的选中状态
        Map<String, Boolean> selectedItemsMap = new HashMap<>();
        Map<String, ?> allEntries = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            selectedItemsMap.put(entry.getKey(), (Boolean) entry.getValue());
        }
        return selectedItemsMap;
    }

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
