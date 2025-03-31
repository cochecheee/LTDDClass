package com.example.bt2;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ViewFlipper;
import com.bumptech.glide.Glide;
import com.example.bt2.R;

import java.util.ArrayList;
import java.util.List;

public class ViewFlipperActivity extends AppCompatActivity {

    ViewFlipper viewFlipperMain;
    // List<String> arrayListFlipper; // Can be local if only used in ActionViewFlipperMain

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Ensure the layout name matches your file
        setContentView(R.layout.activity_view_flipper);

        viewFlipperMain = findViewById(R.id.viewFlipperMain);
        ActionViewFlipperMain();
    }

    // Helper function to setup ViewFlipper
    private void ActionViewFlipperMain() {
        List<String> arrayListFlipper = new ArrayList<>();
        // URLs from the slide - ensure they are accessible
        arrayListFlipper.add("http://app.iotstar.vn/appfoods/flipper/quangcao.png");
        arrayListFlipper.add("http://app.iotstar.vn/appfoods/flipper/coffee.jpg");
        arrayListFlipper.add("http://app.iotstar.vn/appfoods/flipper/companypizza.jpeg");
        arrayListFlipper.add("http://app.iotstar.vn/appfoods/flipper/themoingon.jpeg");

        for (int i = 0; i < arrayListFlipper.size(); i++) {
            ImageView imageView = new ImageView(getApplicationContext());
            Glide.with(getApplicationContext())
                    .load(arrayListFlipper.get(i))
                    .into(imageView);
            // FIT_XY can distort aspect ratio, consider CENTER_CROP or FIT_CENTER
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipperMain.addView(imageView);
        }

        // Configure ViewFlipper's behavior
        viewFlipperMain.setFlipInterval(3000); // Interval in milliseconds
        viewFlipperMain.setAutoStart(true);    // Start flipping automatically

        // Load animations (ensure R.anim files exist and match names)
        Animation slide_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
        Animation slide_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);

        // Set animations
        viewFlipperMain.setInAnimation(slide_in);
        viewFlipperMain.setOutAnimation(slide_out);
    }
}