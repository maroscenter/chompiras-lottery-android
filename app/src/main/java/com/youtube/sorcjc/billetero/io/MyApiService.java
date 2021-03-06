package com.youtube.sorcjc.billetero.io;

import com.youtube.sorcjc.billetero.io.response.EarningsResponse;
import com.youtube.sorcjc.billetero.io.response.LoginResponse;
import com.youtube.sorcjc.billetero.io.response.SimpleResponse;
import com.youtube.sorcjc.billetero.io.response.SoldTicketsResponse;
import com.youtube.sorcjc.billetero.io.response.TicketResponse;
import com.youtube.sorcjc.billetero.model.BalanceMovement;
import com.youtube.sorcjc.billetero.model.Lottery;
import com.youtube.sorcjc.billetero.model.Ticket;
import com.youtube.sorcjc.billetero.model.TicketBody;
import com.youtube.sorcjc.billetero.model.Winner;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MyApiService {

    @Headers("Accept: application/json")
    @GET("earnings")
    Call<EarningsResponse> getEarnings(
            @Header("Authorization") String authHeader
    );

    @Headers("Accept: application/json")
    @GET("winners")
    Call<ArrayList<Winner>> getWinners(
            @Header("Authorization") String authHeader
    );

    @Headers("Accept: application/json")
    @GET("paid/{winnerId}")
    Call<SimpleResponse> payWinner(
            @Header("Authorization") String authHeader,
            @Path("winnerId") int winnerId
    );

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

    @Headers("Accept: application/json")
    @GET("tickets/{ticket}")
    Call<TicketResponse> getTicket(
            @Header("Authorization") String authHeader,
            @Path("ticket") String ticketId
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

    @Headers("Accept: application/json")
    @POST("tickets/{ticket}/delete")
    Call<SimpleResponse> deleteTicket(
            @Header("Authorization") String authHeader,
            @Path("ticket") String ticketId
    );

    @Headers("Accept: application/json")
    @GET("balance_movements")
    Call<ArrayList<BalanceMovement>> getBalanceMovements(
            @Header("Authorization") String authHeader
    );
}