package com.example.bt3;

import android.os.Bundle;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bt3.adapter.IconAdapter;
import com.example.bt3.model.IconModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView rcIcon;
    IconAdapter iconAdapter;
    ArrayList<IconModel> arrayList1; // Holds the original, unfiltered list
    SearchView searchView;

    // The 'shopeeActivity' reference from slide 7 was likely an example placeholder
    // for the activity context. The DP calculation usually belongs inside the
    // ItemDecoration class itself, using Resources.getSystem().getDisplayMetrics().density

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rcIcon = findViewById(R.id.rcIcon);
        searchView = findViewById(R.id.searchView);

        // 1. Initialize and Populate Data
        loadData(); // Keep data loading separate

        // 2. Setup Adapter
        // Pass a copy initially, or the original list if adapter handles filtering internally
        iconAdapter = new IconAdapter(this, new ArrayList<>(arrayList1)); // Pass context 'this'

        // 3. Setup RecyclerView Layout Manager
        // Horizontal grid with 2 rows (spanCount=2)
        GridLayoutManager gridLayoutManager = new GridLayoutManager(
                this, // context
                2,    // spanCount (number of rows for horizontal)
                GridLayoutManager.HORIZONTAL, // orientation
                false // reverseLayout
        );
        rcIcon.setLayoutManager(gridLayoutManager);

        // 4. Set Adapter
        rcIcon.setAdapter(iconAdapter);

        // 5. Add Custom Indicator Decoration (Requires LinePagerIndicatorDecoration class)
        rcIcon.addItemDecoration(new LinePagerIndicatorDecoration());

        // 6. Setup Search
        setupSearch();
    }

    private void loadData() {
        arrayList1 = new ArrayList<>();
        // Populate with sample data (replace with your actual data source)
        // Use more descriptive names if possible as suggested in slide 8 note
        arrayList1.add(new IconModel(R.drawable.icon1, "Description 1"));
        arrayList1.add(new IconModel(R.drawable.icon2, "Another Item 2"));
        arrayList1.add(new IconModel(R.drawable.icon3, "Third Sample"));
        arrayList1.add(new IconModel(R.drawable.icon4, "Test Desc 4"));
        arrayList1.add(new IconModel(R.drawable.icon5, "Description 5"));
        arrayList1.add(new IconModel(R.drawable.icon6, "Another Item 6"));
        arrayList1.add(new IconModel(R.drawable.icon7, "Third Sample 7"));
        arrayList1.add(new IconModel(R.drawable.icon8, "Test Desc 8"));
        arrayList1.add(new IconModel(R.drawable.icon9, "Description 9"));
        arrayList1.add(new IconModel(R.drawable.icon1, "Description 1 Repeat"));
        arrayList1.add(new IconModel(R.drawable.icon2, "Another Item 2 Repeat"));
        // ... add more items as needed
    }

    private void setupSearch() {
        searchView.clearFocus(); // Clear focus initially
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Optional: Handle action when user presses submit/search button
                return false; // Return false if search is handled in onQueryTextChange
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter the list as the user types
                filterListener(newText);
                return true; // Return true to indicate the listener handled the text change
            }
        });
    }

    /**
     * Filters the original list based on the search text and updates the adapter.
     * From Slide 10.
     * @param text The search query entered by the user.
     */
    private void filterListener(String text) {
        List<IconModel> filteredList = new ArrayList<>();
        // Always filter from the original, complete list (arrayList1)
        for (IconModel iconModel : arrayList1) {
            if (iconModel.getDesc() != null &&
                    iconModel.getDesc().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(iconModel);
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
            // Optional: Clear the adapter list when no results
            iconAdapter.setListenerList(new ArrayList<>()); // Pass empty list
        } else {
            // Update the adapter with the filtered list
            iconAdapter.setListenerList(filteredList);
        }
    }
}