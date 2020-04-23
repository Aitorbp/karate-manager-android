package com.example.karate_manager.Models.JoinGroupResponse;

import com.google.gson.annotations.SerializedName;

public class Participant {
    @SerializedName("id_user")
    private int id_user;

    @SerializedName("id_group")
    private int id_group;

    @SerializedName("admin_user_group")
    private String admin_user_group;

    @SerializedName("points")
    private int points;

    @SerializedName("own_budget")
    private int own_budget;

    @SerializedName("updated_at")
    private String updated_at = null;

    @SerializedName("created_at")
    private String created_at;

    @SerializedName("id")
    private int id;

    public Participant(int id_user, int id_group, String admin_user_group, int points, int own_budget, String updated_at, String created_at, int id) {
        this.id_user = id_user;
        this.id_group = id_group;
        this.admin_user_group = admin_user_group;
        this.points = points;
        this.own_budget = own_budget;
        this.updated_at = updated_at;
        this.created_at = created_at;
        this.id = id;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public void setId_group(int id_group) {
        this.id_group = id_group;
    }

    public void setAdmin_user_group(String admin_user_group) {
        this.admin_user_group = admin_user_group;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setOwn_budget(int own_budget) {
        this.own_budget = own_budget;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getId_user() {
        return id_user;
    }

    public int getId_group() {
        return id_group;
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

    public int getId() {
        return id;
    }
}
