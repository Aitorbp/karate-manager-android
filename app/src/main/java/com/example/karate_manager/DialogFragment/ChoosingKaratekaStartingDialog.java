package com.example.karate_manager.DialogFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.karate_manager.Adapters.AdapterListviewChangeStartingKarateka;
import com.example.karate_manager.Fragments.MyTeamFragment;
import com.example.karate_manager.MainActivity;
import com.example.karate_manager.Models.KaratekaModel.Karateka;
import com.example.karate_manager.Models.KaratekaModel.MarketResponse;
import com.example.karate_manager.Models.ParticipantModel.ParticipantResponse;
import com.example.karate_manager.Network.APIService;
import com.example.karate_manager.Network.ApiUtils;
import com.example.karate_manager.R;

import java.util.prefs.Preferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChoosingKaratekaStartingDialog extends DialogFragment implements AdapterListviewChangeStartingKarateka.ClickOnChangeKarteka, View.OnClickListener {

    private  APIService APIService;
    ListView listViewKartekasStarting;
    AdapterListviewChangeStartingKarateka adapterListviewChangeStartingKarateka;
    int idUser;
    int idGroup;
    int idParticipant;
    int idKarateka;
    int indexGrid;
    Karateka karateka = new Karateka(0,null,null,0, null,0,null,null);
    MarketResponse choosemKaratekaResponse = new MarketResponse(200,null,null );



public ChoosingKaratekaStartingDialog(Karateka karateka){this.karateka = karateka;}
public ChoosingKaratekaStartingDialog(){}

    @RequiresApi(api = Build.VERSION_CODES.M)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        APIService = ApiUtils.getAPIService();

        Bundle mArgs = getArguments();
        idUser = mArgs.getInt("idUser");
        idGroup = mArgs.getInt("idGroup");
        idParticipant = mArgs.getInt("idParticipant");
        idKarateka = mArgs.getInt("idKarateka");
        indexGrid = mArgs.getInt("indexGrid");


        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.popup_change_karateka_starting, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        adapterListviewChangeStartingKarateka = new AdapterListviewChangeStartingKarateka(getContext(), R.layout.item_change_karateka, choosemKaratekaResponse.getKaratekas(), fragmentManager, this );
        listViewKartekasStarting =  (ListView) view.findViewById(R.id.starting_listview);
        getKaratekasAlternateByParticipant(idParticipant);

         view.findViewById(R.id.exit_popup_change_karateka).setOnClickListener(this);
      //  getParticipantByGroupAndUser( idUser,  idGroup);





        return builder.create();
    }



private void getKaratekasAlternateByParticipant(int id_participants){
    Call<MarketResponse> call = APIService.getAlternateKaratekaByParticipant(id_participants);
    call.enqueue(new Callback<MarketResponse>() {
        @Override
        public void onResponse(Call<MarketResponse> call, Response<MarketResponse> response) {
            choosemKaratekaResponse = response.body();
            adapterListviewChangeStartingKarateka.notifyDataSetChanged();
            adapterListviewChangeStartingKarateka.setData(choosemKaratekaResponse);
            listViewKartekasStarting.setAdapter(adapterListviewChangeStartingKarateka);
        }

        @Override
        public void onFailure(Call<MarketResponse> call, Throwable t) {

        }
    });
}


    @Override
    public void onClick(Karateka karateka) {

    //Hacer aqui llamada a la api para que cambie uno por el otro


        dismiss();
        Intent intent = new Intent();

        intent.putExtra("karatekaChanged",  karateka);
        getTargetFragment().onActivityResult(
                getTargetRequestCode(), Activity.RESULT_OK, intent);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.exit_popup_change_karateka:
                Log.d("EXIT", "PULSANDO");
                dismiss();
                break;

            default:
                break;
        }
    }
}
