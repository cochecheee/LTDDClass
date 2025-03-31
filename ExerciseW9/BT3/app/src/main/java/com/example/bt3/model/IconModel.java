package com.example.bt3.model;

import java.io.Serializable;

public class IconModel implements Serializable {
    private Integer imgId; // Resource ID for the image
    private String desc;   // Description text

    // Constructor
    public IconModel(Integer imgId, String desc) {
        this.imgId = imgId;
        this.desc = desc;
    }

    // Getters
    public Integer getImgId() {
        return imgId;
    }

    public String getDesc() {
        return desc;
    }

    // Setters
    public void setImgId(Integer imgId) {
        this.imgId = imgId;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
