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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.karate_manager.Fragments.MarketFragment;
import com.example.karate_manager.MainActivity;
import com.example.karate_manager.Models.KaratekaModel.Karateka;
import com.example.karate_manager.Models.KaratekaModel.MarketResponse;
import com.example.karate_manager.Models.ParticipantModel.ParticipantGroup;
import com.example.karate_manager.Models.ParticipantModel.ParticipantResponse;
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

public class AdapterMarket extends ArrayAdapter{
    private FragmentManager fm;
    Dialog dialogDoBidKarateka;
    Context context;
    int item_Layaut;
    ArrayList<Karateka> data;
    ApiUtils apiUtils;


    public AdapterMarket(@NonNull Context context, int resource, @NonNull ArrayList objects, FragmentManager fm) {
        super(context, resource, objects);
        this.context = context;
        this.item_Layaut = resource;
        this.data = objects;
        this.fm = fm;
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
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(item_Layaut, parent, false);
        }

       // dialogDoBidKarateka = new Dialog(getContext());
        String image = data.get(position).getPhoto_karateka();
        String name = String.valueOf(data.get(position).getName());
        String value = String.valueOf(data.get(position).getValue());
        String weigth = String.valueOf(data.get(position).getWeight());

        if(image!= null) {
            ImageView elementImage = convertView.findViewById(R.id.picture_karateka);
            Picasso.get().load(apiUtils.BASE_URL_PICTURE + image).fit().into(elementImage);
        }


        TextView elementName = convertView.findViewById(R.id.name_karateka);
        elementName.setText(name);



        TextView elementWeigth = convertView.findViewById(R.id.weigth_karateka);
        elementWeigth.setText(weigth);

        Button buttonValue = convertView.findViewById(R.id.item_button_value_karateka);
        buttonValue.setText(value);

        popupBidKarateka(buttonValue);

        return convertView;
    }


    public void popupBidKarateka(Button buttonValue){
        buttonValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                DialogFragment newFragment = new BidKaratekaDialogFragment();


                newFragment.show(fm, "bid" );



            }
        });
    }

    public static class BidKaratekaDialogFragment extends DialogFragment implements View.OnClickListener{



        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            LayoutInflater inflater = requireActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.popup_do_bid_karateka, null);
            view.findViewById(R.id.popup_do_bid).setOnClickListener(this);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setView(view);

            return builder.create();
        }



        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.popup_do_bid:
                    Log.d("PULSANDO", "PULSANDO");
                    break;

                default:
                    break;
            }
        }
    }


}