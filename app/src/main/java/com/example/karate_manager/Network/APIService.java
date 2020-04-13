package com.example.karate_manager.Network;

import com.example.karate_manager.Models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers("Content-Type: application/json")
    @POST("user")
    Call<User> createUser(@Body User user);
    @POST("user/login")
    Call<User> sendUser(@Body User user);
    @POST("user/password/reset")
    Call<User> recoverPass(@Body User user);
}
