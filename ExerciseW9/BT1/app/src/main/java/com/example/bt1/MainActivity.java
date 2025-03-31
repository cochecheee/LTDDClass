package com.example.bt1;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.bt1.adapter.ViewPager2Adapter;
import com.example.bt1.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator; // Import for cleaner Tab <-> ViewPager connection


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding; // ViewBinding variable
    private ViewPager2Adapter viewPager2Adapter;
    private String[] tabTitles = {"Xác nhận", "Lấy hàng", "Đang giao", "Đánh giá", "Hủy"}; // Tab titles array

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // --- ViewBinding Inflation ---
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // --- End ViewBinding ---

        // --- Toolbar Setup ---
        setSupportActionBar(binding.toolBar);
        // Optional: Set title if not set in XML or needs dynamic change
        // getSupportActionBar().setTitle("My Title");
        // --- End Toolbar Setup ---

        // --- ViewPager2 Adapter Setup ---
        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager2Adapter = new ViewPager2Adapter(fragmentManager, getLifecycle());
        binding.viewPager2.setAdapter(viewPager2Adapter);
        // --- End ViewPager2 Adapter Setup ---


        // --- TabLayout <-> ViewPager2 Synchronization (using TabLayoutMediator) ---
        new TabLayoutMediator(binding.tabLayout, binding.viewPager2,
                (tab, position) -> tab.setText(tabTitles[position])
        ).attach();
        // --- End TabLayout <-> ViewPager2 Synchronization ---


        // --- Optional: Listener for Tab selection (if extra actions needed) ---
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // ViewPager is already updated by TabLayoutMediator
                changeFabIcon(tab.getPosition()); // Call method to change FAB icon
                // Add any other specific actions needed when a tab is selected
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Optional: Add behavior when a tab is unselected
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Optional: Add behavior when the current tab is selected again
            }
        });
        // --- End Optional Tab Listener ---


        // --- FAB Setup ---
        binding.fabAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "FAB Clicked!", Snackbar.LENGTH_SHORT)
                        .setAction("Undo", null).show(); // Example action
            }
        });
        // Initial FAB icon setup (optional, based on starting tab)
        changeFabIcon(0);
        // --- End FAB Setup ---


        /* --- OLD Tab Setup (Replaced by TabLayoutMediator) ---
        // --- TabLayout Setup (Adding Tabs Programmatically - Less recommended with ViewPager2) ---
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Xác nhận"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Lấy hàng"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Đang giao"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Đánh giá"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Hủy"));
        // --- End TabLayout Setup ---

        // --- OLD Listener for ViewPager2 page changes (Handled by TabLayoutMediator) ---
        binding.viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position));
                // changeFabIcon(position); // Moved to onTabSelected for consistency
            }
        });
        --- */

    } // End of onCreate

    /**
     * Changes the FloatingActionButton icon based on the selected tab index.
     * @param index The index of the currently selected tab.
     */
    private void changeFabIcon(final int index) {
        // Consider hiding/showing only if the icon actually changes or animation is desired
        binding.fabAction.hide(new FloatingActionButton.OnVisibilityChangedListener() {
            @Override
            public void onHidden(FloatingActionButton fab) {
                super.onHidden(fab);
                int iconResId;
                switch (index) {
                    case 0: // Xác nhận
                        iconResId = R.drawable.ic_launcher_foreground; // Make sure this drawable exists
                        break;
                    case 1: // Lấy hàng
                        iconResId = R.drawable.ic_launcher_foreground; // Make sure this drawable exists
                        break;
                    case 2: // Đang giao
                        iconResId = R.drawable.ic_launcher_background; // Make sure this drawable exists
                        break;
                    // Add cases for other tabs if they need specific icons
                    case 3: // Đánh giá
                        iconResId = android.R.drawable.ic_menu_edit; // Example: Edit icon
                        break;
                    case 4: // Hủy
                        iconResId = android.R.drawable.ic_menu_close_clear_cancel; // Example: Cancel icon
                        break;
                    default:
                        iconResId = android.R.drawable.ic_dialog_email; // Default icon
                        break;
                }
                // Use ContextCompat for safety, although getDrawable is fine for API 21+
                binding.fabAction.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, iconResId));
                fab.show();
            }
        });

        /* // Original delayed version (less common for simple icon change)
        binding.fabAction.hide(); // Hide FAB temporarily
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                 // ... (switch statement as above) ...
                 binding.fabAction.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, iconResId));
                 binding.fabAction.show(); // Show FAB with new icon
            }
        }, 200); // Delay in milliseconds
        */
    }

    // --- Menu Handling ---
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menuSearch) {
            Toast.makeText(this, "Search selected", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.menuNewGroup) {
            Toast.makeText(this, "New Group selected", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.menuBroadcast) {
            Toast.makeText(this, "Broadcast selected", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.menuWeb) {
            Toast.makeText(this, "WhatsApp Web selected", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.menuMessage) {
            Toast.makeText(this, "Starred Messages selected", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.menuSetting) {
            Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return super.onOptionsItemSelected(item); // Important for handling other items (like Up button)
        }
    }
    // --- End Menu Handling ---

} // End of MainActivity