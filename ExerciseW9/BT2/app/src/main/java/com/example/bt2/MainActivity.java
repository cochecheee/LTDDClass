package com.example.bt2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.example.bt2.adapters.ImagesViewPageAdapter;
import com.example.bt2.models.Images;

import java.util.ArrayList;
import java.util.List;
import me.relex.circleindicator.CircleIndicator;

// Assumes Images.java, ImagesViewPageAdapter.java, and layouts exist
// Assumes drawable resources quangcao, coffee, companypizza, themoingon exist

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private List<Images> imagesList;
    private ImagesViewPageAdapter adapter;

    // Autorun variables
    private Handler handler;
    private Runnable runnable;
    private static final long AUTO_SCROLL_DELAY = 3000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Use the correct layout file

        viewPager = findViewById(R.id.viewpage);
        circleIndicator = findViewById(R.id.circle_indicator);

        imagesList = getListImages();
        adapter = new ImagesViewPageAdapter(this, imagesList);
        viewPager.setAdapter(adapter);

        // Link ViewPager and Indicator
        circleIndicator.setViewPager(viewPager);
        // Optional: if adapter data changes dynamically
        // adapter.registerDataSetObserver(circleIndicator.getDataSetObserver());

        // Autorun setup
        setupAutoRun();
    }

    private List<Images> getListImages() {
        List<Images> list = new ArrayList<>();
        // Ensure these drawables exist in your res/drawable folder
        list.add(new Images(R.drawable.coffee));
        list.add(new Images(R.drawable.quangcao));
        list.add(new Images(R.drawable.companypizza));
        list.add(new Images(R.drawable.themoingon));
        return list;
    }

    private void setupAutoRun() {
        handler = new Handler(Looper.getMainLooper());
        runnable = new Runnable() {
            @Override
            public void run() {
                if (adapter == null || adapter.getCount() == 0) return; // Prevent errors
                int currentItem = viewPager.getCurrentItem();
                int nextItem = currentItem + 1;
                if (nextItem >= adapter.getCount()) {
                    nextItem = 0; // Wrap around
                }
                viewPager.setCurrentItem(nextItem, true); // Smooth scroll
                // Handler repost is managed in listener and onResume
            }
        };

        // Listener to reset delay on user interaction or page change
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // Reset and repost the runnable when page changes
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, AUTO_SCROLL_DELAY);
            }
        });
    }

    // Manage autorun based on activity lifecycle
    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable); // Stop scrolling when not visible
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (handler != null && runnable != null) {
            handler.postDelayed(runnable, AUTO_SCROLL_DELAY); // Start scrolling when visible
        }
    }

    // Optional: Clean up observer if registered
    // @Override
    // protected void onDestroy() {
    //     super.onDestroy();
    //     if (adapter != null && circleIndicator != null && circleIndicator.getDataSetObserver() != null) {
    //         adapter.unregisterDataSetObserver(circleIndicator.getDataSetObserver());
    //     }
    // }
}