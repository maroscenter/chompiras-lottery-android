package com.youtube.sorcjc.billetero.io;

import com.youtube.sorcjc.billetero.io.response.LoginResponse;
import com.youtube.sorcjc.billetero.io.response.SimpleResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface MyApiService {

//    @GET("responsables.php")
//    Call<ResponsableResponse> getResponsables();

    @FormUrlEncoded
    @POST("login")
    @Headers("Accept: application/json")
    Call<LoginResponse> postLogin(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("sent-list")
    Call<SimpleResponse> postSentList(
            @Field("times_sold[]") ArrayList<Integer> times_sold,
            @Field("user_id") int user_id
    );

}