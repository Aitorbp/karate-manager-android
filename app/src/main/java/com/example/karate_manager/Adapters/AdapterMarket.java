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
        buttonValue.setText(value.replace(".0", "") + " €");

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







}