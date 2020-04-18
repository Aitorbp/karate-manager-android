package com.example.karate_manager.Models.ParticipantModel;


import com.example.karate_manager.Models.KaratekaModel.Karateka;

import java.util.ArrayList;

public class ParticipantResponse {

    private float code;
    private Participant participant;
    private ArrayList<Karateka > karateka = new ArrayList <> ();
    private String msg;
}
