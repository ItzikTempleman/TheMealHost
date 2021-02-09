package com.example.therecipehost.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.therecipehost.Adapters.MealAdapter;
import com.example.therecipehost.Adapters.SavedMealAdapter;
import com.example.therecipehost.AsyncTasks.MealAsyncTask;
import com.example.therecipehost.Models.IResponse;
import com.example.therecipehost.Models.Meal;
import com.example.therecipehost.R;
import com.example.therecipehost.Utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static com.example.therecipehost.Constants.GlobalConstants.MEAL;
import static com.example.therecipehost.Constants.GlobalConstants.SHARED_PREFS;

public class ChooseMealFragment extends Fragment implements IResponse {
    private EditText searchET;
    private MealAdapter mealAdapter;
    private SavedMealAdapter savedMealAdapter;
    private RecyclerView mealRV;
    private ProgressBar progressBar;
    public ImageView emptyStateIV;
    public List<Meal> mealList;
    private TextView featuredTV;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_choose_meal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        setListeners();
    }

    private void initView(View view) {
        sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        featuredTV = view.findViewById(R.id.featured_tv);
        searchET = view.findViewById(R.id.search_recipe_et);

        mealRV = view.findViewById(R.id.choose_meal_rv);
        progressBar = view.findViewById(R.id.loading_pb);
        emptyStateIV = view.findViewById(R.id.choose_meal_empty_state_image_view);
        savedMealAdapter = new SavedMealAdapter(getContext());
        mealAdapter = new MealAdapter(requireContext(), this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        mealRV.setLayoutManager(linearLayoutManager);
        mealRV.setAdapter(mealAdapter);
    }

    private void setListeners() {

        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                featuredTV.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                MealAsyncTask mealAsyncTask = new MealAsyncTask(s.toString());
                mealAsyncTask.setIResponse(ChooseMealFragment.this);
                mealAsyncTask.execute();
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onSuccess(String data) {
        progressBar.setVisibility(View.VISIBLE);
        try {
            mealList = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.getJSONArray("meals");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String title = obj.getString("strMeal");
                String nationality = obj.getString("strArea");
                String instructions = obj.getString("strInstructions");
                String category = obj.getString("strCategory");
                String thumbImage = obj.getString("strMealThumb");
                String id = obj.getString("idMeal");
                Meal meal = new Meal(title, nationality, category, instructions, thumbImage, id);
                meal.initIngredients(obj);
                meal.initAmounts(obj);
                mealList.add(meal);
            }
            showEmptyState(false);
            updateRelevantMealsIfNeeded(mealList);
            mealAdapter.updateList(mealList);

        } catch (JSONException jsonException) {
            showEmptyState(true);
            jsonException.printStackTrace();
            onError(jsonException.getMessage());
        }
    }

    private void updateRelevantMealsIfNeeded(List<Meal> mealList) {
        List<Meal> savedMealList = getSavedMealList();
        if (!savedMealList.isEmpty()) {//if data is saved
            for (int i = 0; i < mealList.size(); i++) {
                Meal currentMeal = mealList.get(i);////current meal is a given saved meal
                for (int j = 0; j < savedMealList.size(); j++) {//running on second list
                    if (savedMealList.get(j).getTitle().equals(currentMeal.getTitle())) {
                        // we have already liked this recipe
                        //TODO UPDATE ID TWO RECIPES EXIST
                        mealList.remove(savedMealList.get(j));
                        mealList.get(i).setLiked(true);
                    }
                }
            }
        }
    }

    private void showEmptyState(boolean show) {
        emptyStateIV.post(new Runnable() {
            @Override
            public void run() {
                emptyStateIV.setVisibility(show ? View.VISIBLE : View.GONE);
                Glide.with(ChooseMealFragment.this).load("https://i.pinimg.com/originals/88/36/65/8836650a57e0c941b4ccdc8a19dee887.png").into(emptyStateIV);
            }
        });
    }

    @Override
    public void onLoading(boolean isLoading) {
        progressBar.post(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public void onError(String error) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mealAdapter.updateList(new ArrayList<>());
                showEmptyState(true);
                progressBar.setVisibility(View.GONE);
                Utils.toast(getContext(), error);
            }
        });
    }

    public void handleRecipe(Meal meal) {
        if (meal.isLiked()) saveRecipe(meal);
        else Utils.remove(requireContext(), meal);
    }

    public void saveRecipe(Meal meal) {
        Utils.saveRecipe(requireContext(), meal);
        savedMealAdapter.updateProducts(Utils.getSavedMealList(requireContext()));
    }

    private List<Meal> getSavedMealList() {
        String currentSavedRecipes = sharedPreferences.getString(MEAL, null);
        Gson gson = new Gson();

        List<Meal> savedMeal;
        if (currentSavedRecipes != null) {
            // have have something already saved!!

            savedMeal = gson.fromJson(currentSavedRecipes, new TypeToken<List<Meal>>() {
            }.getType());

        } else savedMeal = new ArrayList<>();
        return savedMeal;
    }
}