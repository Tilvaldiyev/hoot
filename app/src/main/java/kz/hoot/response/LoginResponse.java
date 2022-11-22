package kz.hoot.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("accessToken")
    @Expose()
    private String accessToken;

    @SerializedName("refreshToken")
    @Expose()
    private String refreshToken;

    @SerializedName("userType")
    @Expose()
    private String userType;

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getUserType() {
        return userType;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "access token='" + accessToken + '\'' + "refresh token='" + refreshToken +  '\'' +
                '}';
    }
}