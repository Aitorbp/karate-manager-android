package com.example.karate_manager.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karate_manager.Models.BidModel.BidRivalsResponse;
import com.example.karate_manager.Models.KaratekaModel.Karateka;
import com.example.karate_manager.Models.KaratekaModel.MarketResponse;
import com.example.karate_manager.Network.ApiUtils;
import com.example.karate_manager.R;
import com.squareup.picasso.Picasso;

import androidx.fragment.app.DialogFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BidKaratekaRivalDialogFragment extends DialogFragment implements View.OnClickListener {

    Karateka karateka = new Karateka(0,null,null,0, null,0,null,null);
    ApiUtils apiUtils;
    int lessValue;
    int moreValue;

    int idParticipantRival;
    int idParticipantOwn;


    private com.example.karate_manager.Network.APIService APIService;
    public BidKaratekaRivalDialogFragment(Karateka karateka) {
        this.karateka = karateka;
    }

    EditText editTextKaratekaValue;
    int bidEditTex;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        APIService = ApiUtils.getAPIService();
        lessValue = (int) (karateka.getValue());
        moreValue = (int) (karateka.getValue());

        Bundle mArgs = getArguments();
        idParticipantRival = mArgs.getInt("idParticipantRival");
        idParticipantOwn = mArgs.getInt("idParticipantOwn");

        Log.d("Name Karateka", String.valueOf(karateka.getName()));
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.popup_do_bid_karateka, null);



        view.findViewById(R.id.popup_do_bid).setOnClickListener(this);
        view.findViewById(R.id.popup_bid_less_money).setOnClickListener(this);
        view.findViewById(R.id.popup_bid_more_money).setOnClickListener(this);
        view.findViewById(R.id.popup_close_bid).setOnClickListener(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);


 //       Log.d("Participant in Bid", String.valueOf(idGroup) );
        return builder.create();
    }


    @Override
    public void onStart() {
        super.onStart();
        TextView nameKaratekaText = (TextView) getDialog().findViewById(R.id.popup_bid_name_karateka);
        String nameKarateka = karateka.getName();
        nameKaratekaText.setText(nameKarateka);

        editTextKaratekaValue = (EditText) getDialog().findViewById(R.id.popup_bid_money);
        final String putMoneyKarateka = String.valueOf(karateka.getValue());
        editTextKaratekaValue.setText(putMoneyKarateka.replace(".0", ""), TextView.BufferType.EDITABLE);

        TextView valueKaratekaText = (TextView) getDialog().findViewById(R.id.popup_bid_value);
        String valueKarateka = String.valueOf(karateka.getValue());
        valueKaratekaText.setText(valueKarateka.replace(".0", "") + " €");

        TextView weightKaratekaText = (TextView) getDialog().findViewById(R.id.pop_bid_weigth);
        String weigthKarateka = String.valueOf(karateka.getWeight());
        weightKaratekaText.setText(weigthKarateka);

        ImageView pictureKaratekaImage = (ImageView) getDialog().findViewById(R.id.popup_bid_image_karateka);
        String pictureKarateka = String.valueOf(karateka.getPhoto_karateka());
        if(pictureKarateka.isEmpty() || pictureKarateka == null){
            pictureKaratekaImage.setImageResource(R.drawable.default_image);
        }else{
            Picasso.get().load(apiUtils.BASE_URL_PICTURE + pictureKarateka).fit().into(pictureKaratekaImage);
        }

        ImageView pictureCountryKaratekaImage = (ImageView) getDialog().findViewById(R.id.popup_bid_country);
        String countryKarateka = String.valueOf(karateka.getPhoto_country());
        if(countryKarateka.isEmpty() || countryKarateka == null){
            pictureKaratekaImage.setImageResource(R.drawable.default_image);
        }else{
            Picasso.get().load(apiUtils.BASE_URL_PICTURE + countryKarateka).fit().into(pictureCountryKaratekaImage);
        }


        TextView elementPointsKarateka =  (TextView) getDialog().findViewById(R.id.popup_bid_points_karateka);
        String pointsKarateka = String.valueOf(karateka.getPoints_karateka());
        if(pointsKarateka.isEmpty() || pointsKarateka==null){
            elementPointsKarateka.setText("0");
        }else{
            elementPointsKarateka.setText(pointsKarateka);

        }


    }



    @Override
    public void onClick(View view) {
        //  editTextKaratekaValue = (EditText) getDialog().findViewById(R.id.popup_bid_money);

        switch (view.getId()) {
            case R.id.popup_do_bid:
                int bidToBBDD = Integer.parseInt(editTextKaratekaValue.getText().toString());
                Log.d("PULSANDO", "PULSANDO");
                Log.d("idParticipantOwn", String.valueOf(idParticipantOwn));
                Log.d("idParticipantRival", String.valueOf(idParticipantRival));
                Log.d("bid", String.valueOf(bidEditTex));
                Log.d("karateka.getId()", String.valueOf(karateka.getId()));
                createBidRivals(idParticipantOwn, idParticipantRival, karateka.getId(), bidToBBDD);
                break;

            case R.id.popup_close_bid:
                dismiss();
                break;
            case R.id.popup_bid_less_money:
                lessValue = lessValue - 10;
                moreValue=lessValue;
                editTextKaratekaValue.setText(String.valueOf(lessValue), TextView.BufferType.EDITABLE);
                bidEditTex = Integer.valueOf(String.valueOf(lessValue));
                break;
            case R.id.popup_bid_more_money:
                moreValue = moreValue + 10;
                lessValue=moreValue;
                editTextKaratekaValue.setText(String.valueOf(moreValue), TextView.BufferType.EDITABLE);
                bidEditTex = Integer.valueOf(String.valueOf(moreValue));
                break;

            default:
                break;
        }
    }


    private void createBidRivals(int id_participant_bid_send, int id_participant_bid_receive, int id_karateka, int bid_rival){
        Call<BidRivalsResponse> call =  APIService.createBidRivals(id_participant_bid_send, id_participant_bid_receive, id_karateka, bid_rival);
        call.enqueue(new Callback<BidRivalsResponse>() {
            @Override
            public void onResponse(Call<BidRivalsResponse> call, Response<BidRivalsResponse> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getContext(), "Bet sent to your rival", Toast.LENGTH_SHORT).show();

                    dismiss();
                }else{
                    Log.d("Failllll", "jajajjajaja");
                }

            }

            @Override
            public void onFailure(Call<BidRivalsResponse> call, Throwable t) {

            }
        });
    }
}
