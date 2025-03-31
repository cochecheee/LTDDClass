package com.example.bt2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import com.bumptech.glide.Glide; // Assuming usage with Glide
import com.example.bt2.R;
import com.example.bt2.models.Images;

import java.util.List;

// Assumes Images.java model and item_images.xml layout exist

public class ImagesViewPageAdapter extends PagerAdapter {

    private Context context;
    private List<Images> imagesList;

    public ImagesViewPageAdapter(Context context, List<Images> imagesList) {
        this.context = context;
        this.imagesList = imagesList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_images, container, false);

        ImageView imageView = view.findViewById(R.id.imgView);
        Images currentImage = imagesList.get(position);

        if (currentImage != null) {
            // Load image using Glide from the resource ID in the Images model
            Glide.with(context)
                    .load(currentImage.getImagesId()) // Load resource ID
                    .into(imageView);
        }

        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return (imagesList != null) ? imagesList.size() : 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}