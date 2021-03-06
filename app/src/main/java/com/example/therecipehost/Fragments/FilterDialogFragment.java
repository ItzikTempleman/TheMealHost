package com.example.therecipehost.Fragments;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.helper.widget.Flow;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.example.therecipehost.Models.Category;
import com.example.therecipehost.Models.Meal;
import com.example.therecipehost.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static com.example.therecipehost.Constants.GlobalConstants.FILTERED_CATEGORY;
import static com.example.therecipehost.Constants.GlobalConstants.SHARED_PREFS;

public class FilterDialogFragment extends DialogFragment implements View.OnClickListener {

    private final List<String> selectedCategories = new ArrayList<>();
    private List<Meal> mealList;
    private Button filterBtn;
    public ChooseMealFragment chooseMealFragment;
    private final Category[] categories = {new Category("Chicken"), new Category("Beef"), new Category("Fish"), new Category("Lamb"), new Category("Vegetarian"), new Category("Pasta"), new Category("Starter"), new Category("Dessert"), new Category("Side"), new Category("Other")};


    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.filter_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        initView(view);
        setListeners();
    }

    private void initView(View view) {
        mealList = ChooseMealFragment.mealList;
        initCategories(view);
        filterBtn = view.findViewById(R.id.fragment_choose_meal_categories_filter_button);
        //loadFilteredCategories();
    }


    private void initCategories(View view) {
        // TODO: Load selected categories (if exist) - 3/1/21
        ConstraintLayout container = view.findViewById(R.id.fragment_choose_meal_categories_container);
        Flow flow = view.findViewById(R.id.fragment_choose_meal_categories_flow_view);

        int[] buttonsIds = new int[10];
        for (int i = 0; i < categories.length; i++) {
            Button button = new Button(requireContext());
            String currentCategoryTitle = categories[i].getText();
            button.setText(currentCategoryTitle);
            categories[i].setId(i + 1);
            button.setId(categories[i].getId());
            button.setPadding(48, 0, 48, 0);
            button.setAllCaps(false);
            button.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.choose_meal_white));
            button.setOnClickListener(this);
            container.addView(button);
            buttonsIds[i] = (button.getId());
        }
        flow.setReferencedIds(buttonsIds);
    }

    public void setChooseMealFragmentRef(ChooseMealFragment chooseMealFragment) {
        this.chooseMealFragment = chooseMealFragment;
    }

    private void setListeners() {
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chooseMealFragment != null) {
                    chooseMealFragment.filter(selectedCategories);
                    saveFilterState();
                }
                dismiss();
            }

            private void saveFilterState() {
                SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                String filteredList = gson.toJson(selectedCategories);
                editor.putString(FILTERED_CATEGORY, filteredList);
                editor.apply();
            }
        });
    }

    @Override
    public void onClick(View v) {
        Button button = (Button) v;
        String currentButtonText = button.getText().toString();
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
    }


    private List<Meal> loadFilteredCategories() {
        List<Meal> savedFilteredList = new ArrayList<>();
        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(FILTERED_CATEGORY, null);
        if (json != null) {
            Type type = new TypeToken<List<Meal>>() {
            }.getType();
            savedFilteredList = gson.fromJson(json, type);
        }
        return savedFilteredList;
    }
}


