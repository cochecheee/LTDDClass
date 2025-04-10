package com.example.bt2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.lifecycleScope;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.bt2.adapter.VideosAdapter;
import com.example.bt2.model.Video1Model;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.github.jan.supabase.SupabaseClient;
import io.github.jan.supabase.exceptions.RestException;
import io.github.jan.supabase.postgrest.Postgrest;
import io.github.jan.supabase.postgrest.PostgrestResult;
import io.github.jan.supabase.postgrest.PostgrestKt;
import io.github.jan.supabase.storage.Storage;
import kotlin.Unit;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.CoroutineScopeKt;
import kotlinx.coroutines.BuildersKt;

import static io.github.jan.supabase.SupabaseClientBuilderKt.createSupabaseClient;

public class MainActivity extends AppCompatActivity implements LifecycleOwner {

    private static final String TAG = "MainActivitySupabase";
    private final String SUPABASE_URL = "https://wjcwngquuthyylbsdaqj.supabase.co";
    private final String SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6IndqY3duZ3F1dXRoeXlsYnNkYXFqIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDQyNjYwODgsImV4cCI6MjA1OTg0MjA4OH0.Uq8Q82d4N5KI8LsrIC1sgURBbIqDeKfLO0xnlFD76C0";

    private ViewPager2 viewPager2;
    private VideosAdapter videosAdapter;
    private final List<Video1Model> videoList = new ArrayList<>();
    private SupabaseClient supabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        viewPager2 = findViewById(R.id.vpager);
        setupViewPager();
        initializeSupabase();

        if (supabase != null) {
            fetchVideosFromSupabase();
        } else {
            Log.e(TAG, "Supabase client is not initialized! Cannot fetch videos.");
            Toast.makeText(this, "Lỗi khởi tạo kết nối!", Toast.LENGTH_LONG).show();
        }
    }

    private void setupViewPager() {
        videosAdapter = new VideosAdapter(this, videoList);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        viewPager2.setAdapter(videosAdapter);
        Log.d(TAG, "ViewPager and Adapter setup complete.");
    }

    private void initializeSupabase() {
        try {
            supabase = createSupabaseClient(
                    SUPABASE_URL,
                    SUPABASE_ANON_KEY,
                    builder -> {
                        builder.install(Postgrest.INSTANCE, config -> {});
                        builder.install(Storage.INSTANCE, config -> {});
                        return Unit.INSTANCE;
                    }
            );
            Log.i(TAG, "Supabase client initialized successfully.");
        } catch (Exception e) {
            Log.e(TAG, "Supabase client initialization error", e);
            supabase = null;
        }
    }

    private void fetchVideosFromSupabase() {
        lifecycleScope.launchWhenStarted(() -> {
            Job job = BuildersKt.launch(
                    CoroutineScopeKt.MainScope().getCoroutineContext(),
                    Dispatchers.getIO(),
                    null,
                    (scope, continuation) -> {
                        try {
                            Log.d(TAG, "Running Supabase query on IO thread...");
                            PostgrestResult result = PostgrestKt.select(
                                    supabase.getPostgrest().from("videos"),
                                    null, false, null, null, null, null, null, null, null,
                                    continuation
                            );

                            String json = result.getBody().toString();
                            Log.d(TAG, "Received JSON: " + json);

                            Gson gson = new Gson();
                            Type listType = new TypeToken<ArrayList<Video1Model>>() {}.getType();
                            List<Video1Model> videos = gson.fromJson(json, listType);

                            if (videos == null) {
                                videos = new ArrayList<>();
                                Log.w(TAG, "Parsed list is null, using empty list");
                            }

                            List<Video1Model> finalVideos = videos;
                            runOnUiThread(() -> {
                                videoList.clear();
                                videoList.addAll(finalVideos);
                                videosAdapter.notifyDataSetChanged();

                                if (finalVideos.isEmpty()) {
                                    Toast.makeText(this, "Không tìm thấy video nào.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(this, "Tải " + finalVideos.size() + " video thành công!", Toast.LENGTH_SHORT).show();
                                }
                            });

                        } catch (RestException e) {
                            Log.e(TAG, "Supabase RestException: " + e.getMessage(), e);
                            runOnUiThread(() -> Toast.makeText(this, "Lỗi Supabase: " + e.getMessage(), Toast.LENGTH_LONG).show());
                        } catch (Exception e) {
                            Log.e(TAG, "Exception when fetching videos", e);
                            runOnUiThread(() -> Toast.makeText(this, "Lỗi không xác định khi tải video", Toast.LENGTH_LONG).show());
                        }

                        return Unit.INSTANCE;
                    }
            );
            return null;
        });
    }
}
