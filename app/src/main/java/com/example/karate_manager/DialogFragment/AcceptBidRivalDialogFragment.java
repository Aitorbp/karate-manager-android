package com.example.karate_manager.DialogFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karate_manager.Fragments.MyTeamFragment;
import com.example.karate_manager.Models.BidModel.BidToFromRivalsResponse;
import com.example.karate_manager.Models.BidModel.KaratekaRival;
import com.example.karate_manager.Network.ApiUtils;
import com.example.karate_manager.R;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AcceptBidRivalDialogFragment extends DialogFragment implements View.OnClickListener {

    private com.example.karate_manager.Network.APIService APIService;
    ApiUtils apiUtils;

    KaratekaRival karatekaRival = new KaratekaRival();
    String image;
    String imageCountry;
    String pointsKarateka;

    public AcceptBidRivalDialogFragment(KaratekaRival karatekaRival) {
        this.karatekaRival =karatekaRival;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        APIService = ApiUtils.getAPIService();

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.popup_accept_bid_rival, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

         image = karatekaRival.getPhoto_karateka();
         imageCountry = karatekaRival.getPhoto_country();
         pointsKarateka = String.valueOf(karatekaRival.getPoints_karateka());

        view.findViewById(R.id.accept_button_yes).setOnClickListener(this);
        view.findViewById(R.id.accept_button_no).setOnClickListener(this);
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("KaratekaRivalAccepted", String.valueOf(karatekaRival.getId()));
        TextView nameKaratekaTexview, weightKaratekaTexview, pointsKaratekaTexview , bidRival, bidRivalTexview ;

        bidRivalTexview =  (TextView) getDialog().findViewById(R.id.accept_rival_bid);
        nameKaratekaTexview = (TextView) getDialog().findViewById(R.id.accept_name_karateka);
        weightKaratekaTexview = (TextView) getDialog().findViewById(R.id.accept_weigth);
        pointsKaratekaTexview = (TextView) getDialog().findViewById(R.id.accept_points_karateka);


        nameKaratekaTexview.setText(karatekaRival.getName());
        bidRivalTexview.setText(String.valueOf(karatekaRival.getBid_rival()));
        weightKaratekaTexview.setText(karatekaRival.getWeight());


        ImageView pictureKaratekaImage = (ImageView) getDialog().findViewById(R.id.accept_picture_karateka);
        ImageView pictureCountryKaratekaImage = (ImageView) getDialog().findViewById(R.id.accept_country);

        if(pointsKarateka.isEmpty() || pointsKarateka== "null"){
            pointsKaratekaTexview.setText("0");
        }else{
            pointsKaratekaTexview.setText(pointsKarateka);

        }


        if(image.isEmpty() || image == null){
            pictureKaratekaImage.setImageResource(R.drawable.default_image);
        }else{
            Picasso.get().load(apiUtils.BASE_URL_PICTURE + image).fit().into(pictureKaratekaImage);
        }
        if(imageCountry.isEmpty() || imageCountry == "null"){
            pictureCountryKaratekaImage.setImageResource(R.drawable.default_image);
        }else{
            Picasso.get().load(apiUtils.BASE_URL_PICTURE + imageCountry).fit().into(pictureCountryKaratekaImage);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.accept_button_yes:
                Log.d("ENTER", "PULSANDO");
                int id_participant_bid_send= karatekaRival.getId_participant_bid_send();
                int id_participant_bid_receive= karatekaRival.getId_participant_bid_receive();
                int id_karateka= karatekaRival.getId();
                 acceptBitRival(id_participant_bid_send, id_participant_bid_receive, id_karateka);
                Toast.makeText(getActivity(), "You have accepted the bet", Toast.LENGTH_LONG).show();

                dismiss();
                Intent intent = new Intent();
                getTargetFragment().onActivityResult(
                        getTargetRequestCode(), Activity.RESULT_OK, intent);

                break;
            case R.id.accept_button_no:
                Log.d("EXIT", "PULSANDO");
                dismiss();
                break;

            default:
                break;
        }
    }


    public void acceptBitRival(int id_participant_bid_send, int id_participant_bid_receive, int id_karateka){
         Call<BidToFromRivalsResponse> call = APIService.acceptBidRival(id_participant_bid_send,id_participant_bid_receive, id_karateka);
        call.enqueue(new Callback<BidToFromRivalsResponse>() {
            @Override
            public void onResponse(Call<BidToFromRivalsResponse> call, Response<BidToFromRivalsResponse> response) {
                if(response.isSuccessful()){
                    Log.d("rival bet accepted", "rival bet accepted");
                }else{
                    Log.d("Something wrong", "ededede");
                }
            }

            @Override
            public void onFailure(Call<BidToFromRivalsResponse> call, Throwable t) {
                Log.d("FAIL ACCEPT", t.toString());
            }
        });
    }

}
