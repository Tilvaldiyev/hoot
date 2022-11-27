package kz.hoot;

import static kz.hoot.request.Servicey.hoot;

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

public class ActorsActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private RecyclerView actorsRecView;
    private ActorAdapter actorAdapter;
    private ImageView avatar;
    private EditText searchField;
    private ImageButton searchBtn;

    private TextView countTxt;

    private static String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actors);
        SharedPreferences preferences = getSharedPreferences("auth", MODE_PRIVATE);
        token = preferences.getString("token", "");
        initViews();
        initBottomNav();
        initRecView();
        getActors();
        getUserInfo();

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchValue = searchField.getText().toString();
                ArrayList<Actor> actorsAll = actorAdapter.getActors();
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
    }

    private void initRecView () {
        actorAdapter = new ActorAdapter(this);
        actorsRecView.setAdapter(actorAdapter);
        actorsRecView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
    }

    private void initBottomNav () {
        bottomNavigationView.setSelectedItemId(R.id.actor_item__fav_btn);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
//                    case R.id.actor_item__fav_btn:
//                        Intent intent = new Intent(ActorsActivity.this, MainActivity.class);
//                        startActivity(intent);
//                        break;
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
}