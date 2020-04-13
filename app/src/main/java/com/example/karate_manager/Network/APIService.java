package com.example.karate_manager.Network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers("Content-Type: application/json")
    @POST("api/user")
    Call<User> createUser(@Body User user);
    @POST("api/user/login")
    Call<User> sendUser(@Body User user);
    @POST("api/user/password/reset")
    Call<User> recoverPass(@Body User user);
}
