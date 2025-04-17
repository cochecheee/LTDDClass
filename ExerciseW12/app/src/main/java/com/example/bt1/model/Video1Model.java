package com.example.bt1.model;

import java.io.Serializable;

public class Video1Model implements Serializable {
    private static final String TAG = "Video1Model";

    private String id;
    private String title;
    private String desc;
    private String url;
    private String created_at;
    private String video_path;

    public Video1Model() {}

    public Video1Model(String id, String title, String desc, String url, String created_at) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.url = url;
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getVideo_path() {
        return video_path;
    }

    public void setVideo_path(String video_path) {
        this.video_path = video_path;
    }
}