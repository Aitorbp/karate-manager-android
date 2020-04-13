package com.example.karate_manager.Models;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("password")
    private String password;
    @SerializedName("email")
    private String email;
    @SerializedName("name")
    private String name;

    public User(String password, String email, String name) {
        this.password = password;
        this.email = email;
        this.name = name;
    }
    public User(String password, String email){
        this.password = password;
        this.email = email;
    }
    public User(String email){
        this.email = email;
    }
    public String getPassword() { return password; }
    public void setPassword(String value) { this.password = value; }

    public String getEmail() { return email; }
    public void setEmail(String value) { this.email = value; }

    public String getName() { return name; }
    public void setName(String value) { this.name = value; }
}
