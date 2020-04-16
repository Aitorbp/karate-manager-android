package com.example.karate_manager.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.karate_manager.MainActivity;
import com.example.karate_manager.Models.Group;
import com.example.karate_manager.Models.User;
import com.example.karate_manager.Network.APIService;
import com.example.karate_manager.Network.ApiUtils;
import com.example.karate_manager.R;
import com.example.karate_manager.RegisterActivity;
import com.example.karate_manager.Utils.Storage;


public class CreateGroupFragment extends Fragment {

    EditText password_group, name_group;
    Spinner spinner_budget;
    Button btn_create_group;
    Switch switch_genre;
    boolean genre = false;
    Dialog dialogLoading;
    private  APIService APIService;
    MainActivity mainActivity;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.fragment_create_group, container, false);

        dialogLoading = new Dialog(getContext());
        dialogLoading.setContentView(R.layout.popup_loading);
        spinner_budget = (Spinner) RootView.findViewById(R.id.budget_spinner);
        password_group = (EditText) RootView.findViewById(R.id.password_group);
        name_group = (EditText) RootView.findViewById(R.id.name_group);
        btn_create_group = (Button)   RootView.findViewById(R.id.btn_create_group);
        switch_genre = (Switch)   RootView.findViewById(R.id.switch_genre);
        APIService = ApiUtils.getAPIService();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(),
                R.array.budgets, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_budget.setAdapter(adapter);
        String token = Storage.getToken(getContext());

        switch_genre.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    genre = true; //edit here
                }else{
                    genre = false; //edit here
                }
            }
        });



        btn_create_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = name_group.getText().toString();
                String pass = password_group.getText().toString();
                int budget = Integer.parseInt(spinner_budget.getSelectedItem().toString());
                String switch_genre = String.valueOf(genre);

                registerPOST("1",name, pass, budget, switch_genre, 0);
            }
        });

        return RootView;
    }

    private void registerPOST(String id_user, String name, String pass,  int budget, String switch_genre, int points)
    {
        Log.d("eeer","addede");
        Log.d("OBJETO:", pass);
        dialogLoading.show();
        dialogLoading.setCancelable(false);

        Group group = new Group(id_user ,name, pass, budget, switch_genre, points);
        APIService.createGroup(group).enqueue(new Callback<Group>() {
            @Override
            public void onResponse(Call<Group> call, Response<Group> response) {
                dialogLoading.dismiss();
                if(response.isSuccessful()) {

                    Log.d("RESPUESTA DEL MENSAJE", response.toString());
                    (Toast.makeText(getContext(), "Group created", Toast.LENGTH_LONG)).show();
                    mainActivity.changeSecreen(R.id.nav_profile);
                }
            }

            @Override
            public void onFailure(Call<Group> call, Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();

                dialogLoading.dismiss();
            }
        });


    }


}
