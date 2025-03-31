package com.example.bt3_loginregister;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bt3_loginregister.api.ApiService;
import com.example.bt3_loginregister.api.RetrofitClient;
import com.example.bt3_loginregister.model.request.OtpRequest;
import com.example.bt3_loginregister.model.request.RegisterRequest;
import com.example.bt3_loginregister.model.response.OtpResponse;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpVerificationActivity extends AppCompatActivity {
    private static final String TAG = "OtpVerificationActivity";
    private EditText editTextOtp1, editTextOtp2, editTextOtp3, editTextOtp4, editTextOtp5, editTextOtp6;
    private Button buttonVerifyOtp;
    private String email, username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        editTextOtp1 = findViewById(R.id.editTextOtp1);
        editTextOtp2 = findViewById(R.id.editTextOtp2);
        editTextOtp3 = findViewById(R.id.editTextOtp3);
        editTextOtp4 = findViewById(R.id.editTextOtp4);
        editTextOtp5 = findViewById(R.id.editTextOtp5);
        editTextOtp6 = findViewById(R.id.editTextOtp6);
        buttonVerifyOtp = findViewById(R.id.buttonVerifyOtp);

        email = getIntent().getStringExtra("email");
        username = getIntent().getStringExtra("username");
        password = getIntent().getStringExtra("password");

        buttonVerifyOtp.setOnClickListener(v -> {
            String otp = getOtpFromFields();
            if (otp.length() != 6) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ 6 chữ số OTP", Toast.LENGTH_SHORT).show();
                return;
            }

            RegisterRequest registerRequest = new RegisterRequest(username, email, "1111", password,password);
            OtpRequest otpRequest = new OtpRequest(registerRequest,otp);

            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

            apiService.verifyOtp(otpRequest).enqueue(new Callback<OtpResponse>() {
                @Override
                public void onResponse(Call<OtpResponse> call, Response<OtpResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        OtpResponse otpResponse = response.body();
                        if (otpResponse.isSuccess()) {
                            Toast.makeText(OtpVerificationActivity.this, "Xác nhận OTP thành công", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            Toast.makeText(OtpVerificationActivity.this, otpResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        handleErrorResponse(response.code());
                    }
                }

                @Override
                public void onFailure(Call<OtpResponse> call, Throwable t) {
                    Toast.makeText(OtpVerificationActivity.this, "Đã xảy ra lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "onFailure: ", t);
                }
            });
        });
    }

    private String getOtpFromFields() {
        return editTextOtp1.getText().toString().trim() +
                editTextOtp2.getText().toString().trim() +
                editTextOtp3.getText().toString().trim() +
                editTextOtp4.getText().toString().trim() +
                editTextOtp5.getText().toString().trim() +
                editTextOtp6.getText().toString().trim();
    }

    private void handleErrorResponse(int code) {
        if (code == 400) {
            Toast.makeText(this, "Mã OTP không hợp lệ", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Xác nhận thất bại. Mã lỗi: " + code, Toast.LENGTH_SHORT).show();
        }
    }
}