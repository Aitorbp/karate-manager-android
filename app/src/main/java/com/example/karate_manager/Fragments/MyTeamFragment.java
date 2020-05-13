package com.example.karate_manager.Fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.karate_manager.Adapters.AdapterBetsFromRivals;
import com.example.karate_manager.Adapters.AdapterMyTeam;
import com.example.karate_manager.Adapters.AdapterStartingKarateka;
import com.example.karate_manager.DialogFragment.ChoosingKaratekaStartingDialog;
import com.example.karate_manager.DialogFragment.SellKaratekaDialogFragment;
import com.example.karate_manager.Models.BidModel.BidToFromRivalsResponse;
import com.example.karate_manager.Models.KaratekaModel.Karateka;
import com.example.karate_manager.Models.KaratekaModel.MarketResponse;
import com.example.karate_manager.Models.ParticipantModel.ParticipantResponse;
import com.example.karate_manager.Models.SaleModel.StartingResponse;
import com.example.karate_manager.Models.UserModel.UserResponse;
import com.example.karate_manager.Network.APIService;
import com.example.karate_manager.Network.ApiUtils;
import com.example.karate_manager.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyTeamFragment extends Fragment implements AdapterMyTeam.ClickOnSell {

    AdapterBetsFromRivals adapterBetsFromRivals;
    AdapterStartingKarateka adapterStartingKarateka;
    AdapterMyTeam adapterMyTeam;
    ListView listViewMyTeam, listviewBetsFromRivals;
    GridView gridViewMyTeam;
    ArrayList<Karateka>    startingKaratekas = new ArrayList<>();
    UserResponse user = new UserResponse(200,null,null );
    int groupSelectedId = 0;
    private APIService APIService;
    MarketResponse myTeamResponse = new MarketResponse(200,null,null );

    MarketResponse myStartingResponse = new MarketResponse(200,null,null );
    Karateka karateka = new Karateka(0,null,null,0, null,0,null,null);
    LinearLayout card_karateka;
    LinearLayout card_default;
    final int CODIGO_REQUEST_CHANGE_KARATEKA = 1;
    final int CODIGO_REQUEST_SELL_KARATEKA = 2;
    int idParticipant;
    int indexGrid;
    int idKarateka;
 Button add_new_karateka_starting;
    BidToFromRivalsResponse bidToFromRivalsResponse = new BidToFromRivalsResponse();
    TabLayout tabs;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.fragment_my_team, container, false);


        listViewMyTeam = (ListView) RootView.findViewById(R.id.myteam_listview);
        listviewBetsFromRivals = (ListView) RootView.findViewById(R.id.bet_from_rivals_listview);
        gridViewMyTeam = (GridView) RootView.findViewById(R.id.myteam_gridview);
        APIService = ApiUtils.getAPIService();



        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        adapterMyTeam = new AdapterMyTeam(getActivity().getApplicationContext(), R.layout.item_myteam_layout, myTeamResponse.getKaratekas(), fragmentManager, this);
        adapterStartingKarateka = new AdapterStartingKarateka(getActivity().getApplicationContext(), R.layout.item_card_karateka_myteam, myStartingResponse.getKaratekas());

        adapterBetsFromRivals = new AdapterBetsFromRivals(getActivity(), R.layout.item_bet_from_rival, bidToFromRivalsResponse.getKaratekas());

        card_karateka = (LinearLayout) RootView.findViewById(R.id.card_karateka);
        card_default = (LinearLayout) RootView.findViewById(R.id.card_default);
        add_new_karateka_starting = (Button ) RootView.findViewById(R.id.add_new_karateka_starting);
        tabs = (TabLayout) RootView.findViewById((R.id.tabs));

        getParticipantByGroupAndUser(user.getUser().getId(), groupSelectedId);


    tabs.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            switch (tabs.getSelectedTabPosition()) {
                case 0:
                    listViewMyTeam.setVisibility(View.VISIBLE);
                    listviewBetsFromRivals.setVisibility(View.GONE);

                    break;

                case 1:
                    listViewMyTeam.setVisibility(View.GONE);
                    listviewBetsFromRivals.setVisibility(View.VISIBLE);

                    break;


            }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
});




        gridViewMyTeam.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                 idKarateka = (int) myStartingResponse.getKaratekas().get(i).getId();
                showDialogFragment(idKarateka,i);


            }

        });



        return RootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        add_new_karateka_starting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(myStartingResponse.getKaratekas().size()>= 8){
                    Toast.makeText(getContext(), "You cannot add more karatekas to the starting team", Toast.LENGTH_SHORT).show();
                    Log.d("Pulsando", "jdwnjdjnwjdwjdjwdnjw");
                }else{
                    showDialogWithZeroStartingFragment();
                }
            }
        });


    }


    //El onactivityresult trae la información que señalamos del adappter del dialogfragment
    // para ello necesitamos un  choosingKaratekaStartingDialog.setTargetFragment(this,1); en el métido de apertura del fragmentDialog
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case CODIGO_REQUEST_CHANGE_KARATEKA:
                if (resultCode == Activity.RESULT_OK) {
                    if(data!=null){
                        // set value to your TextView
                        Karateka karatekaChanged = (Karateka) data.getSerializableExtra("karatekaChanged");

                       int  idKaratekaChanged = (int) karatekaChanged.getId();

                        if(myStartingResponse.getKaratekas().size() >= 8){
                             postAlternateKarateka(idParticipant, idKarateka);
                             postStartingKarateka(idParticipant, idKaratekaChanged);

                        }else{
                        postStartingKarateka(idParticipant, idKaratekaChanged);
                        Log.d("Entrada en <3", "kekekkekekek");
                        }

                        getStartingKaratekaByParticipant(idParticipant);


                        refreshFragmnet();
                        Log.d("idKaratekaChangeaaa", String.valueOf(karatekaChanged.getId()));


                    }
                }
                break;
            case CODIGO_REQUEST_SELL_KARATEKA:
                if (resultCode == Activity.RESULT_OK) {

                    getParticipantByGroupAndUser(user.getUser().getId(), groupSelectedId);
                    Log.d("Devuelta","devuelta");
                    refreshFragmnet();
                }

                break;
        }
    }



public void refreshFragmnet(){
    FragmentTransaction ft = getFragmentManager().beginTransaction();
    if (Build.VERSION.SDK_INT >= 26) {
        ft.setReorderingAllowed(false);
    }
    ft.detach(this).attach(this).commit();

}
    private void showDialogFragment(int idKarateka, int i){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        ChoosingKaratekaStartingDialog choosingKaratekaStartingDialog = new ChoosingKaratekaStartingDialog();
        Bundle args = new Bundle();
        args.putInt("idUser", user.getUser().getId());
        args.putInt("idGroup", groupSelectedId);
        args.putInt("idParticipant", idParticipant);
        args.putInt("idKarateka", idKarateka);
        args.putInt("indexGrid", i);
        indexGrid= i;

        choosingKaratekaStartingDialog.setArguments(args);
        choosingKaratekaStartingDialog.setTargetFragment(this,1);
        choosingKaratekaStartingDialog.show(fragmentManager, "karateka" );
    }
    private void showDialogWithZeroStartingFragment(){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        ChoosingKaratekaStartingDialog choosingKaratekaStartingDialog = new ChoosingKaratekaStartingDialog();
        Bundle args = new Bundle();
        args.putInt("idUser", user.getUser().getId());
        args.putInt("idGroup", groupSelectedId);
        args.putInt("idParticipant", idParticipant);
        args.putInt("idKarateka", idKarateka);
        args.putInt("indexGrid", 0);


        choosingKaratekaStartingDialog.setArguments(args);
        choosingKaratekaStartingDialog.setTargetFragment(this,CODIGO_REQUEST_CHANGE_KARATEKA);
        choosingKaratekaStartingDialog.show(fragmentManager, "karateka" );
    }

    public void postAlternateKarateka(int id_participants, int id_karatekas){
        Call<StartingResponse> call = APIService.postAlternateKarateka(id_participants, id_karatekas);
        call.enqueue(new Callback<StartingResponse>() {
            @Override
            public void onResponse(Call<StartingResponse> call, Response<StartingResponse> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getContext(), "Karateka added", Toast.LENGTH_SHORT).show();

                }else {
                    Log.d("Error alternate", "some error");
                }

            }

            @Override
            public void onFailure(Call<StartingResponse> call, Throwable t) {

            }
        });
    }

    public void postStartingKarateka(int id_participants, int id_karatekas){
        Call<StartingResponse> call = APIService.postStartingKarateka(id_participants, id_karatekas);
        call.enqueue(new Callback<StartingResponse>() {
            @Override
            public void onResponse(Call<StartingResponse> call, Response<StartingResponse> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getContext(), "Karateka added", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<StartingResponse> call, Throwable t) {

            }
        });
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
                    getStartingKaratekaByParticipant(idParticipant);
                    myBidsToRivals(idParticipant);
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

    private void getStartingKaratekaByParticipant(int id_participant){
        Call<MarketResponse> call =  APIService.getStartingKaratekaByParticipant(id_participant);
        call.enqueue(new Callback<MarketResponse>() {
            @Override
            public void onResponse(Call<MarketResponse> call, Response<MarketResponse> response) {
              if(response.isSuccessful()){
                  myStartingResponse = response.body();
                  adapterStartingKarateka.notifyDataSetChanged();
                  adapterStartingKarateka.setData(myStartingResponse);
                  gridViewMyTeam.setAdapter(adapterStartingKarateka);


                  if(myStartingResponse.getKaratekas().size() == 0){

                                      showDialogWithZeroStartingFragment();

                      Log.d("Starting empty", "Starting emptyy");
                  }else {Log.d("Starting not empty", "Starting not emptyy");}



              }else{
                  Log.d("Failllll", "jajajjajaja");
              }

            }

            @Override
            public void onFailure(Call<MarketResponse> call, Throwable t) {

            }
        });
    }


    private void myBidsToRivals(int id_participant_bid_receive){
        Call<BidToFromRivalsResponse> call = APIService.myBidsRecieveFromToRivals(id_participant_bid_receive);
        call.enqueue(new Callback<BidToFromRivalsResponse>() {
            @Override
            public void onResponse(Call<BidToFromRivalsResponse> call, Response<BidToFromRivalsResponse> response) {
                if(response.isSuccessful()){
                    bidToFromRivalsResponse = response.body();

                    //bidToFromRivalsResponse.getKaratekas().get(0);
                    adapterBetsFromRivals.notifyDataSetChanged();
                    adapterBetsFromRivals.setData(bidToFromRivalsResponse);
                    listviewBetsFromRivals.setAdapter(adapterBetsFromRivals);
                    Log.d("YUUUUJUU", "bieeeeen");


                }else{
                    Log.d("Maaal", "maaal");
                }
            }

            @Override
            public void onFailure(Call<BidToFromRivalsResponse> call, Throwable t) {
                Log.d("Mssssaaal", t.toString());
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
        SellKaratekaDialogFragment sellKaratekaDialogFragment = new  SellKaratekaDialogFragment(karateka);
        Bundle args = new Bundle();
        args.putInt("idUser", user.getUser().getId());
        args.putInt("idGroup", groupSelectedId);
        sellKaratekaDialogFragment.setArguments(args);
        sellKaratekaDialogFragment.setTargetFragment(this, CODIGO_REQUEST_SELL_KARATEKA);
        sellKaratekaDialogFragment.show(fragmentManager, "sell");
    }




}
