package com.example.karate_manager.Models.ParticipantModel;


import com.example.karate_manager.Models.KaratekaModel.Karateka;

import java.util.ArrayList;

public class ParticipantResponse {

    private int code;
    private ParticipantGroup participantGroup;
    private ArrayList<ParticipantGroup > participants = new ArrayList <> ();

    public ParticipantResponse(int code, ParticipantGroup participantGroup, ArrayList<ParticipantGroup> participants) {
        this.code = code;
        this.participantGroup = participantGroup;
        this.participants = participants;
    }

    public int getCode() {
        return code;
    }

    public ParticipantGroup getParticipantGroup() {
        return participantGroup;
    }

    public ArrayList<ParticipantGroup> getParticipants() {
        return participants;
    }
}
