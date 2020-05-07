package com.example.karate_manager.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karate_manager.Fragments.MarketFragment;
import com.example.karate_manager.MainActivity;
import com.example.karate_manager.Models.BidModel.BidResponse;
import com.example.karate_manager.Models.KaratekaModel.Karateka;
import com.example.karate_manager.Models.KaratekaModel.MarketResponse;
import com.example.karate_manager.Models.ParticipantModel.ParticipantGroup;
import com.example.karate_manager.Models.ParticipantModel.ParticipantResponse;
import com.example.karate_manager.Network.APIService;
import com.example.karate_manager.Network.ApiUtils;
import com.example.karate_manager.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterMarket extends ArrayAdapter{

    private FragmentManager fm;
    Context context;
    int item_Layaut;
    ArrayList<Karateka> data;
    ApiUtils apiUtils;
    ClickOnBid listener;

    public AdapterMarket(@NonNull Context context, int resource, @NonNull ArrayList objects, FragmentManager fm, ClickOnBid listener) {
        super(context, resource, objects);
        this.context = context;
        this.item_Layaut = resource;
        this.data = objects;
        this.fm = fm;
        this.listener = listener;

    }



    @Override
    public int getCount() {
        return data.size();
    }
    public void setData(MarketResponse data) {
        if(data!=null){
            this.data = data.getKaratekas();
            this.notifyDataSetChanged();
        }

    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(item_Layaut, parent, false);
        }

       // dialogDoBidKarateka = new Dialog(getContext());
        String image = data.get(position).getPhoto_karateka();
        String name = String.valueOf(data.get(position).getName());
        String value = String.valueOf(data.get(position).getValue());
        String weigth = String.valueOf(data.get(position).getWeight());
        String pointsKarateka = String.valueOf(data.get(position).getPoints_karateka());
        String imageCountry = data.get(position).getPhoto_country();

        ImageView iconCountry = convertView.findViewById(R.id.icon_country);
        if(imageCountry== null) {
            iconCountry.setImageResource(R.drawable.default_image);
        }else{
            Picasso.get().load(apiUtils.BASE_URL_PICTURE + imageCountry).fit().into(iconCountry);}



        ImageView elementImage = convertView.findViewById(R.id.picture_karateka);
        if(image!= null || !!!image.isEmpty() ) {
            Picasso.get().load(apiUtils.BASE_URL_PICTURE + image).fit().into(elementImage);
        }else{ elementImage.setImageResource(R.drawable.default_image); }

        TextView elementName = convertView.findViewById(R.id.name_karateka);
        elementName.setText(name);

        TextView elementWeigth = convertView.findViewById(R.id.weigth_karateka);
        elementWeigth.setText(weigth);

        TextView elementPointsKarateka = convertView.findViewById(R.id.points_karateka_market);
        if(pointsKarateka==null){
            elementPointsKarateka.setText("0");
        }else{
            elementPointsKarateka.setText(pointsKarateka);
        }





        Button buttonValue = convertView.findViewById(R.id.item_button_value_karateka);
        buttonValue.setText(value.replace(".0", ""));

        final View finalConvertView = convertView;
                finalConvertView.findViewById(R.id.item_button_value_karateka).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onClick(data.get(position));
                    }
                });

        return convertView;
    }


    public interface ClickOnBid{
        void onClick(Karateka karateka);
    }


    // Popup para abrir pantalla bid
    public static class BidKaratekaDialogFragment extends DialogFragment implements View.OnClickListener{

        Karateka karateka = new Karateka(0,null,null,0, null,0,null,null);
        ApiUtils apiUtils;
        int lessValue;
        int moreValue;

        int idUser;
        int idGroup;


        private APIService APIService;
        public BidKaratekaDialogFragment(Karateka karateka) {
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
             idUser = mArgs.getInt("idUser");
             idGroup = mArgs.getInt("idGroup");

            Log.d("Name Karateka", String.valueOf(karateka.getName()));
            LayoutInflater inflater = requireActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.popup_do_bid_karateka, null);



            view.findViewById(R.id.popup_do_bid).setOnClickListener(this);
            view.findViewById(R.id.popup_bid_less_money).setOnClickListener(this);
            view.findViewById(R.id.popup_bid_more_money).setOnClickListener(this);
            view.findViewById(R.id.popup_close_bid).setOnClickListener(this);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setView(view);


            Log.d("Participant in Bid", String.valueOf(idGroup) );
            return builder.create();
        }


        @Override
        public void onStart() {
            super.onStart();
            TextView  nameKaratekaText = (TextView) getDialog().findViewById(R.id.popup_bid_name_karateka);
            String nameKarateka = karateka.getName();
            nameKaratekaText.setText(nameKarateka);

            editTextKaratekaValue = (EditText) getDialog().findViewById(R.id.popup_bid_money);
            final String putMoneyKarateka = String.valueOf(karateka.getValue());
            editTextKaratekaValue.setText(putMoneyKarateka.replace(".0", ""), TextView.BufferType.EDITABLE);

            TextView valueKaratekaText = (TextView) getDialog().findViewById(R.id.popup_bid_value);
            String valueKarateka = String.valueOf(karateka.getValue());
            valueKaratekaText.setText(valueKarateka.replace(".0", ""));

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
                    Log.d("PULSANDO", "PULSANDO");
                    getParticipantByGroupAndUser(idUser, idGroup);
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


        public void getParticipantByGroupAndUser(int idUser, int idGroup){

            Call<ParticipantResponse> call = APIService.getParticipant(idUser,idGroup);
            call.enqueue(new Callback<ParticipantResponse>() {
                @Override
                public void onResponse(Call<ParticipantResponse> call, Response<ParticipantResponse> response) {
                    if (response.isSuccessful()) {

                        Log.d("BID PARTICIPANT",String.valueOf(response.body().getParticipants().get(0).getId()));
                        int idParticipant =response.body().getParticipants().get(0).getId();
                        int idKarateka = (int) karateka.getId();
                        int idGroup = response.body().getParticipants().get(0).getId_group();
                        int bid = Integer.parseInt(editTextKaratekaValue.getText().toString());
                        Log.d("RESPONSE_SUCCESS", "Everything All right");
                        if(bid <= karateka.getValue()){
                            Toast.makeText(getContext(), "Your bet must be higher than the market price", Toast.LENGTH_LONG).show();
                        }else{
                            createBidInGroup(idKarateka, idGroup, idParticipant, bid );
                        }

                    }
                }

                @Override
                public void onFailure(Call<ParticipantResponse> call, Throwable t) {

                    Log.d("RESPONSE_FAILURE", String.valueOf(t));
                }
            });
        }
        public void createBidInGroup(int idKarateka, int idGroup, int idParticipant, int bid){

            Log.d("idKarateka!!!!", String.valueOf(idKarateka));
            Log.d("idGroup!!!!", String.valueOf(idGroup));
            Log.d("idParticipant!!!!", String.valueOf(idParticipant));
            Log.d("bid!!!!", String.valueOf(bid));
            Call<BidResponse> call = APIService.createBidInGroup(idKarateka,idParticipant,bid,idGroup);
            call.enqueue(new Callback<BidResponse>() {
                @Override
                public void onResponse(Call<BidResponse> call, Response<BidResponse> response) {
                    if(response.isSuccessful()){
                        Log.d("Bid done!!!!", "Bid doneeeee!");
                        Toast.makeText(getContext(), "Good luck, you have bet on this karateka ", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }else{
                        Log.d("Bid failll!!!!", "Bid failll!");
                    }


                }

                @Override
                public void onFailure(Call<BidResponse> call, Throwable t) {


                    Log.d("Feeeeeilllll!!!!", String.valueOf(t));
                }
            });

        }

    }





}