package com.example.karate_manager.Network;

import com.example.karate_manager.Models.GroupModel.Group;
import com.example.karate_manager.Models.GroupModel.GroupsResponse;
import com.example.karate_manager.Models.ParticipantModel.ParticipantResponse;
import com.example.karate_manager.Models.UserModel.UserResponse;
import com.example.karate_manager.Models.UserModel.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {

  //  @Headers("Content-Type: application/json")
    @FormUrlEncoded
    @POST("user")
    Call<UserResponse> createUser(
            @Field("email") String email,
            @Field("password") String password,
            @Field("name") String user_name);

    @FormUrlEncoded
    @POST("user/login")
    Call<UserResponse> sendUser(
            @Field("email") String email,
            @Field("password") String password
            );


    @GET("user/one")
    Call<UserResponse> getUserByToken(@Query("api_token") String api_token);


  @POST("user/password/reset")
  Call<User> recoverPass(@Body User user);


  //GROUP ENDPOINTS
    @POST("group")
    Call<Group> createGroup(@Body Group group , @Query("api_token") String authToken);

  @GET("participant/get/groupsByParticipant/{id_user}")
  Call<GroupsResponse> getGroupsByUser(@Path("id_user") String id_user);

  //PARTICIPANT ENDPOINTS
  @FormUrlEncoded
  @POST("participant")
  Call<ParticipantResponse> createParticipantInGroup(
          @Field("password_group") String password_group,
          @Field("name_group") String name_group,
          @Field("id_user") String id_user,
          @Query("api_token") String authToken);

}
