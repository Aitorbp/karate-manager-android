package com.example.karate_manager.Models.BidModel;

import com.example.karate_manager.Models.KaratekaModel.Karateka;

import java.util.ArrayList;

public class BidToRivalsResponse {

    private int code;
    ArrayList<KaratekaRival> karatekas = new ArrayList < KaratekaRival > ();
    private String msg;

    public int getCode() {
        return code;
    }

    public ArrayList<KaratekaRival> getKaratekas() {
        return karatekas;
    }

    public String getMsg() {
        return msg;
    }
}
