package com.vydev.homekeeperapp.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.color.DynamicColors;
import com.vydev.homekeeperapp.Api.RqBody.LoginBody;
import com.vydev.homekeeperapp.Api.Services;
import com.vydev.homekeeperapp.R;

import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends FlexibleActivity {

    Context context = this;

    Button submit;
    TextView login;
    TextView password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        DynamicColors.applyToActivitiesIfAvailable(this.getApplication());
        hideAppbar();

        setContentView(R.layout.activity_login);

        submit = (Button) findViewById(R.id.submitBtn);
        login = (TextView) findViewById(R.id.loginTextView);
        password = (TextView) findViewById(R.id.passwordTextView);

        submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String sLogin = login.getText().toString();
                String sPassword = password.getText().toString();

                if (sLogin.equals("") || sPassword.equals("")) {
                    new AlertDialog.Builder(context)
                            .setTitle("Ошибка")
                            .setMessage("Необходимо ввести логин и пароль.")
                            .show();
                } else {

                    Services.getLoginService().performLogin(
                            new LoginBody()
                                    .setUsername(sLogin)
                                    .setPassword(sPassword)
                                    .toMultipartBody()
                    ).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.code() == 302) {
                                if (Objects.requireNonNull(response.headers().get("Location")).contains("login?success=true")) {
                                    Intent intent = new Intent(context, MainActivity.class);
                                    startActivity(intent);
                                    finishAffinity();
                                } else if (Objects.requireNonNull(response.headers().get("Location")).contains("login?error=true")) {
                                    password.setText("");
                                    new AlertDialog.Builder(context)
                                            .setTitle("Ошибка")
                                            .setMessage("Неверные логин и/или пароль.")
                                            .show();
                                }
                            } else {
                                new AlertDialog.Builder(context)
                                        .setTitle("Ошибка")
                                        .setMessage("Не удалось выполнить запрос авторизации. Попробуйте позже.")
                                        .show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }

}