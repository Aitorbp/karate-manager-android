package com.example.karate_manager.Models.GroupModel;

import com.example.karate_manager.Models.JoinGroupResponse.Participant;
import com.example.karate_manager.Models.KaratekaModel.Karateka;

import java.util.ArrayList;

public class GroupResponse {
    public GroupResponse(int code, Group group, Participant participant, ArrayList<Karateka> karatekas, String msg) {
        this.code = code;
        this.group = group;
        this.participant = participant;
        Karatekas = karatekas;
        this.msg = msg;
    }

    private int code;
    Group group;
    Participant participant;
    ArrayList <Karateka> Karatekas = new ArrayList <Karateka> ();
    private String msg;

    public int getCode() {
        return code;
    }

    public Group getGroup() {
        return group;
    }

    public Participant getParticipant() {
        return participant;
    }

    public ArrayList<Karateka> getKaratekas() {
        return Karatekas;
    }

    public String getMsg() {
        return msg;
    }
}
