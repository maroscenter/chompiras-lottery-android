package com.youtube.sorcjc.billetero.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.youtube.sorcjc.billetero.Global;
import com.youtube.sorcjc.billetero.R;
import com.youtube.sorcjc.billetero.io.MyApiAdapter;
import com.youtube.sorcjc.billetero.io.response.LoginResponse;
import com.youtube.sorcjc.billetero.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, Callback<LoginResponse> {

    private EditText etEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // There is an active session?
        final String accessToken = User.getAccessToken(this);

        if (!accessToken.isEmpty()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        // When there isn't an active session => just fill with the latest email used
        etEmail.setText(Global.getStringFromPrefs(this, "email"));
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnLogin) {
            final String email = etEmail.getText().toString();
            final String password = etPassword.getText().toString();

            // Store the latest email input
            Global.saveStringPref(this, "email", email);

            if (password.length() < 6) {
                Global.showMessageDialog(
                    this,
                    "Mensaje de error",
                    "La contraseña debe presentar al menos 6 caracteres."
                );
                return;
            }

            Call<LoginResponse> call = MyApiAdapter.getApiService().postLogin(
                    email, password
            );
            call.enqueue(this);
        }
    }

    @Override
    public void onResponse(@NonNull Call<LoginResponse> call, Response<LoginResponse> response) {
        if (response.isSuccessful()) {
            LoginResponse loginResponse = response.body();

            Global.saveStringPref(this, "access_token", loginResponse.getAccessToken());
            goToMainActivity();
        } else {
            Global.showMessageDialog(this, getString(R.string.dialog_title_error), "Ha ocurrido un error inesperado.");
        }
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onFailure(@NonNull Call<LoginResponse> call, Throwable t) {
        Global.showMessageDialog(this, getString(R.string.dialog_title_error), t.getMessage());
    }
}
