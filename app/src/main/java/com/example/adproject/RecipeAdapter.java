package com.example.adproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.adproject.Recipe;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private List<Recipe> mRecipeList; // 使用Recipe对象列表

    public void updateRecipes(List<Recipe> recipes) {
            mRecipeList.clear();
            mRecipeList.addAll(recipes);
            notifyDataSetChanged();


    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView descriptionTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.recipe_name);
            descriptionTextView = itemView.findViewById(R.id.recipe_description);
        }
    }

    public RecipeAdapter(List<Recipe> recipes) {
        mRecipeList = recipes;
    }

    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View recipeView = inflater.inflate(R.layout.recipe_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(recipeView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        Recipe recipe = mRecipeList.get(position);

        // Set item views based on your views and data model
        holder.nameTextView.setText(recipe.getName());
        holder.descriptionTextView.setText(recipe.getDescription());
    }

    @Override
    public int getItemCount() {
        return mRecipeList.size();
    }
}
