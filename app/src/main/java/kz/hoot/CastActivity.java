package kz.hoot;

import static kz.hoot.request.Servicey.hoot;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
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

import kz.hoot.adapter.CastAdapter;
import kz.hoot.model.Cast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CastActivity extends AppCompatActivity {

    private CastAdapter castAdapter;
    private BottomNavigationView bottomNavigationView;
    private LinearLayout linearLayout;
    private TextView countTxt;
    private RecyclerView castsRecView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_casts);

        initViews();
        initBottomNav();
        initRecView();
        getCasts();
    }

    private void initViews() {
        bottomNavigationView = findViewById(R.id.bottom_nav);
        countTxt = findViewById(R.id.casts_count_txt);
        castsRecView = findViewById(R.id.casts_rec_view);
    }

    private void initRecView() {
        castAdapter = new CastAdapter(this);
        castsRecView.setAdapter(castAdapter);
        castsRecView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
    }

    private void initBottomNav() {
        bottomNavigationView.setSelectedItemId(R.id.actor_item__fav_btn);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.actor_item__fav_btn:
//                        Intent intent = new Intent(CastActivity.this, MainActivity.class);
//                        startActivity(intent);
//                        break;
//                }
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
                        System.out.println( casts.get(i).toString());
                    }
                    castAdapter.setCasts(casts);
                    countTxt.setText("Найдено " + castAdapter.getItemCount());
                } else {
                    try {
                        Toast.makeText(CastActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
}
