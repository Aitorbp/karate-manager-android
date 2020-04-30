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


        ImageView elementImage = convertView.findViewById(R.id.picture_karateka);
        if(image!= null || !!!image.isEmpty() ) {
            Picasso.get().load(apiUtils.BASE_URL_PICTURE + image).fit().into(elementImage);
        }else{ elementImage.setImageResource(R.drawable.default_image); }

        TextView elementName = convertView.findViewById(R.id.name_karateka);
        elementName.setText(name);

        TextView elementWeigth = convertView.findViewById(R.id.weigth_karateka);
        elementWeigth.setText(weigth);

        Button buttonValue = convertView.findViewById(R.id.item_button_value_karateka);
        buttonValue.setText(value);

        final View finalConvertView = convertView;
        buttonValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finalConvertView.findViewById(R.id.item_button_value_karateka).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onClick(data.get(position));
                    }
                });

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

        public BidKaratekaDialogFragment(Karateka karateka) {
            this.karateka = karateka;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            lessValue = (int) (karateka.getValue());
            moreValue = (int) (karateka.getValue());


            Log.d("Name Karateka", String.valueOf(karateka.getName()));
            LayoutInflater inflater = requireActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.popup_do_bid_karateka, null);



            view.findViewById(R.id.popup_do_bid).setOnClickListener(this);
            view.findViewById(R.id.popup_bid_less_money).setOnClickListener(this);
            view.findViewById(R.id.popup_bid_more_money).setOnClickListener(this);
            view.findViewById(R.id.popup_close_bid).setOnClickListener(this);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setView(view);

            return builder.create();
        }


        @Override
        public void onStart() {
            super.onStart();
            TextView   nameKaratekaText = (TextView) getDialog().findViewById(R.id.popup_bid_name_karateka);
            String nameKarateka = karateka.getName();
            nameKaratekaText.setText(nameKarateka);

            final EditText editTextKaratekaValue = (EditText) getDialog().findViewById(R.id.popup_bid_money);
            final String putMoneyKarateka = String.valueOf(karateka.getValue());
            editTextKaratekaValue.setText(putMoneyKarateka, TextView.BufferType.EDITABLE);

            TextView   valueKaratekaText = (TextView) getDialog().findViewById(R.id.popup_bid_value);
            String valueKarateka = String.valueOf(karateka.getValue());
            valueKaratekaText.setText(valueKarateka);

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



        }



        @Override
        public void onClick(View view) {
            final EditText editTextKaratekaValue = (EditText) getDialog().findViewById(R.id.popup_bid_money);

            switch (view.getId()) {
                case R.id.popup_do_bid:
                    Log.d("PULSANDO", "PULSANDO");
                    break;

                case R.id.popup_close_bid:
                    dismiss();
                    break;
                case R.id.popup_bid_less_money:
                    lessValue = lessValue - 10;
                    moreValue=lessValue;
                    editTextKaratekaValue.setText(String.valueOf(lessValue), TextView.BufferType.EDITABLE);

                    break;
                case R.id.popup_bid_more_money:
                    moreValue = moreValue + 10;
                    lessValue=moreValue;
                    editTextKaratekaValue.setText(String.valueOf(moreValue), TextView.BufferType.EDITABLE);

                    break;

                default:
                    break;
            }
        }


    }





}