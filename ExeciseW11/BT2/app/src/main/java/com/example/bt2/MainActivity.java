package com.example.bt2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.bt2.model.Video1Model;

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
//        progressBar = findViewById(R.id.progressBar);
//        retryButton = findViewById(R.id.retryButton);
//        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
//        if (retryButton != null) retryButton.setVisibility(View.GONE);

        loadVideosFromSupabase();

//        if (retryButton != null) {
//            retryButton.setOnClickListener(v -> {
//                retryButton.setVisibility(View.GONE);
//                if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
//                loadVideosFromSupabase();
//            });
//        }
    }

    private void loadVideosFromSupabase() {
        SupabaseClient.getInstance().fetchVideos(this, new SupabaseClient.FetchCallback<List<Video1Model>>() {
            @Override
            public void onSuccess(List<Video1Model> videos) {
                runOnUiThread(() -> {
//                    if (progressBar != null) progressBar.setVisibility(View.GONE);

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
//private static final String SUPABASE_URL = "https://wjcwngquuthyylbsdaqj.supabase.co";
//private static final String SUPABASE_API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6IndqY3duZ3F1dXRoeXlsYnNkYXFqIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDQyNjYwODgsImV4cCI6MjA1OTg0MjA4OH0.Uq8Q82d4N5KI8LsrIC1sgURBbIqDeKfLO0xnlFD76C0";