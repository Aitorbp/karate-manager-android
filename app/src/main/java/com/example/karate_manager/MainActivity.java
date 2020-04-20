package com.example.karate_manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.karate_manager.Fragments.CalendaryFragment;
import com.example.karate_manager.Fragments.CreateGroupFragment;
import com.example.karate_manager.Fragments.JoinGroupFragment;
import com.example.karate_manager.Fragments.MarketFragment;
import com.example.karate_manager.Fragments.MyTeamFragment;
import com.example.karate_manager.Fragments.ProfileFragment;
import com.example.karate_manager.Fragments.ScoringFragment;
import com.example.karate_manager.Models.GroupModel.GroupsResponse;
import com.example.karate_manager.Models.UserModel.UserResponse;
import com.example.karate_manager.Network.APIService;
import com.example.karate_manager.Network.ApiUtils;
import com.example.karate_manager.Utils.Storage;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import static com.example.karate_manager.Utils.PreferencesUtility.*;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    BottomNavigationView navigationViewHorizontal;
    private APIService APIService;
    UserResponse user;
    GroupsResponse groups;
    int groupSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationViewHorizontal = findViewById(R.id.hor_menu);
navigationViewHorizontal.setOnNavigationItemSelectedListener((BottomNavigationView.OnNavigationItemSelectedListener) horListener);
        navigationView.setNavigationItemSelectedListener(this);
        APIService = ApiUtils.getAPIService();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

       String api_token = Storage.getToken(getApplicationContext());


       //METODO PARA RECOGER LOS DATOS DEL USUARIO Y PODER UTILIZARLOS
        getUser(api_token, new GetUserCallback() {
            @Override
            public void onSuccess(UserResponse userResponse) {
                user = userResponse;

                getAllGroupsByUser(String.valueOf(user.getUser().getId()), new GetAllGroupsByUserCallback() {
                    @Override
                    public void onSuccess(GroupsResponse groupsResponse) {
                        groups = groupsResponse;

                    }

                    @Override
                    public void onError() {

                    }
                });
            }
            @Override
            public void onError() {
            }
        });


        Log.d("Valor del grupo en me", String.valueOf(groupSelected));
    }


    //Accediendo al menu horizontal
    private BottomNavigationView.OnNavigationItemSelectedListener horListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            //Validar si groupSelected es igual a cero, poner el primer grupo
            //Mejora, guardar el grupo en shavePreference para que te guarde siempre el mismo grupo al arrancar la applicación
            if(groupSelected == 0){
                groupSelected = groups.getGroupByParticipant().get(0).getId();
            }

            switch (menuItem.getItemId()){
                case R.id.hor_scoring:
                    final ScoringFragment scoringFragment = new ScoringFragment();
                    addFragment(scoringFragment);
                    sendUserGroupToScoring(user,groupSelected,scoringFragment);
                    break;

                case R.id.hor_market:
                    final MarketFragment marketFragment = new MarketFragment();
                    addFragment(marketFragment);
                    sendUserGroupToMarket(user,groupSelected,marketFragment);
                    break;
                case R.id.hor_myteam:
                    final MyTeamFragment myTeamFragment = new MyTeamFragment();
                    addFragment(myTeamFragment);

                    sendUserGroupToMyTeam(user,groupSelected,myTeamFragment);
                    break;

                case R.id.hor_calendary:
                    Log.d("aeaeaeaeea","wdwwdwdwd");
                    final CalendaryFragment calendaryFragment = new CalendaryFragment();
                    addFragment(calendaryFragment);
                    break;
            }
            return true;
        }
    };

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int screen =  menuItem.getItemId();


        for (int i = 0; i < groups.getGroupByParticipant().size() ; i++) {
            if(menuItem.getItemId() == groups.getGroupByParticipant().get(i).getId()){
                Log.d("MenuItem  clicked----", String.valueOf(groups.getGroupByParticipant().get(i).getId()));

                //Recogiendo el id del grupo para enviarlo al menu horizontal
                groupSelected = groups.getGroupByParticipant().get(i).getId();

                //Ir a pantalla Scoring
                final ScoringFragment scoringFragment = new ScoringFragment();
                addFragment(scoringFragment);
                sendUserGroupToScoring(user,groupSelected,scoringFragment);
            }
        }
        changeSecreen(screen);
        Log.d("Id del user", String.valueOf(user.getUser().getId()));
        return true;
    }

    public void changeSecreen(int screen){
        Intent intent;
        switch (screen) {
            case R.id.nav_home:
                intent = new Intent(this, MainActivity.class );
                startActivity(intent);
                break;
            case R.id.nav_profile:
                ProfileFragment profileFragment = new ProfileFragment();
                addFragment(profileFragment);
                break;
            case R.id.nav_join_group:
                JoinGroupFragment joinGroupFragment = new JoinGroupFragment();
                addFragment(joinGroupFragment);
                sendUserToJoinGroup(user,joinGroupFragment);
                break;

            case R.id.nav_create_group:
                final CreateGroupFragment createGroupFragment = new CreateGroupFragment();
                addFragment(createGroupFragment);
                sendUserToCreateGroup(user,createGroupFragment);
                break;

            case R.id.nav_log_out:
                Storage.removeToken(getApplicationContext(),TOKEN);
                Storage.setLoggedIn(getApplicationContext(), false);
                intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;


        }


    }

    public void addFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(null)
                .commit();
        drawerLayout.closeDrawer(Gravity.LEFT); //para cerrar el drawer_menu lateral cuando pulsemos un boton
    }



    private void getUser(String api_token, final GetUserCallback callback){

      Call<UserResponse> call =  APIService.getUserByToken(api_token);

            call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {

                if(response.isSuccessful()) {
                    callback.onSuccess(response.body());
                    Log.d("GET API TOKEN USER MAIN", String.valueOf(response.body().getUser().getId()));
                }
            }
            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.d("Falloooooo", t.getMessage());
            }
        });

    }
    //Interface para que se ejecute de forma asincrona y podamos rescatar el User para poder utilizarlo en todo el programa
    public interface GetUserCallback{
        void onSuccess(UserResponse userResponse);
        void onError();
    }



    public void getAllGroupsByUser(String id_user,  final GetAllGroupsByUserCallback callback){


        Call<GroupsResponse> call = APIService.getGroupsByUser(id_user);
        call.enqueue(new Callback<GroupsResponse>() {
            @Override
            public void onResponse(Call<GroupsResponse> call, Response<GroupsResponse> response) {
                if(response.isSuccessful()) {
                    callback.onSuccess(response.body());
                    if(response.body().getGroupByParticipant().size() == 0){
                        Log.d("Your arent in any group", String.valueOf( response.body().getGroupByParticipant().size()));
                    }else{
                        final Menu menu = navigationView.getMenu();

                        int groups = response.body().getGroupByParticipant().size();

                        for (int i = 0; i < groups; i++) {
                            int idItem = response.body().getGroupByParticipant().get(i).getId();
                            menu.add(R.id.dynamic_group_menu, idItem,1, response.body().getGroupByParticipant().get(i).getName_group()).setIcon(R.drawable.logo_karate_manager);
                        }

                    }

                }
            }

            @Override
            public void onFailure(Call<GroupsResponse> call, Throwable t) {

            }
        });

    }
    public interface GetAllGroupsByUserCallback{
        void onSuccess(GroupsResponse groupsResponse);
        void onError();
    }

    public void sendUserToCreateGroup(UserResponse userResponse, CreateGroupFragment fragment){
        fragment.recievedUser(userResponse);

    }

    public void sendUserToJoinGroup(UserResponse userResponse, JoinGroupFragment fragment){
        fragment.recievedUser(userResponse);

    }

    public void sendUserGroupToScoring(UserResponse userResponse, int groupSelected, ScoringFragment fragment){
        fragment.recievedUserGroup(userResponse, groupSelected);

    }

    public void sendUserGroupToMarket(UserResponse userResponse, int groupSelected, MarketFragment fragment){
        fragment.recievedUserGroup(userResponse, groupSelected);

    }
    public void sendUserGroupToMyTeam(UserResponse userResponse, int groupSelected, MyTeamFragment fragment){
        fragment.recievedUserGroup(userResponse, groupSelected);

    }



}
