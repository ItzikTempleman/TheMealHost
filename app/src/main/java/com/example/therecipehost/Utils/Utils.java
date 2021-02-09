package com.example.therecipehost.Utils;

import android.content.Context;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.therecipehost.Fragments.RecipeDetailsFragment;
import com.example.therecipehost.MainActivity;
import com.example.therecipehost.Models.Meal;
import com.example.therecipehost.R;

import static com.example.therecipehost.Constants.GlobalConstants.MEAL;

public class Utils {

    public static void loadImage(String url, ImageView imageView) {
        Glide.with(imageView.getContext()).load(url).into(imageView);
    }

    public static void toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static String toDecimalFormat(double price) {
        return new DecimalFormat("0.0").format(price);
    }

    public static void changeFragment(FragmentManager fm, int id, Fragment fragment, boolean addToBackStack) {
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        if (addToBackStack) fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(id, fragment).commit();
    }

    public static String getURL(String searchTitle) {
        return "https://themealdb.p.rapidapi.com/search.php?s=" + searchTitle;
    }
    public static void moveToDetailsFragment(Meal meal, Context context) {
        if (context instanceof MainActivity) {
            RecipeDetailsFragment recipeDetailsFragment = new RecipeDetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(MEAL, meal);
            recipeDetailsFragment.setArguments(bundle);
           changeFragment(((MainActivity) context).getSupportFragmentManager(), R.id.main_activity_frame_layout, recipeDetailsFragment, true);
        }
    }
}