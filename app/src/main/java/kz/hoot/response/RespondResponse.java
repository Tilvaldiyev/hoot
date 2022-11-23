package kz.hoot.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RespondResponse {
    @SerializedName("message")
    @Expose()
    private String message;

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "RespondResponse{" +
                "message='" + message + '\'' +
                '}';
    }
}
