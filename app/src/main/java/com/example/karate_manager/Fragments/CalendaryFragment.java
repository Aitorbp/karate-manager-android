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
import android.widget.Button;
import android.widget.ListView;


import com.example.karate_manager.Adapters.AdapterResultsParticipantCalendary;
import com.example.karate_manager.Models.ChampionshipModel.ChampionshipResponse;
import com.example.karate_manager.Models.ResultsParticipant.ResultsParticipantResponse;
import com.example.karate_manager.Models.UserModel.UserResponse;
import com.example.karate_manager.Network.ApiUtils;
import com.example.karate_manager.R;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
    ChampionshipResponse championshipResponse = new ChampionshipResponse();
    int indexNextChamp;
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


        getAllChampionships();




       //
        return RootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        previous_season.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(indexNextChamp==0){
                    season.setText(championshipResponse.getChampionships().get(indexNextChamp).getName());
                    getResultByChampGroup(indexNextChamp, groupSelectedId);
                }else{
                    indexNextChamp--;
                    season.setText(championshipResponse.getChampionships().get(indexNextChamp).getName());
                    getResultByChampGroup(indexNextChamp, groupSelectedId);
                }

            }
        });

        following_season.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int lastIndex = championshipResponse.getChampionships().size() - 1;
                if(indexNextChamp == lastIndex){
                    season.setText(championshipResponse.getChampionships().get(indexNextChamp).getName());
                    getResultByChampGroup(indexNextChamp, groupSelectedId);
                }else {
                    indexNextChamp++;
                    season.setText(championshipResponse.getChampionships().get(indexNextChamp).getName());
                    getResultByChampGroup(indexNextChamp, groupSelectedId);
                }
            }
        });

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

    public void getAllChampionships(){
        Call<ChampionshipResponse> call = APIService.getAllChampionships();
        call.enqueue(new Callback<ChampionshipResponse>() {
            @Override
            public void onResponse(Call<ChampionshipResponse> call, Response<ChampionshipResponse> response) {
                if(response.isSuccessful()){
                    championshipResponse =response.body();

                    convertStringToDate(championshipResponse);
                    Date nearestDate = getNearestDate(convertStringToDate(championshipResponse));
                    String nearestDateString =   convertDateToString(nearestDate);


                  //  Date currentDay = Calendar.getInstance().getTime();;
                  //  Log.d("Current day", String.valueOf(currentDay));



                    for (int i = 0; i < championshipResponse.getChampionships().size(); i++) {
                        if(championshipResponse.getChampionships().get(i).getStart_day().equals(nearestDateString)){
                            season.setText(championshipResponse.getChampionships().get(i).getName());

                            indexNextChamp = i;

                            getResultByChampGroup(indexNextChamp, groupSelectedId);
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<ChampionshipResponse> call, Throwable t) {

            }
        });
    }

    public ArrayList convertStringToDate(ChampionshipResponse championshipResponse){
        ArrayList<Date> dates= new ArrayList<Date>();
        for (int i = 0; i < championshipResponse.getChampionships().size(); i++) {
            String dateChampionship = championshipResponse.getChampionships().get(i).getStart_day();
            Log.d("date in calendary", dateChampionship);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date date = format.parse(dateChampionship);
                dates.add(date);

                //Log.d("yalaaaaaaa", String.valueOf(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return dates;
    }

    public String convertDateToString(Date nearestDate){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nearestDateString;
        nearestDateString = dateFormat.format(nearestDate);
        System.out.println("Current Date Time : " + nearestDateString);
        return nearestDateString;
    }
    public Date getNearestDate(ArrayList<Date> dates) {
        Date targetDate = new Date();
        Date nearestDate = null;
        int index = 0;
        long prevDiff = -1;
        long targetTS = targetDate.getTime();
        for (int i = 0; i < dates.size(); i++) {
            Date date = dates.get(i);
            long currDiff = Math.abs(date.getTime() - targetTS);
            if (prevDiff == -1 || currDiff < prevDiff) {
                prevDiff = currDiff;
                nearestDate = date;
                index = i;
            }
        }
     //   System.out.println("Nearest Date: " + nearestDate);
        System.out.println("Index: " + index);
        return nearestDate;
    }
    public void recievedUserGroup(UserResponse userResponse, int groupSelected) {
        if(user!=null ){
            user =userResponse;
            groupSelectedId = groupSelected;
        }
    }
}
