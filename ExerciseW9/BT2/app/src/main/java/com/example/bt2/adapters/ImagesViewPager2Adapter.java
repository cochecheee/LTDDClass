package com.example.bt2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide; // Assuming usage with Glide
import com.example.bt2.R;
import com.example.bt2.models.Images;

import java.util.List;

// Assumes Images.java model and item_images.xml layout exist

public class ImagesViewPager2Adapter extends RecyclerView.Adapter<ImagesViewPager2Adapter.ImagesViewHolder> {

    private Context context;
    private List<Images> imagesList;

    public ImagesViewPager2Adapter(Context context, List<Images> imagesList) {
        this.context = context;
        this.imagesList = imagesList;
    }

    @NonNull
    @Override
    public ImagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context); // Use context
        View view = inflater.inflate(R.layout.item_images, parent, false);
        return new ImagesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagesViewHolder holder, int position) {
        Images currentImage = imagesList.get(position);
        if (currentImage != null) {
            // Load image using Glide from resource ID
            Glide.with(context) // Use context
                    .load(currentImage.getImagesId())
                    .into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return (imagesList != null) ? imagesList.size() : 0;
    }

    // --- ViewHolder ---
    public static class ImagesViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImagesViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgView);
        }
    }
    // --- End ViewHolder ---
}