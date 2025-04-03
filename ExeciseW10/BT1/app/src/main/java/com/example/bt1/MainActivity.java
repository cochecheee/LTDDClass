package com.example.bt1; // Replace with your actual package name

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat; // Use ContextCompat for checkSelfPermission

import com.bumptech.glide.Glide; // Make sure Glide is imported
import com.example.bt1.R;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    // --- UI Elements --- (from Slide 11)
    Button btnChoose, btnUpload;
    ImageView imageViewChoose, imageViewUpload;
    EditText editTextUserName;
    TextView textViewUsername;

    // --- Member Variables --- (from Slide 11)
    private Uri mUri;
    private ProgressDialog mProgressDialog;
    public static final int MY_REQUEST_CODE = 100; // Renamed from MY_REQUEST_CODE=100 for clarity
    public static final String TAG = MainActivity.class.getName();

    // --- Permissions Arrays --- (from Slide 7)
    public static String[] storge_permissions = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static String[] storge_permissions_33 = {
            android.Manifest.permission.READ_MEDIA_IMAGES,
            android.Manifest.permission.READ_MEDIA_AUDIO,
            android.Manifest.permission.READ_MEDIA_VIDEO
    };

    // --- ActivityResultLauncher --- (from Slide 10)
    private final ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.e(TAG, "onActivityResult"); // Corrected Log parameter order
                    if (result.getResultCode() == RESULT_OK) { // Use Activity.RESULT_OK
                        Intent data = result.getData();
                        if (data == null) {
                            Toast.makeText(MainActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Uri uri = data.getData();
                        if (uri != null) { // Check if uri is not null
                            mUri = uri;
                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                                imageViewChoose.setImageBitmap(bitmap); // Display preview
                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(MainActivity.this, "Failed to load image", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Image selection cancelled", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) { // (from Slide 12)
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Make sure this layout file exists

        // Initialize Views
        AnhXa(); // (from Slide 12)

        // Initialize ProgressDialog
        mProgressDialog = new ProgressDialog(this); // (from Slide 12) - Use 'this' for context
        mProgressDialog.setMessage("Please wait upload....");
        mProgressDialog.setCancelable(false); // Optional: prevent dialog dismissal on touch outside

        // Set OnClick Listeners (from Slide 12)
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckPermission(); // checks quyền before opening gallery
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUri != null) {
                    // Use UploadImage1 as per Slide 13 (assuming it handles single image upload)
                    UploadImage1();
                } else {
                    Toast.makeText(MainActivity.this, "Please choose an image first", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // --- View Initialization --- (from Slide 11)
    private void AnhXa() {
        btnChoose = findViewById(R.id.btnChoose);
        btnUpload = findViewById(R.id.btnUpload);
        imageViewUpload = findViewById(R.id.imgMultipart); // Shows uploaded image
        imageViewChoose = findViewById(R.id.imgChoose);     // Shows chosen image preview
        editTextUserName = findViewById(R.id.editUserName);
        textViewUsername = findViewById(R.id.tvUsername);   // Displays username from response
    }

    // --- Permission Helper --- (from Slide 8)
    public static String[] permissions() {
        String[] p;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            p = storge_permissions_33;
        } else {
            p = storge_permissions;
        }
        return p;
    }

    // --- Check Permissions --- (from Slide 8)
    private void CheckPermission() {
        // No need to check M, checkSelfPermission handles lower APIs gracefully
        // (returns PERMISSION_GRANTED implicitly)
        // However, explicit request only needed for M+

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            openGallery(); // Permissions granted at install time
            return;
        }

        // Determine which permission to check based on API level
        String readPermission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            readPermission = Manifest.permission.READ_MEDIA_IMAGES;
        } else {
            readPermission = Manifest.permission.READ_EXTERNAL_STORAGE;
        }

        if (ContextCompat.checkSelfPermission(this, readPermission) == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            // Request permissions
            ActivityCompat.requestPermissions(this, permissions(), MY_REQUEST_CODE);
        }
    }

    // --- Handle Permission Result --- (from Slide 8)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_REQUEST_CODE) {
            // Check if READ permission was granted (the primary one needed for picking)
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                // Permission was denied. Handle appropriately (e.g., show rationale, disable functionality)
                Toast.makeText(this, "Storage permission is required to choose an image", Toast.LENGTH_LONG).show();
            }
        }
    }

    // --- Open Image Gallery --- (from Slide 9)
    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Use the ActivityResultLauncher
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Select Picture")); // Corrected title parameter name
    }


    // --- Upload Image Logic --- (from Slides 13 & 14)
    private void UploadImage1() {
        if (mUri == null) {
            Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show();
            return;
        }
        mProgressDialog.show();

        // Get username
        String username = editTextUserName.getText().toString().trim();
        if (username.isEmpty()) {
            // Optional: handle empty username if required by API
            username = "default_user"; // Or show an error
            // Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT).show();
            // mProgressDialog.dismiss();
            // return;
        }
        RequestBody requestUsername = RequestBody.create(MediaType.parse("multipart/form-data"), username);

        // Get real path and create File object
        String realPath = RealPathUtil.getRealPath(this, mUri);
        if (realPath == null) {
            Log.e(TAG, "Could not get real path for URI: " + mUri.toString());
            Toast.makeText(this, "Could not get file path. Try a different image or file manager.", Toast.LENGTH_LONG).show();
            mProgressDialog.dismiss();
            return;
        }
        Log.d(TAG, "Real Path: " + realPath); // Use Log.d or Log.i for info
        File file = new File(realPath);

        // Create RequestBody for the file
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // Create MultipartBody.Part
        // Use Const.MY_IMAGES for the part name as per Slide 5 & 6
        MultipartBody.Part partbodyavatar =
                MultipartBody.Part.createFormData(Const.MY_IMAGES, file.getName(), requestFile);

        // Make the API call using the ServiceAPI instance (ensure it's initialized)
        // Assuming ServiceAPI.servieapi holds the Retrofit service instance as shown on slide 6
        if (ServiceAPI.servieapi == null) {
            Log.e(TAG, "ServiceAPI.servieapi is not initialized!");
            Toast.makeText(this, "Network service not available", Toast.LENGTH_SHORT).show();
            mProgressDialog.dismiss();
            return;
        }

        ServiceAPI.servieapi.upload(requestUsername, partbodyavatar).enqueue(new Callback<List<ImageUpload>>() {
            @Override
            public void onResponse(@NonNull Call<List<ImageUpload>> call, @NonNull Response<List<ImageUpload>> response) {
                mProgressDialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    List<ImageUpload> imageUploadList = response.body();
                    if (!imageUploadList.isEmpty()) {
                        // Process the first item in the list (assuming single upload response)
                        ImageUpload imageUpload = imageUploadList.get(0);
                        textViewUsername.setText(imageUpload.getUsername()); // Display username from response

                        // Use Glide to load the uploaded image URL into imageViewUpload
                        Glide.with(MainActivity.this)
                                .load(imageUpload.getAvatar()) // getAvatar() should return the URL
                                //.placeholder(R.drawable.placeholder_image) // Optional placeholder
                                //.error(R.drawable.error_image) // Optional error image
                                .into(imageViewUpload);

                        Toast.makeText(MainActivity.this, "Thành công", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "Upload successful. Avatar URL: " + imageUpload.getAvatar());
                    } else {
                        Toast.makeText(MainActivity.this, "Thất bại: Empty response list", Toast.LENGTH_SHORT).show();
                        Log.w(TAG, "Upload successful but response list is empty.");
                    }
                } else {
                    // Handle API error (e.g., 4xx, 5xx)
                    String errorBody = "Unknown error";
                    try {
                        if (response.errorBody() != null) {
                            errorBody = response.errorBody().string();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Error reading error body", e);
                    }
                    Toast.makeText(MainActivity.this, "Thất bại: " + response.code() + " " + errorBody, Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Upload failed. Code: " + response.code() + ", Message: " + response.message() + ", ErrorBody: " + errorBody);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ImageUpload>> call, @NonNull Throwable t) {
                mProgressDialog.dismiss();
                Log.e(TAG, "Gọi API thất bại", t); // Log the full exception
                Toast.makeText(MainActivity.this, "Gọi API thất bại: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}