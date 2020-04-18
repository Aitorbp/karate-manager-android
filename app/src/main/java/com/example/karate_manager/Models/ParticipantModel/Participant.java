package com.example.karate_manager.Models.ParticipantModel;

import com.google.gson.annotations.SerializedName;

public class Participant {
    @SerializedName("id_user")
    private float id_user;

    @SerializedName("id_group")
    private String email;

    @SerializedName("admin_user_group")
    private String admin_user_group;

    @SerializedName("points")
    private float points;

    @SerializedName("own_budget")
    private float own_budget;

    @SerializedName("updated_at")
    private String updated_at = null;

    @SerializedName("created_at")
    private String created_at;

    @SerializedName("id")
    private String id;

    public Participant(float id_user, String email, String admin_user_group, float points, float own_budget, String updated_at, String created_at, String id) {
        this.id_user = id_user;
        this.email = email;
        this.admin_user_group = admin_user_group;
        this.points = points;
        this.own_budget = own_budget;
        this.updated_at = updated_at;
        this.created_at = created_at;
        this.id = id;
    }

    public void setId_user(float id_user) {
        this.id_user = id_user;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAdmin_user_group(String admin_user_group) {
        this.admin_user_group = admin_user_group;
    }

    public void setPoints(float points) {
        this.points = points;
    }

    public void setOwn_budget(float own_budget) {
        this.own_budget = own_budget;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getId_user() {
        return id_user;
    }

    public String getEmail() {
        return email;
    }

    public String getAdmin_user_group() {
        return admin_user_group;
    }

    public float getPoints() {
        return points;
    }

    public float getOwn_budget() {
        return own_budget;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getId() {
        return id;
    }
}
