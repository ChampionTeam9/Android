package com.example.adproject;

import android.app.Activity;


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

public class LoginFragment extends Fragment {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        ImageView loginIcon = view.findViewById(R.id.loginicon);
        loginIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToLogin();
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
        if(username== null){
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }
        else {
            // 否则，启动 LoginActivity 以便用户登录


            Intent intent = new Intent(getActivity(), MyProfile.class);
            startActivity(intent);
        }
    }



    private void login() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        // 在这里实现您的登录逻辑
    }
}

