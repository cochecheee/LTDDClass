package com.example.bt3_loginregister;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bt3_loginregister.api.ApiService;
import com.example.bt3_loginregister.api.RetrofitClient;
import com.example.bt3_loginregister.model.request.LoginRequest;
import com.example.bt3_loginregister.model.response.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Exercise01Login extends AppCompatActivity {
    private static final String TAG = "Exercise01Login";
    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin, facebookButton, googleButton;
    private TextView textViewRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise01);

        // Bind views
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        facebookButton = findViewById(R.id.facebookButton);
        googleButton = findViewById(R.id.googleButton);
        textViewRegister = findViewById(R.id.textViewRegister);

        // Login button click listener
        buttonLogin.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }


            buttonLogin.setEnabled(false);
            buttonLogin.setText("Đang đăng nhập...");

            // call login API here
            // creating request for calling API
            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
            LoginRequest loginRequest = new LoginRequest(email, password);

            apiService.login(loginRequest).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    buttonLogin.setEnabled(true);
                    buttonLogin.setText("Đăng nhập");

                    if (response.isSuccessful() && response.body() != null) {
                        LoginResponse loginResponse = response.body();
                        if (loginResponse.isSuccess()) {
                            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("token", loginResponse.getToken());
                            editor.apply();

                            Toast.makeText(Exercise01Login.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            // Navigate to the next screen or activity
                            Intent intent = new Intent(Exercise01Login.this, DashboardActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(Exercise01Login.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Exercise01Login.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    buttonLogin.setEnabled(true);
                    buttonLogin.setText("Đăng nhập");
                    Toast.makeText(Exercise01Login.this, "Đã xảy ra lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "onFailure: ", t);
                }
            });
        });

        // Placeholder for social logins
        facebookButton.setOnClickListener(v -> Toast.makeText(this, "Chưa triển khai", Toast.LENGTH_SHORT).show());
        googleButton.setOnClickListener(v -> Toast.makeText(this, "Chưa triển khai", Toast.LENGTH_SHORT).show());

        // Navigate to register
        textViewRegister.setOnClickListener(v -> startActivity(new Intent(this, Exercise01Register.class)));

    }
}