package com.example.bt3_loginregister.model.request;

public class OtpRequest {

    private RegisterRequest data;
    private String otp;

    public OtpRequest(RegisterRequest data, String otp) {
        this.data = data;
        this.otp = otp;
    }
}
