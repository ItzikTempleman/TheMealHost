package com.example.therecipehost;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.therecipehost.Constants.GlobalConstants;
import com.example.therecipehost.activities.LoginActivity;

import java.util.Locale;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        checkIfLoginOk();
        setImageTitle();
    }

    private void setImageTitle() {
        ImageView imageView = findViewById(R.id.app_name);
        if (Locale.getDefault().getLanguage().equals("en"))
            imageView.setBackgroundResource(R.drawable.english_title_recipe_host);
        else imageView.setBackgroundResource(R.drawable.project_title_hebrew);
    }

    private void checkIfLoginOk() {
        SharedPreferences sharedPreferences = getSharedPreferences(GlobalConstants.SHARED_PREFS, MODE_PRIVATE);
        GlobalConstants.isConfirmed = sharedPreferences.getBoolean(GlobalConstants.IS_CONFIRMED_STR, false);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, !GlobalConstants.isConfirmed ? LoginActivity.class : MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000);
    }
}
