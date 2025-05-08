package com.ltdd.bt104;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.textfield.TextInputEditText;
import com.ltdd.bt104.network.ApiService;
import com.ltdd.bt104.network.RetrofitClient;
import com.ltdd.bt104.payload.LoginRequest;
import com.ltdd.bt104.payload.MessageResponse;
import com.ltdd.bt104.session.SessionManager;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private TextInputEditText etUsername;
    private TextInputEditText etPassword;
    private Button btnLogin;
    private TextView tvRegister;

    private ApiService apiService;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  // this is your login layout

        etUsername  = findViewById(R.id.etUsername);
        etPassword  = findViewById(R.id.etPassword);
        btnLogin    = findViewById(R.id.btnLogin);
        tvRegister  = findViewById(R.id.tvRegister);

        apiService = RetrofitClient.getInstance(this).create(ApiService.class);
        session    = new SessionManager(this);

        btnLogin.setOnClickListener(view -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
                return;
            }

            LoginRequest req = new LoginRequest();
            req.setUsername(username);
            req.setPassword(password);

            apiService.login(req).enqueue(new Callback<MessageResponse>() {
                @Override
                public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        // Use getToken() instead of getData()
                        String token = response.body().getToken();
                        session.saveAuthToken("Bearer " + token);

                        Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, FeedActivity.class));
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "Login failed: " + response.message(),
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<MessageResponse> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Error: " + t.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        });

        tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, RegisterActivity.class));
        });
    }
}