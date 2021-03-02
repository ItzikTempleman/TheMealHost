package com.example.therecipehost.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.therecipehost.Constants.GlobalConstants;
import com.example.therecipehost.MainActivity;
import com.example.therecipehost.R;
import com.example.therecipehost.activities.LoginActivity;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static com.example.therecipehost.Constants.GlobalConstants.PASSWORD_KEY;
import static com.example.therecipehost.Constants.GlobalConstants.USER_INFO_SHARED_PREFS;
import static com.example.therecipehost.Constants.GlobalConstants.USER_NAME_KEY;
import static com.example.therecipehost.Constants.GlobalConstants.isConfirmed;


public class ProfileFragment extends Fragment {
private ImageButton disconnectBtn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setListeners();
    }

    private void initView(View view) {
       disconnectBtn=view.findViewById(R.id.disconnect_btn);
        TextView usernameTV = view.findViewById(R.id.username);
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(USER_INFO_SHARED_PREFS, MODE_PRIVATE);
        String nameText = sharedPreferences.getString(USER_NAME_KEY, "");
        String correctUerName = nameText.substring(0, nameText.indexOf("@"));
        usernameTV.setText(correctUerName);
    }
    private void setListeners() {
        disconnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isConfirmed = false;
                moveToMainActivity();
                saveState();
            }
        });
    }
    private void moveToMainActivity() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        Objects.requireNonNull(getActivity()).finish();
    }

    private void saveState() {
        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences(GlobalConstants.SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(GlobalConstants.IS_CONFIRMED_STR, isConfirmed);
        editor.apply();
    }
}