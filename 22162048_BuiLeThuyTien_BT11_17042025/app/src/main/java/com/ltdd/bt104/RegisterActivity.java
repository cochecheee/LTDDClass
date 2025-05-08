package com.ltdd.bt104;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.ltdd.bt104.network.ApiService;
import com.ltdd.bt104.network.RetrofitClient;
import com.ltdd.bt104.payload.MessageResponse;
import com.ltdd.bt104.payload.SignupRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText etFullName, etUsername, etEmail, etPassword;
    private Button btnRegister;
    private TextView tvLoginLink;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etFullName   = findViewById(R.id.tilFullName);
        etUsername   = findViewById(R.id.etUsername);
        etEmail      = findViewById(R.id.etEmail);
        etPassword   = findViewById(R.id.etPassword);
        btnRegister  = findViewById(R.id.btnRegister);
        tvLoginLink  = findViewById(R.id.tvLoginLink);

        apiService = RetrofitClient.getInstance(this).create(ApiService.class);

        btnRegister.setOnClickListener(v -> {
            String fullName = etFullName.getText().toString().trim();
            String username = etUsername.getText().toString().trim();
            String email    = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (fullName.isEmpty() || username.isEmpty() ||
                    email.isEmpty()    || password.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            SignupRequest req = new SignupRequest();
            req.setFullName(fullName);
            req.setUsername(username);
            req.setEmail(email);
            req.setPassword(password);

            apiService.register(req).enqueue(new Callback<MessageResponse>() {
                @Override
                public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(RegisterActivity.this,
                                response.body().getMessage(),
                                Toast.LENGTH_SHORT).show();
                        // navigate to login
                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this,
                                "Registration failed: " + response.message(),
                                Toast.LENGTH_SHORT).show();
                    }
                }



                @Override
                public void onFailure(Call<MessageResponse> call, Throwable t) {
                    Toast.makeText(RegisterActivity.this,
                            "Error: " + t.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        });

        tvLoginLink.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            finish();
        });
    }
}