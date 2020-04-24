package com.example.karate_manager.Models.KaratekaModel;

import java.util.ArrayList;

public class MarketResponse {
    private int code;
    ArrayList < Karateka > karatekas = new ArrayList < Karateka > ();
    private String msg;

    public MarketResponse(int code, ArrayList<Karateka> karatekas, String msg) {
        this.code = code;
        this.karatekas = karatekas;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public ArrayList<Karateka> getKaratekas() {
        return karatekas;
    }

    public String getMsg() {
        return msg;
    }
}
