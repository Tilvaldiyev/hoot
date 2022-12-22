package kz.hoot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import kz.hoot.model.Cast;
import kz.hoot.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static kz.hoot.request.Servicey.hoot;

public class DirectorProfileActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private String token;
    private Button logoutBtn;
    private LinearLayout mainContent;
    private ProgressBar loadingGif;
    private ImageView avatar;
    private TextView fullName, type, casts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_director_profile);

        SharedPreferences preferences = getSharedPreferences("auth", MODE_PRIVATE);
        token = preferences.getString("token", "");

        initViews();
        initBottomNav();
        getUserInfo();
        getCasts();

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences.edit().remove("token").apply();
                Intent intent = new Intent(DirectorProfileActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }

    private void initViews() {
        bottomNavigationView = findViewById(R.id.bottom_nav);
        logoutBtn = findViewById(R.id.activity_director_profile__logout_btn);
        avatar = findViewById(R.id.activity_director_profile__avatar);
        fullName = findViewById(R.id.activity_director_profile__fullname);
        type = findViewById(R.id.activity_director_profile__type);
        casts = findViewById(R.id.activity_director_profile__casts);
        mainContent = findViewById(R.id.activity_director_profile__main_content);
        loadingGif = findViewById(R.id.activity_director_profile__progress_bar);
    }

    private void initBottomNav () {
        bottomNavigationView.setSelectedItemId(R.id.bottom_nav__profile_btn);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottom_nav__actors_btn:
                        Intent intent = new Intent(DirectorProfileActivity.this, ActorsActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.bottom_nav__favorites_btn:
                        intent = new Intent(DirectorProfileActivity.this, FavouriteActorActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.bottom_nav__castings_btn:
                        intent = new Intent(DirectorProfileActivity.this, DirectorCastActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.bottom_nav__create_btn:
                        intent = new Intent(DirectorProfileActivity.this, CreateCastActivity.class);
                        startActivity(intent);
                        break;
                }
                return false;
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
                    fullName.setText(user.getFullname());

                    if (user.getUserType().equals("DIRECTOR")) {
                        type.setText("Кастинг директор");
                    }

                    loadingGif.setVisibility(View.GONE);
                    mainContent.setVisibility(View.VISIBLE);
                } else {
                    try {
                        Toast.makeText(DirectorProfileActivity.this, "Error", Toast.LENGTH_SHORT).show();
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

    private void getCasts() {
        Call<List<Cast>> responseCall = hoot.getDirectorCasts("Bearer " + token);

        responseCall.enqueue(new Callback<List<Cast>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<List<Cast>> call, Response<List<Cast>> response) {
                if (response.code() == 200) {
                    ArrayList<Cast> castList = (ArrayList<Cast>) response.body();
                    casts.setText(String.valueOf(castList.size()) + " кастинга");
                } else {
                    try {
                        Toast.makeText(DirectorProfileActivity.this, "Произошла ошибка", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Cast>> call, Throwable t) {
                Toast.makeText(DirectorProfileActivity.this, "Произошла ошибка", Toast.LENGTH_SHORT).show();
            }
        });
    }
}