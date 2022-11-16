package kz.hoot.request;

import kz.hoot.utils.Config;
import kz.hoot.utils.Hoot;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Servicey {
    private static Retrofit.Builder retrofitBuilder =
            new Retrofit.Builder()
                    .baseUrl(Config.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = retrofitBuilder.build();

    public static Hoot hoot = retrofit.create(Hoot.class);

    public static Hoot getHoot() {
        return hoot;
    }
}
