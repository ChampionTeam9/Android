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

    // 更新数据的方法保留，可能用于初次数据加载或完全刷新场景
    public void updateRecipes(List<Recipe> recipes) {
        mRecipeList.clear();
        mRecipeList.addAll(recipes);
        notifyDataSetChanged();
    }

    // 新增追加数据的方法
    public void appendRecipes(List<Recipe> newRecipes) {
        int startPosition = this.mRecipeList.size(); // 获取追加前的数据量
        this.mRecipeList.addAll(newRecipes); // 追加新数据
        notifyItemRangeInserted(startPosition, newRecipes.size()); // 仅通知追加的数据部分更新
    }
    // 更新数据的方法保留，可能用于初次数据加载或完全刷新场景

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
        private OnItemClickListener listener;
        public TextView descriptionTextView;

        public ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.recipe_name);
            descriptionTextView = itemView.findViewById(R.id.recipe_description);
            this.listener = listener; // 初始化成员变量


        }
    }


    public RecipeAdapter(List<Recipe> recipes) {
        mRecipeList = recipes;
    }

    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Make sure the layout name here matches your XML file name
        View recipeView = inflater.inflate(R.layout.recipe_item, parent, false);

        return new ViewHolder(recipeView, onItemClickListener);
    }




    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Recipe recipe = mRecipeList.get(position);
        holder.nameTextView.setText(recipe.getName());
        holder.descriptionTextView.setText(recipe.getDescription());

        // 在这里设置点击监听器
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(recipe); // 正确地传递Recipe对象
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mRecipeList.size();
    }
}
