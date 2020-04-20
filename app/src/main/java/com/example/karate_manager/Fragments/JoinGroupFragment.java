package com.example.karate_manager.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.karate_manager.MainActivity;
import com.example.karate_manager.Models.ParticipantModel.Participant;
import com.example.karate_manager.Models.ParticipantModel.ParticipantResponse;
import com.example.karate_manager.Models.UserModel.UserResponse;
import com.example.karate_manager.Network.APIService;
import com.example.karate_manager.Network.ApiUtils;
import com.example.karate_manager.R;
import com.example.karate_manager.Utils.Storage;

import android.content.Intent;
public class JoinGroupFragment extends Fragment {
    Button btn_join_group;
    EditText password_group, name_group;
    private APIService APIService;
    UserResponse user = new UserResponse(200,null,null );

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.fragment_join_group, container, false);
        password_group = (EditText) RootView.findViewById(R.id.password_group);
        name_group = (EditText) RootView.findViewById(R.id.name_group);
        btn_join_group = (Button)   RootView.findViewById(R.id.btn_join_group);
        APIService = ApiUtils.getAPIService();


        btn_join_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameGroup = name_group.getText().toString();
                String passGroup = password_group.getText().toString();

                String userId = String.valueOf(user.getUser().getId());
                joinGroupPost(passGroup,nameGroup,userId);
            }
        });


        return RootView;
    }


    private void joinGroupPost(String passGroup, String name, String id_user){

        String api_token = Storage.getToken(getContext());
        APIService.createParticipantInGroup(passGroup, name, id_user, api_token).enqueue(new Callback<ParticipantResponse>() {
            @Override
            public void onResponse(Call<ParticipantResponse> call, Response<ParticipantResponse> response) {

                if(response.isSuccessful()) {


              //     Log.d("JOIN GROUP", response.body().getParticipant().getId());
                    (Toast.makeText(getContext(), "Group created", Toast.LENGTH_LONG)).show();

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);

                }else{
                    (Toast.makeText(getContext(), "There was an error. Maybe the participant already registered in this group.", Toast.LENGTH_LONG)).show();
                }
            }

            @Override
            public void onFailure(Call<ParticipantResponse> call, Throwable t) {
                (Toast.makeText(getContext(), "Participant already registered in this group.", Toast.LENGTH_LONG)).show();
            }
        });
    }

    public void recievedUser(UserResponse userResponse) {
        if(user!=null){
            user =userResponse;
        }
    }
}
