package com.example.karate_manager.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karate_manager.Adapters.AdapterBetsToRivals;
import com.example.karate_manager.Models.BidModel.BidToFromRivalsResponse;
import com.example.karate_manager.Models.BidModel.KaratekaRival;
import com.example.karate_manager.Network.APIService;
import com.example.karate_manager.Network.ApiUtils;
import com.example.karate_manager.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BetsToRivalsDialogFragment extends DialogFragment implements View.OnClickListener, AdapterBetsToRivals.ClickOnRefuseOwnBid {
    private  APIService APIService;
    int ownParticipant;
    ListView listview_bets_to_rivals;
    TextView main_text_bets_to_rivals,default_text_bets_to_rivals;
    AdapterBetsToRivals adapterBetsToRivals;
   // KaratekaRival karatekaRival = new KaratekaRival();
    BidToFromRivalsResponse bidToFromRivalsResponse = new BidToFromRivalsResponse();

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


        adapterBetsToRivals = new AdapterBetsToRivals(getContext(), R.layout.item_bet_to_rival, bidToFromRivalsResponse.getKaratekas(), this);
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
        Call<BidToFromRivalsResponse> call = APIService.myBidsToRivals(id_participant_bid_send);
        call.enqueue(new Callback<BidToFromRivalsResponse>() {
            @Override
            public void onResponse(Call<BidToFromRivalsResponse> call, Response<BidToFromRivalsResponse> response) {
              if(response.isSuccessful()){
                  bidToFromRivalsResponse = response.body();
                  adapterBetsToRivals.notifyDataSetChanged();
                  adapterBetsToRivals.setData(bidToFromRivalsResponse);
                  listview_bets_to_rivals.setAdapter(adapterBetsToRivals);


                  Log.d("Bieeeeen", "bieeeeen");

                  if(bidToFromRivalsResponse.getKaratekas().size()==0){
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
            public void onFailure(Call<BidToFromRivalsResponse> call, Throwable t) {
                Log.d("Mssssaaal", t.toString());
            }
        });
    }

    @Override
    public void onClick(KaratekaRival karatekaRival) {
        refuseOwnBid(ownParticipant, karatekaRival.getId());

        myBidsToRivals(ownParticipant);

        refreshFragmnet();
    }


    public void refuseOwnBid(int id_participant_bid_send,  int id_karatekas){
        Call<BidToFromRivalsResponse> call = APIService.refuseOwnBid(id_participant_bid_send, id_karatekas);
        call.enqueue(new Callback<BidToFromRivalsResponse>() {
            @Override
            public void onResponse(Call<BidToFromRivalsResponse> call, Response<BidToFromRivalsResponse> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getActivity(), "You have refused you own offer ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BidToFromRivalsResponse> call, Throwable t) {
                Log.d("Fail","Something is wrong");
            }
        });
    }

    public void refreshFragmnet(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false);
        }
        ft.detach(this).attach(this).commit();

    }
}
