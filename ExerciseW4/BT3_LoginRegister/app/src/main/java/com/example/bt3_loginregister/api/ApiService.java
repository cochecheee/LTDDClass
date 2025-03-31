package com.example.bt3_loginregister.api;

import com.example.bt3_loginregister.model.Category;
import com.example.bt3_loginregister.model.Product;
import com.example.bt3_loginregister.model.request.LoginRequest;
import com.example.bt3_loginregister.model.request.OtpRequest;
import com.example.bt3_loginregister.model.request.RegisterRequest;
import com.example.bt3_loginregister.model.response.LoginResponse;
import com.example.bt3_loginregister.model.response.OtpResponse;
import com.example.bt3_loginregister.model.response.RegisterResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface ApiService {
    // Endpoint để lấy tất cả danh mục
    @GET("categories")
    Call<List<Category>> getAllCategories();

    // Endpoint để lấy sản phẩm mới nhất (sử dụng endpoint random meals)
    @GET("meals/random/multiple")
    Call<List<Product>> getLatestProducts(@Query("count") int count);

    // Phương thức helper mặc định lấy 6 sản phẩm
    @GET("meals/random/multiple")
    Call<List<Product>> getLatestProducts();

    // Authentication endpoints
    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
    @POST("auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest registerRequest);

    @POST("auth/verify-otp")
    Call<OtpResponse> verifyOtp(@Body OtpRequest otpRequest);

}

