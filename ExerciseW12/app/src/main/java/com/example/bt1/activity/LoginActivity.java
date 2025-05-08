package com.example.bt1.activity;

import android.app.ProgressDialog;
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
    private TextView switchModeTextView, titleTextView, verificationReminderTextView;
    private boolean isLoginMode = true;
    private SupabaseClient supabaseClient;
    private SharedPreferences prefs;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        prefs = getSharedPreferences("auth_prefs", MODE_PRIVATE);
        supabaseClient = SupabaseClient.getInstance();

        String savedToken = prefs.getString("access_token", null);
        if (savedToken != null) {
            supabaseClient.setAccessToken(savedToken);
            navigateToMainActivity();
            return;
        }

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        actionButton = findViewById(R.id.actionButton);
        switchModeTextView = findViewById(R.id.switchModeTextView);
        titleTextView = findViewById(R.id.titleTextView);
        verificationReminderTextView = findViewById(R.id.verificationReminderTextView);

        // Initialize ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing...");
        progressDialog.setCancelable(false);

        actionButton.setOnClickListener(v -> handleAuthAction());
        switchModeTextView.setOnClickListener(v -> switchMode());
    }

    private void switchMode() {
        isLoginMode = !isLoginMode;
        if (isLoginMode) {
            titleTextView.setText("Login");
            actionButton.setText("Login");
            switchModeTextView.setText("Don't have an account? Sign up");
            verificationReminderTextView.setVisibility(TextView.VISIBLE);
        } else {
            titleTextView.setText("Sign Up");
            actionButton.setText("Sign Up");
            switchModeTextView.setText("Already have an account? Login");
            verificationReminderTextView.setVisibility(TextView.GONE);
        }
    }

    private void handleAuthAction() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

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

        progressDialog.show();
        if (isLoginMode) {
            login(email, password);
        } else {
            signUp(email, password);
        }
    }

    private void login(String email, String password) {
        Log.d("LoginActivity", "Attempting login with email: " + email);
        supabaseClient.signIn(email, password, new SupabaseClient.AuthCallback() {
            @Override
            public void onSuccess(String userId, String accessToken) {
                supabaseClient.insertLoginHistory(userId, email, new SupabaseClient.AuthCallback() {
                    @Override
                    public void onSuccess(String userId, String accessToken) {
                        runOnUiThread(() -> {
                            prefs.edit()
                                    .putString("access_token", accessToken)
                                    .putString("user_id", userId)
                                    .apply();
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                            navigateToMainActivity();
                        });
                    }

                    @Override
                    public void onError(String errorMessage) {
                        runOnUiThread(() -> {
                            Log.e("LoginActivity", "Failed to save login history: " + errorMessage);
                            progressDialog.dismiss();
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
                    } else if (errorMessage.contains("email_not_confirmed")) {
                        userFriendlyMessage = "Please verify your email before logging in.";
                    } else if (errorMessage.contains("network")) {
                        userFriendlyMessage = "Network error. Please check your connection.";
                    } else if (errorMessage.contains("parsing")) {
                        userFriendlyMessage = "Unexpected server response. Please try again.";
                    }
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Login failed: " + userFriendlyMessage, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void signUp(String email, String password) {
        Log.d("LoginActivity", "Attempting signup with email: " + email);
        supabaseClient.signUp(email, password, new SupabaseClient.AuthCallback() {
            @Override
            public void onSuccess(String userId, String accessToken) {
                runOnUiThread(() -> {
                    progressDialog.dismiss();
                    Log.d("LoginActivity", "Signup success: userId=" + userId + ", accessToken=" + (accessToken != null ? "present" : "null"));
                    if (accessToken == null) {
                        Toast.makeText(LoginActivity.this, "Sign up successful. Please verify your email.", Toast.LENGTH_LONG).show();
                        emailEditText.setText("");
                        passwordEditText.setText("");
                        if (!isLoginMode) {
                            switchMode();
                        }
                    } else {
                        supabaseClient.insertUser(email, userId, new SupabaseClient.AuthCallback() {
                            @Override
                            public void onSuccess(String userId, String accessToken) {
                                runOnUiThread(() -> {
                                    prefs.edit()
                                            .putString("access_token", accessToken)
                                            .putString("user_id", userId)
                                            .apply();
                                    Toast.makeText(LoginActivity.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                                    navigateToMainActivity();
                                    Log.d("LoginActivity", "User insert successful for userId: " + userId);
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
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> {
                    Log.e("LoginActivity", "Sign up error: " + errorMessage);
                    String userFriendlyMessage = errorMessage;
                    if (errorMessage.contains("already registered")) {
                        userFriendlyMessage = "This email is already registered. Please use another email.";
                    } else if (errorMessage.contains("Password too weak")) {
                        userFriendlyMessage = "Password is too weak. Please use a stronger password.";
                    } else if (errorMessage.contains("network")) {
                        userFriendlyMessage = "Network error. Please check your connection.";
                    } else if (errorMessage.contains("email_address_invalid")) {
                        userFriendlyMessage = "Invalid email address. Please use a valid email.";
                    } else if (errorMessage.contains("parsing")) {
                        userFriendlyMessage = "Unexpected server response. Please try again later.";
                    }
                    progressDialog.dismiss();
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