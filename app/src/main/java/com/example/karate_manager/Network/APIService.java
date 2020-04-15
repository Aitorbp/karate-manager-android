package com.example.karate_manager.Network;

import com.example.karate_manager.Models.Group;
import com.example.karate_manager.Models.LoginResponse;
import com.example.karate_manager.Models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

  //  @Headers("Content-Type: application/json")
    @FormUrlEncoded
    @POST("user")
    Call<LoginResponse> createUser(
            @Field("email") String email,
            @Field("password") String password,
            @Field("name") String user_name);

    @FormUrlEncoded
    @POST("user/login")
    Call<LoginResponse> sendUser(
            @Field("email") String email,
            @Field("password") String password
            );

    @POST("user/password/reset")
    Call<User> recoverPass(@Body User user);

    @POST("group")
    Call<Group> createGroup(@Body Group group);
}
