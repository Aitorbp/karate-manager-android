package com.example.karate_manager.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.karate_manager.DialogFragment.ChoosingKaratekaStartingDialog;
import com.example.karate_manager.Models.KaratekaModel.Karateka;
import com.example.karate_manager.Models.KaratekaModel.MarketResponse;
import com.example.karate_manager.Network.ApiUtils;
import com.example.karate_manager.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

public class AdapterListviewChangeStartingKarateka extends ArrayAdapter {


    Context context;
    int item_Layaut;
    ArrayList<Karateka> data;
    ApiUtils apiUtils;


    public AdapterListviewChangeStartingKarateka(Context context, int item_Layaut, ArrayList<Karateka> data) {
        super(context, item_Layaut,data);
        this.context = context;
        this.item_Layaut = item_Layaut;
        this.data = data;


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

        ImageView iconCountry = convertView.findViewById(R.id.change_starting_icon_country);
        if(imageCountry== null) {
            iconCountry.setImageResource(R.drawable.default_image);
        }else{
            Picasso.get().load(apiUtils.BASE_URL_PICTURE + imageCountry).fit().into(iconCountry);}

        ImageView elementImage = convertView.findViewById(R.id.change_starting_picture_karateka);
        if(image!= null || !!!image.isEmpty() ) {
            Picasso.get().load(apiUtils.BASE_URL_PICTURE + image).fit().into(elementImage);
        }else{ elementImage.setImageResource(R.drawable.default_image); }

        TextView elementName = convertView.findViewById(R.id.change_starting_name_karateka);
        elementName.setText(name);

        TextView elementWeigth = convertView.findViewById(R.id.change_starting_weigth_karateka);
        elementWeigth.setText(weigth);

        TextView elementValue = convertView.findViewById(R.id.change_starting_item_value_karateka);
        elementValue.setText(value.replace(".0", ""));

        TextView elementPointsKarateka = convertView.findViewById(R.id.change_starting_points_karateka_market);
        if(pointsKarateka==null){
            elementPointsKarateka.setText("0");
        }else{
            elementPointsKarateka.setText(pointsKarateka);
        }

        Button buttonToChange= convertView.findViewById(R.id.item_button_to_change);


        return convertView;
    }


}
