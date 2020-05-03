package com.example.karate_manager.Adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karate_manager.Models.KaratekaModel.Karateka;
import com.example.karate_manager.Models.KaratekaModel.MarketResponse;
import com.example.karate_manager.Models.ParticipantModel.ParticipantResponse;
import com.example.karate_manager.Network.APIService;
import com.example.karate_manager.Network.ApiUtils;
import com.example.karate_manager.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterMyTeam extends ArrayAdapter {
    Context context;
    int item_Layaut;
    ArrayList<Karateka> data;
    ApiUtils apiUtils;
    ClickOnSell listener;
    private FragmentManager fm;
    public AdapterMyTeam(Context context, int item_Layaut, ArrayList<Karateka> data, FragmentManager fm, ClickOnSell listener) {
        super(context, item_Layaut, data);
        this.context = context;
        this.item_Layaut = item_Layaut;
        this.data = data;
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

        TextView elementValue = convertView.findViewById(R.id.item_button_value_karateka);
        elementValue.setText(value.replace(".0", ""));

        TextView elementPointsKarateka = convertView.findViewById(R.id.points_karateka_market);
        if(pointsKarateka==null){
            elementPointsKarateka.setText("0");
        }else{
            elementPointsKarateka.setText(pointsKarateka);
        }

        Button buttonToSell = convertView.findViewById(R.id.item_button_to_sell);

        final View finalConvertView = convertView;
        finalConvertView.findViewById(R.id.item_button_to_sell).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(data.get(position));
            }
        });
        return convertView;
    }

    public interface ClickOnSell{
        void onClick(Karateka karateka);
    }




    public static class SellKaratekaDialogFragment extends DialogFragment implements View.OnClickListener {
        Karateka karateka = new Karateka(0,null,null,0, null,0,null,null);
        ApiUtils apiUtils;
        private APIService APIService;
        int idUser;
        int idGroup;


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
            TextView  nameKaratekaText = (TextView) getDialog().findViewById(R.id.sell_name_karateka);
            String nameKarateka = karateka.getName();
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

                        sellKaratekaByParticipant(idParticipant, idGroup);

                    }
                }

                @Override
                public void onFailure(Call<ParticipantResponse> call, Throwable t) {

                    Log.d("RESPONSE_FAILURE", String.valueOf(t));
                }
            });
        }
        public void sellKaratekaByParticipant(int idParticipant, int idGroup){
            Call<ParticipantResponse> call = APIService.sellKaratekaByParticipant(idParticipant, idGroup);
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


}

