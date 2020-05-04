package com.example.karate_manager.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.karate_manager.Models.KaratekaModel.Karateka;
import com.example.karate_manager.Models.KaratekaModel.MarketResponse;
import com.example.karate_manager.Network.ApiUtils;
import com.example.karate_manager.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

public class AdapterStartingKarateka extends ArrayAdapter {

    Context context;
    int item_Layaut;
    ArrayList<Karateka> data;
//    ClickOnKaratekaStarting listener;
    ApiUtils apiUtils;
//    private FragmentManager fm;
    public AdapterStartingKarateka(@NonNull Context context, int item_Layaut, ArrayList<Karateka> data ) {
        super(context, item_Layaut, Collections.singletonList(data));
        this.context = context;
        this.item_Layaut = item_Layaut;

        this.data = data;
//        this.fm = fm;
//        this.listener = listener;
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

        ImageView elementImage = convertView.findViewById(R.id.card_imagen_karateka_myTeam);
        if(image!= null ) {
            Picasso.get().load(apiUtils.BASE_URL_PICTURE + image).fit().into(elementImage);
        }else{ elementImage.setImageResource(R.drawable.default_image); }

        ImageView elementImageDefault = convertView.findViewById(R.id.card_imagen_karateka_default_myTeam);
        elementImageDefault.setImageResource(R.drawable.default_image);

        ImageView iconCountry = convertView.findViewById(R.id.card_country_karateka_myTeam);
        if(imageCountry== null) {
            iconCountry.setImageResource(R.drawable.default_image);
        }else{
            Picasso.get().load(apiUtils.BASE_URL_PICTURE + imageCountry).fit().into(iconCountry);}

        TextView elementName = convertView.findViewById(R.id.card_name_karateka_myTeam);
        elementName.setText(name);

        TextView elementWeigth = convertView.findViewById(R.id.card_weigth_karateka_myTeam);
        elementWeigth.setText(weigth);

        TextView elementPointsKarateka = convertView.findViewById(R.id.card_points_karateka_myTeam);
        if(pointsKarateka==null){
            elementPointsKarateka.setText("0");
        }else{
            elementPointsKarateka.setText(pointsKarateka);
        }

//        final View finalConvertView = convertView;
//        finalConvertView.findViewById(R.id.adapter_container).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                listener.onClick(data.get(position));
//            }
//        });
        return convertView;
    }


//    public interface ClickOnKaratekaStarting{
//        void onClick(Karateka karateka);
//    }
}
