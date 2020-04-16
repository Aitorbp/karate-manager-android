package com.example.karate_manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;

import com.example.karate_manager.Fragments.CreateGroupFragment;
import com.example.karate_manager.Fragments.JoinGroupFragment;
import com.example.karate_manager.Fragments.ProfileFragment;
import com.example.karate_manager.Utils.Storage;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        String token = Storage.getToken(getApplicationContext());
        Log.d("TOKEEN RECIBIDO", token);
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
                break;

            case R.id.nav_create_group:
                CreateGroupFragment createGroupFragment = new CreateGroupFragment();
                addFragment(createGroupFragment);
                break;

            case R.id.nav_log_out:
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



}
