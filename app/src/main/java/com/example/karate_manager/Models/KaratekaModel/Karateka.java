package com.example.karate_manager.Models.KaratekaModel;

public class Karateka {
    private int id;
    private String name;
    private String country;
    private float gender;
    private String weight;
    private float value;
    private String created_at;
    private String updated_at;
    private String photo_karateka;

    public Karateka(int id, String name, String country, float gender, String weight, float value, String created_at, String updated_at, String photo_karateka) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.gender = gender;
        this.weight = weight;
        this.value = value;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.photo_karateka = photo_karateka;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setGender(float gender) {
        this.gender = gender;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public void setPhoto_karateka(String photo_karateka) {
        this.photo_karateka = photo_karateka;
    }

    public float getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public float getGender() {
        return gender;
    }

    public String getWeight() {
        return weight;
    }

    public float getValue() {
        return value;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getPhoto_karateka() {
        return photo_karateka;
    }
}
