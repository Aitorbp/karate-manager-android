package com.example.karate_manager.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.karate_manager.Adapters.AdapterBetsToRivals;
import com.example.karate_manager.Models.BidModel.BidToRivalsResponse;
import com.example.karate_manager.Models.BidModel.KaratekaRival;
import com.example.karate_manager.Network.APIService;
import com.example.karate_manager.Network.ApiUtils;
import com.example.karate_manager.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BetsToRivalsDialogFragment extends DialogFragment implements View.OnClickListener {
    private  APIService APIService;
    int ownParticipant;
    ListView listview_bets_to_rivals;
    TextView main_text_bets_to_rivals,default_text_bets_to_rivals;
    AdapterBetsToRivals adapterBetsToRivals;
   // KaratekaRival karatekaRival = new KaratekaRival();
    BidToRivalsResponse bidToRivalsResponse = new BidToRivalsResponse();

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        APIService = ApiUtils.getAPIService();

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.popup_bets_to_rivals, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

        Bundle mArgs = getArguments();
        ownParticipant = mArgs.getInt("idParticipantOwn");

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        adapterBetsToRivals = new AdapterBetsToRivals(getContext(), R.layout.item_bet_to_rival, bidToRivalsResponse.getKaratekas());
        listview_bets_to_rivals = (ListView) view.findViewById(R.id.listview_bets_to_rivals);
        main_text_bets_to_rivals = (TextView) view.findViewById(R.id.main_text_bets_to_rivals);
        default_text_bets_to_rivals = (TextView) view.findViewById(R.id.default_text_bets_to_rivals);

        view.findViewById(R.id.exit_popup_bets_to_rivals).setOnClickListener(this);
        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        myBidsToRivals(ownParticipant);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.exit_popup_bets_to_rivals:
                Log.d("EXIT", "PULSANDO");
                dismiss();
                break;

            default:
                break;
        }
    }

    private void myBidsToRivals(int id_participant_bid_send){
        Call<BidToRivalsResponse> call = APIService.myBidsToRivals(id_participant_bid_send);
        call.enqueue(new Callback<BidToRivalsResponse>() {
            @Override
            public void onResponse(Call<BidToRivalsResponse> call, Response<BidToRivalsResponse> response) {
              if(response.isSuccessful()){
                  bidToRivalsResponse = response.body();
                  adapterBetsToRivals.notifyDataSetChanged();
                  adapterBetsToRivals.setData(bidToRivalsResponse);
                  listview_bets_to_rivals.setAdapter(adapterBetsToRivals);
                  Log.d("Bieeeeen", "bieeeeen");

                  if(bidToRivalsResponse.getKaratekas().size()==0){
                      listview_bets_to_rivals.setVisibility(View.GONE);
                      main_text_bets_to_rivals.setVisibility(View.GONE);
                      default_text_bets_to_rivals.setVisibility(View.VISIBLE);

                  }else{
                      listview_bets_to_rivals.setVisibility(View.VISIBLE);
                      main_text_bets_to_rivals.setVisibility(View.VISIBLE);
                      default_text_bets_to_rivals.setVisibility(View.GONE);
                  }
                }else{
                  Log.d("Maaal", "maaal");
              }
            }

            @Override
            public void onFailure(Call<BidToRivalsResponse> call, Throwable t) {
                Log.d("Mssssaaal", t.toString());
            }
        });
    }

}
