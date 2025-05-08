package com.ltdd.bt104.payload;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}