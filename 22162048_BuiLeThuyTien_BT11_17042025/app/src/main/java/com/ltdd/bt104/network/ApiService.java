package com.ltdd.bt104.network;

import com.ltdd.bt104.payload.*;

import java.util.List;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {
    // Auth
    @POST("auth/login")
    Call<MessageResponse> login(@Body LoginRequest request);

    @POST("auth/register")
    Call<MessageResponse> register(@Body SignupRequest request);

    @GET("media/public")
    Call<List<MediaResponse>> getPublicMedia();

    @Multipart
    @POST("media/upload")
    Call<MessageResponse> uploadMedia(
        @Part MultipartBody.Part file,
        @Part("data") RequestBody mediaData
    );

    @GET("media/{id}")
    Call<MediaResponse> getMediaDetails(@Path("id") Long id);
}
