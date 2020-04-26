package com.example.karate_manager.Models.ParticipantModel;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ParticipantGroup {


    ArrayList<ParticipantGroup> participantGroup = new ArrayList<ParticipantGroup>();

    public ArrayList<ParticipantGroup> getParticipantGroup() {
        return participantGroup;
    }
    public ParticipantGroup() {
    }
    @SerializedName("id")
    private int id;
    @SerializedName("own_budget")
    private int own_budget;

    @SerializedName("points")
    private int points;

    @SerializedName("admin_user_group")
    private int admin_user_group;

    @SerializedName("id_user")
    private int id_user;

    @SerializedName("id_group")
    private int id_group;

    @SerializedName("created_at")
    private String created_at;

    @SerializedName("updated_at")
    private String updated_at;

    @SerializedName("email")
    private String email;

    @SerializedName("name")
    private String name;

    @SerializedName("photo_profile")
    private String photo_profile = null;


    // Getter Methods

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

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPhoto_profile() {
        return photo_profile;
    }

    // Setter Methods

    public void setId( int id ) {
        this.id = id;
    }

    public void setOwn_budget( int own_budget ) {
        this.own_budget = own_budget;
    }

    public void setPoints( int points ) {
        this.points = points;
    }

    public void setAdmin_user_group( int admin_user_group ) {
        this.admin_user_group = admin_user_group;
    }

    public void setId_user( int id_user ) {
        this.id_user = id_user;
    }

    public void setId_group( int id_group ) {
        this.id_group = id_group;
    }

    public void setCreated_at( String created_at ) {
        this.created_at = created_at;
    }

    public void setUpdated_at( String updated_at ) {
        this.updated_at = updated_at;
    }

    public void setEmail( String email ) {
        this.email = email;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public void setPhoto_profile( String photo_profile ) {
        this.photo_profile = photo_profile;
    }
}

