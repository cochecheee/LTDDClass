package com.ltdd.bt104.network;

import android.content.Context;

import com.ltdd.bt104.session.SessionManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://musicandvideo-brh9bdc6cabcb4f0.canadacentral-01.azurewebsites.net/api/";
    private static Retrofit instance;

    // Build an OkHttpClient that injects the token
    private static OkHttpClient getClient(Context ctx) {
        return new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        SessionManager session = new SessionManager(ctx);
                        String token = session.getAuthToken();
                        Request original = chain.request();
                        Request.Builder builder = original.newBuilder();
                        if (token != null) {
                            builder.header("Authorization", "Bearer " + token);
                        }
                        return chain.proceed(builder.build());
                    }
                })
                .build();
    }

    // Use getClient(context) so every request gets the header
    public static Retrofit getInstance(Context ctx) {
        if (instance == null) {
            instance = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(getClient(ctx))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return instance;
    }
}