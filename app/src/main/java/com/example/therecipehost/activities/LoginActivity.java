package com.example.therecipehost.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.therecipehost.Constants.GlobalConstants;
import com.example.therecipehost.MainActivity;
import com.example.therecipehost.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

import static com.example.therecipehost.Constants.GlobalConstants.PASSWORD_KEY;
import static com.example.therecipehost.Constants.GlobalConstants.USER_NAME_KEY;
import static com.example.therecipehost.Constants.GlobalConstants.isConfirmed;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout userNameET, passwordET;
    private Button nextBtn;
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^" + "(?=.*[a-zA-Z])" + "(?=.*[@#$%^&+=])" + "(?=\\S+$)" + ".{4,}" + "$");
    private String userName, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_login);
        initView();
        setListeners();
    }

    private void initView() {
        nextBtn = findViewById(R.id.next_btn);
        userNameET = findViewById(R.id.username_et);
        passwordET = findViewById(R.id.password_et);

    }

    private void setListeners() {
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmInput(v);
            }
        });
    }

    private boolean validateUserName() {
        userName = userNameET.getEditText().getText().toString().trim();
        if (userName.isEmpty()) {
            userNameET.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(userName).matches()) {
            userNameET.setError("Please enter a valid email address");
            return false;
        } else {
            userNameET.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        password = passwordET.getEditText().getText().toString().trim();
        if (password.isEmpty()) {
            passwordET.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            passwordET.setError("Password too weak");
            return false;
        } else {
            passwordET.setError(null);
            return true;
        }
    }


    public void confirmInput(View v) {
        if (validateUserName() | validatePassword()) {
            isConfirmed = true;
            saveState();
            moveToMainActivity();

        }
    }

    private void moveToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra(USER_NAME_KEY, userName);
        intent.putExtra(PASSWORD_KEY, password);
        startActivity(intent);
        finish();
    }

    private void saveState() {
        SharedPreferences sharedPreferences = getSharedPreferences(GlobalConstants.SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(GlobalConstants.IS_CONFIRMED_STR, isConfirmed);
        editor.apply();
    }
}