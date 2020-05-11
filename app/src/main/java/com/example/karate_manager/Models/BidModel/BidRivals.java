package com.example.karate_manager.Models.BidModel;

public class BidRivals {
    public float getId() {
        return id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public int getBid_rival() {
        return bid_rival;
    }

    public int getId_karateka() {
        return id_karateka;
    }

    public int getId_participant_bid_send() {
        return id_participant_bid_send;
    }

    public int getId_participant_bid_receive() {
        return id_participant_bid_receive;
    }

    private float id;
    private String created_at;
    private String updated_at;
    private int bid_rival;
    private int id_karateka;
    private int id_participant_bid_send;
    private int id_participant_bid_receive;
}
