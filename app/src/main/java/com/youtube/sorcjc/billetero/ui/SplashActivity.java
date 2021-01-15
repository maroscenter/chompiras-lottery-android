package com.youtube.sorcjc.billetero.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.youtube.sorcjc.billetero.ui.MainActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

        finish();
    }
}
