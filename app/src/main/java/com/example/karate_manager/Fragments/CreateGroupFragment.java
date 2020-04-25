package com.example.karate_manager.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karate_manager.MainActivity;
import com.example.karate_manager.Models.GroupModel.Group;
import com.example.karate_manager.Models.GroupModel.GroupResponse;
import com.example.karate_manager.Models.UserModel.UserResponse;
import com.example.karate_manager.Network.APIService;
import com.example.karate_manager.Network.ApiUtils;
import com.example.karate_manager.R;
import com.example.karate_manager.Utils.Storage;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;


public class CreateGroupFragment extends Fragment {

    private EditText password_group, name_group;
    private Spinner spinner_budget;
    private Button btn_create_group;
    private Switch switch_genre;
    boolean genre = false;
    Dialog dialogLoading;
    Dialog dialogGalleryPhoto;
    private  APIService APIService;

    final int CODIGO_PETICION_GALLERY = 1;
    final int CODIGO_PETICION_CAMERA = 2;
    Bitmap imageGallery, imageCamera, imageChoose, imageSend;
    private CircleImageView group_image;
    InputStream stream;
    UserResponse user = new UserResponse(200,null,null );
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

        group_image = (CircleImageView) RootView.findViewById(R.id.group_image);
        dialogGalleryPhoto = new Dialog(getContext());

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

        Log.d("AAAAAAAAAAAAAAAAA", String.valueOf(user.getUser().getId()));
        btn_create_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = name_group.getText().toString();
                String pass = password_group.getText().toString();
                int budget = Integer.parseInt(spinner_budget.getSelectedItem().toString());
                String switch_genre = String.valueOf(genre);

                checkInputsRegister(name, pass, budget, switch_genre);
            }
        });

        group_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenuPopUpGalleryPhoto(view);
            }
        });

        return RootView;
    }

    //Method to check inputs of the login view
    private void checkInputsRegister(String name,  String pass, int budget, String switch_genre) {

        //Check if inputs are empty
        if(name.isEmpty()){
            Log.d("valor del email", "ëdededede");
            name_group.setError("Empty inputs are not allowed");
        }else{
            Log.d("valor del email", "no está vacio");
            checkUsername(name);

        }
        if(pass.isEmpty()){
            password_group.setError("Empty inputs are not allowed");
        }else{
            checkPass(pass);

        }

        if ( checkPass(pass)==true &&checkUsername(name)==true) {


            registerGroup(name, pass, budget, switch_genre, 0);
        }

    }


    private boolean checkUsername(String userName){
        if (userName.length() <= 3) {
            name_group.setError("The name group must be greater than 3 characters");
            return false;
        }
        return true;
    }


    private boolean checkPass(String pass) {
        if (pass.length() < 8) {
            password_group.setError("The password must be greater than 8 characters");
            return false;
        }
        if (!pass.matches("(?=.*[0-8]).*")) {
            password_group.setError("The password must contain at least one number");
            return false;
        }

        return true;
    }

    private void registerGroup(String name, String pass,  int budget, String switch_genre, int points)
    {
        Log.d("eeer","addede");

        String id_user = String.valueOf(user.getUser().getId());
        String api_token = Storage.getToken(getContext());
        dialogLoading.show();
        dialogLoading.setCancelable(false);
   //     Group group = new Group(name,budget,switch_genre,id_user,pass,points);

        APIService.createGroup(api_token,name,switch_genre,id_user,pass,String.valueOf(budget)).enqueue(new Callback<GroupResponse>() {
            @Override
            public void onResponse(Call<GroupResponse> call, Response<GroupResponse> response) {
                dialogLoading.dismiss();
                if(response.isSuccessful()) {
                    Log.d("ID_GROUP in registerGr", String.valueOf(response.body().getGroup().getId()));
                    (Toast.makeText(getContext(), "Group created", Toast.LENGTH_LONG)).show();
                    Storage.saveGroupPrincipal(getActivity(), response.body().getGroup().getId());
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);

                }else{
                    (Toast.makeText(getContext(), "There was an error. Maybe the group already registered.", Toast.LENGTH_LONG)).show();
                }
            }

            @Override
            public void onFailure(Call<GroupResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();

                dialogLoading.dismiss();


            }
        });


    }
    public void showMenuPopUpGalleryPhoto(View v) {
        TextView textClose;

        dialogGalleryPhoto.setContentView(R.layout.popup_choose_picture);
        textClose = (TextView) dialogGalleryPhoto.findViewById(R.id.closePopUp);
  

        textClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogGalleryPhoto.dismiss();
            }
        });
        dialogGalleryPhoto.show();
        IntoToGallery();
        IntoToCamera();
    }

    /***
     * Boton para acceder a la galeria y coger una foto
     */
    public void IntoToGallery(){
        Button gallery;

        gallery =(Button) dialogGalleryPhoto.findViewById(R.id.gallery);

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, CODIGO_PETICION_GALLERY);
                dialogGalleryPhoto.dismiss();
            }
        });
    }
    /***
     * Boton para acceder a la camara y coger una foto
     */
    public void IntoToCamera(){
        Button camera;
        camera = (Button) dialogGalleryPhoto.findViewById(R.id.camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CODIGO_PETICION_CAMERA);
                dialogGalleryPhoto.dismiss();
            }
        });
    }

    /***
     * Código para poner la foto en el imageview
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CODIGO_PETICION_CAMERA && resultCode == RESULT_OK){
            Bundle bundle = data.getExtras();
            Bitmap image = (Bitmap) bundle.get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            imageCamera = BitmapFactory.decodeByteArray(byteArray, 0,
                    byteArray.length);
            group_image.setImageBitmap(imageCamera);
            Log.d("iiiiii", String.valueOf(group_image));

        }

        if (requestCode == CODIGO_PETICION_GALLERY && resultCode == RESULT_OK) {
            try {
                Uri uri = data.getData();
                stream = getActivity().getContentResolver().openInputStream(uri);
                imageGallery = BitmapFactory.decodeStream(stream);
                group_image.setImageBitmap(imageGallery);
                Log.d("tttt", String.valueOf(group_image));

            } catch (FileNotFoundException e) {
                Toast.makeText(getActivity(),"Imagen no encontrada", Toast.LENGTH_SHORT);
            }
        }

        imageSend = ChooseParameters(imageCamera, imageGallery, imageChoose);
    }

    /***
     * Metodo pra que te elija la foto de la galeria o de la camara
     */
    public Bitmap ChooseParameters(Bitmap imageCamera, Bitmap imageGallery, Bitmap imageChoose){
        if(imageCamera!= null && imageGallery ==null){
            imageChoose = imageCamera;
        }
        if(imageCamera== null && imageGallery !=null){
            imageChoose = imageGallery;
        }

        return imageChoose;
    }
    public void recievedUser(UserResponse userResponse) {
        if(user!=null){
            user =userResponse;
        }

    }
}
