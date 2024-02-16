package com.example.adproject;

import android.view.LayoutInflater;

import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;
import android.view.ViewGroup;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder>{
    private List<String> ingredients;
    public IngredientsAdapter(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsAdapter.ViewHolder holder, int position) {
        String ingredient = ingredients.get(position);
        holder.ingredientTextView.setText(ingredient);
    }

    @Override
    public int getItemCount() {
        return ingredients != null ? ingredients.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView ingredientTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientTextView = itemView.findViewById(R.id.ingredient_text);
        }
    }
}
