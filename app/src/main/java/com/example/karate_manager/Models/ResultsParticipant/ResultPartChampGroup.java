package com.example.karate_manager.Models.ResultsParticipant;

public class ResultPartChampGroup {
    private int id;
    private int own_budget;
    private int points;
    private int admin_user_group;
    private int id_user;
    private int id_group;
    private String created_at;
    private String updated_at;
    private String name;
    private String photo_profile;
    private String points_karateka = null;
    private String id_championship = null;
    private int total_points_in_championship;

    public ResultPartChampGroup(int id, int own_budget, int points, int admin_user_group, int id_user, int id_group, String created_at, String updated_at, String name, String photo_profile, String points_karateka, String id_championship, int total_points_in_championship) {
        this.id = id;
        this.own_budget = own_budget;
        this.points = points;
        this.admin_user_group = admin_user_group;
        this.id_user = id_user;
        this.id_group = id_group;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.name = name;
        this.photo_profile = photo_profile;
        this.points_karateka = points_karateka;
        this.id_championship = id_championship;
        this.total_points_in_championship = total_points_in_championship;
    }

    public int getId() {
        return id;
    }

    public int getOwn_budget() {
        return own_budget;
    }

    public int getPoints() {
        return points;
    }

    public int getAdmin_user_group() {
        return admin_user_group;
    }

    public int getId_user() {
        return id_user;
    }

    public int getId_group() {
        return id_group;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getName() {
        return name;
    }

    public String getPhoto_profile() {
        return photo_profile;
    }

    public String getPoints_karateka() {
        return points_karateka;
    }

    public String getId_championship() {
        return id_championship;
    }

    public int getTotal_points_in_championship() {
        return total_points_in_championship;
    }
}
