package com.example.therecipehost.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.therecipehost.Adapters.HistoryAdapter;
import com.example.therecipehost.Adapters.MealAdapter;
import com.example.therecipehost.Adapters.SavedMealAdapter;
import com.example.therecipehost.Models.IResponse;
import com.example.therecipehost.Models.Meal;
import com.example.therecipehost.R;
import com.example.therecipehost.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.therecipehost.Constants.GlobalConstants.HISTORY;
import static com.example.therecipehost.Constants.GlobalConstants.MEAL;

public class ChooseMealFragment extends Fragment implements IResponse {
    private EditText searchET;
    private MealAdapter mealAdapter;
    private SavedMealAdapter savedMealAdapter;
    private ProgressBar progressBar;
    public ImageView emptyStateIV, filterIV;
    public static List<Meal> mealList;
    public Button removeHistoryBtn;
    private HistoryAdapter historyAdapter;
    public TextView previouslySearchedTV;

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
        initHistory(view);
        loadHistory();
    }

    private void getAllMeals() {
        mealList = new ArrayList<>();
        Utils.loadAsyncTask("", this);
    }

    private void initView(View view) {
        previouslySearchedTV = view.findViewById(R.id.previously_searched_tv);
        removeHistoryBtn = view.findViewById(R.id.remove_history);
        handleRemoveButtonVisibility();
        searchET = view.findViewById(R.id.search_recipe_et);
        filterIV = view.findViewById(R.id.filter_image_view);
        RecyclerView mealRV = view.findViewById(R.id.choose_meal_rv);
        progressBar = view.findViewById(R.id.loading_pb);
        emptyStateIV = view.findViewById(R.id.choose_meal_empty_state_image_view);
        savedMealAdapter = new SavedMealAdapter(getContext());
        mealAdapter = new MealAdapter(requireContext(), this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        mealRV.setLayoutManager(linearLayoutManager);
        mealRV.setAdapter(mealAdapter);
        Utils.handleSwiping(mealRV);
    }

    private void handleRemoveButtonVisibility() {
        removeHistoryBtn.setVisibility(Utils.<Meal>getList(requireContext(), HISTORY).isEmpty() ? View.GONE : View.VISIBLE);
    }

    private void initHistory(View view) {
        historyAdapter = new HistoryAdapter(getContext());
        LinearLayoutManager historyLayout = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        RecyclerView historyRV = view.findViewById(R.id.history_of_recipe_rv);
        historyRV.setLayoutManager(historyLayout);
        historyRV.setAdapter(historyAdapter);
    }

    private void loadHistory() {
        List<Meal> savedHistoryList = Utils.getList(getContext(), HISTORY);
        if (!savedHistoryList.isEmpty()) {
            historyAdapter.updateList(savedHistoryList);
        } else historyAdapter.updateList(new ArrayList<>());
    }

    public void updateHistory() {
        historyAdapter.updateList(Utils.getList(getContext(), HISTORY));
    }

    private void setListeners() {
        removeHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.removeHistory(getContext(), HISTORY)) {
                    removeHistoryBtn.setVisibility(View.GONE);
                    previouslySearchedTV.setText(R.string.previous_search);
                }

                historyAdapter.updateList(new ArrayList<>());

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
                    Utils.loadAsyncTask(s.toString(), ChooseMealFragment.this);
                }
            }
        });

        filterIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToFilterDialogFragment();
            }
        });

    }

    private void moveToFilterDialogFragment() {
        FilterDialogFragment filterDialogFragment = new FilterDialogFragment();
        filterDialogFragment.setChooseMealFragmentRef(this);
        filterDialogFragment.show(requireActivity().getSupportFragmentManager(), null);
    }

    public void filter(List<String> selectedCategories) {
        List<Meal> filteredMeals = new ArrayList<>();
        for (int i = 0; i < mealList.size(); i++) {
            for (int j = 0; j < selectedCategories.size(); j++) {
                if (mealList.get(i).getCategory().equals(selectedCategories.get(j))) {
                    filteredMeals.add(mealList.get(i));
                }
            }
        }

        mealAdapter.updateList(filteredMeals.isEmpty() ? mealList : filteredMeals);
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
        if (getContext() == null) return;
        List<Meal> savedMealList = Utils.getList(requireContext(), MEAL);
        if (!savedMealList.isEmpty()) {

            for (int i = 0; i < mealList.size(); i++) {
                Meal currentMeal = mealList.get(i);
                for (int j = 0; j < savedMealList.size(); j++) {
                    Meal savedMeal = savedMealList.get(j);
                    if (savedMealList.get(j).getTitle().equals(currentMeal.getTitle())) {
                        mealList.remove(savedMealList.get(j));
                        mealList.get(i).setLiked(savedMeal.isLiked());
                    }
                }
            }
        } else clearAllLikedMeals();
    }

    private void clearAllLikedMeals() {
        for (int i = 0; i < mealList.size(); i++) {
            mealList.get(i).setLiked(false);
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
        else Utils.remove(requireContext(), meal, MEAL);
    }

    private void saveRecipe(Meal meal) {
        Utils.saveList(requireContext(), meal, MEAL);
        savedMealAdapter.updateProducts(Utils.getList(requireContext(), MEAL));
    }

    @Override
    public void onResume() {
        super.onResume();
        updateRelevantMealsIfNeeded(mealList);
        mealAdapter.updateList(mealList);
    }
}
