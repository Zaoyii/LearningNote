package com.zcyi.learningnote.Pager.LoginPager;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.zcyi.learningnote.Pager.HomePager.HomeActivity;
import com.zcyi.learningnote.Util.UtilMethod;
import com.zcyi.learningnote.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initMethod();
    }

    private void initMethod() {
        binding.buttonLogin.setOnClickListener(view -> {
            String username = binding.inputUsername.getText().toString();
            String password = binding.inputPassword.getText().toString();
            if (username.isEmpty()) {
                binding.inputLayoutUsername.setError("username is empty!");
                return;
            } else {
                binding.inputLayoutUsername.setErrorEnabled(false);
            }
            if (password.isEmpty()) {
                binding.inputLayoutPassword.setError("password is empty!");
                return;
            } else {
                binding.inputLayoutPassword.setErrorEnabled(false);
            }
            if (username.equals("zcyi") && password.equals("zaoyi...")) {
                startActivity(new Intent(this, HomeActivity.class));
                UtilMethod.showToast(getApplicationContext(), "Login success!");
                finish();
            } else {
                UtilMethod.showToast(getApplicationContext(), "username or password is incorrect!");
            }
        });
    }

}
