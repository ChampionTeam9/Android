package com.example.adproject;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 执行网络请求
        OkHttpClient client = new OkHttpClient();
        String url = "http://10.249.24.122:3000/getRecipeData";

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // 处理服务器响应
                if (response.isSuccessful()) {
                    final String responseData = response.body().string();

                    // 在主线程更新 UI
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // 在这里处理获取到的 JSON 数据
                            Log.d("YourActivity", "Response Data: " + responseData);
                            TextView textView = findViewById(R.id.resultTextView); // Replace with your actual TextView ID
                            textView.setText(responseData);
                        }
                    });
                } else {
                    Log.e("YourActivity", "Unexpected response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                // 处理请求失败的情况
                Log.e("YourActivity", "Request failed: " + e.getMessage());
            }
        });
    }
}