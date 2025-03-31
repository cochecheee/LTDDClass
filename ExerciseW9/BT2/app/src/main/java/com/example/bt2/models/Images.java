package com.example.bt2.models;

import java.io.Serializable;

public class Images implements Serializable {
    private int imagesId; // Resource ID (e.g., R.drawable.image_name)
    public Images(int imagesId) {
        this.imagesId = imagesId;
    }
    public int getImagesId() {
        return imagesId;
    }
    public void setImagesId(int imagesId) {
        this.imagesId = imagesId;
    }
}
