package com.example.karate_manager.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.karate_manager.Models.KaratekaModel.Karateka;
import com.example.karate_manager.Models.ParticipantModel.ParticipantGroup;
import com.example.karate_manager.Models.ParticipantModel.ParticipantResponse;
import com.example.karate_manager.Network.ApiUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.example.karate_manager.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

public class AdapterScoring extends ArrayAdapter {

    Context context;
    int item_Layaut;
    ArrayList<ParticipantGroup> data;
    ApiUtils apiUtils;
    private FragmentManager fm;
    ClickOnRival listener;

    public AdapterScoring(@NonNull Context context, int resource, ArrayList<ParticipantGroup> data,FragmentManager fm, ClickOnRival listener) {
        super(context, resource, data);
        this.context = context;
        this.item_Layaut = resource;
        this.fm = fm;
        this.data = data;
        this.listener = listener;
    }

    @Override
    public int getCount() {

        return data.size();
    }
    public void setData(ParticipantResponse data) {
        if(data!=null){
            this.data = data.getParticipants();
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
        String points = String.valueOf(data.get(position).getPoints());


        if(image!= null) {
            ImageView elementImage = convertView.findViewById(R.id.picture_participant);
            Picasso.get().load(apiUtils.BASE_URL_PICTURE + image).fit().into(elementImage);
        }


        TextView elementName = convertView.findViewById(R.id.name_participant);
        elementName.setText(name);

        TextView elementPoints = convertView.findViewById(R.id.points_participant);
        elementPoints.setText(points);



        convertView.findViewById(R.id.item_participant).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(data.get(position));
            }
        });

        return convertView;
    }

    public interface ClickOnRival{
        void onClick(ParticipantGroup participantGroup);
    }
}
