package com.example.bt1.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.bt1.R;
import com.example.bt1.SupabaseClient;
import com.example.bt1.model.Video1Model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProfileActivity extends AppCompatActivity {
    private ImageView profileImageView;
    private EditText displayNameEditText;
    private TextView videoCountTextView;
    private Button updateButton, uploadImageButton;
    private Uri imageUri;
    private static final int PICK_IMAGE_REQUEST = 2;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileImageView = findViewById(R.id.profileImageView);
        displayNameEditText = findViewById(R.id.displayNameEditText);
        videoCountTextView = findViewById(R.id.videoCountTextView);
        updateButton = findViewById(R.id.updateButton);
        uploadImageButton = findViewById(R.id.uploadImageButton);

        prefs = getSharedPreferences("auth_prefs", MODE_PRIVATE);
        String userId = prefs.getString("user_id", null);

        fetchProfile(userId);
        fetchVideoCount(userId);

        uploadImageButton.setOnClickListener(v -> selectImage());
        updateButton.setOnClickListener(v -> updateProfile(userId));
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            profileImageView.setImageURI(imageUri);
        }
    }

    private void fetchProfile(String userId) {
        SupabaseClient.getInstance().fetchUserProfile(userId, new SupabaseClient.FetchCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject userProfile) {
                String profilePictureUrl = userProfile.optString("profile_picture", "");
                String displayName = userProfile.optString("display_name", "");
                runOnUiThread(() -> {
                    displayNameEditText.setText(displayName);
                    if (!profilePictureUrl.isEmpty()) {
                        Glide.with(ProfileActivity.this).load(profilePictureUrl).into(profileImageView);
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> Toast.makeText(ProfileActivity.this, "Failed to load profile: " + errorMessage, Toast.LENGTH_LONG).show());
            }
        });
    }

    private void fetchVideoCount(String userId) {
        SupabaseClient.getInstance().fetchVideos(this, userId, new SupabaseClient.FetchCallback<List<Video1Model>>() {
            @Override
            public void onSuccess(List<Video1Model> videos) {
                runOnUiThread(() -> videoCountTextView.setText("Videos: " + videos.size()));
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> Toast.makeText(ProfileActivity.this, "Failed to load video count: " + errorMessage, Toast.LENGTH_LONG).show());
            }
        }, 3);
    }

    private void updateProfile(String userId) {
        String displayName = displayNameEditText.getText().toString().trim();
        if (displayName.isEmpty()) {
            Toast.makeText(this, "Display name is required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (imageUri != null) {
            SupabaseClient.getInstance().uploadProfilePicture(this, imageUri, userId, new SupabaseClient.UploadCallback() {
                @Override
                public void onSuccess(String imageUrl) {
                    updateProfileData(userId, displayName, imageUrl);
                }

                @Override
                public void onError(String errorMessage) {
                    runOnUiThread(() -> Toast.makeText(ProfileActivity.this, "Failed to upload image: " + errorMessage, Toast.LENGTH_LONG).show());
                }
            });
        } else {
            updateProfileData(userId, displayName, null);
        }
    }

    private void updateProfileData(String userId, String displayName, String imageUrl) {
        JSONObject json = new JSONObject();
        try {
            json.put("display_name", displayName);
            if (imageUrl != null) {
                json.put("profile_picture", imageUrl);
            }
        } catch (JSONException e) {
            Toast.makeText(this, "Error creating JSON", Toast.LENGTH_SHORT).show();
            return;
        }

        Request request = new Request.Builder()
                .url(SupabaseClient.SUPABASE_URL + "/rest/v1/users?id=eq." + userId)
                .addHeader("apikey", SupabaseClient.SUPABASE_API_KEY)
                .addHeader("Authorization", "Bearer " + SupabaseClient.getInstance().getAccessToken())
                .addHeader("Content-Type", "application/json")
                .patch(RequestBody.create(json.toString(), MediaType.parse("application/json")))
                .build();

        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(ProfileActivity.this, "Update failed: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    runOnUiThread(() -> {
                        Toast.makeText(ProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(ProfileActivity.this, "Update failed: " + response.code(), Toast.LENGTH_LONG).show());
                }
            }
        });
    }
}