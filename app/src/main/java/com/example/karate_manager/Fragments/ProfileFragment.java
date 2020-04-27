package com.example.karate_manager.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karate_manager.Models.Prueba;
import com.example.karate_manager.Models.UserModel.UserResponse;
import com.example.karate_manager.Network.APIService;
import com.example.karate_manager.Network.ApiUtils;
import com.example.karate_manager.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static android.os.Environment.getExternalStoragePublicDirectory;


public class ProfileFragment extends Fragment {

    Dialog dialogGalleryPhoto;

    final int CODIGO_PETICION_GALLERY = 1;
    final int CODIGO_PETICION_CAMERA = 2;
    Bitmap imageGallery, imageCamera;
    private com.example.karate_manager.Network.APIService APIService;
    MultipartBody.Part imageToServe;
    String pathToFileCamera, pathToFileGallery, pathChoose, pathSend;
    InputStream stream;
    UserResponse user = new UserResponse(200,null,null );
    private CircleImageView user_image;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.fragment_profile, container, false);

        user_image = (CircleImageView) RootView.findViewById(R.id.user_image);
        dialogGalleryPhoto = new Dialog(getContext());
        APIService = ApiUtils.getAPIService();

        user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenuPopUpGalleryPhoto(view);
            }
        });
        return RootView;
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
            user_image.setImageBitmap(imageCamera);
        }

        if (requestCode == CODIGO_PETICION_GALLERY && resultCode == RESULT_OK) {
            try {
                Uri uri = data.getData();
                pathToFileGallery = getFileNameByUri(getActivity(),uri);
                stream = getActivity().getContentResolver().openInputStream(uri);
                imageGallery = BitmapFactory.decodeStream(stream);
                user_image.setImageBitmap(imageGallery);

                Log.d("tttt", String.valueOf(uri.getPath()));

            } catch (FileNotFoundException e) {
                Toast.makeText(getActivity(), "Imagen no encontrada", Toast.LENGTH_SHORT);
            }
        }

        pathSend = ChooseParameters(pathToFileCamera, pathToFileGallery, pathChoose);

        uploadFile(pathSend,user.getUser().getId());
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

    public MultipartBody.Part uploadFile(String filePath, int id_user){
        File file = new File(filePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("imagen/*"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("photo_profile", file.getName(), requestBody); // vcogemos el nombre de la tabal que hacemos referencia

        APIService.uploadImageUser(part, id_user).enqueue(new Callback<Prueba>() {
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
        if(user!=null ){
            user = userResponse;
        }
    }
}
