package com.example.karate_manager.Models;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.Nullable;

public class User {

    public User(float id, String email, String name, String api_token, float admin_user, float active, String photo_profile, String email_verified_at, String created_at, String updated_at) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.api_token = api_token;
        this.admin_user = admin_user;
        this.active = active;
        this.photo_profile = photo_profile;
        this.email_verified_at = email_verified_at;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }




    @SerializedName("id")
    private float id;

    @SerializedName("email")
    private String email;

    @SerializedName("name")
    private String name;

    @SerializedName("api_token")
    private String api_token;
    @SerializedName("admin_user")
    private float admin_user;

    @SerializedName("active")
    private float active;

    @SerializedName("photo_profile")
    private String photo_profile = null;

    @SerializedName("email_verified_at")
    private String email_verified_at = null;
    @SerializedName("created_at")
    private String created_at;

    @SerializedName("updated_at")
    private String updated_at;

    public float getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getApitoken() {
        return api_token;
    }

    public float getAdmin_user() {
        return admin_user;
    }

    public float getActive() {
        return active;
    }

    public String getPhoto_profile() {
        return photo_profile;
    }

    public String getEmail_verified_at() {
        return email_verified_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }
}
