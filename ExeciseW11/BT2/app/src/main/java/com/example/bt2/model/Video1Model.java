package com.example.bt2.model;

import java.io.Serializable;

public class Video1Model implements Serializable {
    private String title;
    private String desc;
    private String url;
    private long id;

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private String created_at;

    // Constructor mặc định rỗng cần thiết cho Firebase Realtime Database
    public Video1Model() {
    }

    // Constructor có tham số (Tùy chọn)
    public Video1Model(String title, String desc, String url) {
        this.title = title;
        this.desc = desc;
        this.url = url;
    }

    // Getters (Cần thiết cho Firebase)
    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getUrl() {
        return url;
    }

    // Setters (Cần thiết cho Firebase)
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}