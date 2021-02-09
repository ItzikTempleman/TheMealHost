package com.example.therecipehost.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.therecipehost.Fragments.RecipeDetailsFragment;
import com.example.therecipehost.Fragments.SavedFragment;
import com.example.therecipehost.MainActivity;
import com.example.therecipehost.Models.Meal;
import com.example.therecipehost.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static com.example.therecipehost.Constants.GlobalConstants.MEAL;
import static com.example.therecipehost.Constants.GlobalConstants.SHARED_PREFS;

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

    public static List<Meal> getSavedMealList(Context context){
        List<Meal> savedMealList = new ArrayList<>();
        SharedPreferences sharedPreferences = Objects.requireNonNull(context).getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(MEAL, null);
        if (json != null) {
            Type type = new TypeToken<List<Meal>>() {}.getType();
            savedMealList = gson.fromJson(json, type);
        }

        return savedMealList;
    }

    public static void saveRecipe(Context context, Meal meal) {
        SharedPreferences sharedPreferences = Objects.requireNonNull(context).getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        List<Meal> savedMeal = getSavedMealList(context);
        savedMeal.add(meal);
        // transform List to String
        String updatedList = gson.toJson(savedMeal);
        editor.putString(MEAL, updatedList);
        editor.apply();
    }

    public static void remove(Context context, Meal deletedMeal) {
        SharedPreferences sharedPreferences = Objects.requireNonNull(context).getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        List<Meal> savedMealList = getSavedMealList(context);
        int deletedMealIndex = getMealById(savedMealList, deletedMeal.getId());
        if (deletedMealIndex == -1) return;
        savedMealList.remove(deletedMealIndex);

        String updateMeal = new Gson().toJson(savedMealList);
        editor.putString(MEAL, updateMeal);
        editor.apply();
    }

    private static int getMealById(List<Meal> meals,String mealId){
        int index = -1;

        for (int i = 0; i < meals.size(); i++) {
            if (meals.get(i).getId().equals(mealId)){
                index = i;
            }
        }

        return index;
    }
}