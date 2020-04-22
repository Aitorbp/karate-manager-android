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
    private float id;
    @SerializedName("own_budget")
    private float own_budget;

    @SerializedName("points")
    private float points;

    @SerializedName("admin_user_group")
    private float admin_user_group;

    @SerializedName("id_user")
    private float id_user;

    @SerializedName("id_group")
    private float id_group;

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

    public float getId() {
        return id;
    }

    public float getOwn_budget() {
        return own_budget;
    }

    public float getPoints() {
        return points;
    }

    public float getAdmin_user_group() {
        return admin_user_group;
    }

    public float getId_user() {
        return id_user;
    }

    public float getId_group() {
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

    public void setId( float id ) {
        this.id = id;
    }

    public void setOwn_budget( float own_budget ) {
        this.own_budget = own_budget;
    }

    public void setPoints( float points ) {
        this.points = points;
    }

    public void setAdmin_user_group( float admin_user_group ) {
        this.admin_user_group = admin_user_group;
    }

    public void setId_user( float id_user ) {
        this.id_user = id_user;
    }

    public void setId_group( float id_group ) {
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

