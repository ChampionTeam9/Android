package com.example.adproject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private List<Recipe> mRecipeList; // 使用Recipe对象列表

    public void updateRecipes(List<Recipe> recipes) {
        System.out.println("updateRecipes Called");
        System.out.println("recipes.size(): " + recipes.size());
        mRecipeList.clear();
        mRecipeList.addAll(recipes);
        notifyDataSetChanged();
    }

    // 新增追加数据的方法
    public void appendRecipes(List<Recipe> newRecipes) {
        Log.d("RecipeAdapter", "Appending recipes: " + newRecipes.size());
        int startPosition = this.mRecipeList.size(); // 获取追加前的数据量
        this.mRecipeList.addAll(newRecipes); // 追加新数据
        notifyItemRangeInserted(startPosition, newRecipes.size()); // 仅通知追加的数据部分更新
    }

    public interface OnItemClickListener {
        void onItemClick(Recipe recipe);
    }



    private OnItemClickListener onItemClickListener;

    // 构造函数中添加监听器参数
    public RecipeAdapter(List<Recipe> recipes, OnItemClickListener listener) {
        mRecipeList = recipes;
        onItemClickListener = listener;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView descriptionTextView;
        public ImageView imageView;

        public TextView usernameTextView;

        private OnItemClickListener listener;

        public ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.recipe_name);
            descriptionTextView = itemView.findViewById(R.id.recipe_description);
            imageView = itemView.findViewById(R.id.recipe_image);
            usernameTextView = itemView.findViewById(R.id.member_username);
            this.listener = listener;
        }
    }



    public RecipeAdapter(List<Recipe> recipes) {
        mRecipeList = recipes;
    }

    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View recipeView = inflater.inflate(R.layout.recipe_item, parent, false);

        return new ViewHolder(recipeView, onItemClickListener);
    }




    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Recipe recipe = mRecipeList.get(position);
        Log.d("RecipeAdapter", "Binding view holder for position: " + position);
        holder.nameTextView.setText(recipe.getName());
        holder.descriptionTextView.setText(recipe.getDescription());
        holder.usernameTextView.setText(recipe.getUsername());



        // 构建图片 URL 并使用 Picasso 加载图片
        String imageUrl = "http://10.0.2.2:8080/images/" + recipe.getImage();
        Picasso.get().load(imageUrl).into(holder.imageView);

        // 设置点击监听器
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(recipe);
                }
            }
        });
    }



    @Override
    public int getItemCount() {
        return mRecipeList.size();
    }
}
