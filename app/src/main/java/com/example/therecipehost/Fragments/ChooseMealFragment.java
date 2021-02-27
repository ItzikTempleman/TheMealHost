package com.example.therecipehost.Fragments;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.helper.widget.Flow;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.therecipehost.Adapters.MealAdapter;
import com.example.therecipehost.Adapters.SavedMealAdapter;
import com.example.therecipehost.Models.Category;
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
import java.util.Arrays;
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
    private RecyclerView mealRV;
    public static List<Meal> mealList, filteredMeals;
    private TextView featuredTV;
    private SharedPreferences sharedPreferences;
    private final Category[] categories = {
            new Category("Chicken"),
            new Category("Beef"),
            new Category("Fish"),
            new Category("Lamb"),
            new Category("Vegetarian"),
            new Category("Pasta"),
            new Category("Starter"),
            new Category("Dessert"),
            new Category("Side"),
            new Category("Other")
    };

    private final List<String> selectedCategories = new ArrayList<>();
    private Button filterBtn, moveToFilterBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_choose_meal, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        getAllMeals();
        setListeners();

    }

    private void getAllMeals() {
        mealList = new ArrayList<>();
        Utils.loadAsyncTask("", this);
    }

    private void initView(View view) {
        //initCategories(view);
        sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        featuredTV = view.findViewById(R.id.featured_tv);
        searchET = view.findViewById(R.id.search_recipe_et);
        filterBtn = view.findViewById(R.id.fragment_choose_meal_categories_filter_button);
        moveToFilterBtn = view.findViewById(R.id.move_to_filter);
        mealRV = view.findViewById(R.id.choose_meal_rv);
        progressBar = view.findViewById(R.id.loading_pb);
        emptyStateIV = view.findViewById(R.id.choose_meal_empty_state_image_view);
        savedMealAdapter = new SavedMealAdapter(getContext());
        mealAdapter = new MealAdapter(requireContext(), this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, true);
        mealRV.setLayoutManager(linearLayoutManager);
        mealRV.setAdapter(mealAdapter);
        Utils.handleSwiping(mealRV);
    }


//    private void initCategories(View view) {
//        ConstraintLayout container = view.findViewById(R.id.fragment_choose_meal_categories_container);
//        Flow flow = view.findViewById(R.id.fragment_choose_meal_categories_flow_view);
//
//        int[] buttonsIds = new int[10];
//        for (int i = 0; i < 10; i++) {
//            Button button = new Button(requireContext());
//
//            String currentCategoryTitle = categories[i].getText();
//            button.setText(currentCategoryTitle);
//
//            categories[i].setId(i + 1);
//            button.setId(categories[i].getId());
//            button.setPadding(48, 0, 48, 0);
//            button.setAllCaps(false);
//            button.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.choose_meal_white));
//            button.setOnClickListener(this);
//
//            container.addView(button);
//            buttonsIds[i] = (button.getId());
//        }
//        flow.setReferencedIds(buttonsIds);
//    }


    private void setListeners() {
        moveToFilterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToFilterDialogFragment();
            }
        });
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
                    //hideKeyBoard();
                    Utils.loadAsyncTask(s.toString(), ChooseMealFragment.this);

                } else featuredTV.setVisibility(View.VISIBLE);
            }
        });

//        filterBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                filter();
//            }
//        });
    }

    private void moveToFilterDialogFragment() {
        FilterDialogFragment filterDialogFragment = new FilterDialogFragment();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.filter_frame_layout, filterDialogFragment).commit();
    }


//    private void filter() {
//        filteredMeals = new ArrayList<>();
//        for (int i = 0; i < mealList.size(); i++) {
//            for (int j = 0; j < selectedCategories.size(); j++) {
//                if (mealList.get(i).getCategory().equals(selectedCategories.get(j))) {
//                    filteredMeals.add(mealList.get(i));
//                }
//            }
//        }
//        Log.d("FilteredMeals", Arrays.toString(filteredMeals.toArray()));
//        if (!filteredMeals.isEmpty()) {
//            mealAdapter.updateList(filteredMeals);
//        }
//    }

//    private void hideKeyBoard() {
//        InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//    }

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

    private void saveRecipe(Meal meal) {
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

        Button button = (Button) v;
        String currentButtonText = button.getText().toString();

        // Handling different Button 'UI' text and requested Category text
        if (currentButtonText.equals("Fish")) currentButtonText = "Seafood";
        else if (currentButtonText.equals("Other")) currentButtonText = "Miscellaneous";

        if (!selectedCategories.contains(currentButtonText)) {
            selectedCategories.add(currentButtonText);
            button.setBackgroundResource(R.drawable.choose_meal_filled);
            button.setTextColor(Color.WHITE);
        } else {
            selectedCategories.remove(currentButtonText);
            button.setBackgroundResource(R.drawable.choose_meal_white);
            button.setTextColor(Color.BLACK);
        }

        filterBtn.setEnabled(selectedCategories.size() > 0);
        Log.d("SELECTED", Arrays.toString(selectedCategories.toArray()));
    }
}