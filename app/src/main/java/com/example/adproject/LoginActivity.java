package com.example.adproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 初始化视图组件
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        backButton = findViewById(R.id.back_button);

        // 为登录按钮设置点击监听器
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在这里执行登录逻辑
                login();
            }
        });

        // 为返回按钮设置点击监听器
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 结束当前Activity，返回上一Activity
                finish();
            }
        });
    }

    private void login() {
        // 获取用户名和密码
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        OkHttpClient client = new OkHttpClient();
        String url = "http://10.0.2.2:8080/api/login";

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
            jsonObject.put("password", password);
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
                    // 请求成功的处理
                    String responseData = response.body().string();
                    String username = usernameEditText.getText().toString();;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Toast消息通知用户登录成功
                            Toast.makeText(LoginActivity.this, "Login successful，username：" + username, Toast.LENGTH_SHORT).show();
                            // 保存用户名到SharedPreferences
                            SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences("user_pref", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            Log.d("username",username);
                            editor.putString("username", username);
                            editor.apply();

                            // 跳转到MainActivity
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            LoginActivity.this.finish();
                        }
                    });

                } else {
                    // 登录失败的处理
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "username or password error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
