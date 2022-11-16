package kz.hoot.utils;

import kz.hoot.model.RegistrationModel;
import kz.hoot.response.LoginResponse;
import kz.hoot.response.RegistrationResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface Hoot {
    @POST("auth/login")
    Call<LoginResponse> authorization(
            @Header("Authorization") String basicAuth
    );

    @POST("auth/registration")
    Call<RegistrationResponse> registration(
            @Body RegistrationModel registrationModel
            );
}
