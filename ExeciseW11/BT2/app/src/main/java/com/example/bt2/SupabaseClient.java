package com.example.bt2;

import android.content.Context;
import android.util.Log;

import com.example.bt2.model.Video1Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SupabaseClient {
    private static final String TAG = "SupabaseClient";
    private static final String SUPABASE_URL = "https://wjcwngquuthyylbsdaqj.supabase.co";
    private static final String SUPABASE_API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6IndqY3duZ3F1dXRoeXlsYnNkYXFqIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDQyNjYwODgsImV4cCI6MjA1OTg0MjA4OH0.Uq8Q82d4N5KI8LsrIC1sgURBbIqDeKfLO0xnlFD76C0";

    private static SupabaseClient instance;
    private final OkHttpClient httpClient;

    private SupabaseClient() {
        httpClient = new OkHttpClient();
    }

    public static synchronized SupabaseClient getInstance() {
        if (instance == null) {
            instance = new SupabaseClient();
        }
        return instance;
    }

    public interface FetchCallback<T> {
        void onSuccess(T result);
        void onError(String errorMessage);
    }

    public void fetchVideos(Context context, FetchCallback<List<Video1Model>> callback, int retryCount) {
        if (retryCount <= 0) {
            callback.onError("Max retries reached for fetching videos");
            return;
        }

        String endpoint = SUPABASE_URL + "/rest/v1/videos?select=*";

        Request request = new Request.Builder()
                .url(endpoint)
                .addHeader("apikey", SUPABASE_API_KEY)
                .addHeader("Authorization", "Bearer " + SUPABASE_API_KEY)
                .addHeader("Content-Type", "application/json")
                .addHeader("Prefer", "return=representation")
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Failed to fetch videos (retry " + retryCount + "): " + e.getMessage(), e);
                fetchVideos(context, callback, retryCount - 1);
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

    public String getStorageUrl(String bucket, String path) {
        return SUPABASE_URL + "/storage/v1/object/public/" + bucket + "/" + path;
    }

    private Video1Model parseVideoJson(JSONObject json, Context context) {
        Video1Model video = new Video1Model();

        try {
            video.setId(json.optString("id", ""));
            video.setTitle(json.optString("title", "No Title"));
            video.setDesc(json.optString("desc", "No Description"));
            video.setCreated_at(json.optString("created_at", ""));

            // Use the url column directly since video_path is not present
            String videoUrl = json.optString("url", "");
            video.setUrl(videoUrl);

            Log.d(TAG, "Parsed video: " + video.getTitle() + ", URL: " + video.getUrl());

        } catch (Exception e) {
            Log.e(TAG, "Error parsing video JSON", e);
        }

        return video;
    }
}