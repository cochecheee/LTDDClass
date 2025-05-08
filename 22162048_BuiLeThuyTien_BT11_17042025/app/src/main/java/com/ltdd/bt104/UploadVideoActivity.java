package com.ltdd.bt104;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.VideoView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ltdd.bt104.network.ApiService;
import com.ltdd.bt104.network.RetrofitClient;
import com.ltdd.bt104.payload.MessageResponse;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.InputStream;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadVideoActivity extends AppCompatActivity {
    private static final int REQUEST_VIDEO = 1001;

    private Button btnSelect, btnUpload;
    private VideoView videoPreview;
    private TextInputEditText etTitle, etDescription;
    private Uri videoUri;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);

        btnSelect      = findViewById(R.id.btnSelectVideo);
        btnUpload      = findViewById(R.id.btnUpload);
        videoPreview   = findViewById(R.id.videoPreview);
        etTitle        = findViewById(R.id.etTitle);
        etDescription  = findViewById(R.id.etDescription);

        apiService = RetrofitClient.getInstance(this).create(ApiService.class);

        btnSelect.setOnClickListener(v -> openGalleryForVideo());
        btnUpload.setOnClickListener(v -> uploadVideo());
    }

    private void openGalleryForVideo() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("video/*");
        startActivityForResult(intent, REQUEST_VIDEO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_VIDEO && resultCode == RESULT_OK && data != null) {
            videoUri = data.getData();
            videoPreview.setVideoURI(videoUri);
            videoPreview.setVisibility(VideoView.VISIBLE);
            videoPreview.start();
        }
    }

    private void uploadVideo() {
        String title = etTitle.getText().toString().trim();
        String desc  = etDescription.getText().toString().trim();
        if (videoUri == null || title.isEmpty()) {
            Toast.makeText(this, "Select a video and enter a title", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Convert URI to File via cache
            String fileName = "video_" + UUID.randomUUID() + ".mp4";
            File tempFile = File.createTempFile("upload", ".mp4", getCacheDir());
            InputStream in = getContentResolver().openInputStream(videoUri);
            java.nio.file.Files.copy(in, tempFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);

            // Prepare parts
            RequestBody reqFile = RequestBody.create(
                    tempFile, MediaType.parse("video/mp4"));
            MultipartBody.Part filePart = MultipartBody.Part.createFormData(
                    "file", tempFile.getName(), reqFile);

            // JSON metadata
            String json = "{\"title\":\""+title+"\",\"description\":\""+desc+"\",\"type\":\"video\",\"isPublic\":true}";
            RequestBody dataPart = RequestBody.create(
                    json, MediaType.parse("application/json"));

            // API call
            apiService.uploadMedia(filePart, dataPart)
                    .enqueue(new Callback<MessageResponse>() {
                        @Override
                        public void onResponse(Call<MessageResponse> call,
                                               Response<MessageResponse> response) {
                            if (response.isSuccessful() && response.body()!=null) {
                                Toast.makeText(UploadVideoActivity.this,
                                        "Upload successful", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(UploadVideoActivity.this,
                                        "Upload failed: " + response.message(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<MessageResponse> call, Throwable t) {
                            Toast.makeText(UploadVideoActivity.this,
                                    "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (Exception e) {
            Toast.makeText(this, "File error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}