package com.example.karate_manager.Network;

import com.example.karate_manager.Models.BidModel.BidResponse;
import com.example.karate_manager.Models.BidModel.BidRivalsResponse;
import com.example.karate_manager.Models.BidModel.BidToFromRivalsResponse;
import com.example.karate_manager.Models.GroupModel.GroupResponse;
import com.example.karate_manager.Models.GroupModel.GroupsResponse;
import com.example.karate_manager.Models.JoinGroupResponse.JoinGroupResponse;
import com.example.karate_manager.Models.KaratekaModel.MarketResponse;
import com.example.karate_manager.Models.ParticipantModel.ParticipantResponse;
import com.example.karate_manager.Models.Prueba;
import com.example.karate_manager.Models.SaleModel.StartingResponse;
import com.example.karate_manager.Models.UserModel.UserResponse;
import com.example.karate_manager.Models.UserModel.User;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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

//Images
  @Multipart
  @POST("upload/image")
  Call<Prueba> uploadImage(
                  @Part MultipartBody.Part part,
                  @Part ("id") int id_group
          );

  @Multipart
  @POST("upload/image/user")
  Call<Prueba> uploadImageUser(
          @Part MultipartBody.Part part,
          @Part ("id") int id_user
  );
  //GROUP ENDPOINTS
    @FormUrlEncoded
    @POST("group")
    Call<GroupResponse> createGroup(@Query("api_token") String authToken,
                                    @Field("name_group") String name_group,
                                    @Field("gender") String gender,
                                    @Field("id_user") String id_user,
                                    @Field("password_group") String password_group,
                                    @Field("budget") String budget
                            );

  @GET("participant/get/groupsByParticipant/{id_user}")
  Call<GroupsResponse> getGroupsByUser(@Path("id_user") String id_user);


  @GET("group/one/{id}")
  Call<GroupResponse> getGroupByParticipant(@Path("id") int id);

  //PARTICIPANT ENDPOINTS
  @FormUrlEncoded
  @POST("participant")
  Call<JoinGroupResponse> createParticipantInGroup(
          @Field("password_group") String password_group,
          @Field("name_group") String name_group,
          @Field("id_user") String id_user,
          @Query("api_token") String authToken);


  @GET("participant/get/one/{id_user}/{id_group}")
  Call<ParticipantResponse> getParticipant(@Path("id_user") int id_user, @Path("id_group") int id_group);

  @GET("participant/getall/{id_group}")
  Call<ParticipantResponse> getParticipantsByGroup(@Path("id_group") String id_group);

  //MARKET ENDPOINTS
  @GET("market/show/karatekas/{id_group}")
  Call<MarketResponse> getKaratekasInSaleByGroup(@Path("id_group") String id_group);


  @GET("karatekas/byparticipant/{id_participants}")
  Call<MarketResponse> getKaratekasByParticipant(@Path("id_participants") int id_participant);

  @DELETE("sell/karateka/{id_participants}/{id_group}/{id_karatekas}")
  Call<ParticipantResponse> sellKaratekaByParticipant(@Path("id_participants") int id_participant,
                                                      @Path("id_group") int id_group,
                                                      @Path("id_karatekas") int id_karatekas);
//Starting

@GET("karatekas/starting/{id_participants}")
Call<MarketResponse> getStartingKaratekaByParticipant(@Path("id_participants") int id_participants);

  @GET("karatekas/alternate/{id_participants}")
  Call<MarketResponse> getAlternateKaratekaByParticipant(@Path("id_participants") int id_participants);
  @FormUrlEncoded
  @POST("karatekas/update/starting")
  Call<StartingResponse> postStartingKarateka(@Field("id_participants") int id_participants,
                                            @Field("id_karatekas") int id_karatekas);
  @FormUrlEncoded
  @POST("karatekas/update/alternate")
  Call<StartingResponse> postAlternateKarateka(@Field("id_participants") int id_participants,
                                               @Field("id_karatekas") int id_karatekas);
  //BID
  //DO BID
  @FormUrlEncoded
  @POST("bid")
  Call<BidResponse> createBidInGroup(
          @Field("id_karatekas") int id_karateka,
          @Field("id_participant") int id_participant,
          @Field("bid") int bid,
          @Field("id_group") int id_group);

  //BID BETWEEN RIVALS
  @FormUrlEncoded
  @POST("bid/rivals/create")
  Call<BidRivalsResponse> createBidRivals(
          @Field("id_participant_bid_send") int id_participant_bid_send,
          @Field("id_participant_bid_receive") int id_participant_bid_receive,
          @Field("id_karateka") int id_karateka,
          @Field("bid_rival") int bid_rival
  );


  @GET("bid/to/rivals/{id_participant_bid_send}")
  Call<BidToFromRivalsResponse> myBidsToRivals(@Path("id_participant_bid_send") int id_participant_bid_send);

  @GET("bid/from/rivals/{id_participant_bid_receive}")
  Call<BidToFromRivalsResponse> myBidsRecieveFromToRivals(@Path("id_participant_bid_receive") int id_participant_bid_receive);

  @GET("bid/rivals/accepted/{id_participant_bid_send}/{id_participant_bid_receive}/{id_karateka}")
  Call<BidToFromRivalsResponse> acceptBidRival(@Path("id_participant_bid_send") int id_participant_bid_send,
                                               @Path("id_participant_bid_receive") int id_participant_bid_receive,
                                               @Path("id_karateka") int id_karateka);
}
