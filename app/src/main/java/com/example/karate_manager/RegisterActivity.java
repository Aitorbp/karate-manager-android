package com.example.karate_manager;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.karate_manager.Models.UserModel.UserResponse;
import com.example.karate_manager.Network.APIService;
import com.example.karate_manager.Network.ApiUtils;
import com.example.karate_manager.Utils.Storage;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    EditText input_email, input_password, input_username, input_confirm_password ;
    Button btn_register;
    Dialog dialogLoading;
    private APIService APIService;
    Storage storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        input_email = findViewById(R.id.input_email);
        input_password = findViewById(R.id.input_password);
        input_username = findViewById(R.id.input_username);
        btn_register = findViewById(R.id.btn_register);
        input_confirm_password = findViewById(R.id.input_confirm_password);
        dialogLoading = new Dialog(RegisterActivity.this);
        APIService = ApiUtils.getAPIService();
        dialogLoading.setContentView(R.layout.popup_loading);


        //Click Listener del botón del login
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = input_email.getText().toString();
                String pass = input_password.getText().toString();
                String username = input_username.getText().toString();
                String confirmPass = input_confirm_password.getText().toString();


                checkInputsRegister(username, email, pass,  confirmPass);


            }
        });
    }
    //Method to check inputs of the login view
    private void checkInputsRegister(String username, String email, String pass, String confirmPass) {

        //Check if inputs are empty
        if(username.isEmpty()){
            Log.d("valor del email", "ëdededede");
            input_username.setError("Empty inputs are not allowed");
        }else{
            Log.d("valor del email", "no está vacio");
            checkUsername(username);

        }

        if (email.isEmpty()){
            input_email.setError("Empty inputs are not allowed");
        }else{
            checkEmail(email);

        }

        if(pass.isEmpty()){
            confirmPassword(pass, confirmPass);
            input_password.setError("Empty inputs are not allowed");
        }else{
            checkPass(pass);

        }

        if (checkEmail(email)==true
                && checkPass(pass)==true
                && confirmPassword(pass, confirmPass)==true
                &&checkUsername(username)==true) {


            registerPOST(pass, email, username);
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
    private boolean checkUsername(String userName){
        if (userName.length() <= 3) {
            input_username.setError("The username must be greater than 3 characters");
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
            input_password.setError("The password must contain at least one number");
            return false;
        }

        return true;
    }


    private boolean confirmPassword(String password , String confirmPassword){

        if(!password.equals(confirmPassword) || password.isEmpty() ){
            input_confirm_password.setError("Passwords must match");
            Log.d("tusmuertos", password);
            Log.d("tusmuertos2", confirmPassword);
            return false;
        }
        return true;
    }


    private void registerPOST(String password, String email, String user_name)
    {
        Log.d("eeer","addede");
        Log.d("OBJETO:", email);
        dialogLoading.show();
        dialogLoading.setCancelable(false);

      //  User user1 = new User(password, email, user_name);
        APIService.createUser(email, password, user_name).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                dialogLoading.dismiss();
                if(response.isSuccessful()) {

                  Log.d("RESPUESTA DEL MENSAJE", response.body().getUser().getApitoken());
                    Storage.saveToken(getApplicationContext(),response.body().getUser().getApitoken());
                    storage.setLoggedIn(getApplicationContext(), true);

                    (Toast.makeText(getApplicationContext(), "Welcome to PetIt", Toast.LENGTH_LONG)).show();

                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class );
                    startActivity(intent);

                }else{           Toast.makeText(getApplicationContext(), "The user is already register with this email", Toast.LENGTH_SHORT).show();}
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();

                dialogLoading.dismiss();
            }
        });


    }

}
