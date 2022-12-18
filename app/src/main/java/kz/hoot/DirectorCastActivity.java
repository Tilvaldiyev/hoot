package kz.hoot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import kz.hoot.adapter.CastAdapter;
import kz.hoot.model.Cast;
import kz.hoot.model.Invite;
import kz.hoot.model.User;
import kz.hoot.response.LoginResponse;
import kz.hoot.response.RespondResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static kz.hoot.request.Servicey.hoot;

public class DirectorCastActivity extends AppCompatActivity {

    private CastAdapter castAdapter;
    private BottomNavigationView bottomNavigationView;
    private RecyclerView castsRecView;
    private static String token;
    private ImageView avatar;
    private RelativeLayout mainContent;
    private ProgressBar loadingGif;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_director_cast);

        SharedPreferences preferences = getSharedPreferences("auth", MODE_PRIVATE);
        token = preferences.getString("token", "");

        initViews();
        initBottomNav();
        initRecView();
        getCasts();
        getUserInfo();
    }

    private void initViews() {
        bottomNavigationView = findViewById(R.id.bottom_nav);
        castsRecView = findViewById(R.id.activity_director_cast__recview);
        avatar = findViewById(R.id.activity_director_cast__avatar);
        mainContent = findViewById(R.id.activity_director_cast__main_content);
        loadingGif = findViewById(R.id.activity_director_cast__progress_bar);
    }

    private void initRecView() {
        castAdapter = new CastAdapter(this,true);
        castsRecView.setAdapter(castAdapter);
        castsRecView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
    }

    private void initBottomNav() {
        bottomNavigationView.setSelectedItemId(R.id.bottom_nav__castings_btn);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottom_nav__actors_btn:
                        Intent intent = new Intent(DirectorCastActivity.this, ActorsActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.bottom_nav__favorites_btn:
                        intent = new Intent(DirectorCastActivity.this, FavouriteActorActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.bottom_nav__create_btn:
                        intent = new Intent(DirectorCastActivity.this, CreateCastActivity.class);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });
    }

    private void getCasts() {
        Call<List<Cast>> responseCall = hoot.getDirectorCasts("Bearer " + token);

        responseCall.enqueue(new Callback<List<Cast>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<List<Cast>> call, Response<List<Cast>> response) {
                if (response.code() == 200) {
                    ArrayList<Cast> casts = (ArrayList<Cast>) response.body();
                    castAdapter.setCasts(casts);
                    loadingGif.setVisibility(View.GONE);
                    mainContent.setVisibility(View.VISIBLE);
                } else {
                    try {
                        Toast.makeText(DirectorCastActivity.this, "Произошла ошибка", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Cast>> call, Throwable t) {
                Toast.makeText(DirectorCastActivity.this, "Произошла ошибка", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(DirectorCastActivity.this, "Error", Toast.LENGTH_SHORT).show();
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