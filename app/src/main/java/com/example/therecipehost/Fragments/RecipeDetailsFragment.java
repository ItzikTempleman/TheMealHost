package com.example.therecipehost.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.therecipehost.Models.Meal;
import com.example.therecipehost.R;

import java.util.List;

import static com.example.therecipehost.Constants.GlobalConstants.MEAL;
import static com.example.therecipehost.MainActivity.tabLayout;


public class RecipeDetailsFragment extends Fragment {

    private Meal meal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipe_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (isMealInArguments()) {
            initViews(view);
        } else requireActivity().getSupportFragmentManager().popBackStack();
    }


    private boolean isMealInArguments() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            // we have something inside our bundle
            meal = arguments.getParcelable(MEAL);
            return meal != null;
        }
        return false;
    }

    private void initViews(View view) {
        List<String> ingredients, amountsOfEach;

        ImageView mealIV = view.findViewById(R.id.details_recipe_image);
        TextView mealTitleTV = view.findViewById(R.id.title_tv);
        TextView mealIngredients = view.findViewById(R.id.ingredients_tv);
        TextView amounts = view.findViewById(R.id.amounts_tv);
        TextView instructions = view.findViewById(R.id.instructions_tv);

        ingredients = meal.getIngredients();
        amountsOfEach = meal.getAmountOfEach();
        mealTitleTV.setText(meal.getTitle());
        Glide.with(this).load(meal.getThumbPath()).into(mealIV);
        instructions.setText(meal.getInstructions());

        amounts.setText("".join("\n", amountsOfEach));
        mealIngredients.setText("".join("\n", ingredients));
        hideTabLayout();
    }

    private void hideTabLayout() {
        tabLayout.setVisibility(View.GONE);
    }
}
