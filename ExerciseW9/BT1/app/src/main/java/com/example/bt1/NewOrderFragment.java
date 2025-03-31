package com.example.bt1;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bt1.databinding.FragmentNeworderBinding;

public class NewOrderFragment extends Fragment {

    // Declare binding variable. Naming convention: _binding or just binding.
    private FragmentNeworderBinding binding;

    // Default constructor (optional but good practice for Fragments)
    public NewOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialization code that doesn't involve views (e.g., getting arguments)
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment using view binding
        binding = FragmentNeworderBinding.inflate(inflater, container, false);

        // --- Access views via binding object ---
        // Example: Set text on the TextView defined in fragment_neworder.xml
        binding.textViewLabel.setText("Content for New Order");

        // --- Setup RecyclerView, Adapters, Load data here ---
        // if (binding.recyclerView != null) { // Check if RecyclerView exists in layout
        //     binding.recyclerView.setLayoutManager(...);
        //     binding.recyclerView.setAdapter(...);
        //     loadData();
        // }

        // Return the root view from the binding object
        return binding.getRoot();
    }

    // Recommended practice: Clean up the binding reference in onDestroyView
    // to prevent memory leaks with Fragments.
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Release the binding reference
    }

    // private void loadData() {
    //    // Method to load data for RecyclerView, etc.
    // }
}