package com.example.bt3_loginregister.model.request;

public class RegisterRequest {
    private String fulname; //full name
    private String email;
    private String phonenumber;
    private String password;
    private String confirmPassword;

    public RegisterRequest(String fulname, String email, String phonenumber, String password, String confirmPassword) {
        this.fulname = fulname;
        this.email = email;
        this.phonenumber = phonenumber;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }
}
