package com.example.adproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

public class ShoppingListActivity extends AppCompatActivity {

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
        } else {
            Log.d("ShoppingListActivity", "No INGREDIENTS_ARRAY extra found in intent.");
        }

    }
}