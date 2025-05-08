package com.example.bt1.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.bt1.R;
import com.example.bt1.SupabaseClient;
import com.example.bt1.adapter.VideoAdapter;
import com.example.bt1.model.Video1Model;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private final List<Video1Model> videoList = new ArrayList<>();
    private ViewPager2 viewPager;
    private SharedPreferences prefs;
    private ImageView profileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("auth_prefs", MODE_PRIVATE);

        viewPager = findViewById(R.id.vpager);
        Button uploadButton = findViewById(R.id.uploadButton);
        Button profileButton = findViewById(R.id.profileButton);
        profileImageView = findViewById(R.id.profileImageView);

        uploadButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, UploadActivity.class);
            startActivity(intent);
        });

        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        });

        loadUserProfile();
        loadVideosFromSupabase();
    }

    private void loadUserProfile() {
        String userId = prefs.getString("user_id", null);
        if (userId == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        SupabaseClient.getInstance().fetchUserProfile(userId, new SupabaseClient.FetchCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject userProfile) {
                String profilePictureUrl = userProfile.optString("profile_picture", "");
                runOnUiThread(() -> {
                    if (!profilePictureUrl.isEmpty()) {
                        Glide.with(MainActivity.this).load(profilePictureUrl).into(profileImageView);
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Failed to load profile: " + errorMessage, Toast.LENGTH_LONG).show());
            }
        });
    }

    private void logout() {
        SupabaseClient.getInstance().signOut(new SupabaseClient.AuthCallback() {
            @Override
            public void onSuccess(String userId, String accessToken) {
                runOnUiThread(() -> {
                    prefs.edit().remove("access_token").apply();
                    Toast.makeText(MainActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Logout failed: " + errorMessage, Toast.LENGTH_LONG).show());
            }
        });
    }

    private void loadVideosFromSupabase() {
        String userId = prefs.getString("user_id", null);
        if (userId == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        SupabaseClient.getInstance().fetchVideos(this, userId, new SupabaseClient.FetchCallback<List<Video1Model>>() {
            @Override
            public void onSuccess(List<Video1Model> videos) {
                runOnUiThread(() -> {
                    if (videos.isEmpty()) {
                        Log.w(TAG, "No videos returned from Supabase.");
                        Toast.makeText(MainActivity.this, "No videos found.", Toast.LENGTH_LONG).show();
                    } else {
                        videoList.clear();
                        videoList.addAll(videos);
                        setupViewPager();
                        Log.d(TAG, "Loaded " + videos.size() + " videos");
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                Log.e(TAG, "Error fetching videos: " + errorMessage);
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Failed to load videos: " + errorMessage, Toast.LENGTH_LONG).show());
            }
        }, 3);
    }

    private void setupViewPager() {
        VideoAdapter adapter = new VideoAdapter(this, videoList);
        viewPager.setAdapter(adapter);
        viewPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
    }
}