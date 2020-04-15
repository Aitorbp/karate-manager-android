package com.example.karate_manager.Models;

import com.google.gson.annotations.SerializedName;

public class Group {
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
    @SerializedName("points")
    private int points;

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

    public Group(String id_user, String name_group,String password_group, int budget, String gender,  int points) {
        this.name_group = name_group;
        this.budget = budget;
        this.gender = gender;

        this.id_user = id_user;
        this.password_group = password_group;
        this.points = points;
    }


}
