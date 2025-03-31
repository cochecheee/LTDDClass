package com.example.bt1.adapter;

// Assuming these fragment classes exist in your project structure
// import your.package.name.fragments.NewOrderFragment;
// import your.package.name.fragments.PickupFragment;
// import your.package.name.fragments.DeliveryFragment;
// import your.package.name.fragments.DanhGiaFragment;
// import your.package.name.fragments.CancelFragment;


import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPager2Adapter extends FragmentStateAdapter {

    private static final int NUM_TABS = 5; // Define number of tabs

    public ViewPager2Adapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                // return new NewOrderFragment(); // Replace with your actual Fragment class
                return new PlaceholderFragment("Xác nhận"); // Example using a placeholder
            case 1:
                // return new PickupFragment();
                return new PlaceholderFragment("Lấy hàng");
            case 2:
                // return new DeliveryFragment();
                return new PlaceholderFragment("Đang giao");
            case 3:
                // return new DanhGiaFragment();
                return new PlaceholderFragment("Đánh giá");
            case 4:
                // return new CancelFragment();
                return new PlaceholderFragment("Hủy");
            default:
                // return new NewOrderFragment(); // Or return a default/error fragment
                return new PlaceholderFragment("Default");
        }
    }

    @Override
    public int getItemCount() {
        return NUM_TABS; // Return the total number of fragments/tabs
    }

    // --- Optional: PlaceholderFragment for demonstration ---
    // You should replace this with your actual Fragment classes
    public static class PlaceholderFragment extends Fragment {
        private String label;

        public PlaceholderFragment(String label) {
            this.label = label;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            TextView textView = new TextView(getContext());
            textView.setText(label + " Fragment");
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(20);
            return textView;
        }
        // Remember to add imports for View, LayoutInflater, ViewGroup, Bundle, TextView, Gravity
    }
    // --- End PlaceholderFragment ---
}