package com.example.karate_manager.DialogFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karate_manager.Models.KaratekaModel.Karateka;
import com.example.karate_manager.Models.ParticipantModel.ParticipantResponse;
import com.example.karate_manager.Network.APIService;
import com.example.karate_manager.Network.ApiUtils;
import com.example.karate_manager.R;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SellKaratekaDialogFragment extends DialogFragment implements View.OnClickListener {
    Karateka karateka = new Karateka(0,null,null,0, null,0,null,null);
    ApiUtils apiUtils;
    private com.example.karate_manager.Network.APIService APIService;
    int idUser;
    int idGroup;
    int idKarateka;

    public SellKaratekaDialogFragment(Karateka karateka){this.karateka = karateka;}
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        APIService = ApiUtils.getAPIService();

        Bundle mArgs = getArguments();
        idUser = mArgs.getInt("idUser");
        idGroup = mArgs.getInt("idGroup");
        Log.d("Name karateka in myteam", String.valueOf(karateka.getName()));

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.popup_sell_karateka, null);
        view.findViewById(R.id.sell_button_no).setOnClickListener(this);
        view.findViewById(R.id.sell_button_yes).setOnClickListener(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

        return builder.create();
    }


    @Override
    public void onStart() {
        super.onStart();
        TextView nameKaratekaText = (TextView) getDialog().findViewById(R.id.sell_name_karateka);
        String nameKarateka = karateka.getName();
        idKarateka = karateka.getId();
        nameKaratekaText.setText(nameKarateka);

        TextView valueKaratekaText = (TextView) getDialog().findViewById(R.id.sell_value_karateka);
        String valueKarateka = String.valueOf(karateka.getValue());
        valueKaratekaText.setText(valueKarateka.replace(".0", ""));

        ImageView pictureKaratekaImage = (ImageView) getDialog().findViewById(R.id.sell_picture_karateka);
        String pictureKarateka = String.valueOf(karateka.getPhoto_karateka());
        if(pictureKarateka.isEmpty() || pictureKarateka == null){
            pictureKaratekaImage.setImageResource(R.drawable.default_image);
        }else{
            Picasso.get().load(apiUtils.BASE_URL_PICTURE + pictureKarateka).fit().into(pictureKaratekaImage);
        }

        TextView weightKaratekaText = (TextView) getDialog().findViewById(R.id.sell_weigth);
        String weigthKarateka = String.valueOf(karateka.getWeight());
        weightKaratekaText.setText(weigthKarateka);

        ImageView pictureCountryKaratekaImage = (ImageView) getDialog().findViewById(R.id.sell_country);
        String countryKarateka = String.valueOf(karateka.getPhoto_country());
        if(countryKarateka.isEmpty() || countryKarateka == null){
            pictureKaratekaImage.setImageResource(R.drawable.default_image);
        }else{
            Picasso.get().load(apiUtils.BASE_URL_PICTURE + countryKarateka).fit().into(pictureCountryKaratekaImage);
        }

        TextView elementPointsKarateka =  (TextView) getDialog().findViewById(R.id.sell_points_karateka);
        String pointsKarateka = String.valueOf(karateka.getPoints_karateka());
        if(pointsKarateka.isEmpty() || pointsKarateka==null){
            elementPointsKarateka.setText("0");
        }else{
            elementPointsKarateka.setText(pointsKarateka);

        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sell_button_yes:
                Log.d("PULSANDO", "PULSANDO");
                getParticipantByGroupAndUser(idUser, idGroup);
                Intent intent = new Intent();
                getTargetFragment().onActivityResult(
                        getTargetRequestCode(), Activity.RESULT_OK, intent);
                break;

            case R.id.sell_button_no:
                dismiss();
                break;


            default:
                break;
        }
    }

    public void getParticipantByGroupAndUser(int idUser, int idGroup){

        Call<ParticipantResponse> call = APIService.getParticipant(idUser,idGroup);
        call.enqueue(new Callback<ParticipantResponse>() {
            @Override
            public void onResponse(Call<ParticipantResponse> call, Response<ParticipantResponse> response) {
                if (response.isSuccessful()) {

                    Log.d("BID PARTICIPANT",String.valueOf(response.body().getParticipants().get(0).getId()));
                    int idParticipant =response.body().getParticipants().get(0).getId();

                    int idGroup = response.body().getParticipants().get(0).getId_group();

                    sellKaratekaByParticipant(idParticipant, idGroup, idKarateka);

                }
            }

            @Override
            public void onFailure(Call<ParticipantResponse> call, Throwable t) {

                Log.d("RESPONSE_FAILURE", String.valueOf(t));
            }
        });
    }
    public void sellKaratekaByParticipant(int idParticipant, int idGroup, int idKarateka){
        Call<ParticipantResponse> call = APIService.sellKaratekaByParticipant(idParticipant, idGroup, idKarateka);
        call.enqueue(new Callback<ParticipantResponse>() {
            @Override
            public void onResponse(Call<ParticipantResponse> call, Response<ParticipantResponse> response) {
                Log.d("Sell done!!!!", "Sell doneeeee!");

                Toast.makeText(getActivity(), "You have sold this karateka ", Toast.LENGTH_SHORT).show();
                dismiss();

            }

            @Override
            public void onFailure(Call<ParticipantResponse> call, Throwable t) {

            }
        });
    }
}