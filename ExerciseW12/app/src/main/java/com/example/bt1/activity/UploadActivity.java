package com.example.bt1.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bt1.R;
import com.example.bt1.SupabaseClient;

public class UploadActivity extends AppCompatActivity {
    private static final int PICK_VIDEO_REQUEST = 1;
    private EditText titleEditText, descEditText;
    private Button selectVideoButton, uploadButton;
    private Uri videoUri;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        prefs = getSharedPreferences("auth_prefs", MODE_PRIVATE);

        titleEditText = findViewById(R.id.titleEditText);
        descEditText = findViewById(R.id.descEditText);
        selectVideoButton = findViewById(R.id.selectVideoButton);
        uploadButton = findViewById(R.id.uploadButton);

        selectVideoButton.setOnClickListener(v -> selectVideo());
        uploadButton.setOnClickListener(v -> uploadVideo());
    }

    private void selectVideo() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_VIDEO_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK && data != null) {
            videoUri = data.getData();
            Toast.makeText(this, "Video selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadVideo() {
        String title = titleEditText.getText().toString().trim();
        String desc = descEditText.getText().toString().trim();
        String userId = prefs.getString("user_id", null);

        if (title.isEmpty() || videoUri == null || userId == null) {
            Toast.makeText(this, "Title, video, and user login are required", Toast.LENGTH_SHORT).show();
            return;
        }

        SupabaseClient.getInstance().uploadVideo(this, videoUri, title, desc, userId, new SupabaseClient.UploadCallback() {
            @Override
            public void onSuccess(String videoUrl) {
                runOnUiThread(() -> {
                    Toast.makeText(UploadActivity.this, "Video uploaded successfully", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> Toast.makeText(UploadActivity.this, "Upload failed: " + errorMessage, Toast.LENGTH_LONG).show());
            }
        });
    }
}