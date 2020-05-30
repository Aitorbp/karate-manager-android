package com.example.karate_manager.Models.ResultsParticipant;

import java.util.ArrayList;

public class ResultsParticipantResponse {
    private int code;
    ArrayList<ResultPartChampGroup> resultPartChampGroup = new ArrayList<ResultPartChampGroup>();
    private String msg;

    public ResultsParticipantResponse(int code, ArrayList<ResultPartChampGroup> resultPartChampGroup, String msg) {
        this.code = code;
        this.resultPartChampGroup = resultPartChampGroup;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public ArrayList<ResultPartChampGroup> getResultPartChampGroup() {
        return resultPartChampGroup;
    }

    public String getMsg() {
        return msg;
    }
}
