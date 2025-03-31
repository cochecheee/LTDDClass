package com.example.bt3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bt3.R;
import com.example.bt3.model.IconModel;

import java.util.List;

public class IconAdapter extends RecyclerView.Adapter<IconAdapter.IconHolder> {

    private Context context;
    // This list holds the currently displayed items (can be filtered)
    private List<IconModel> arrayList;

    // Constructor
    public IconAdapter(Context context, List<IconModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public IconHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout
        View view = LayoutInflater.from(context) // Use context here
                .inflate(R.layout.item_icon_promotion, parent, false);
        return new IconHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IconHolder holder, int position) {
        // Get the data model for this position
        IconModel iconModel = arrayList.get(position);

        if (iconModel != null) {
            // Bind data to the views
            holder.tvIcon.setText(iconModel.getDesc());
            // Load image using Glide (ensure Glide dependency is added)
            Glide.with(context) // Use context
                    .load(iconModel.getImgId()) // Load the resource ID
                    // .placeholder(R.drawable.placeholder_image) // Optional placeholder
                    // .error(R.drawable.error_image) // Optional error image
                    .into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        // Return the size of the current list
        return (arrayList != null) ? arrayList.size() : 0;
    }

    /**
     * Updates the list displayed by the adapter (used for search filtering).
     * From Slide 9.
     * @param iconModelList The new (potentially filtered) list to display.
     */
    public void setListenerList(List<IconModel> iconModelList) {
        this.arrayList = iconModelList; // Update the internal list reference
        notifyDataSetChanged(); // Notify the adapter that the data set has changed
    }


    // --- ViewHolder Class ---
    public static class IconHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvIcon;

        public IconHolder(@NonNull View itemView) {
            super(itemView);
            // Find views within the item layout
            imageView = itemView.findViewById(R.id.ivImgIcon);
            tvIcon = itemView.findViewById(R.id.tvIcon);
        }
    }
    // --- End ViewHolder ---
}