package com.example.karate_manager.Models.BidModel;

public class KaratekaRival {
    private int id;
    private String name;
    private String country;
    private int gender;
    private String weight;
    private int value;
    private String created_at;
    private String updated_at;
    private String photo_karateka;
    private String photo_country;
    private String points_karateka;
    private int bid_rival;
    private String date_bid;
    private String name_rival;
    private int id_participant_bid_send;
    private int id_participant_bid_receive;

    public int getId_participant_bid_send() {
        return id_participant_bid_send;
    }

    public int getId_participant_bid_receive() {
        return id_participant_bid_receive;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public int getGender() {
        return gender;
    }

    public String getWeight() {
        return weight;
    }

    public int getValue() {
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

    public String getPhoto_country() {
        return photo_country;
    }

    public String getPoints_karateka() {
        return points_karateka;
    }

    public int getBid_rival() {
        return bid_rival;
    }

    public String getDate_bid() {
        return date_bid;
    }

    public String getName_rival() {
        return name_rival;
    }
}
