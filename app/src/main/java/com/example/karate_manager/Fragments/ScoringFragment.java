package com.example.karate_manager.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.karate_manager.Models.UserModel.UserResponse;
import com.example.karate_manager.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScoringFragment extends Fragment {

    UserResponse user = new UserResponse(200,null,null );
    int groupSelectedId = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("User in Scoring", user.getUser().getName());
        Log.d("User in Scoring", String.valueOf(groupSelectedId));

        return inflater.inflate(R.layout.fragment_scoring, container, false);
    }



    public void recievedUserGroup(UserResponse userResponse, int groupSelected) {
        if(user!=null ){
            user =userResponse;
            groupSelectedId = groupSelected;
        }
    }
}
