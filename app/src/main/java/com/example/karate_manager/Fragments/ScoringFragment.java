package com.example.karate_manager.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.karate_manager.Adapters.AdapterScoring;
import com.example.karate_manager.Models.ParticipantModel.ParticipantResponse;
import com.example.karate_manager.Models.UserModel.UserResponse;
import com.example.karate_manager.Network.APIService;
import com.example.karate_manager.Network.ApiUtils;
import com.example.karate_manager.R;


public class ScoringFragment extends Fragment {
    AdapterScoring adapterScoring;
    ListView listViewScoring;
    int groupSelectedId = 0;
    private APIService APIService;
    UserResponse user = new UserResponse(200,null,null );
    ParticipantResponse participantResponse = new ParticipantResponse(200,null,null );


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.fragment_scoring, container, false);
        listViewScoring = (ListView) RootView.findViewById(R.id.scoring_listview);
        APIService = ApiUtils.getAPIService();


        adapterScoring = new AdapterScoring(getActivity().getApplicationContext(), R.layout.item_participant_layout, participantResponse.getParticipants());

        Log.d("User in Scoring", user.getUser().getName());
        Log.d("User in Scoring", String.valueOf(groupSelectedId));
        getAllParticipantByGroup(String.valueOf(groupSelectedId));

        return RootView;
    }


    public void getAllParticipantByGroup(String id_group){

        Call<ParticipantResponse> call = APIService.getParticipantsByGroup(id_group);
        call.enqueue(new Callback<ParticipantResponse>() {
            @Override
            public void onResponse(Call<ParticipantResponse> call, Response<ParticipantResponse> response) {
                if (response.isSuccessful()) {
                    participantResponse = response.body();
              //      Log.d("VALUE",String.valueOf(response.body().getParticipants().get(1).getId()));

                    adapterScoring.notifyDataSetChanged();
                    adapterScoring.setData(participantResponse);
                    listViewScoring.setAdapter(adapterScoring);
                    Log.d("RESPONSE_SUCCESS", "Everything All right");

                }
            }

            @Override
            public void onFailure(Call<ParticipantResponse> call, Throwable t) {

                Log.d("RESPONSE_FAILURE", String.valueOf(t));
            }
        });



    }

    public void recievedUserGroup(UserResponse userResponse, int groupSelected) {
        if(user!=null ){
            user =userResponse;
            groupSelectedId = groupSelected;
        }
    }



}
