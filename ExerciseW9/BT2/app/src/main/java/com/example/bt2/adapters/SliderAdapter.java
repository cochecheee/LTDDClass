package com.example.bt2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.example.bt2.R;
import com.smarteist.autoimageslider.SliderViewAdapter;
import java.util.ArrayList;
import java.util.List;

// Assumes R.layout.images_slider_shoppe and R.id.iv_auto_image_slider exist

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderHolder> {

    private Context context;
    // Assuming the list holds drawable resource IDs as per slide 28
    private List<Integer> arrayList;

    public SliderAdapter(Context context, List<Integer> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public SliderHolder onCreateViewHolder(ViewGroup parent) {
        // Use context, or parent.getContext()
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.images_slider_shoppe, parent, false);
        return new SliderHolder(view);
    }

    @Override
    public void onBindViewHolder(SliderHolder viewHolder, int position) {
        if (arrayList != null && position < arrayList.size()) {
            // Load drawable resource using Glide
            Glide.with(viewHolder.itemView) // Use itemView's context
                    .load(arrayList.get(position)) // Load the resource ID
                    .fitCenter() // Or centerCrop, etc.
                    .into(viewHolder.imageView);
        }
    }

    @Override
    public int getCount() {
        return (arrayList != null) ? arrayList.size() : 0;
    }

    // --- ViewHolder ---
    static class SliderHolder extends SliderViewAdapter.ViewHolder {
        ImageView imageView;

        public SliderHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_auto_image_slider);
        }
    }
    // --- End ViewHolder ---
}