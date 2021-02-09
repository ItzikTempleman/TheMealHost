package com.example.therecipehost;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.therecipehost.Constants.GlobalConstants;
import com.example.therecipehost.Fragments.CustomLogin;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ProgressBar progressBar = findViewById(R.id.splash_pb);
        checkIfLoginOk();
    }

    private void checkIfLoginOk() {
        SharedPreferences sharedPreferences = getSharedPreferences(GlobalConstants.SHARED_PREFS, MODE_PRIVATE);
        GlobalConstants.isConfirmed = sharedPreferences.getBoolean(GlobalConstants.IS_CONFIRMED_STR, false);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, !GlobalConstants.isConfirmed ? CustomLogin.class : MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}
