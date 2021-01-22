package com.youtube.sorcjc.billetero.io;

import com.youtube.sorcjc.billetero.io.response.LoginResponse;
import com.youtube.sorcjc.billetero.io.response.SimpleResponse;
import com.youtube.sorcjc.billetero.io.response.SoldTicketsResponse;
import com.youtube.sorcjc.billetero.model.Lottery;
import com.youtube.sorcjc.billetero.model.TicketBody;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface MyApiService {

    @Headers("Accept: application/json")
    @GET("lotteries")
    Call<ArrayList<Lottery>> getLotteries(
            @Header("Authorization") String authHeader
    );

    @Headers("Accept: application/json")
    @GET("tickets")
    Call<SoldTicketsResponse> getTickets(
            @Header("Authorization") String authHeader
    );

    @FormUrlEncoded
    @POST("auth/login")
    @Headers("Accept: application/json")
    Call<LoginResponse> postLogin(
            @Field("email") String email,
            @Field("password") String password
    );

    @Headers("Accept: application/json")
    @POST("tickets")
    Call<SimpleResponse> storeTicket(
            @Header("Authorization") String authHeader,
            @Body TicketBody ticketBody
    );

}