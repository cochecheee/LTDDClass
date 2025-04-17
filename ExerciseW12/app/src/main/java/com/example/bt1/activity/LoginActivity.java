package com.example.bt1.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bt1.R;
import com.example.bt1.SupabaseClient;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button actionButton;
    private TextView switchModeTextView, titleTextView;
    private boolean isLoginMode = true;
    private SupabaseClient supabaseClient;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize SharedPreferences để lưu token và userId
        prefs = getSharedPreferences("auth_prefs", MODE_PRIVATE);
        supabaseClient = SupabaseClient.getInstance();

        // Check if user is already logged in
        String savedToken = prefs.getString("access_token", null);
        if (savedToken != null) {
            supabaseClient.setAccessToken(savedToken);
            navigateToMainActivity();
            return;
        }

        // Initialize views
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        actionButton = findViewById(R.id.actionButton);
        switchModeTextView = findViewById(R.id.switchModeTextView);
        titleTextView = findViewById(R.id.titleTextView);

        // Set click listeners
        actionButton.setOnClickListener(v -> handleAuthAction());
        switchModeTextView.setOnClickListener(v -> switchMode());
    }

    private void switchMode() {
        isLoginMode = !isLoginMode;
        if (isLoginMode) {
            titleTextView.setText("Login");
            actionButton.setText("Login");
            switchModeTextView.setText("Don't have an account? Sign up");
        } else {
            titleTextView.setText("Sign Up");
            actionButton.setText("Sign Up");
            switchModeTextView.setText("Already have an account? Login");
        }
    }

    private void handleAuthAction() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Kiểm tra đầu vào
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email and password are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isLoginMode) {
            login(email, password);
        } else {
            signUp(email, password);
        }
    }

    private void login(String email, String password) {
        supabaseClient.signIn(email, password, new SupabaseClient.AuthCallback() {
            @Override
            public void onSuccess(String userId, String accessToken) {
                // Lưu lịch sử đăng nhập
                supabaseClient.insertLoginHistory(userId, email, new SupabaseClient.AuthCallback() {
                    @Override
                    public void onSuccess(String userId, String accessToken) {
                        runOnUiThread(() -> {
                            // Lưu token và userId vào SharedPreferences
                            prefs.edit()
                                    .putString("access_token", accessToken)
                                    .putString("user_id", userId)
                                    .apply();
                            Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                            navigateToMainActivity();
                        });
                    }

                    @Override
                    public void onError(String errorMessage) {
                        runOnUiThread(() -> {
                            Log.e("LoginActivity", "Failed to save login history: " + errorMessage);
                            Toast.makeText(LoginActivity.this, "Failed to save login history: " + errorMessage, Toast.LENGTH_LONG).show();
                        });
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> {
                    Log.e("LoginActivity", "Login error: " + errorMessage);
                    String userFriendlyMessage = errorMessage;
                    if (errorMessage.contains("Invalid login credentials")) {
                        userFriendlyMessage = "Incorrect email or password";
                    } else if (errorMessage.contains("network")) {
                        userFriendlyMessage = "Network error. Please check your connection.";
                    }
                    Toast.makeText(LoginActivity.this, "Login failed: " + userFriendlyMessage, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void signUp(String email, String password) {
        supabaseClient.signUp(email, password, new SupabaseClient.AuthCallback() {
            @Override
            public void onSuccess(String userId, String accessToken) {
                // Thêm thông tin người dùng vào bảng users
                supabaseClient.insertUser(email, userId, new SupabaseClient.AuthCallback() {
                    @Override
                    public void onSuccess(String userId, String accessToken) {
                        runOnUiThread(() -> {
                            // Lưu token và userId
                            prefs.edit()
                                    .putString("access_token", accessToken)
                                    .putString("user_id", userId)
                                    .apply();
                            Toast.makeText(LoginActivity.this, "Sign up successful. Please verify your email.", Toast.LENGTH_LONG).show();
                            navigateToMainActivity();
                        });
                    }

                    @Override
                    public void onError(String errorMessage) {
                        runOnUiThread(() -> {
                            Log.e("LoginActivity", "Failed to save user info: " + errorMessage);
                            Toast.makeText(LoginActivity.this, "Failed to save user info: " + errorMessage, Toast.LENGTH_LONG).show();
                        });
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> {
                    Log.e("LoginActivity", "Sign up error: " + errorMessage);
                    String userFriendlyMessage = errorMessage;
                    if (errorMessage.contains("User already registered")) {
                        userFriendlyMessage = "This email is already registered. Please use another email.";
                    } else if (errorMessage.contains("Password")) {
                        userFriendlyMessage = "Password is too weak. Please use a stronger password.";
                    } else if (errorMessage.contains("network")) {
                        userFriendlyMessage = "Network error. Please check your connection.";
                    }
                    Toast.makeText(LoginActivity.this, "Sign up failed: " + userFriendlyMessage, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}