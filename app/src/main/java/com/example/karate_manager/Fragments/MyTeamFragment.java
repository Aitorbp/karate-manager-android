package com.example.karate_manager.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.karate_manager.Adapters.AdapterMarket;
import com.example.karate_manager.Adapters.AdapterMyTeam;
import com.example.karate_manager.Models.KaratekaModel.MarketResponse;
import com.example.karate_manager.Models.ParticipantModel.ParticipantResponse;
import com.example.karate_manager.Models.UserModel.UserResponse;
import com.example.karate_manager.Network.APIService;
import com.example.karate_manager.Network.ApiUtils;
import com.example.karate_manager.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyTeamFragment extends Fragment {


    AdapterMyTeam adapterMyTeam;
    ListView listViewMyTeam;
    UserResponse user = new UserResponse(200,null,null );
    int groupSelectedId = 0;
    private APIService APIService;
    MarketResponse myTeamResponse = new MarketResponse(200,null,null );


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.fragment_my_team, container, false);
        listViewMyTeam = (ListView) RootView.findViewById(R.id.myteam_listview);
        APIService = ApiUtils.getAPIService();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        adapterMyTeam = new AdapterMyTeam(getActivity().getApplicationContext(), R.layout.item_myteam_layout, myTeamResponse.getKaratekas());

        getParticipantByGroupAndUser(user.getUser().getId(), groupSelectedId);



        return RootView;
    }

    public void getParticipantByGroupAndUser(int idUser, int idGroup){

        Call<ParticipantResponse> call = APIService.getParticipant(idUser,idGroup);
        call.enqueue(new Callback<ParticipantResponse>() {
            @Override
            public void onResponse(Call<ParticipantResponse> call, Response<ParticipantResponse> response) {
                if (response.isSuccessful()) {

                    Log.d("PARTICIPANT",String.valueOf(response.body().getParticipants().get(0).getId()));
                    int idParticipant =response.body().getParticipants().get(0).getId();
                    getKaratekasByParticipant(String.valueOf(idParticipant));
                    Log.d("RESPONSE_SUCCESS", "Everything All right");

                }
            }

            @Override
            public void onFailure(Call<ParticipantResponse> call, Throwable t) {

                Log.d("RESPONSE_FAILURE", String.valueOf(t));
            }
        });
    }
    private void getKaratekasByParticipant(String id_participants){
        Call<MarketResponse> call = APIService.getKaratekasByParticipant(Integer.parseInt(id_participants));
        call.enqueue(new Callback<MarketResponse>() {
       @Override
       public void onResponse(Call<MarketResponse> call, Response<MarketResponse> response) {
           myTeamResponse = response.body();
           adapterMyTeam.notifyDataSetChanged();
           adapterMyTeam.setData(myTeamResponse);
           listViewMyTeam.setAdapter(adapterMyTeam);
           Log.d("RESPONSE_SUCCESS", "response market by karateka done");

       }

       @Override
       public void onFailure(Call<MarketResponse> call, Throwable t) {

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
