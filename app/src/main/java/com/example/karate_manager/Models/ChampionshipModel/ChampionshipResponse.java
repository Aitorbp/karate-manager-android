package com.example.karate_manager.Models.ChampionshipModel;

import java.util.ArrayList;

public class ChampionshipResponse {
    private int code;
    ArrayList<Championship> championships = new ArrayList<Championship>();
    private String msg;

    public int getCode() {
        return code;
    }

    public ArrayList<Championship> getChampionships() {
        return championships;
    }

    public String getMsg() {
        return msg;
    }
}
