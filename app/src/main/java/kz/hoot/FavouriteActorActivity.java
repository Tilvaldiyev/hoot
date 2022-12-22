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
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import kz.hoot.adapter.ActorAdapter;
import kz.hoot.model.Actor;
import kz.hoot.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavouriteActorActivity extends AppCompatActivity implements ActorAdapter.ActorActivityService {

    private BottomNavigationView bottomNavigationView;
    private RecyclerView actorsRecView;
    private ActorAdapter actorAdapter;
    private RelativeLayout mainContent;
    private ProgressBar loadingGif;
    private ImageView avatar;
    private ArrayList<Actor> actors;

    private static String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_actor);

        SharedPreferences preferences = getSharedPreferences("auth", MODE_PRIVATE);
        token = preferences.getString("token", "");

        initViews();
        initBottomNav();
        getFavActors();
        initRecView();
        getUserInfo();
        getActors();
    }

    private void initViews () {
        bottomNavigationView = findViewById(R.id.bottom_nav);
        actorsRecView = findViewById(R.id.activity_fav_actor__recview);
        mainContent = findViewById(R.id.activity_fav_actor__main_content);
        loadingGif = findViewById(R.id.activity_fav_actor__progress_bar);
        avatar = findViewById(R.id.activity_fav_actor__avatar);
    }

    private void initRecView () {
        actorAdapter = new ActorAdapter(this, this);
        actorsRecView.setAdapter(actorAdapter);
        actorsRecView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
    }

    private void initBottomNav () {
        bottomNavigationView.setSelectedItemId(R.id.bottom_nav__favorites_btn);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottom_nav__actors_btn:
                        Intent intent = new Intent(FavouriteActorActivity.this, ActorsActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.bottom_nav__create_btn:
                        intent = new Intent(FavouriteActorActivity.this, CreateCastActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.bottom_nav__castings_btn:
                        intent = new Intent(FavouriteActorActivity.this, DirectorCastActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.bottom_nav__profile_btn:
                        intent = new Intent(FavouriteActorActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });
    }

    private void getFavActors () {
        Call<List<Actor>> responseCall = hoot.getFavActors("Bearer " + token);

        responseCall.enqueue(new Callback<List<Actor>>() {
            @Override
            public void onResponse(Call<List<Actor>> call, Response<List<Actor>> response) {
                if (response.code() == 200) {
                    actors = (ArrayList<Actor>) response.body();
                    actorAdapter.setActors(actors);
                } else {
                    try {
                        Toast.makeText(FavouriteActorActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        Log.v("Tag", "error" + response.code());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Actor>> call, Throwable t) {
                t.printStackTrace();
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
                        Toast.makeText(FavouriteActorActivity.this, "Error", Toast.LENGTH_SHORT).show();
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

    @Override
    public int addToFavList(int castId) {
        return 0;
    }

    @Override
    public ArrayList<Actor> favActors() {
        return actors;
    }

    private void getActors () {
        Call<List<Actor>> responseCall = hoot.getActors();

        responseCall.enqueue(new Callback<List<Actor>>() {
            @Override
            public void onResponse(Call<List<Actor>> call, Response<List<Actor>> response) {
                if (response.code() == 200) {
                    ArrayList<Actor> actors = (ArrayList<Actor>) response.body();
                    ArrayList<Actor> favActors = favActors();
                    ArrayList<Actor> merged = new ArrayList<>();
                    for (Actor actor :
                            actors) {
                        for (Actor favActor:
                             favActors) {
                            if (actor.getActorId() == favActor.getActorId()) {
                                merged.add(actor);
                            }
                        }
                    }
                    actorAdapter.setActors(merged);
                } else {
                    try {
                        Toast.makeText(FavouriteActorActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        Log.v("Tag", "error" + response.errorBody().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                loadingGif.setVisibility(View.GONE);
                mainContent.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<List<Actor>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}