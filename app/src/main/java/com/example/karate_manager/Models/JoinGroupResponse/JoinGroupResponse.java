package com.example.karate_manager.Models.JoinGroupResponse;

import com.example.karate_manager.Models.KaratekaModel.Karateka;

import java.util.ArrayList;

public class JoinGroupResponse {

    private int code;
    Participant Participant;
    ArrayList <Karateka> karateka = new ArrayList < Karateka > ();
    private String msg;

    public JoinGroupResponse(int code, com.example.karate_manager.Models.JoinGroupResponse.Participant participant, ArrayList<Karateka> karateka, String msg) {
        this.code = code;
        Participant = participant;
        this.karateka = karateka;
        this.msg = msg;
    }



    public int getCode() {
        return code;
    }

    public com.example.karate_manager.Models.JoinGroupResponse.Participant getParticipant() {
        return Participant;
    }

    public ArrayList<Karateka> getKarateka() {
        return karateka;
    }

    public String getMsg() {
        return msg;
    }
}
