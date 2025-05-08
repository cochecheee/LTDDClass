package com.ltdd.bt104.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.ltdd.bt104.R;
import com.ltdd.bt104.network.ApiService;
import com.ltdd.bt104.network.RetrofitClient;
import com.ltdd.bt104.payload.MediaResponse;
import com.ltdd.bt104.session.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MediaDetailActivity extends AppCompatActivity {
    private ImageView ivThumbnail;
    private TextView tvTitle, tvDescription;
    private ApiService apiService;
    private SessionManager session;
    private long mediaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_detail);

        ivThumbnail   = findViewById(R.id.ivThumbnail);
        tvTitle       = findViewById(R.id.tvTitle);
        tvDescription = findViewById(R.id.tvDescription);

        session    = new SessionManager(this);
        apiService = RetrofitClient.getInstance(this).create(ApiService.class);

        mediaId = getIntent().getLongExtra("media_id", -1);
        if (mediaId == -1) {
            Toast.makeText(this, "Invalid media ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadDetails();
    }

    private void loadDetails() {
        Call<MediaResponse> call = apiService.getMediaDetails(mediaId);
        call.enqueue(new Callback<MediaResponse>() {
            @Override
            public void onResponse(Call<MediaResponse> call,
                                   Response<MediaResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MediaResponse m = response.body();
                    tvTitle.setText(m.getTitle());
                    tvDescription.setText(m.getDescription());
                    Glide.with(MediaDetailActivity.this)
                            .load(m.getUrl())
                            .placeholder(R.drawable.ic_placeholder)
                            .into(ivThumbnail);
                } else {
                    Toast.makeText(MediaDetailActivity.this,
                            "Failed to load media", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<MediaResponse> call, Throwable t) {
                Toast.makeText(MediaDetailActivity.this,
                        "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}