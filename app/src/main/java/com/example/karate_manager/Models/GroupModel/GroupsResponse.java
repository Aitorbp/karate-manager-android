package com.example.karate_manager.Models.GroupModel;



import java.util.ArrayList;

public class GroupsResponse {
    public float getCode() {
        return code;
    }

    public void setCode(float code) {
        this.code = code;
    }

    public ArrayList<Group> getGroupByParticipant() {
        return groupByParticipant;
    }

    public void setGroupByParticipant(ArrayList<Group> groupByParticipant) {
        this.groupByParticipant = groupByParticipant;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private float code;
    private ArrayList<Group> groupByParticipant = new ArrayList <> ();
    private String msg;

    public GroupsResponse(float code, ArrayList<Group> groupByParticipant, String msg) {
        this.code = code;
        this.groupByParticipant = groupByParticipant;
        this.msg = msg;
    }
}
