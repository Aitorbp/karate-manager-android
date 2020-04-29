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

import com.example.karate_manager.Adapters.AdapterMarket;
import com.example.karate_manager.Adapters.AdapterScoring;
import com.example.karate_manager.Models.KaratekaModel.MarketResponse;
import com.example.karate_manager.Models.ParticipantModel.ParticipantResponse;
import com.example.karate_manager.Models.UserModel.UserResponse;
import com.example.karate_manager.Network.APIService;
import com.example.karate_manager.Network.ApiUtils;
import com.example.karate_manager.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MarketFragment extends Fragment {

    AdapterMarket adapterMarket;
    ListView listViewMarket;
    int groupSelectedId = 0;
    private APIService APIService;
    UserResponse user = new UserResponse(200,null,null );
    MarketResponse marketResponse = new MarketResponse(200,null,null );


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.fragment_market, container, false);
        listViewMarket = (ListView) RootView.findViewById(R.id.market_listview);
        APIService = ApiUtils.getAPIService();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        adapterMarket = new AdapterMarket(getActivity().getApplicationContext(), R.layout.item_market_layout, marketResponse.getKaratekas(), fragmentManager);

        Log.d("User in Scoring", user.getUser().getName());


        getKaratekasInSaleByGroup(String.valueOf(groupSelectedId));
        Log.d("Group in market ", String.valueOf(groupSelectedId));
        return RootView;
    }

    private void getKaratekasInSaleByGroup(String id_group) {

        Call<MarketResponse> call = APIService.getKaratekasInSaleByGroup(id_group);
        call.enqueue(new Callback<MarketResponse>() {
            @Override
            public void onResponse(Call<MarketResponse> call, Response<MarketResponse> response) {
                if (response.isSuccessful()) {
                    marketResponse = response.body();
                    //      Log.d("VALUE",String.valueOf(response.body().getParticipants().get(1).getId()));

                    adapterMarket.notifyDataSetChanged();
                    adapterMarket.setData(marketResponse);
                    listViewMarket.setAdapter(adapterMarket);
                    Log.d("RESPONSE_SUCCESS", "response market by karateka done");

                }
            }

            @Override
            public void onFailure(Call<MarketResponse> call, Throwable t) {

            }
        });
    }


    public void recievedUserGroup(UserResponse userResponse, int groupSelected) {
        if(user!=null ){
            user = userResponse;
            groupSelectedId = groupSelected;
        }
    }
}
