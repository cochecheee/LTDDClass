package com.example.bt1;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;


import com.example.bt1.model.Video1Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SupabaseClient {
    private static final String TAG = "SupabaseClient";
    private static final String SUPABASE_URL = "https://wjcwngquuthyylbsdaqj.supabase.co";
    private static final String SUPABASE_API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6IndqY3duZ3F1dXRoeXlsYnNkYXFqIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDQyNjYwODgsImV4cCI6MjA1OTg0MjA4OH0.Uq8Q82d4N5KI8LsrIC1sgURBbIqDeKfLO0xnlFD76C0";
    private static final String AUTH_ENDPOINT = SUPABASE_URL + "/auth/v1";

    private static SupabaseClient instance;
    private final OkHttpClient httpClient;
    private String accessToken; // Lưu token để sử dụng cho các yêu cầu được xác thực

    private SupabaseClient() {
        httpClient = new OkHttpClient();
    }

    public static synchronized SupabaseClient getInstance() {
        if (instance == null) {
            instance = new SupabaseClient();
        }
        return instance;
    }

    public interface AuthCallback {
        void onSuccess(String userId, String accessToken);
        void onError(String errorMessage);
    }

    public interface FetchCallback<T> {
        void onSuccess(T result);
        void onError(String errorMessage);
    }

    public interface UploadCallback {
        void onSuccess(String videoUrl);
        void onError(String errorMessage);
    }

    // Đăng nhập
    public void signIn(String email, String password, AuthCallback callback) {
        JSONObject json = new JSONObject();
        try {
            json.put("email", email);
            json.put("password", password);
        } catch (JSONException e) {
            callback.onError("Error creating JSON: " + e.getMessage());
            return;
        }

        RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(AUTH_ENDPOINT + "/token?grant_type=password")
                .post(body)
                .addHeader("apikey", SUPABASE_API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError("Login failed: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                if (!response.isSuccessful()) {
                    callback.onError("Login failed: " + response.code() + " - " + responseBody);
                    return;
                }

                try {
                    JSONObject jsonResponse = new JSONObject(responseBody);
                    String userId = jsonResponse.getJSONObject("user").getString("id");
                    String token = jsonResponse.getString("access_token");
                    accessToken = token; // Lưu token
                    callback.onSuccess(userId, token);
                } catch (JSONException e) {
                    callback.onError("Error parsing response: " + e.getMessage());
                }
            }
        });
    }

    // Đăng ký
    public void signUp(String email, String password, AuthCallback callback) {
        JSONObject json = new JSONObject();
        try {
            json.put("email", email);
            json.put("password", password);
        } catch (JSONException e) {
            callback.onError("Error creating JSON: " + e.getMessage());
            return;
        }

        RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(AUTH_ENDPOINT + "/signup")
                .post(body)
                .addHeader("apikey", SUPABASE_API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError("Sign up failed: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                if (!response.isSuccessful()) {
                    callback.onError("Sign up failed: " + response.code() + " - " + responseBody);
                    return;
                }

                try {
                    JSONObject jsonResponse = new JSONObject(responseBody);
                    String userId = jsonResponse.getJSONObject("user").getString("id");
                    String token = jsonResponse.getString("access_token");
                    accessToken = token; // Lưu token
                    callback.onSuccess(userId, token);
                } catch (JSONException e) {
                    callback.onError("Error parsing response: " + e.getMessage());
                }
            }
        });
    }

    // Đăng xuất
    public void signOut(AuthCallback callback) {
        Request request = new Request.Builder()
                .url(AUTH_ENDPOINT + "/logout")
                .post(RequestBody.create("", MediaType.parse("application/json")))
                .addHeader("apikey", SUPABASE_API_KEY)
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Content-Type", "application/json")
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError("Logout failed: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    callback.onError("Logout failed: " + response.code());
                    return;
                }
                accessToken = null; // Xóa token
                callback.onSuccess(null, null);
            }
        });
    }

    // Lưu lịch sử đăng nhập
    public void insertLoginHistory(String userId, String email, AuthCallback callback) {
        JSONObject loginJson = new JSONObject();
        try {
            loginJson.put("user_id", userId);
            loginJson.put("email", email);
        } catch (JSONException e) {
            callback.onError("Error creating login history JSON");
            return;
        }

        Request request = new Request.Builder()
                .url(SUPABASE_URL + "/rest/v1/login_history")
                .addHeader("apikey", SUPABASE_API_KEY)
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(loginJson.toString(), MediaType.parse("application/json")))
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError("Failed to insert login history: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    callback.onError("Error inserting login history: " + response.code());
                    return;
                }
                callback.onSuccess(userId, accessToken);
            }
        });
    }

    // Lấy danh sách video
    public void fetchVideos(Context context, String userId, FetchCallback<List<Video1Model>> callback, int retryCount) {
        if (retryCount <= 0) {
            callback.onError("Max retries reached for fetching videos");
            return;
        }

        String endpoint = SUPABASE_URL + "/rest/v1/videos?select=*&user_id=eq." + userId;

        Request request = new Request.Builder()
                .url(endpoint)
                .addHeader("apikey", SUPABASE_API_KEY)
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Content-Type", "application/json")
                .addHeader("Prefer", "return=representation")
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Failed to fetch videos (retry " + retryCount + "): " + e.getMessage(), e);
                fetchVideos(context, userId, callback, retryCount - 1);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "Response code: " + response.code());
                String responseBody = response.body().string();
                Log.d(TAG, "Response body: " + responseBody);

                if (!response.isSuccessful()) {
                    Log.e(TAG, "Error response: " + response.code());
                    callback.onError("Server error: " + response.code());
                    return;
                }

                try {
                    JSONArray jsonArray = new JSONArray(responseBody);
                    List<Video1Model> videos = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject videoJson = jsonArray.getJSONObject(i);
                        Video1Model video = parseVideoJson(videoJson, context);
                        videos.add(video);
                    }

                    Log.d(TAG, "Parsed " + videos.size() + " videos");
                    callback.onSuccess(videos);

                } catch (JSONException e) {
                    Log.e(TAG, "JSON parsing error", e);
                    callback.onError("Data parsing error: " + e.getMessage());
                }
            }
        });
    }

    // Tải video lên
    public void uploadVideo(Context context, Uri videoUri, String title, String desc, String userId, UploadCallback callback) {
        String filePath = getRealPathFromURI(context, videoUri);
        if (filePath == null) {
            callback.onError("Unable to get video file path");
            return;
        }

        File videoFile = new File(filePath);
        String fileName = System.currentTimeMillis() + "_" + videoFile.getName();

        // Tải file lên Storage
        RequestBody fileBody = RequestBody.create(videoFile, MediaType.parse("video/*"));
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", fileName, fileBody)
                .build();

        Request uploadRequest = new Request.Builder()
                .url(SUPABASE_URL + "/storage/v1/object/videos/" + fileName)
                .post(requestBody)
                .addHeader("apikey", SUPABASE_API_KEY)
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        httpClient.newCall(uploadRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError("Upload failed: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    callback.onError("Upload failed: " + response.code());
                    return;
                }

                String videoUrl = getStorageUrl("videos", fileName);

                // Lưu metadata vào bảng videos
                JSONObject videoJson = new JSONObject();
                try {
                    videoJson.put("user_id", userId);
                    videoJson.put("title", title);
                    videoJson.put("desc", desc);
                    videoJson.put("url", videoUrl);
                } catch (JSONException e) {
                    callback.onError("Error creating video JSON");
                    return;
                }

                Request saveRequest = new Request.Builder()
                        .url(SUPABASE_URL + "/rest/v1/videos")
                        .addHeader("apikey", SUPABASE_API_KEY)
                        .addHeader("Authorization", "Bearer " + accessToken)
                        .addHeader("Content-Type", "application/json")
                        .post(RequestBody.create(videoJson.toString(), MediaType.parse("application/json")))
                        .build();

                httpClient.newCall(saveRequest).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        callback.onError("Failed to save video metadata: " + e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            callback.onSuccess(videoUrl);
                        } else {
                            callback.onError("Server error: " + response.code());
                        }
                    }
                });
            }
        });
    }

    // Thêm thông tin người dùng vào bảng users
    public void insertUser(String email, String userId, AuthCallback callback) {
        JSONObject userJson = new JSONObject();
        try {
            userJson.put("id", userId);
            userJson.put("email", email);
            userJson.put("display_name", email.split("@")[0]);
        } catch (JSONException e) {
            callback.onError("Error creating user JSON");
            return;
        }

        Request request = new Request.Builder()
                .url(SUPABASE_URL + "/rest/v1/users")
                .addHeader("apikey", SUPABASE_API_KEY)
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(userJson.toString(), MediaType.parse("application/json")))
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError("Failed to insert user: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    callback.onError("Error inserting user: " + response.code());
                    return;
                }
                callback.onSuccess(userId, accessToken);
            }
        });
    }

    // Lấy URL công khai của file trong Storage
    public String getStorageUrl(String bucket, String path) {
        return SUPABASE_URL + "/storage/v1/object/public/" + bucket + "/" + path;
    }

    // Phân tích JSON video
    private Video1Model parseVideoJson(JSONObject json, Context context) {
        Video1Model video = new Video1Model();

        try {
            video.setId(json.optString("id", ""));
            video.setTitle(json.optString("title", "No Title"));
            video.setDesc(json.optString("desc", "No Description"));
            video.setCreated_at(json.optString("created_at", ""));
            String videoUrl = json.optString("url", "");
            video.setUrl(videoUrl);

            Log.d(TAG, "Parsed video: " + video.getTitle() + ", URL: " + video.getUrl());

        } catch (Exception e) {
            Log.e(TAG, "Error parsing video JSON", e);
        }

        return video;
    }

    // Lấy đường dẫn thực từ Uri
    private String getRealPathFromURI(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Video.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();
        return path;
    }

    // Getter và Setter cho accessToken
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String token) {
        this.accessToken = token;
    }
}