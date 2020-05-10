package com.example.karate_manager.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.karate_manager.Adapters.AdapterRivalKaratekas;
import com.example.karate_manager.Models.KaratekaModel.MarketResponse;
import com.example.karate_manager.Models.ParticipantModel.ParticipantGroup;
import com.example.karate_manager.Network.APIService;
import com.example.karate_manager.Network.ApiUtils;
import com.example.karate_manager.R;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RivalDialogFragment extends DialogFragment {

    AdapterRivalKaratekas adapterRivalKaratekas;
    ParticipantGroup participantGroup = new ParticipantGroup();
    ApiUtils apiUtils;
    APIService APIService;
    int idParticipantOwn;
    MarketResponse karatekasRivalResponse = new MarketResponse(200,null,null );
    ListView listviewKaratekasRival;

    public RivalDialogFragment(ParticipantGroup participantGroup){this.participantGroup = participantGroup;}


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.popup_rival, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        APIService = ApiUtils.getAPIService();

        Bundle mArgs = getArguments();
        idParticipantOwn = mArgs.getInt("idParticipantOwn");
        listviewKaratekasRival = (ListView) view.findViewById(R.id.listview_karatekas_rival);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        adapterRivalKaratekas = new AdapterRivalKaratekas(getContext(), R.layout.item_rival_karateka, karatekasRivalResponse.getKaratekas());



        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();


        TextView nameRivalText = (TextView) getDialog().findViewById(R.id.rival_name);
        String nameKarateka = participantGroup.getName();
        nameRivalText.setText(nameKarateka);

        TextView pointsRivalText = (TextView) getDialog().findViewById(R.id.rival_points);
        String pointsRival = String.valueOf(participantGroup.getPoints());
        pointsRivalText.setText(pointsRival);

        ImageView pictureRivalImage = (ImageView) getDialog().findViewById(R.id.rival_picture);
        String pictureRival = String.valueOf(participantGroup.getPhoto_profile());
        if(pictureRival.isEmpty()){
            pictureRivalImage.setImageResource(R.drawable.default_image);
        }
        if(pictureRival == "null"){
            pictureRivalImage.setImageResource(R.drawable.default_image);
        }
        else{
            Picasso.get().load(apiUtils.BASE_URL_PICTURE + pictureRival).fit().into(pictureRivalImage);
        }
Log.d("Id rival", String.valueOf(participantGroup.getId()));
       getKaratekasByParticipant(participantGroup.getId());

    }



    private void getKaratekasByParticipant(int id_participants){
        Call<MarketResponse> call = APIService.getKaratekasByParticipant(id_participants);
        call.enqueue(new Callback<MarketResponse>() {
            @Override
            public void onResponse(Call<MarketResponse> call, Response<MarketResponse> response) {
                karatekasRivalResponse = response.body();
                adapterRivalKaratekas.notifyDataSetChanged();
                adapterRivalKaratekas.setData(karatekasRivalResponse);
                listviewKaratekasRival.setAdapter(adapterRivalKaratekas);
                Log.d("RESPONSE_SUCCESS", "response market by karateka done");

            }

            @Override
            public void onFailure(Call<MarketResponse> call, Throwable t) {

            }
        });

    }
}
