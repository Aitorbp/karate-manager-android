package com.example.karate_manager.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.karate_manager.Models.UserModel.UserResponse;
import com.example.karate_manager.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MarketFragment extends Fragment {

    UserResponse user = new UserResponse(200,null,null );
    int groupSelectedId = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_market, container, false);
    }

    public void recievedUserGroup(UserResponse userResponse, int groupSelected) {
        if(user!=null ){
            user = userResponse;
            groupSelectedId = groupSelected;
        }
    }
}
