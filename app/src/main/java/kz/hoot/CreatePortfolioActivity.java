package kz.hoot;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class CreatePortfolioActivity extends AppCompatActivity {

    private EditText portfolioLink, aboutPortfolio;
    private Spinner portfolioAge;
    private ArrayList<String> castTypeList;
    private ArrayAdapter<String> castTypeAdapter;
    private BottomNavigationView bottomNavigationView;
    private Button createButton;

    private String castTypeField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_portfolio);
        initViews();
        initMockData();
        initBottomNav();
        createButtonClicked();
    }

    private void initViews() {
        portfolioLink = findViewById(R.id.activity_create_portfolio__link);
        aboutPortfolio = findViewById(R.id.activity_create_portfolio__about_field);
        portfolioAge = findViewById(R.id.activity_create_portfolio__age);
        bottomNavigationView = findViewById(R.id.bottom_nav);
    }

    private void initMockData() {
        castTypeList = new ArrayList<>();
        castTypeList.add("18-25");
        castTypeList.add("25-35");
        castTypeList.add("35-45");
        castTypeList.add("45+");

        castTypeAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                castTypeList
        );

        portfolioAge.setAdapter(castTypeAdapter);

        portfolioAge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    private void createButtonClicked(){
        createButton = findViewById(R.id.activity_create_portfolio__create_btn);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreatePortfolioActivity.this, CastActivity.class);
                startActivity(intent);
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
                        Intent intent = new Intent(CreatePortfolioActivity.this, CastActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.bottom_nav__castings_btn:
                        intent = new Intent(CreatePortfolioActivity.this, CastHistoryActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.bottom_nav__profile_btn:
                        intent = new Intent(CreatePortfolioActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });
    }
}