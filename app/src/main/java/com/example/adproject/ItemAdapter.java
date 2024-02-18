package com.example.adproject;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<String> ingredientsList;
    private List<String> selectedItem = new ArrayList<>();
    private Map<Integer, String> positionToName;
    private Map<Integer, Boolean> positionToIsChecked;

    public ItemAdapter(List<String> ingredientsList) {
        this.ingredientsList = ingredientsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient, parent, false);
        positionToName = new HashMap<>();
        positionToIsChecked = new HashMap<>();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String ingredient = ingredientsList.get(position);
        holder.ingredientText.setText(ingredient);

        positionToName.put(position, holder.ingredientText.getText().toString().trim());


        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int currPosition = holder.getAdapterPosition();
                String ingredientToAdd = holder.ingredientText.getText().toString().trim();
                positionToIsChecked.put(currPosition, isChecked);
                positionToName.put(currPosition, ingredientToAdd);
            }
        });

        holder.ingredientText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int currPosition = holder.getAdapterPosition();
                String newText = s.toString().trim();
                positionToName.put(currPosition, newText);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ingredientsList.size();
    }


    public List<String> getCheckedItemsWithText() {
        List<String> checkedItems = new ArrayList<>();
        for (int i = 0; i < ingredientsList.size(); i++) {
            if (positionToIsChecked.containsKey(i) && positionToIsChecked.get(i)) {
                String ingredient = positionToName.get(i);
                checkedItems.add(ingredient);
            }
        }
        return checkedItems;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkbox;
        EditText ingredientText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkbox = itemView.findViewById(R.id.checkbox_ingredient);
            ingredientText = itemView.findViewById(R.id.text_ingredient);
        }
    }
}

