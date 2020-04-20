package com.example.karate_manager.Models.GroupModel;

import com.google.gson.annotations.SerializedName;

public class Group {


    public int getId() {
        return id;
    }

    public String getName_group() {
        return name_group;
    }

    public int getBudget() {
        return budget;
    }

    public String getGender() {
        return gender;
    }

    public String getId_user() {
        return id_user;
    }

    public String getPassword_group() {
        return password_group;
    }

    public int getPoints() {
        return points;
    }

    @SerializedName("id")
    private int id;
    @SerializedName("name_group")
    private String name_group;
    @SerializedName("budget")
    private int budget;
    @SerializedName("gender")
    private String gender;
    @SerializedName("id_user")
    private String id_user;
    @SerializedName("password_group")
    private String password_group;

    public Group(String name_group, int budget, String gender, String id_user, String password_group, int points) {
        this.name_group = name_group;
        this.budget = budget;
        this.gender = gender;
        this.id_user = id_user;
        this.password_group = password_group;
        this.points = points;
    }

    @SerializedName("points")
    private int points;



}
