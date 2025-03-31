package com.example.bt2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat; // For getColor
import android.graphics.Color;
import android.os.Bundle;

import com.example.bt2.adapters.SliderAdapter;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations; // If using transformations
import com.smarteist.autoimageslider.SliderView;
import java.util.ArrayList;
import java.util.List;

// Assumes layouts, Adapter, drawables, and R.color.red exist

public class SliderViewActivity extends AppCompatActivity {

    private SliderView sliderView;
    private List<Integer> arrayList;
    private SliderAdapter sliderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider_view); // Use correct layout

        sliderView = findViewById(R.id.imageSlider);

        // Initialize Data (Drawable resource IDs)
        arrayList = new ArrayList<>();
        arrayList.add(R.drawable.shoppe1); // Ensure these exist
        arrayList.add(R.drawable.shoppe2);
        arrayList.add(R.drawable.shoppe1); // Repeated?
        arrayList.add(R.drawable.shoppe4);

        // Setup Adapter
        sliderAdapter = new SliderAdapter(this, arrayList);
        sliderView.setSliderAdapter(sliderAdapter);

        // --- Configure SliderView ---

        // Indicator animation type
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);

        // Optional: Slider transformation animation
        // sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);

        // Auto cycle direction (matches XML)
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);

        // Indicator colors
        // Use ContextCompat.getColor or Color constants
        // sliderView.setIndicatorSelectedColor(ContextCompat.getColor(this, R.color.red));
        sliderView.setIndicatorSelectedColor(Color.RED); // Using constant
        sliderView.setIndicatorUnselectedColor(Color.GRAY);

        // Scroll time per slide
        sliderView.setScrollTimeInSec(5); // Set to 5 seconds as per slide

        // Enable auto cycling (redundant if true in XML)
        sliderView.setAutoCycle(true);

        // Start auto cycling (redundant if true in XML)
        sliderView.startAutoCycle();
    }
}