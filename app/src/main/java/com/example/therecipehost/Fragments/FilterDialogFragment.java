package com.example.therecipehost.Fragments;

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

import com.example.therecipehost.Adapters.MealAdapter;
import com.example.therecipehost.Models.Category;
import com.example.therecipehost.Models.Meal;
import com.example.therecipehost.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilterDialogFragment extends DialogFragment implements View.OnClickListener {
    private final List<String> selectedCategories = new ArrayList<>();
    private List<Meal> mealList;
    public MealAdapter mealAdapter;
    private Button filterBtn;
    public ChooseMealFragment chooseMealFragment = new ChooseMealFragment();
    private final Category[] categories = {new Category("Chicken"), new Category("Beef"), new Category("Fish"), new Category("Lamb"), new Category("Vegetarian"), new Category("Pasta"), new Category("Starter"), new Category("Dessert"), new Category("Side"), new Category("Other")
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.filter_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setListeners();
    }


    private void initView(View view) {
        mealList = ChooseMealFragment.mealList;
        initCategories(view);
        filterBtn = view.findViewById(R.id.fragment_choose_meal_categories_filter_button);
        mealAdapter = new MealAdapter(requireContext(), chooseMealFragment);
    }

    private void setListeners() {
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter();
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
        Log.d("SELECTED", Arrays.toString(selectedCategories.toArray()));

    }


    private void initCategories(View view) {

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

    private void filter() {
        List<Meal> filteredMeals = new ArrayList<>();
        for (int i = 0; i < mealList.size(); i++) {
            for (int j = 0; j < selectedCategories.size(); j++) {
                if (mealList.get(i).getCategory().equals(selectedCategories.get(j))) {
                    filteredMeals.add(mealList.get(i));
                }
            }
        }
        Log.d("FilteredMeals", Arrays.toString(filteredMeals.toArray()));
        if (!filteredMeals.isEmpty()) {
            mealAdapter.updateList(filteredMeals);
        }
    }
}


