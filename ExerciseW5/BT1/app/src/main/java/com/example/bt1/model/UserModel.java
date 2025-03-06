package com.example.bt1.model;

import java.io.Serializable;

public class UserModel implements Serializable {
    private String name;

    public UserModel(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String address;

}
