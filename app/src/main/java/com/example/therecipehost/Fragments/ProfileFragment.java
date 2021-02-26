package com.example.therecipehost.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.therecipehost.R;

import static android.content.Context.MODE_PRIVATE;
import static com.example.therecipehost.Constants.GlobalConstants.USER_INFO_SHARED_PREFS;
import static com.example.therecipehost.Constants.GlobalConstants.USER_NAME_KEY;


public class ProfileFragment extends Fragment {
    private TextView usernameTV;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        usernameTV=view.findViewById(R.id.username);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(USER_INFO_SHARED_PREFS, MODE_PRIVATE);
        String nameText = sharedPreferences.getString(USER_NAME_KEY, "");
        String correctUerName = nameText.substring(0, nameText.indexOf("@"));
        usernameTV.setText(correctUerName);
    }
}