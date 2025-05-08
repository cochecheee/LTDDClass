package com.ltdd.bt104.payload;

import lombok.Data;

@Data
public class MediaUploadRequest {
    private String title;
    private String description;
    // The type of media: MUSIC or VIDEO
    private String type;
    // Indicate whether the media is public (true) or private (false)
    private boolean isPublic;
}