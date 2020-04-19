package com.example.karate_manager.Models.ParticipantModel;


import com.example.karate_manager.Models.KaratekaModel.Karateka;

import java.util.ArrayList;

public class ParticipantResponse {
    public float getCode() {
        return code;
    }

    public void setCode(float code) {
        this.code = code;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public ArrayList<Karateka> getKarateka() {
        return karateka;
    }

    public void setKarateka(ArrayList<Karateka> karateka) {
        this.karateka = karateka;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ParticipantResponse(float code, Participant participant, ArrayList<Karateka> karateka, String msg) {
        this.code = code;
        this.participant = participant;
        this.karateka = karateka;
        this.msg = msg;
    }

    private float code;
    private Participant participant;
    private ArrayList<Karateka > karateka = new ArrayList <> ();
    private String msg;
}
