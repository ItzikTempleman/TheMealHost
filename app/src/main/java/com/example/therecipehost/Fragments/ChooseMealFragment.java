package com.example.therecipehost.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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
import com.example.therecipehost.Models.IResponse;
import com.example.therecipehost.Models.Meal;
import com.example.therecipehost.R;
import com.example.therecipehost.Utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static com.example.therecipehost.Constants.GlobalConstants.MEAL;
import static com.example.therecipehost.Constants.GlobalConstants.SHARED_PREFS;

public class ChooseMealFragment extends Fragment implements IResponse, View.OnClickListener {
    private EditText searchET;
    private MealAdapter mealAdapter;
    private SavedMealAdapter savedMealAdapter;
    private ProgressBar progressBar;
    public ImageView emptyStateIV;
    public static List<Meal> mealList;
    private TextView featuredTV;
    private SharedPreferences sharedPreferences;
    private boolean isFiltered;
    private Button chickenBtn, beefBtn, lambBtn, fishBtn, vegetarianBtn, otherBtn, starterBtn, desertBtn, pastaBtn, sideBtn;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_choose_meal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        initAsyncTaskFirstTime();
        setListeners();

    }

    private void initAsyncTaskFirstTime() {
        mealList = new ArrayList<>();
        Utils.loadAsyncTask("", this);
    }

    private void initView(View view) {
        initBtn(view);
        sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        featuredTV = view.findViewById(R.id.featured_tv);
        searchET = view.findViewById(R.id.search_recipe_et);

        RecyclerView mealRV = view.findViewById(R.id.choose_meal_rv);
        progressBar = view.findViewById(R.id.loading_pb);
        emptyStateIV = view.findViewById(R.id.choose_meal_empty_state_image_view);
        savedMealAdapter = new SavedMealAdapter(getContext());
        mealAdapter = new MealAdapter(requireContext(), this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        mealRV.setLayoutManager(linearLayoutManager);
        mealRV.setAdapter(mealAdapter);
    }

    private void initBtn(View view) {
        chickenBtn = view.findViewById(R.id.category_chicken);
        beefBtn = view.findViewById(R.id.category_beef);
        lambBtn = view.findViewById(R.id.category_lamb);
        fishBtn = view.findViewById(R.id.category_fish);
        vegetarianBtn = view.findViewById(R.id.category_vegetarian);
        otherBtn = view.findViewById(R.id.category_other);
        starterBtn = view.findViewById(R.id.category_starter);
        desertBtn = view.findViewById(R.id.category_desert);
        pastaBtn = view.findViewById(R.id.category_pasta);
        sideBtn = view.findViewById(R.id.category_side);

        chickenBtn.setOnClickListener(this);
        beefBtn.setOnClickListener(this);
        lambBtn.setOnClickListener(this);
        fishBtn.setOnClickListener(this);
        vegetarianBtn.setOnClickListener(this);
        otherBtn.setOnClickListener(this);
        starterBtn.setOnClickListener(this);
        desertBtn.setOnClickListener(this);
        pastaBtn.setOnClickListener(this);
        sideBtn.setOnClickListener(this);
    }


    private void setListeners() {
        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    progressBar.setVisibility(View.VISIBLE);
                    featuredTV.setVisibility(View.GONE);
                    hideKeyBoard();
                    Utils.loadAsyncTask(s.toString(), ChooseMealFragment.this);

                } else featuredTV.setVisibility(View.VISIBLE);
            }
        });
    }

    private void hideKeyBoard() {
        InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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
        if (!savedMealList.isEmpty()) {
            for (int i = 0; i < mealList.size(); i++) {
                Meal currentMeal = mealList.get(i);
                for (int j = 0; j < savedMealList.size(); j++) {
                    if (savedMealList.get(j).getTitle().equals(currentMeal.getTitle())) {
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
            savedMeal = gson.fromJson(currentSavedRecipes, new TypeToken<List<Meal>>() {
            }.getType());

        } else savedMeal = new ArrayList<>();
        return savedMeal;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.category_chicken:
                if (!isFiltered) {
                    addToFilter();
                    chickenBtn.setBackgroundResource(R.drawable.choose_meal_filled);
                } else {
                    removeFilter();
                    chickenBtn.setBackgroundResource(R.drawable.choose_meal_white);
                }
                isFiltered = !isFiltered;
                break;

            case R.id.category_beef:
                if (!isFiltered) {
                    addToFilter();
                    beefBtn.setBackgroundResource(R.drawable.choose_meal_filled);
                } else {
                    removeFilter();
                    beefBtn.setBackgroundResource(R.drawable.choose_meal_white);
                }
                isFiltered = !isFiltered;
                break;

            case R.id.category_lamb:
                if (!isFiltered) {
                    addToFilter();
                    lambBtn.setBackgroundResource(R.drawable.choose_meal_filled);
                } else {
                    removeFilter();
                    lambBtn.setBackgroundResource(R.drawable.choose_meal_white);
                }
                isFiltered = !isFiltered;
                break;

            case R.id.category_fish:
                if (!isFiltered) {
                    addToFilter();
                    fishBtn.setBackgroundResource(R.drawable.choose_meal_filled);
                } else {
                    removeFilter();
                    fishBtn.setBackgroundResource(R.drawable.choose_meal_white);
                }
                isFiltered = !isFiltered;
                break;


            case R.id.category_vegetarian:
                if (!isFiltered) {
                    addToFilter();
                    vegetarianBtn.setBackgroundResource(R.drawable.choose_meal_filled);
                } else {
                    removeFilter();
                    vegetarianBtn.setBackgroundResource(R.drawable.choose_meal_white);
                }
                isFiltered = !isFiltered;
                break;


            case R.id.category_other:
                if (!isFiltered) {
                    addToFilter();
                    otherBtn.setBackgroundResource(R.drawable.choose_meal_filled);
                } else {
                    removeFilter();
                    otherBtn.setBackgroundResource(R.drawable.choose_meal_white);
                }
                isFiltered = !isFiltered;
                break;


            case R.id.category_starter:
                if (!isFiltered) {
                    addToFilter();
                    starterBtn.setBackgroundResource(R.drawable.choose_meal_filled);
                } else {
                    removeFilter();
                    starterBtn.setBackgroundResource(R.drawable.choose_meal_white);
                }
                isFiltered = !isFiltered;
                break;

            case R.id.category_desert:
                if (!isFiltered) {
                    addToFilter();
                    desertBtn.setBackgroundResource(R.drawable.choose_meal_filled);
                } else {
                    removeFilter();
                    desertBtn.setBackgroundResource(R.drawable.choose_meal_white);
                }
                isFiltered = !isFiltered;
                break;

            case R.id.category_pasta:
                if (!isFiltered) {
                    addToFilter();
                    pastaBtn.setBackgroundResource(R.drawable.choose_meal_filled);
                } else {
                    removeFilter();
                    pastaBtn.setBackgroundResource(R.drawable.choose_meal_white);

                }
                isFiltered = !isFiltered;
                break;

            case R.id.category_side:
                if (!isFiltered) {
                    addToFilter();
                    sideBtn.setBackgroundResource(R.drawable.choose_meal_filled);
                } else {
                    removeFilter();
                    sideBtn.setBackgroundResource(R.drawable.choose_meal_white);
                }
                isFiltered = !isFiltered;
                break;
        }
    }

    List<Meal> mealFilter = new ArrayList<>();

    private void addToFilter() {

    }

    private void removeFilter() {

    }
}