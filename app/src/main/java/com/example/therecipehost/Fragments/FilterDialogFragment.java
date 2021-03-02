package com.example.therecipehost.Fragments;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.helper.widget.Flow;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import com.example.therecipehost.Adapters.MealAdapter;
import com.example.therecipehost.Models.Category;
import com.example.therecipehost.Models.Meal;
import com.example.therecipehost.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static com.example.therecipehost.Constants.GlobalConstants.MEAL;
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
    }

    private void initCategories(View view) {
        // TODO: Load selected categories (if exist) - 3/1/21
        ConstraintLayout container = view.findViewById(R.id.fragment_choose_meal_categories_container);
        Flow flow = view.findViewById(R.id.fragment_choose_meal_categories_flow_view);

        int[] buttonsIds = new int[10];
        for (int i = 0; i < 10; i++) {
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
             //       saveFilterState();
                }
                dismiss();
            }

            private void saveFilterState() {
                // todo save selected categories in SharedPref
                SharedPreferences sharedPreferences= Objects.requireNonNull(getActivity()).getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();

                String updatedList = gson.toJson(mealList);
                editor.putString(MEAL, updatedList);
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
}


