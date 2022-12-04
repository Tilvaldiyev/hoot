package kz.hoot;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class CreateCastActivity extends AppCompatActivity {

    private EditText castName, aboutCast;
    private Spinner castType;
    private ArrayList<String> castTypeList;
    private ArrayAdapter<String> castTypeAdapter;
    private BottomNavigationView bottomNavigationView;

    private String castTypeField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_cast);
        initViews();
        initMockData();
        initBottomNav();
    }

    private void initViews() {
        castName = findViewById(R.id.activity_create_cast__name_field);
        aboutCast = findViewById(R.id.activity_create_cast__about_field);
        castType = findViewById(R.id.activity_create_cast__type_field);
        bottomNavigationView = findViewById(R.id.bottom_nav);
    }

    private void initMockData() {
        castTypeList = new ArrayList<>();
        castTypeList.add("Тип аккаунта");
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
                    castTypeField = "";
                } else {
//                        accountTypeField = tv.getText().toString();
                    castTypeField = "MOVIE";
                    tv.setTextColor(Color.BLACK);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }

    private void initBottomNav () {
        bottomNavigationView.setSelectedItemId(R.id.bottom_nav__favorites_btn);

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
                }
                return false;
            }
        });
    }
}