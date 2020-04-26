package com.example.karate_manager.Fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Environment;
import android.provider.MediaStore;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.karate_manager.MainActivity;
import com.example.karate_manager.Models.GroupModel.GroupResponse;
import com.example.karate_manager.Models.Prueba;
import com.example.karate_manager.Models.UserModel.UserResponse;
import com.example.karate_manager.Network.APIService;
import com.example.karate_manager.Network.ApiUtils;
import com.example.karate_manager.R;
import com.example.karate_manager.Utils.Storage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static android.os.Environment.getExternalStoragePublicDirectory;
import static android.os.Environment.getExternalStorageState;


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
    Bitmap imageGallery, imageCamera;


    MultipartBody.Part imageToServe;
    String pathToFileCamera, pathToFileGallery, pathChoose, pathSend;
    InputStream stream;

    private CircleImageView group_image;

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


        if(Build.VERSION.SDK_INT >= 23){
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }

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

        if ( checkPass(pass)==true &&checkUsername(name)==true && checkImagen(pathSend) == true) {

 //
            registerGroup(name, pass, budget, switch_genre, 0);

        }

    }

    private boolean checkImagen(String pathSend){
      if(pathSend ==null){
          (Toast.makeText(getContext(), "Imagen group must be uploaded to create a group ", Toast.LENGTH_LONG)).show();
          return false;
      }
      return true;
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
                    imageToServe = uploadFile(pathSend,response.body().getGroup().getId());
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
        intoToGallery();
        intoToCamera();


    }

    /***
     * Boton para acceder a la galeria y coger una foto
     */
    public void intoToGallery(){
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


    private void intoToCamera(){
        Button camera;
        camera = (Button) dialogGalleryPhoto.findViewById(R.id.camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File photoFile = null;
                photoFile = createPhotoFile();

                if(photoFile != null){
                    pathToFileCamera = photoFile.getAbsolutePath();

                    Log.d("Path absolute pic", String.valueOf(pathToFileCamera));
                    Uri photoURI = FileProvider.getUriForFile(getActivity(),"android.support.v4.content.FileProvider",photoFile);
                    Log.d("URI absolute pic", String.valueOf(photoURI));
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(intent, CODIGO_PETICION_CAMERA);
                }
                dialogGalleryPhoto.dismiss();
            }
        });
    }
    private File createPhotoFile(){
        String name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        Log.d("Storage Directory pic", String.valueOf(storageDir));
        File image = null;
        try {
           image = File.createTempFile(name, ".jpg", storageDir);
           Log.d("Path imagen", String.valueOf(image));
        } catch (IOException e) {
                e.printStackTrace();
        }
        return image;
        }


    /***
     * Código para poner la foto en el imageview
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == CODIGO_PETICION_CAMERA && resultCode == RESULT_OK) {
            imageCamera = BitmapFactory.decodeFile(pathToFileCamera);
            group_image.setImageBitmap(imageCamera);
        }

        if (requestCode == CODIGO_PETICION_GALLERY && resultCode == RESULT_OK) {
            try {
                Uri uri = data.getData();
                pathToFileGallery = getFileNameByUri(getActivity(),uri);
                stream = getActivity().getContentResolver().openInputStream(uri);
                imageGallery = BitmapFactory.decodeStream(stream);
                group_image.setImageBitmap(imageGallery);

                Log.d("tttt", String.valueOf(uri.getPath()));

            } catch (FileNotFoundException e) {
                Toast.makeText(getActivity(), "Imagen no encontrada", Toast.LENGTH_SHORT);
            }
        }

        pathSend = ChooseParameters(pathToFileCamera, pathToFileGallery, pathChoose);
    }

    /***
     * Metodo pra que te elija la foto de la galeria o de la camara
     */
    public String ChooseParameters(String pathToFileCamera, String pathToFileGallery, String pathChoose){
        if(pathToFileCamera!= null && pathToFileGallery ==null){
            pathChoose = pathToFileCamera;
        }
        if(pathToFileCamera== null && pathToFileGallery !=null){
            pathChoose = pathToFileGallery;
        }

        return pathChoose;
    }



    public MultipartBody.Part uploadFile(String filePath, int id_group){
        File file = new File(filePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("imagen/*"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("picture_group", file.getName(), requestBody); // vcogemos el nombre de la tabal que hacemos referencia

        APIService.uploadImage(part, id_group).enqueue(new Callback<Prueba>() {
            @Override
            public void onResponse(Call<Prueba> call, Response<Prueba> response) {
                Log.d("Imagen send to serve", "Imagen send to serve");
            }

            @Override
            public void onFailure(Call<Prueba> call, Throwable t) {
                Log.d("Fallo send image", "Fallo send image");
            }
        });
        return  part;
    }

    //Metodo par acoger el path de la galería
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static String getFileNameByUri(Context context, Uri uri){
        String fileName="unknown";//default fileName
        Uri filePathUri = uri;
        if (uri.getScheme().toString().compareTo("content")==0){
            Cursor cursor =context.getContentResolver().query(uri,null, null, null, null);
            if (cursor.moveToFirst()){
                int column_index =
                        cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                filePathUri = Uri.parse(cursor.getString(column_index));
                if(filePathUri == null){
                    fileName = "xxx.png";//load a default Image from server
                }else{
                    fileName = filePathUri.getPath().toString();
                }
            }
        }else
        if (uri.getScheme().compareTo("file")==0){
            fileName = filePathUri.getPath().toString();
        }else{
            fileName = fileName+"_"+filePathUri.getPath();
        }
        return fileName;
        }


    public void recievedUser(UserResponse userResponse) {
        if(user!=null){
            user =userResponse;
        }

    }
}
