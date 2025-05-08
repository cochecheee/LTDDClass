package com.example.bt1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bt1.MainActivity;
import com.example.bt1.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.imageview.ShapeableImageView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile); // Make sure this matches your XML file name

        // --- Toolbar Setup (if you haven't done it already) ---
        MaterialToolbar toolbar = findViewById(R.id.toolbar); // Use the ID from your profile_activity.xml
        setSupportActionBar(toolbar);

        // Enable the back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed(); // Or finish();
            }
        });
        // --- End Toolbar Setup ---


        // Get a reference to the profile image
        ShapeableImageView profileImageView = findViewById(R.id.iv_profile_image);

        // Set an OnClickListener on the profile image
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start MainActivity
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // ... (Load user data into TextViews, etc.)
        // e.g.,
        // TextView tvIdValue = findViewById(R.id.tv_id_value);
        // tvIdValue.setText("3"); // Or load dynamically
    }
}