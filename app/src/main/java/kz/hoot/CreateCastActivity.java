package kz.hoot;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import kz.hoot.model.Cast;
import kz.hoot.model.Registration;
import kz.hoot.response.RegistrationResponse;
import kz.hoot.response.RespondResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static kz.hoot.request.Servicey.hoot;

public class CreateCastActivity extends AppCompatActivity {

    private EditText castName, aboutCast;
    private Spinner castType;
    private ArrayList<String> castTypeList;
    private ArrayAdapter<String> castTypeAdapter;
    private BottomNavigationView bottomNavigationView;
    private String token;
    private Cast.CastType castTypeValue;
    private Button createBtn;
    private RelativeLayout mainContent;
    private ProgressBar loadingGif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_cast);

        SharedPreferences preferences = getSharedPreferences("auth", MODE_PRIVATE);
        token = preferences.getString("token", "");

        initViews();
        initMockData();
        initBottomNav();

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainContent.setVisibility(View.GONE);
                loadingGif.setVisibility(View.VISIBLE);
                create();
                loadingGif.setVisibility(View.GONE);
                mainContent.setVisibility(View.VISIBLE);
            }
        });

    }

    private void initViews() {
        castName = findViewById(R.id.activity_create_cast__name_field);
        aboutCast = findViewById(R.id.activity_create_cast__about_field);
        castType = findViewById(R.id.activity_create_cast__type_field);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        createBtn = findViewById(R.id.activity_create_cast__create_btn);
        mainContent = findViewById(R.id.activity_create_cast__main_content);
        loadingGif = findViewById(R.id.activity_create_cast__progress_bar);
    }

    private void initMockData() {
        castTypeList = new ArrayList<>();
        castTypeList.add("Тип кастинга");
        castTypeList.add("Фильм");

        castTypeAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                castTypeList
        );

        castType.setAdapter(castTypeAdapter);

        castType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(Color.BLACK);
                    castTypeValue = null;
                } else {
//                        accountTypeField = tv.getText().toString();
                    castTypeValue = Cast.CastType.MOVIE;
                    tv.setTextColor(Color.BLACK);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }

    private void initBottomNav () {
        bottomNavigationView.setSelectedItemId(R.id.bottom_nav__create_btn);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottom_nav__actors_btn:
                        Intent intent = new Intent(CreateCastActivity.this, ActorsActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.bottom_nav__favorites_btn:
                        intent = new Intent(CreateCastActivity.this, FavouriteActorActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.bottom_nav__castings_btn:
                        intent = new Intent(CreateCastActivity.this, DirectorCastActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.bottom_nav__profile_btn:
                        intent = new Intent(CreateCastActivity.this, DirectorProfileActivity.class);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });
    }

    private void create() {
        if (castName.getText().toString().length() > 0 && castTypeValue != null && aboutCast.getText().toString().length() > 0) {
            Cast castModel = new Cast(castName.getText().toString(), castTypeValue, aboutCast.getText().toString());

            Call<RespondResponse> responseCall = hoot.createCast(
                    "Bearer " + token,
                    castModel
            );

            responseCall.enqueue(new Callback<RespondResponse>() {
                @Override
                public void onResponse(@NotNull Call<RespondResponse> call, @NotNull Response<RespondResponse> response) {
                    if (response.code() == 201) {
                        assert response.body() != null;
                        Toast.makeText(CreateCastActivity.this, "Кастинг успешно создан", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CreateCastActivity.this, DirectorCastActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        try {
                            Log.v("Tag", "error" + response.code());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<RespondResponse> call, Throwable t) {
                    Log.v("Tag", "error" + t.toString());
                }
            });
        } else {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
        }
    }
}