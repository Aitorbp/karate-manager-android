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

import com.example.karate_manager.Fragments.CreateGroupFragment;
import com.example.karate_manager.Fragments.JoinGroupFragment;
import com.example.karate_manager.Fragments.ProfileFragment;
import com.example.karate_manager.Models.UserModel.UserResponse;
import com.example.karate_manager.Network.APIService;
import com.example.karate_manager.Network.ApiUtils;
import com.example.karate_manager.Utils.Storage;
import com.google.android.material.navigation.NavigationView;
import static com.example.karate_manager.Utils.PreferencesUtility.*;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;

    private APIService APIService;
    UserResponse user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);


        navigationView.setNavigationItemSelectedListener(this);
        APIService = ApiUtils.getAPIService();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

       String api_token = Storage.getToken(getApplicationContext());

        getUser(api_token, new GetUserCallback() {
            @Override
            public void onSuccess(UserResponse userResponse) {
                user = userResponse;
            }
            @Override
            public void onError() {
            }
        });

        final Menu menu = navigationView.getMenu();
        menu.add(R.id.dinamyc_group_menu, Menu.NONE,0, "My groups");
        for (int i = 1; i<= 2; i++){

            menu.add(R.id.dinamyc_group_menu, Menu.NONE,0, "Karatekas").setIcon(R.drawable.logo_karate_manager);
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int screen =  menuItem.getItemId();
        changeSecreen(screen);
        return true;
    }

    public void changeSecreen(int screen){

        switch (screen) {
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
                Intent intent = new Intent(this, LoginActivity.class);
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



    public void sendUserToCreateGroup(UserResponse userResponse, CreateGroupFragment fragment){
        fragment.recievedUser(userResponse);

    }

    public void sendUserToJoinGroup(UserResponse userResponse, JoinGroupFragment fragment){
        fragment.recievedUser(userResponse);

    }

    


}
