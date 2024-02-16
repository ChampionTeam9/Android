package com.example.adproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.adproject.R;


public class LoginFragment extends Fragment {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        usernameEditText = view.findViewById(R.id.username);
        passwordEditText = view.findViewById(R.id.password);
        loginButton = view.findViewById(R.id.loginButton);

        ImageView loginIcon = view.findViewById(R.id.loginicon);
        loginIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToLogin();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        return view;
    }

    private void navigateToLogin() {
        // 获取 SharedPreferences 对象
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_pref", Context.MODE_PRIVATE);
        // 从 SharedPreferences 中读取用户名，默认值为 null
        String username = sharedPreferences.getString("username", null);
        // 如果用户名不为 null，即用户已经登录了，那么跳转到 MyProfile
        if(username == null) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        } else {
            // 否则，启动 MyProfile
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        }
    }

    private void login() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        // 在这里实现您的登录逻辑
        // 检查用户名和密码是否有效
        if (isValidCredentials(username, password)) {
            // 登录成功，将用户名保存到 SharedPreferences 中
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_pref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username", username);
            editor.apply();

            // 启动 MyProfile Activity
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        } else {
            // 登录失败，显示错误消息
            Toast.makeText(getActivity(), "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidCredentials(String username, String password) {
        // 在这里实现您的验证逻辑，例如与后端服务器进行交互验证用户名和密码
        // 这里仅作示例，总是返回 true，表示登录始终成功
        return true;
    }
}
