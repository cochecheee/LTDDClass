package com.ltdd.bt104.payload;

import lombok.Data;

@Data
public class ProfileUpdateRequest {
    private String fullName;
    private String email;
    // Additional fields (e.g., bio, location) can be added here.
}