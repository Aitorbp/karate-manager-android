package com.example.karate_manager.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.karate_manager.Models.ParticipantModel.ParticipantGroup;
import com.example.karate_manager.Models.ParticipantModel.ParticipantResponse;
import com.example.karate_manager.Models.ResultsParticipant.ResultPartChampGroup;
import com.example.karate_manager.Models.ResultsParticipant.ResultsParticipantResponse;
import com.example.karate_manager.Network.ApiUtils;
import com.example.karate_manager.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

public class AdapterResultsParticipantCalendary extends ArrayAdapter {

    Context context;
    int item_Layaut;
    ArrayList<ResultPartChampGroup> data;
    ApiUtils apiUtils;


    public AdapterResultsParticipantCalendary(@NonNull Context context, int resource, ArrayList<ResultPartChampGroup> data) {
        super(context, resource, data);
        this.context = context;
        this.item_Layaut = resource;

        this.data = data;

    }

    @Override
    public int getCount() {

        return data.size();
    }

    public void setData(ResultsParticipantResponse data) {
        if (data != null) {
            this.data = data.getResultPartChampGroup();
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


        String image = data.get(position).getPhoto_profile();
        String name = String.valueOf(data.get(position).getName());
        String pointsTotal = String.valueOf(data.get(position).getTotal_points_in_championship());


        if (image != null) {
            ImageView elementImage = convertView.findViewById(R.id.result_part_calendary_picture_participant);
            Picasso.get().load(apiUtils.BASE_URL_PICTURE + image).fit().into(elementImage);
        }


        TextView elementName = convertView.findViewById(R.id.result_part_calendary_name_participant);
        elementName.setText(name);

        TextView elementPoints = convertView.findViewById(R.id.result_part_calendary_points_participant);
        if(elementPoints ==null){
            elementPoints.setText(0);
        }else{
            elementPoints.setText(pointsTotal);
        }



        return convertView;
    }
}