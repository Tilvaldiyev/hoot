package kz.hoot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import kz.hoot.model.RegistrationModel;
import kz.hoot.response.RegistrationResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static kz.hoot.request.Servicey.hoot;

public class RegisterActivity extends AppCompatActivity {
    private ScrollView scrollView;
    private FloatingActionButton floatingActionButtonUp;
    private FloatingActionButton floatingActionButtonDown;
    private Button registerBtn;

    private Spinner accountTypes;
    private ArrayList<String> accountTypeList;
    private ArrayAdapter<String> accountTypeAdapter;

    private Spinner countrySpinner;
    private ArrayList<String> countryList;
    private ArrayAdapter<String> countryAdapter;

    private Spinner citySpinner;
    private ArrayList<String> cityList;
    private ArrayAdapter<String> cityAdapter;

    private LinearLayout authBlock, personalDataBlock, locationBlock;

    private static short scrollBlock = 1;

//    private ProgressBar fillingProgressBar;
//    private TextView filledCountText;

    // Form fields
    private EditText loginField, passwordField, repeatPasswordField, surnameField, firstNameField, middleNameField;
    private String accountTypeField = "";
    private String countryField = "";
    private String cityField = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        initMockData();

        floatingActionButtonDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (scrollBlock) {
                case 1:
                    scrollView.scrollTo(0, (int)personalDataBlock.getY());
                    scrollBlock++;
//                    if (checkAuthField()) {
//                        fillingProgressBar.setProgress(fillingProgressBar.getProgress() + (fillingProgressBar.getMax()/3));
//                        filledCountText.setText("1 из 3 отвечено");
//                    }
                    break;
                case 2:
                    scrollView.scrollTo(0, (int)locationBlock.getY());
                    scrollBlock++;
                    break;
                }
            }
        });

        floatingActionButtonUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (scrollBlock) {
                    case 3:
                        scrollView.scrollTo(0, (int)personalDataBlock.getY());
                        scrollBlock--;
                        break;
                    case 2:
                        scrollView.scrollTo(0, (int)authBlock.getY());
                        scrollBlock--;
                        break;
                }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registration();
            }
        });

    }

    private void initViews() {
        accountTypes = findViewById(R.id.activity_register__account_type);
        countrySpinner = findViewById(R.id.activity_register__country_field);
        citySpinner = findViewById(R.id.activity_register__city_field);
        scrollView = findViewById(R.id.activity_register__scroll_view);
        floatingActionButtonDown = findViewById(R.id.activity_register__scroll_down_btn);
        floatingActionButtonUp = findViewById(R.id.activity_register__scroll_up_btn);
        registerBtn = findViewById(R.id.activity_register__register_btn);

        authBlock = findViewById(R.id.activity_register__auth_block);
        personalDataBlock = findViewById(R.id.activity_register__personal_data_block);
        locationBlock = findViewById(R.id.activity_register__location_block);

//        fillingProgressBar = findViewById(R.id.activity_register__answered_progress_bar);
//        filledCountText = findViewById(R.id.activity_register__answered_count_text);

        // Form fields
        loginField = findViewById(R.id.activity_register__login_field);
        passwordField = findViewById(R.id.activity_register__password_field);
        repeatPasswordField = findViewById(R.id.activity_register__password_repeat_field);
        surnameField = findViewById(R.id.activity_register__surname_field);
        firstNameField = findViewById(R.id.activity_register__firstname_field);
        middleNameField = findViewById(R.id.activity_register__middlename_field);
    }

    private void initMockData() {
        accountTypeList = new ArrayList<>();
        accountTypeList.add("Тип аккаунта");
        accountTypeList.add("Актер");
        accountTypeList.add("Режиссер");

        accountTypeAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                accountTypeList
        );

        accountTypes.setAdapter(accountTypeAdapter);

        accountTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view;
                    if (position == 0) {
                        tv.setTextColor(Color.GRAY);
                        accountTypeField = "";
                    } else {
//                        accountTypeField = tv.getText().toString();
                        accountTypeField = "ACTOR";
                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        countryList = new ArrayList<>();
        countryList.add("Страна");
        countryList.add("Казахстан");
        countryList.add("Россия");

        countryAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                countryList
        );

        countrySpinner.setAdapter(countryAdapter);

        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                    countryField = "";
                } else {
//                    countryField = tv.getText().toString();
                    countryField = "KAZ";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        cityList = new ArrayList<>();
        cityList.add("Город");
        cityList.add("Алматы");
        cityList.add("Астана");

        cityAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                cityList
        );

        citySpinner.setAdapter(cityAdapter);

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                    cityField = "";
                } else {
//                    cityField = tv.getText().toString();
                    cityField = "0";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }

    private boolean checkAuthField() {
        return loginField.getText().toString().length() > 0 &&
                passwordField.getText().toString().length() > 0 &&
                accountTypeField.length() > 0 &&
                passwordField.getText().toString().equals(repeatPasswordField.getText().toString());
    }

    private boolean checkPersonalDataField() {
        return surnameField.getText().toString().length() > 0 &&
                firstNameField.getText().toString().length() > 0;
    }

    private boolean checkLocationField() {
        return countryField.length() > 0 &&
                cityField.length() > 0;
    }

    private void registration() {
        if (checkAuthField() && checkLocationField() && checkPersonalDataField()) {
            RegistrationModel registrationModel = new RegistrationModel(loginField.getText().toString(), passwordField.getText().toString(),
                    accountTypeField, firstNameField.getText().toString(), surnameField.getText().toString(), middleNameField.getText().toString(),
                    countryField, Integer.parseInt(cityField));

            System.out.println(loginField.getText().toString() + passwordField.getText().toString() +
                    accountTypeField + firstNameField.getText().toString() + surnameField.getText().toString() + middleNameField.getText().toString() +
                    countryField + Integer.parseInt(cityField));

            Call<RegistrationResponse> responseCall = hoot.registration(
                    registrationModel
            );

            responseCall.enqueue(new Callback<RegistrationResponse>() {
                @Override
                public void onResponse(@NotNull Call<RegistrationResponse> call, @NotNull Response<RegistrationResponse> response) {
                    if (response.code() == 201) {
                        assert response.body() != null;
                        Toast.makeText(RegisterActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        try {
                            Log.v("Tag", "error" + response.errorBody().toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                    Log.v("Tag", "error" + t.toString());
                }
            });
        }
    }
}