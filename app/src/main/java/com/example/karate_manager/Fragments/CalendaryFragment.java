package com.example.karate_manager.Fragments;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.Toast;


import com.example.karate_manager.Adapters.AdapterResultsParticipantCalendary;
import com.example.karate_manager.MainActivity;
import com.example.karate_manager.Models.GroupModel.GroupResponse;
import com.example.karate_manager.Models.ResultsParticipant.ResultsParticipantResponse;
import com.example.karate_manager.Models.UserModel.UserResponse;
import com.example.karate_manager.Network.APIService;
import com.example.karate_manager.Network.ApiUtils;
import com.example.karate_manager.R;
import com.example.karate_manager.Utils.Storage;


import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalendaryFragment extends Fragment {


    UserResponse user = new UserResponse(200,null,null );
    int groupSelectedId = 0;
    private String[] seasons = {"1", "2", "3", "4", "5"};
    ListView resultByParticipantSeason;
    Button previous_season, season,following_season;
    private com.example.karate_manager.Network.APIService APIService;
    ResultsParticipantResponse resultsParticipantResponse = new ResultsParticipantResponse(200,null,null );
    AdapterResultsParticipantCalendary adapterResultsParticipantCalendary;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.fragment_calendary, container, false);
        previous_season = (Button) RootView.findViewById(R.id.previous_season);
        season = (Button) RootView.findViewById(R.id.season);
        following_season = (Button) RootView.findViewById(R.id.following_season);
        resultByParticipantSeason = (ListView) RootView.findViewById(R.id.resultByParticipantSeason);
        APIService = ApiUtils.getAPIService();


        adapterResultsParticipantCalendary = new AdapterResultsParticipantCalendary(getActivity().getApplicationContext(), R.layout.item_result_participant_calendary, resultsParticipantResponse.getResultPartChampGroup());
        previous_season.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        following_season.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        getResultByChampGroup(3, groupSelectedId);
        return RootView;
    }

    public void getResultByChampGroup(int id_championship, int id_group){
        Call<ResultsParticipantResponse> call = APIService.getResultByChampGroup(id_championship, id_group);
        call.enqueue(new Callback<ResultsParticipantResponse>() {
            @Override
            public void onResponse(Call<ResultsParticipantResponse> call, Response<ResultsParticipantResponse> response) {
                if(response.isSuccessful()){
                    resultsParticipantResponse = response.body();
                    Log.d("VALUE",String.valueOf(response.body().getResultPartChampGroup().get(0).getId_group()));


                    adapterResultsParticipantCalendary.notifyDataSetChanged();
                    adapterResultsParticipantCalendary.setData(resultsParticipantResponse);
                    resultByParticipantSeason.setAdapter(adapterResultsParticipantCalendary);
                }



            }

            @Override
            public void onFailure(Call<ResultsParticipantResponse> call, Throwable t) {
                Log.d("fail calendar", "eexxixisidekd");
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
