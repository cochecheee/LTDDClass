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
                    // Handle non-successful response (e.g., invalid credentials)
                    // You might want to parse the error message from responseBody
                    callback.onError("Login failed: " + response.code() + " " + responseBody);
                    return; // Added return here
                }
                try {
                    JSONObject jsonResponse = new JSONObject(responseBody);
                    accessToken = jsonResponse.getString("access_token"); // Store the access token
                    String userId = jsonResponse.getJSONObject("user").getString("id");
                    callback.onSuccess(userId, accessToken);
                } catch (JSONException e) {
                    callback.onError("Error parsing login response: " + e.getMessage());
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
            // You can add additional metadata if needed
            // JSONObject data = new JSONObject();
            // data.put("some_key", "some_value");
            // json.put("data", data);
        } catch (JSONException e) {
            callback.onError("Error creating JSON for signup: " + e.getMessage());
            return;
        }

        RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(AUTH_ENDPOINT + "/signup")
                .post(body)
                .addHeader("apikey", SUPABASE_API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError("Signup failed: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                if (!response.isSuccessful()) {
                    // Handle non-successful response (e.g., user already exists, weak password)
                    try {
                        JSONObject errorJson = new JSONObject(responseBody);
                        String errorMessage = errorJson.optString("msg", "Signup failed with code: " + response.code()); // Use "msg" field if available
                        if (errorMessage.isEmpty() && errorJson.has("error_description")) { // Fallback for GoTrue errors
                            errorMessage = errorJson.getString("error_description");
                        }
                        if (errorMessage.isEmpty()) { // Generic fallback
                            errorMessage = "Signup failed: " + response.code() + " " + responseBody;
                        }
                        callback.onError(errorMessage);
                    } catch (JSONException e) {
                        callback.onError("Signup failed: " + response.code() + " " + responseBody); // Fallback if parsing fails
                    }
                    return; // Added return here
                }
                try {
                    JSONObject jsonResponse = new JSONObject(responseBody);
                    // For signup, Supabase usually returns the user object but not an access token immediately
                    // unless email confirmation is disabled.
                    // You might need the user to confirm their email before they can log in.
                    String userId = jsonResponse.getString("id");

                    // If email confirmation is disabled or you handle it differently,
                    // you might get an access token here. Check the response structure.
                    // accessToken = jsonResponse.optString("access_token", null); // Example

                    // Assuming signup requires email confirmation, just return userId
                    callback.onSuccess(userId, null); // Pass null for accessToken initially

                } catch (JSONException e) {
                    callback.onError("Error parsing signup response: " + e.getMessage());
                }
            }
        });
    }


    // Lấy danh sách video (Yêu cầu xác thực)
    public void fetchVideos(FetchCallback<List<Video1Model>> callback) {
        if (accessToken == null) {
            callback.onError("Not authenticated. Please sign in first.");
            return;
        }

        Request request = new Request.Builder()
                .url(SUPABASE_URL + "/rest/v1/videos?select=*") // Assuming your table is named 'videos'
                .get()
                .addHeader("apikey", SUPABASE_API_KEY)
                .addHeader("Authorization", "Bearer " + accessToken) // Use the stored access token
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError("Failed to fetch videos: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                if (!response.isSuccessful()) {
                    callback.onError("Failed to fetch videos: " + response.code() + " " + responseBody);
                    return;
                }
                try {
                    JSONArray jsonArray = new JSONArray(responseBody);
                    List<Video1Model> videos = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        // Adapt this part based on your Video1Model structure and table columns
                        Video1Model video = new Video1Model(
                                jsonObject.getInt("id"), // Assuming 'id' column
                                jsonObject.getString("title"), // Assuming 'title' column
                                jsonObject.getString("video_url"), // Assuming 'video_url' column
                                jsonObject.getString("created_at") // Assuming 'created_at' column
                        );
                        videos.add(video);
                    }
                    callback.onSuccess(videos);
                } catch (JSONException e) {
                    callback.onError("Error parsing video list response: " + e.getMessage());
                }
            }
        });
    }

    // Upload video (Yêu cầu xác thực)
    public void uploadVideo(Context context, Uri videoUri, String bucketName, UploadCallback callback) {
        if (accessToken == null) {
            callback.onError("Not authenticated. Please sign in first.");
            return;
        }

        File videoFile = getFileFromUri(context, videoUri);
        if (videoFile == null || !videoFile.exists()) {
            callback.onError("Failed to get video file from Uri.");
            return;
        }

        // Determine MIME type (adjust if necessary)
        String mimeType = context.getContentResolver().getType(videoUri);
        if (mimeType == null) {
            mimeType = "video/mp4"; // Default or try to infer
        }

        // Generate a unique file name (e.g., using timestamp or UUID)
        String fileName = "video_" + System.currentTimeMillis() + "_" + videoFile.getName();
        // Ensure the filename doesn't contain problematic characters for URLs/paths if necessary

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", fileName,
                        RequestBody.create(videoFile, MediaType.parse(mimeType)))
                .build();

        // Construct the upload URL for Supabase Storage
        // The path within the bucket will be the fileName specified above.
        String uploadUrl = SUPABASE_URL + "/storage/v1/object/" + bucketName + "/" + fileName;

        Request request = new Request.Builder()
                .url(uploadUrl)
                .post(requestBody)
                .addHeader("apikey", SUPABASE_API_KEY)
                .addHeader("Authorization", "Bearer " + accessToken)
                // Supabase Storage might require specific headers like 'x-upsert' (set to "true" or "false")
                // Check Supabase Storage documentation for required headers. Let's assume upsert=true for now.
                .addHeader("x-upsert", "true")
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Upload failed", e); // Log the full error
                callback.onError("Video upload failed: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                Log.d(TAG, "Upload Response Code: " + response.code());
                Log.d(TAG, "Upload Response Body: " + responseBody);

                if (!response.isSuccessful()) {
                    // Try to parse Supabase error message
                    String errorMessage = "Video upload failed: " + response.code();
                    try {
                        JSONObject errorJson = new JSONObject(responseBody);
                        errorMessage += " - " + errorJson.optString("message", errorJson.optString("error", responseBody));
                    } catch (JSONException e) {
                        // Ignore if response is not JSON
                        errorMessage += " " + responseBody;
                    }
                    Log.e(TAG, "Upload failed response: " + errorMessage);
                    callback.onError(errorMessage);
                    return;
                }

                try {
                    // The response body for a successful upload might be minimal, often just contains the 'Key'.
                    // We need to construct the public URL ourselves or fetch it if needed.
                    // Supabase usually provides a way to get the public URL.
                    // Assuming the public URL follows a pattern:
                    String publicUrl = SUPABASE_URL + "/storage/v1/object/public/" + bucketName + "/" + fileName;
                    Log.d(TAG, "Upload successful. Public URL: " + publicUrl);
                    callback.onSuccess(publicUrl); // Return the constructed public URL

                } catch (Exception e) { // Catch broader exceptions just in case
                    Log.e(TAG, "Error processing upload response", e);
                    callback.onError("Error processing upload response: " + e.getMessage());
                }
            }
        });
    }


    // Helper method to get File from Uri (May need adjustments based on how Uri is obtained)
    private File getFileFromUri(Context context, Uri uri) {
        String filePath = null;
        // Try to get path for different Uri schemes
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { MediaStore.Video.Media.DATA }; // Or MediaStore.Images.Media.DATA / Files.FileColumns.DATA depending on source
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA); // Adjust column name if needed
                    filePath = cursor.getString(columnIndex);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error getting file path from content URI", e);
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            filePath = uri.getPath();
        }

        if (filePath != null) {
            return new File(filePath);
        } else {
            Log.e(TAG, "Could not get file path from URI: " + uri.toString());
            // Fallback or error handling needed if path is still null
            // Maybe try copying the file to app's cache directory if direct path access fails
            return null;
        }
    }

    // Add methods for other operations like fetching user profile, updating data, etc. as needed
    // Remember to add the Authorization header with the Bearer token for authenticated requests.

    // Method to potentially clear the access token on sign out
    public void signOut() {
        this.accessToken = null;
        // Optionally call Supabase auth endpoint for signout if available/needed
        // e.g., POST to /auth/v1/logout
        // Note: Supabase handles token expiry, but explicit server-side logout might be desired.
        Log.d(TAG, "User signed out, access token cleared.");
    }
}