package com.example.adproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SelectedItemsAdapter extends RecyclerView.Adapter<SelectedItemsAdapter.ViewHolder> {

    private List<Item> selectedItems;
    private SharedPreferences sharedPreferences;

    public SelectedItemsAdapter(List<Item> selectedItems, SharedPreferences sharedPreferences) {
        for(Item item : selectedItems){
            System.out.println(item.getItemName());
        }
        this.selectedItems = selectedItems;
        this.sharedPreferences = sharedPreferences;
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = selectedItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return selectedItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox_selected_item);
            checkBox.setButtonDrawable(null);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Item item = selectedItems.get(position);
                        item.setSelected(isChecked);
                        saveSelectedState(item.getItemName(), isChecked);
                        if (isChecked) {
                            checkBox.setPaintFlags(checkBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            item.setSelected(true);
                            Log.d("itemname",item.getItemName());
                            Log.d("ischecked", String.valueOf(item.isSelected()));
                            updateIsChecked(item.isSelected(),item.getId());

                        } else {
                            checkBox.setPaintFlags(checkBox.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                            item.setSelected(false);
                            Log.d("itemname",item.getItemName());
                            Log.d("ischecked", String.valueOf(item.isSelected()));
                            updateIsChecked(item.isSelected(),item.getId());

                        }
                    }
                }
            });
        }

        public void bind(Item item) {
            checkBox.setText(item.getItemName());
            checkBox.setChecked(item.isSelected());
            if (item.isSelected()) {
                checkBox.setPaintFlags(checkBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                checkBox.setPaintFlags(checkBox.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }
        }
    }

    private void saveSelectedState(String itemName, boolean isSelected) {
        // 保存选中状态到 SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(itemName, isSelected);
        editor.apply();
    }
    public void updateIsChecked(Boolean ischecked,int id)
    {
        OkHttpClient client = new OkHttpClient();
        String url = "http://10.0.2.2:8080/api/updateIsChecked";

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("isChecked",ischecked);
            jsonObject.put("id",id);
            Log.d("ischecked try", String.valueOf(ischecked));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("json",jsonObject.toString());
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
                    Log.d("updateIschecked","successed");
                } else {
                    Log.d("updateIschecked","failed");

                }
            }
        });
    }
}
