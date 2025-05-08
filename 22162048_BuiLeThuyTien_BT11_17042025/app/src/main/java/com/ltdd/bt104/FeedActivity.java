package com.ltdd.bt104;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ltdd.bt104.activities.MediaDetailActivity;
import com.ltdd.bt104.adapters.MediaAdapter;
import com.ltdd.bt104.network.ApiService;
import com.ltdd.bt104.network.RetrofitClient;
import com.ltdd.bt104.payload.MediaResponse;
import com.ltdd.bt104.session.SessionManager;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedActivity extends AppCompatActivity {
    private RecyclerView rvMedia;
    private MediaAdapter adapter;
    private ApiService apiService;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        rvMedia = findViewById(R.id.rvMedia);
        rvMedia.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MediaAdapter();
        rvMedia.setAdapter(adapter);
        findViewById(R.id.fabUpload).setOnClickListener(v -> {
            startActivity(new Intent(FeedActivity.this, UploadVideoActivity.class));
        });

        session = new SessionManager(this);
        apiService = RetrofitClient.getInstance(this)
                .create(ApiService.class);

        adapter.setOnItemClickListener(media -> {
            Intent intent = new Intent(FeedActivity.this, MediaDetailActivity.class);
            intent.putExtra("media_id", media.getId());
            startActivity(intent);
        });

        fetchFeed();
        // optional: pull to refresh
        findViewById(R.id.swipeRefresh)
                .setOnClickListener(v -> fetchFeed());
    }

    private void fetchFeed() {
        String token = session.getAuthToken();  // "Bearer <token>"
        Call<List<MediaResponse>> call = apiService.getPublicMedia();
        call.enqueue(new Callback<List<MediaResponse>>() {
            @Override
            public void onResponse(Call<List<MediaResponse>> call,
                                   Response<List<MediaResponse>> response) {
                if (response.isSuccessful() && response.body()!=null) {
                    adapter.setMediaList(response.body());
                } else {
                    Snackbar.make(rvMedia,
                            "Failed to load feed: " + response.message(),
                            Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<MediaResponse>> call, Throwable t) {
                Toast.makeText(FeedActivity.this,
                        "Error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}