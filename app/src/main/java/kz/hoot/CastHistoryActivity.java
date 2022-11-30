package kz.hoot;

import static kz.hoot.request.Servicey.hoot;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import kz.hoot.adapter.CastHistoryAdapter;
import kz.hoot.model.Cast;
import kz.hoot.model.User;
import kz.hoot.response.LoginResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CastHistoryActivity extends AppCompatActivity implements CastHistoryAdapter.CastHistoryActivityService{
    private CastHistoryAdapter castHistoryAdapter;
    private BottomNavigationView bottomNavigationView;
    private LinearLayout linearLayout;
    private TextView countTxt;
    private RecyclerView castsRecView;
    private static String token;
    private static String refreshToken;
    private int respondResult = 0;
    private ImageView avatar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_casts_history);
        SharedPreferences preferences = getSharedPreferences("auth", MODE_PRIVATE);
        token = preferences.getString("token", "");
        refreshToken = preferences.getString("refreshToken", "");

        initViews();
        initBottomNav();
        initRecView();
        getCasts();
        getUserInfo();
    }

    private void initViews() {
        bottomNavigationView = findViewById(R.id.bottom_nav);
        countTxt = findViewById(R.id.casts_history_count_txt);
        castsRecView = findViewById(R.id.casts_history_rec_view);
        avatar = findViewById(R.id.cast_activity_avatar);
    }

    private void initRecView() {
        castHistoryAdapter = new CastHistoryAdapter(this, this);
        castsRecView.setAdapter(castHistoryAdapter);
        castsRecView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
    }

    private void initBottomNav() {
        bottomNavigationView.setSelectedItemId(R.id.actor_item__fav_btn);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottom_nav__actors_btn:
                        Intent intent = new Intent(CastHistoryActivity.this, CastActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.bottom_nav__castings_btn:
                        intent = new Intent(CastHistoryActivity.this, CastHistoryActivity.class);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });
    }
    private void getCasts() {
        Call<List<Cast>> responseCall = hoot.getCasts();

        responseCall.enqueue(new Callback<List<Cast>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<List<Cast>> call, Response<List<Cast>> response) {
                if (response.code() == 200) {
                    ArrayList<Cast> casts = (ArrayList<Cast>) response.body();
                    for (int i = 0; i < casts.size(); i++) {
                        System.out.println(casts.get(i).toString());
                    }
                    castHistoryAdapter.setCasts(casts);
                    countTxt.setText("Найдено " + castHistoryAdapter.getItemCount());
                } else {
                    try {
                        Toast.makeText(CastHistoryActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        Log.v("Tag", "error" + response.errorBody().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Cast>> call, Throwable t) {
                System.out.println("Ошибка запроса - " + t.getMessage());
                System.out.println("Запрос - " + call);
            }
        });
    }

    private void refreshToken() {
        Call<LoginResponse> responseCall = hoot.refresh(refreshToken);

        responseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.code() == 200) {
                    token = response.body().getAccessToken();
                } else {
                    try {
                        Log.v("Tag", "error" + response.code());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.v("Tag", "error" + t.toString());
            }
        });
    }

    private void getUserInfo () {
        Call<User> responseCall = hoot.getUserInfo("Bearer " + token);
        responseCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    User user = response.body();
                    byte[] decodedString = Base64.decode(user.getAvatar(), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    avatar.setImageBitmap(decodedByte);
                } else {
                    try {
                        Toast.makeText(CastHistoryActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        Log.v("Tag", "error" + response.errorBody().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}
