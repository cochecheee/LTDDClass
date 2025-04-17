package com.example.bt1.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.bt1.R;
import com.example.bt1.SupabaseClient;
import com.example.bt1.adapter.VideoAdapter;
import com.example.bt1.model.Video1Model;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private final List<Video1Model> videoList = new ArrayList<>();
    private ViewPager2 viewPager;
//    private ProgressBar progressBar;
//    private Button retryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.vpager);

        loadVideosFromSupabase();
    }

    private void loadVideosFromSupabase() {
        SupabaseClient.getInstance().fetchVideos(this, new SupabaseClient.FetchCallback<List<Video1Model>>() {
            @Override
            public void onSuccess(List<Video1Model> videos) {
                runOnUiThread(() -> {

                    if (videos.isEmpty()) {
                        Log.w(TAG, "No videos returned from Supabase. Check table data and permissions.");
                        Toast.makeText(MainActivity.this,
                                "No videos found. Ensure videos are added in Supabase.", Toast.LENGTH_LONG).show();
//                        if (retryButton != null) retryButton.setVisibility(View.VISIBLE);
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
                runOnUiThread(() -> {
//                    if (progressBar != null) progressBar.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this,
                            "Failed to load videos: " + errorMessage, Toast.LENGTH_LONG).show();
//                    if (retryButton != null) retryButton.setVisibility(View.VISIBLE);
                });
            }
        }, 3);
    }

    private void setupViewPager() {
        VideoAdapter adapter = new VideoAdapter(this, videoList);
        viewPager.setAdapter(adapter);
        viewPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
    }
}