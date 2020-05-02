package com.example.karate_manager.Models.BidModel;

public class Bid {

    private int id;
    private String created_at;
    private String updated_at;
    private String bid;
    private String start_hour_bid = null;
    private int id_market;
    private int id_group;
    private String id_participants;
    private int id_karatekas;

    public int getId() {
        return id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getBid() {
        return bid;
    }

    public String getStart_hour_bid() {
        return start_hour_bid;
    }

    public int getId_market() {
        return id_market;
    }

    public int getId_group() {
        return id_group;
    }

    public String getId_participants() {
        return id_participants;
    }

    public int getId_karatekas() {
        return id_karatekas;
    }


}
