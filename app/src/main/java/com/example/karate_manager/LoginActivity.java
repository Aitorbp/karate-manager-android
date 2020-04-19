package com.example.karate_manager;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.karate_manager.Models.UserModel.UserResponse;
import com.example.karate_manager.Network.APIService;
import com.example.karate_manager.Network.ApiUtils;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.example.karate_manager.Utils.Storage;

public class LoginActivity extends AppCompatActivity {

    EditText input_email, input_password;
    Button loginBtn;
    LinearLayout popup_loading;
    private APIService APIService;
    Dialog dialogLoading;
    Storage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        input_email = findViewById(R.id.input_email);
        input_password = findViewById(R.id.input_password);
        loginBtn = findViewById(R.id.btn_login);
        APIService = ApiUtils.getAPIService();

        popup_loading = findViewById(R.id.popup_loading);


        dialogLoading = new Dialog(LoginActivity.this);
       dialogLoading.setContentView(R.layout.popup_loading);


        if(storage.getLoggedStatus(getApplicationContext())) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }

        //Click Listener del botón del login
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = input_email.getText().toString();
                String pass = input_password.getText().toString();
                checkInputs(email, pass);


            }
        });
    }



    //Method to check inputs of the login view
    private void checkInputs(String email, String pass) {

        //Check if inputs are empty
        if (email.isEmpty() || pass.isEmpty()) {
            input_email.setError("Empty inputs are not allowed");

        }
        else if(checkEmail(email) && checkPass(pass)){

          loginPOST(pass,email);
        }else{
            Log.d("tusmuertos", email);
        }
    }
    private boolean checkEmail(String email) {
        //Check email comparing to the email that arrived through parameters
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            input_email.setError("Introduce a correct email");
            return false;
        }
        return true;
    }

    private boolean checkPass(String pass) {
        if (pass.length() < 8) {
            input_password.setError("The password must be greater than 8 characters");
            return false;
        }
        if (!pass.matches("(?=.*[0-8]).*")) {
            input_password.setError("The password must contains at least one number");
            return false;
        }


        return true;
    }
    //Click from login to register view
    public void link_signup(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    public void link_forgot_password(View view) {
      //  startActivity(new Intent(this, ForgotPassword.class));
    }



    private void loginPOST(String password, String email)
    {
        dialogLoading.show();
        dialogLoading.setCancelable(false);

        APIService.sendUser(email, password).enqueue(new Callback<UserResponse>() {

            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                dialogLoading.dismiss();
                if(response.isSuccessful()) {
                    Log.d("RESPUESTA DEL MENSAJE", response.body().getUser().getApitoken());
                    Storage.saveToken(getApplicationContext(),response.body().getUser().getApitoken());
                    storage.setLoggedIn(getApplicationContext(), true);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class );
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }else{
                    Toast.makeText(getApplicationContext(), "Incorrect username or password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "We are having problems with the server, try again later", Toast.LENGTH_SHORT).show();
                Log.d("RESPUESTA DEL MENSAJE", "ERROR");
                dialogLoading.dismiss();
            }
        });
    }
}
