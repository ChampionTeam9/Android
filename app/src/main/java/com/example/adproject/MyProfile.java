package com.example.adproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyProfile extends AppCompatActivity {
    Button logout;
    Button savedRecipes;
    Button backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        logout = findViewById(R.id.Logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 清除SharedPreferences中的数据
                SharedPreferences preferences = getSharedPreferences("user_pref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear(); // 清除所有数据
                editor.apply(); // 应用更改
                finish();

            }
        });
        backButton=findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });
        savedRecipes=findViewById(R.id.mySavedRecipes);
        savedRecipes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // 启动 MyRecipeActivity
                getSavedRecipes();

            }
        });
    }
    private void getSavedRecipes() {

        OkHttpClient client = new OkHttpClient();
        String url = "http://10.0.2.2:8080/api/getMySavedRecipes";

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            // 获取 SharedPreferences 对象
            SharedPreferences sharedPreferences = this.getSharedPreferences("user_pref", Context.MODE_PRIVATE);
            // 从 SharedPreferences 中读取用户名，默认值为 null
            String username = sharedPreferences.getString("username", null);
            jsonObject.put("username", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
                    // 将响应体转换为字符串
                    String responseData = response.body().string();
                    // 将字符串转换为JSONArray
                    try {
                        JSONArray jsonArray = new JSONArray(responseData);
                        // 遍历JSON数组中的每个对象
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject recipeObject = jsonArray.getJSONObject(i);
                            // 从每个JSONObject中获取食谱的详细信息
                            String recipeName = recipeObject.getString("name");
                            // 根据需要进行其他操作...
                        }
                        // 在UI线程中更新UI，显示食谱列表或进行其他操作
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(MyProfile.this, MyRecipeActivity.class);
                                intent.putExtra("recipeList", responseData); // 将响应数据作为JSON字符串传递
                                startActivity(intent);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // 请求失败的处理，例如显示错误消息
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MyProfile.this, "Failed to retrieve recipes", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });




    }
}
