package kz.hoot;

import static kz.hoot.request.Servicey.hoot;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import kz.hoot.response.LoginResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private Button loginBtn, forgotPswdBtn;

    private String accessToken = "";
    private String refreshToken = "";

    private String userType = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authorization();
            }
        });
    }

    private void initViews() {
        email = findViewById(R.id.login_activity__email_field);
        password = findViewById(R.id.login_activity__password_field);

        loginBtn = findViewById(R.id.login_activity__login_btn);
        forgotPswdBtn = findViewById(R.id.activity_login__forgot_pswd_btn);

        password.setText("");
    }

    private void authorization() {
        String emailTxt = email.getText().toString();
        String passwordTxt = password.getText().toString();

        if (emailTxt.length() > 0 && passwordTxt.length() > 0) {
            Call<LoginResponse> responseCall = hoot.authorization(
                    "Basic " + Base64.encodeToString((emailTxt + ":" + passwordTxt).getBytes(), Base64.NO_WRAP));

            responseCall.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.code() == 200) {
                        accessToken = response.body().getAccessToken();
                        refreshToken = response.body().getRefreshToken();
                        SharedPreferences preferences = getSharedPreferences("auth", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("token", accessToken);
                        editor.putString("refreshToken", refreshToken);
                        editor.apply();

                        Intent intent = new Intent(LoginActivity.this, ActorsActivity.class);

                        userType = response.body().getUserType();
                        if (userType.equals("ACTOR")){
                            intent = new Intent(LoginActivity.this, CastActivity.class);
                        }

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
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Log.v("Tag", "error" + t.toString());
                }
            });
        } else {
            Toast.makeText(this, "Введите логин и пароль", Toast.LENGTH_SHORT).show();
        }
    }
}