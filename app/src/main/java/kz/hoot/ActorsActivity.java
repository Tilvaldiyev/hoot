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
import android.widget.EditText;
import android.widget.ImageButton;
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
import kz.hoot.adapter.CastHistoryAdapter;
import kz.hoot.model.Actor;
import kz.hoot.model.Cast;
import kz.hoot.model.Invite;
import kz.hoot.model.User;
import kz.hoot.response.RespondResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActorsActivity extends AppCompatActivity implements ActorAdapter.ActorActivityService, CastHistoryAdapter.CastHistoryActivityService {
    private BottomNavigationView bottomNavigationView;
    private RecyclerView actorsRecView, botttomCastingsRecView;
    private ActorAdapter actorAdapter;
    private CastHistoryAdapter castHistoryAdapter;
    private ImageView avatar;
    private EditText searchField;
    private ImageButton searchBtn;
    private RelativeLayout mainContent;
    private ProgressBar loadingGif;
    private TextView countTxt;

    private static String token;
    private int responseCode = 0;
    private ArrayList<Actor> favActors = new ArrayList<>();
    private int respondResult = 0;
    public static Invite invite = new Invite();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actors);
        SharedPreferences preferences = getSharedPreferences("auth", MODE_PRIVATE);
        token = preferences.getString("token", "");
        initViews();
        initBottomNav();
        initRecView();
        getCasts();
        getFavActors();
        getActors();
        getUserInfo();

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchActors();
            }
            ;
        });
    }

    private void initViews () {
        bottomNavigationView = findViewById(R.id.bottom_nav);
        actorsRecView = findViewById(R.id.activity_actors__recview);
        countTxt = findViewById(R.id.activity_actors__count_txt);
        avatar = findViewById(R.id.activity_actors__avatar);
        searchField = findViewById(R.id.activity_actors__search_field);
        searchBtn = findViewById(R.id.activity_actors__search_btn);
        mainContent = findViewById(R.id.activity_actors__main_content);
        loadingGif = findViewById(R.id.activity_actors__progress_bar);
    }

    private void initRecView () {
        castHistoryAdapter = new CastHistoryAdapter(this, this, true);
        actorAdapter = new ActorAdapter(this, this, castHistoryAdapter);
        actorsRecView.setAdapter(actorAdapter);
        actorsRecView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
    }

    private void initBottomNav () {
        bottomNavigationView.setSelectedItemId(R.id.bottom_nav__actors_btn);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottom_nav__create_btn:
                        Intent intent = new Intent(ActorsActivity.this, CreateCastActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.bottom_nav__favorites_btn:
                        intent = new Intent(ActorsActivity.this, FavouriteActorActivity.class);
                        startActivity(intent);
                    case R.id.bottom_nav__profile_btn:
                        intent = new Intent(ActorsActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.bottom_nav__castings_btn:
                        intent = new Intent(ActorsActivity.this, DirectorCastActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.bottom_nav__profile_btn:
                        intent = new Intent(ActorsActivity.this, DirectorProfileActivity.class);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });
    }

    private void getActors () {
        Call<List<Actor>> responseCall = hoot.getActors();

        responseCall.enqueue(new Callback<List<Actor>>() {
            @Override
            public void onResponse(Call<List<Actor>> call, Response<List<Actor>> response) {
                if (response.code() == 200) {
                    ArrayList<Actor> actors = (ArrayList<Actor>) response.body();
                    actorAdapter.setActors(actors);
                    countTxt.setText("Найдено " + actorAdapter.getItemCount());
                    loadingGif.setVisibility(View.GONE);
                    mainContent.setVisibility(View.VISIBLE);
                } else {
                    try {
                        Toast.makeText(ActorsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        Log.v("Tag", "error" + response.errorBody().toString());
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
                        Toast.makeText(ActorsActivity.this, "Error", Toast.LENGTH_SHORT).show();
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

    private void searchActors() {
        String searchValue = searchField.getText().toString();
        ArrayList<Actor> actorsAll = actorAdapter.getActors();
        mainContent.setVisibility(View.GONE);
        loadingGif.setVisibility(View.VISIBLE);
        if (searchValue.length() > 0) {
            ArrayList<Actor> filteredActors = new ArrayList<>();
            for (Actor a :
                    actorsAll) {
                if (a.getName().contains(searchValue) || a.getLastname().contains(searchValue)) {
                    filteredActors.add(a);
                }
            }
            actorAdapter.setActors(filteredActors);
        } else {
            getActors();
        }
        loadingGif.setVisibility(View.GONE);
        mainContent.setVisibility(View.VISIBLE);
    }

    @Override
    public int addToFavList(int actorId) {
        Call<String> responseCall = hoot.addActorToFavList(token, actorId);

        responseCall.enqueue(new Callback<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                System.out.println("Response - " + response);
                System.out.println("Response body - " + response.body());
                System.out.println("Response code - " + response.code());
                if (response.code() == 200) {
                    Toast.makeText(ActorsActivity.this, "Успешно добавлено", Toast.LENGTH_SHORT).show();
                    responseCode = response.code();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(ActorsActivity.this, "Произошла ошибка при добавлении! Повторите попытку", Toast.LENGTH_SHORT).show();
            }
        });

        return responseCode;
    }

    @Override
    public ArrayList<Actor> favActors() {
        return favActors;
    }

    private void getFavActors () {
        Call<List<Actor>> responseCall = hoot.getFavActors("Bearer " + token);

        responseCall.enqueue(new Callback<List<Actor>>() {
            @Override
            public void onResponse(Call<List<Actor>> call, Response<List<Actor>> response) {
                if (response.code() == 200) {
                    favActors = (ArrayList<Actor>) response.body();
                } else {
                    try {
                        Toast.makeText(ActorsActivity.this, "Error", Toast.LENGTH_SHORT).show();
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

    private void getCasts() {
        Call<List<Cast>> responseCall = hoot.getDirectorCasts("Bearer " + token);

        responseCall.enqueue(new Callback<List<Cast>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<List<Cast>> call, Response<List<Cast>> response) {
                if (response.code() == 200) {
                    ArrayList<Cast> casts = (ArrayList<Cast>) response.body();
                    castHistoryAdapter.setCasts(casts);
                } else {
                    try {
                        Toast.makeText(ActorsActivity.this, "Error", Toast.LENGTH_SHORT).show();
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

    @Override
    public int inviteToCast(Long castId) {
        invite.setCastId(castId);
        Call<RespondResponse> responseCall = hoot.inviteToCast("Bearer " + token, invite);
        responseCall.enqueue(new Callback<RespondResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<RespondResponse> call, Response<RespondResponse> response) {
                if (response.code() == 202) {
                    respondResult = 202;
                }
            }

            @Override
            public void onFailure(Call<RespondResponse> call, Throwable t) {
                Toast.makeText(ActorsActivity.this, "Ошибка запроса", Toast.LENGTH_SHORT).show();
            }
        });
        return respondResult;
    }
}