package com.example.karate_manager.Models.ChampionshipModel;

import com.google.gson.annotations.SerializedName;

public class Championship {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("host_country")
    private String host_country;
    @SerializedName("photo")
    private String photo;
    @SerializedName("start_day")
    private String start_day;
    @SerializedName("finish_day")
    private String finish_day;
    @SerializedName("created_at")
    private String created_at;
    @SerializedName("updated_at")
    private String updated_at;



    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getHost_country() {
        return host_country;
    }

    public String getPhoto() {
        return photo;
    }

    public String getStart_day() {
        return start_day;
    }

    public String getFinish_day() {
        return finish_day;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }
}
