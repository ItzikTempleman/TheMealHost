package com.example.therecipehost.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.therecipehost.AsyncTasks.MealAsyncTask;
import com.example.therecipehost.Constants.GlobalConstants;
import com.example.therecipehost.Fragments.RecipeDetailsFragment;
import com.example.therecipehost.MainActivity;
import com.example.therecipehost.Models.IResponse;
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

    public static String toDecimalFormat(double doubleToChange) {
        return new DecimalFormat("0.0").format(doubleToChange);
    }

    public static String getURL(String searchTitle) {
        return "https://themealdb.p.rapidapi.com/search.php?s=" + searchTitle;
    }

    public static int getMealById(List<Meal> meals, String mealId) {
        int index = -1;

        for (int i = 0; i < meals.size(); i++) {
            if (meals.get(i).getId().equals(mealId)) {
                index = i;
            }
        }
        return index;
    }

    public static void loadImage(String url, ImageView imageView) {
        Glide.with(imageView.getContext()).load(url).into(imageView);
    }

    public static void toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void changeFragment(FragmentManager fm, int id, Fragment fragment, boolean addToBackStack) {
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        if (addToBackStack) fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(id, fragment).commit();
    }

    public static void moveToDetailsFragment(Meal meal, Context context) {
        RecipeDetailsFragment recipeDetailsFragment = new RecipeDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(MEAL, meal);
        recipeDetailsFragment.setArguments(bundle);
        changeFragment(((MainActivity) context).getSupportFragmentManager(), R.id.choose_meal_frame_layout, recipeDetailsFragment, true);
    }

    public static void remove(Context context, Meal deletedMeal) {
        SharedPreferences sharedPreferences = Objects.requireNonNull(context).getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        List<Meal> savedMealList = getList(context, MEAL);
        int deletedMealIndex = getMealById(savedMealList, deletedMeal.getId());
        if (deletedMealIndex == -1) return;
        savedMealList.remove(deletedMealIndex);

        String updateMeal = new Gson().toJson(savedMealList);
        editor.putString(MEAL, updateMeal);
        editor.apply();
    }

    public static void loadAsyncTask(String searchKey, IResponse iResponse) {
        MealAsyncTask mealAsyncTask = new MealAsyncTask(searchKey);
        mealAsyncTask.setIResponse(iResponse);
        mealAsyncTask.execute();
    }

    public static void handleSwiping(RecyclerView rv) {
        rv.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                int action = e.getAction();
                switch (action) {
                    case MotionEvent.ACTION_MOVE:
                        rv.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }

    public static void saveList(Context context, Meal meal, String prefKey) {
        List<Meal> savedMeal = getList(context, prefKey);

        SharedPreferences sharedPreferences = Objects.requireNonNull(context).getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        savedMeal.add(meal);

        String updatedList = gson.toJson(savedMeal);
        editor.putString(prefKey, updatedList);
        editor.apply();
    }

    public static List<Meal> getList(Context context, String prefKey) {
        List<Meal> savedMealList = new ArrayList<>();
        SharedPreferences sharedPreferences = Objects.requireNonNull(context).getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(prefKey, null);
        if (json != null) {
            Type type = new TypeToken<List<Meal>>() {}.getType();
            savedMealList = gson.fromJson(json, type);
        }
        return savedMealList;
    }
}