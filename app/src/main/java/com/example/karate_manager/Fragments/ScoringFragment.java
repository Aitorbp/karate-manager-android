package com.example.karate_manager.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karate_manager.Adapters.AdapterMarket;
import com.example.karate_manager.Adapters.AdapterScoring;
import com.example.karate_manager.DialogFragment.RivalDialogFragment;
import com.example.karate_manager.Models.GroupModel.Group;
import com.example.karate_manager.Models.GroupModel.GroupResponse;
import com.example.karate_manager.Models.ParticipantModel.ParticipantGroup;
import com.example.karate_manager.Models.ParticipantModel.ParticipantResponse;
import com.example.karate_manager.Models.UserModel.UserResponse;
import com.example.karate_manager.Network.APIService;
import com.example.karate_manager.Network.ApiUtils;
import com.example.karate_manager.R;
import com.example.karate_manager.Utils.Storage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ScoringFragment extends Fragment implements AdapterScoring.ClickOnRival{
    AdapterScoring adapterScoring;
    ListView listViewScoring;
    int groupSelectedId = 0;
    private APIService APIService;
    UserResponse user = new UserResponse(200,null,null );
    ParticipantResponse participantResponse = new ParticipantResponse(200,null,null );
    Group groupFromMain =new Group(null,122,null,null,null,0,null);
    CircleImageView group_image_in_scoring;
    ApiUtils apiUtils;
    TextView group_name, participant_name;
    ParticipantGroup participant;
    int idRival;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.fragment_scoring, container, false);
        listViewScoring = (ListView) RootView.findViewById(R.id.scoring_listview);
        group_image_in_scoring = (CircleImageView) RootView.findViewById(R.id.group_image_in_scoring);
        group_name = (TextView) RootView.findViewById(R.id.group_name);
        participant_name = (TextView) RootView.findViewById(R.id.participant_name);
        APIService = ApiUtils.getAPIService();

        String userName = user.getUser().getName();


        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        adapterScoring = new AdapterScoring(getActivity().getApplicationContext(), R.layout.item_participant_layout, participantResponse.getParticipants(), fragmentManager,this);

        Log.d("User in Scoring", user.getUser().getName());

      //La primera vez entra en null porque no carga los datos, una segunda vez ya si los tiene.
        if(groupFromMain!=null ){
            String groupName = groupFromMain.getName_group();
            group_name.setText(groupName);
            String pathImageGroup = groupFromMain.getPicture_group();
            if(pathImageGroup ==null){
                group_image_in_scoring.setImageResource(R.drawable.default_image);
            }else{
                Picasso.get().load(apiUtils.BASE_URL_PICTURE + pathImageGroup).fit().into(group_image_in_scoring);
            }
            Log.d("Group in Scoring", groupFromMain.getName_group());
        }
        Log.d("User in Scoring", String.valueOf(groupSelectedId));
        getAllParticipantByGroup(String.valueOf(groupSelectedId));

        participant_name.setText(userName);



        return RootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        listViewScoring.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                idRival = (int) participantResponse.getParticipantGroup().getId();

            }
        });

    }

    private void getParticipant(){
        ArrayList<ParticipantGroup> participants = participantResponse.getParticipants();

        for (int i = 0; i <participants.size() ; i++) {
            if(participants.get(i).getId_user()==user.getUser().getId()){
                participant =participants.get(i);
                Log.d("Particpante in Scoring", String.valueOf(participant.getId()));
            }
        }
    }

    public void getAllParticipantByGroup(String id_group){

        Call<ParticipantResponse> call = APIService.getParticipantsByGroup(id_group);
        call.enqueue(new Callback<ParticipantResponse>() {
            @Override
            public void onResponse(Call<ParticipantResponse> call, Response<ParticipantResponse> response) {
                if (response.isSuccessful()) {
                    participantResponse = response.body();
                   Log.d("VALUE",String.valueOf(response.body().getParticipants().get(0).getId_group()));

                    getParticipant();
                    adapterScoring.notifyDataSetChanged();
                    adapterScoring.setData(participantResponse);
                    listViewScoring.setAdapter(adapterScoring);
                    Log.d("RESPONSE_SUCCESS", "Everything All right");
                  //  getGroupByParticipant(idGroup);
                }
            }

            @Override
            public void onFailure(Call<ParticipantResponse> call, Throwable t) {

                Log.d("RESPONSE_FAILURE", String.valueOf(t));
            }
        });



    }

    public void recievedUserGroup(UserResponse userResponse, int groupSelected, Group group) {
        if(user!=null && groupFromMain!=null ){
            user =userResponse;
            groupSelectedId = groupSelected;
            groupFromMain = group;
        }
    }


    @Override
    public void onClick(ParticipantGroup participantGroup) {
        if(participant.getId() == participantGroup.getId()){// Igualamos nuestro id de participante con el que pulsamos para identificar el nuestro y no abrir el popup
        return;
        }else{
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            RivalDialogFragment rivalDialogFragment = new RivalDialogFragment(participantGroup);
            Bundle args = new Bundle();
            args.putInt("idParticipantOwn", participant.getId());
            args.putInt("idParticipantRival", participantGroup.getId());
            rivalDialogFragment.setArguments(args);
            rivalDialogFragment.show(fragmentManager, "rival");
        }


    }
}
