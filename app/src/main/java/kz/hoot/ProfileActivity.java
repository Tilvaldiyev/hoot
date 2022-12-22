package kz.hoot;

import static kz.hoot.request.Servicey.hoot;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import kz.hoot.model.User;
import kz.hoot.response.LoginResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private static String token;
    private static String refreshToken;
    private int respondResult = 0;
    private ImageView avatar;
    private Spinner accountType;
    private ArrayList<String> accountList;
    private ArrayAdapter<String> accountTypeAdapter;
    private String accountTypeField;
    private Switch actorStatusSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);
        SharedPreferences preferences = getSharedPreferences("auth", MODE_PRIVATE);
        token = preferences.getString("token", "");
        refreshToken = preferences.getString("refreshToken", "");

        initViews();
        initBottomNav();
        getUserInfo();
        initMockData();
        changeSwitchStatus();
    }

    private void initViews() {
        bottomNavigationView = findViewById(R.id.bottom_nav);
        avatar = findViewById(R.id.profile_actor__photo);
        accountType = findViewById(R.id.account_type__spinner);
        actorStatusSwitch = findViewById(R.id.profile_actor_status__switch);
    }

    private void initBottomNav() {
        bottomNavigationView.setSelectedItemId(R.id.bottom_nav__profile_btn);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottom_nav__castings_btn:
                        Intent intent = new Intent(ProfileActivity.this, CastHistoryActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.bottom_nav__actors_btn:
                        intent = new Intent(ProfileActivity.this, CastActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.bottom_nav__create_btn:
                        intent = new Intent(ProfileActivity.this, CreatePortfolioActivity.class);
                        startActivity(intent);
                        break;
                }
                return false;
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
                        Toast.makeText(ProfileActivity.this, "Error", Toast.LENGTH_SHORT).show();
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

    private void initMockData() {
        accountList = new ArrayList<>();
        accountList.add("Aктёр");
        accountList.add("Кастинг директор");

        accountTypeAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                accountList
        );

        accountType.setAdapter(accountTypeAdapter);

        accountType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(Color.BLACK);
                    accountTypeField = "";
                } else {
//                        accountTypeField = tv.getText().toString();
                    accountTypeField = "ACTOR";
                    tv.setTextColor(Color.BLACK);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }

    public void changeSwitchStatus(){
        if (actorStatusSwitch.isChecked()){
            System.out.println("Actor status checked");
        }else {
            System.out.println("Actor status Unchecked");
        }

    }
}
