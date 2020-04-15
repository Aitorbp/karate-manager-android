package com.example.karate_manager.Models;

public class LoginResponse {

    public LoginResponse(float code, User user, String msg) {
        this.code = code;
        this.user = user;
        this.msg = msg;
    }

    public float getCode() {
        return code;
    }

    public User getUser() {
        return user;
    }

    public String getMsg() {
        return msg;
    }

    private float code;
    private User user;
    private String msg;
}
