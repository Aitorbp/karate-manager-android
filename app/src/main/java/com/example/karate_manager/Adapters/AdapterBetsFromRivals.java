package com.example.karate_manager.Adapters;

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

import com.example.karate_manager.DialogFragment.AcceptBidRivalDialogFragment;
import com.example.karate_manager.Fragments.MyTeamFragment;
import com.example.karate_manager.Models.BidModel.BidToFromRivalsResponse;
import com.example.karate_manager.Models.BidModel.KaratekaRival;
import com.example.karate_manager.Models.KaratekaModel.Karateka;
import com.example.karate_manager.Network.ApiUtils;
import com.example.karate_manager.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

public class AdapterBetsFromRivals extends ArrayAdapter {


    Context context;
    int item_Layaut;
    ArrayList<KaratekaRival> data;
    ApiUtils apiUtils;
    private FragmentManager fm;
    private ClickOnAccept listener;
    int id_participant_bid_send;
    int id_participant_bid_receive;
    int id_karateka;
    int bid_send_rival;
    String image;
    String nameKarateka;
    String imageCountry;
    String weigth;
    String pointsKarateka;

    public AdapterBetsFromRivals(Context context, int item_Layaut, ArrayList<KaratekaRival> data, FragmentManager fm, ClickOnAccept listener) {
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

    public void setData(BidToFromRivalsResponse data) {
        if (data != null) {
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
        image = data.get(position).getPhoto_karateka();
        nameKarateka = String.valueOf(data.get(position).getName());
        String value = String.valueOf(data.get(position).getValue());
        weigth = String.valueOf(data.get(position).getWeight());
        pointsKarateka = String.valueOf(data.get(position).getPoints_karateka());
        imageCountry = data.get(position).getPhoto_country();

        String dateBid = data.get(position).getDate_bid();
        String dateBidFomated = dateBid.substring(0, 10);

        String bidRival = String.valueOf(data.get(position).getBid_rival());
        String nameRival = data.get(position).getName_rival();

        //Datos que pasamos al dialogfragment AcceptBidDialogFragment
        id_participant_bid_send = data.get(position).getId_participant_bid_send();
        id_participant_bid_receive = data.get(position).getId_participant_bid_receive();
        id_karateka = data.get(position).getId();
        bid_send_rival = data.get(position).getBid_rival();


        TextView elementDateBid = convertView.findViewById(R.id.bet_from_rivals_date_bid_rival);
        elementDateBid.setText(dateBidFomated);

        TextView elementnameRival = convertView.findViewById(R.id.bet_from_rivals_name_rival);
        elementnameRival.setText(nameRival);


        ImageView iconCountry = convertView.findViewById(R.id.bet_from_rivals_icon_country);
        if (imageCountry == null) {
            iconCountry.setImageResource(R.drawable.default_image);
        } else {
            Picasso.get().load(apiUtils.BASE_URL_PICTURE + imageCountry).fit().into(iconCountry);
        }

        ImageView elementImage = convertView.findViewById(R.id.bet_from_rivals_picture_karateka);
        if (image != null || !!!image.isEmpty()) {
            Picasso.get().load(apiUtils.BASE_URL_PICTURE + image).fit().into(elementImage);
        } else {
            elementImage.setImageResource(R.drawable.default_image);
        }

        TextView elementName = convertView.findViewById(R.id.bet_from_rivals_name_karateka);
        elementName.setText(nameKarateka);

        TextView elementWeigth = convertView.findViewById(R.id.bet_from_rivals_weigth_karateka);
        elementWeigth.setText(weigth);

        TextView elementValue = convertView.findViewById(R.id.bet_from_rivals_accept_botton_karateka);
        elementValue.setText(bidRival.replace(".0", ""));

        TextView elementPointsKarateka = convertView.findViewById(R.id.bet_from_rivals_points_karateka_market);
        if (pointsKarateka == "null") {
            elementPointsKarateka.setText("0");
        } else {
            elementPointsKarateka.setText(pointsKarateka);
        }

        final View finalConvertView = convertView;
        finalConvertView.findViewById(R.id.bet_from_rivals_accept_botton_karateka).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(data.get(position));
            }
        });

        finalConvertView.findViewById(R.id.bet_from_rivals_refuse_botton_karateka).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClickOnRefuse(data.get(position));
            }
        });

        return convertView;
    }

    public interface ClickOnAccept {
        void onClick(KaratekaRival karatekaRival);
        void onClickOnRefuse(KaratekaRival karatekaRival);
    }

}


