package com.example.karate_manager.Fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.karate_manager.Adapters.AdapterListviewChangeStartingKarateka;
import com.example.karate_manager.Adapters.AdapterMarket;
import com.example.karate_manager.Adapters.AdapterMyTeam;
import com.example.karate_manager.Adapters.AdapterStartingKarateka;
import com.example.karate_manager.DialogFragment.ChoosingKaratekaStartingDialog;
import com.example.karate_manager.Models.KaratekaModel.Karateka;
import com.example.karate_manager.Models.KaratekaModel.MarketResponse;
import com.example.karate_manager.Models.ParticipantModel.ParticipantResponse;
import com.example.karate_manager.Models.UserModel.UserResponse;
import com.example.karate_manager.Network.APIService;
import com.example.karate_manager.Network.ApiUtils;
import com.example.karate_manager.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyTeamFragment extends Fragment implements AdapterMyTeam.ClickOnSell {

    AdapterStartingKarateka adapterStartingKarateka;
    AdapterMyTeam adapterMyTeam;
    ListView listViewMyTeam;
    GridView gridViewMyTeam;
    ArrayList<Karateka> startingKaratekas = new ArrayList<>();
    UserResponse user = new UserResponse(200,null,null );
    int groupSelectedId = 0;
    private APIService APIService;
    MarketResponse myTeamResponse = new MarketResponse(200,null,null );

    int idParticipant;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.fragment_my_team, container, false);
        listViewMyTeam = (ListView) RootView.findViewById(R.id.myteam_listview);
        gridViewMyTeam = (GridView) RootView.findViewById(R.id.myteam_gridview);
        APIService = ApiUtils.getAPIService();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        adapterMyTeam = new AdapterMyTeam(getActivity().getApplicationContext(), R.layout.item_myteam_layout, myTeamResponse.getKaratekas(), fragmentManager, this);
        adapterStartingKarateka = new AdapterStartingKarateka(getActivity().getApplicationContext(), R.layout.item_card_karateka_myteam, startingKaratekas);


        createDefaultStartingKaratekas(startingKaratekas);



        gridViewMyTeam.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                int idKarateka = (int) startingKaratekas.get(i).getId();

                Log.d("IndexGrid", String.valueOf(i));
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                ChoosingKaratekaStartingDialog choosingKaratekaStartingDialog = new ChoosingKaratekaStartingDialog();
                Bundle args = new Bundle();
                args.putInt("idUser", user.getUser().getId());
                args.putInt("idGroup", groupSelectedId);
                args.putInt("idParticipant", idParticipant);
                args.putInt("idKarateka", idKarateka);
                args.putInt("indexGrid", i);
                choosingKaratekaStartingDialog.setArguments(args);
                choosingKaratekaStartingDialog.show(fragmentManager, "karateka" );
            }

        });

        getParticipantByGroupAndUser(user.getUser().getId(), groupSelectedId);
        gridViewMyTeam.setAdapter(adapterStartingKarateka);


        return RootView;
    }

    public void changeKarateka(ArrayList<Karateka> startingKaratekas, int indexGrid, Karateka karateka){
       startingKaratekas.set(indexGrid, karateka);
    }

    public void createDefaultStartingKaratekas(ArrayList<Karateka> startingKaratekas){
        for (int i = 0; i < 8 ; i++) {
            Karateka startingKarateka = new Karateka(1, "", "", 1,"-67" ,0, null, null);
            startingKaratekas.add(startingKarateka);

        }
    }

    public void getParticipantByGroupAndUser(int idUser, int idGroup){

        Call<ParticipantResponse> call = APIService.getParticipant(idUser,idGroup);
        call.enqueue(new Callback<ParticipantResponse>() {
            @Override
            public void onResponse(Call<ParticipantResponse> call, Response<ParticipantResponse> response) {
                if (response.isSuccessful()) {

                    Log.d("PARTICIPANT",String.valueOf(response.body().getParticipants().get(0).getId()));
                     idParticipant =response.body().getParticipants().get(0).getId();
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



    @Override
    public void onClick(Karateka karateka) {


        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        AdapterMyTeam.SellKaratekaDialogFragment sellKaratekaDialogFragment = new AdapterMyTeam.SellKaratekaDialogFragment(karateka);
        Bundle args = new Bundle();
        args.putInt("idUser", user.getUser().getId());
        args.putInt("idGroup", groupSelectedId);
        sellKaratekaDialogFragment.setArguments(args);
        sellKaratekaDialogFragment.show(fragmentManager, "sell");
    }




}
