package com.example.bt3_loginregister;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bt3_loginregister.api.ApiService;
import com.example.bt3_loginregister.api.RetrofitClient;
import com.example.bt3_loginregister.model.request.RegisterRequest;
import com.example.bt3_loginregister.model.response.RegisterResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Exercise01Register extends AppCompatActivity {
    private static final String TAG = "Exercise01Register";
    private EditText editTextUsername, editTextEmail, editTextPassword, editTextConfirmPassword;
    private Button buttonRegister;
    private TextView textViewLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise01_register);

        editTextUsername = findViewById(R.id.editTextFullName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        textViewLogin = findViewById(R.id.textViewLogin);

        buttonRegister.setOnClickListener(v -> {
            String username = editTextUsername.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            String confirmPassword = editTextConfirmPassword.getText().toString().trim();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                return;
            }


            buttonRegister.setEnabled(false);
            buttonRegister.setText("Đang xử lý...");

            //call API here
            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
            RegisterRequest registerRequest = new RegisterRequest(username, email, "1111", password, confirmPassword);

            apiService.register(registerRequest).enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                    buttonRegister.setEnabled(true);
                    buttonRegister.setText("Đăng ký");

                    if (response.isSuccessful() && response.body() != null) {
                        RegisterResponse registerResponse = response.body();
                        if (registerResponse.isSuccess()) {
                            Toast.makeText(Exercise01Register.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Exercise01Register.this, OtpVerificationActivity.class);
                            intent.putExtra("email", email);
                            intent.putExtra("username", username);
                            intent.putExtra("password", password);
                            startActivityForResult(intent, 1);
                        } else {
                            Toast.makeText(Exercise01Register.this, registerResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Exercise01Register.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {
                    buttonRegister.setEnabled(true);
                    buttonRegister.setText("Đăng ký");
                    Toast.makeText(Exercise01Register.this, "Đã xảy ra lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "onFailure: ", t);
                }
            });

        });

        textViewLogin.setOnClickListener(v -> finish());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}