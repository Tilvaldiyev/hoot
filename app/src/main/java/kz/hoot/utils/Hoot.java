package kz.hoot.utils;

import java.util.List;

import kz.hoot.model.Actor;
import kz.hoot.model.Cast;
import kz.hoot.model.Registration;
import kz.hoot.model.User;
import kz.hoot.response.LoginResponse;
import kz.hoot.response.RegistrationResponse;
import kz.hoot.response.RespondResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Hoot {
    @POST("auth/login")
    Call<LoginResponse> authorization(
            @Header("Authorization") String basicAuth
    );

    @POST("auth/registration")
    Call<RegistrationResponse> registration(
            @Body Registration registrationModel
            );

    @GET("actors/all")
    Call<List<Actor>> getActors();

    @GET("actors/favourites")
    Call<List<Actor>> getFavActors(
            @Header("Authorization") String token
    );

    @GET("user/info")
    Call<User> getUserInfo(
            @Header("Authorization") String token
    );

    @GET("public/casts")
    Call<List<Cast>> getCasts();

    @POST("cast/respond")
    Call<RespondResponse> respondToCast(@Header("Authorization") String token, @Query("castId") Long castId);

    @POST("auth/refresh")
    Call<LoginResponse> refresh(
            @Header("Authorization") String bearerAuth
    );

    @POST("actors/add/favourite")
    Call<String> addActorToFavList(
            @Header("Authorization") String bearerAuth,
            @Query("actorId") int actorId
    );
}
