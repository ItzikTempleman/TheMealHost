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

        ImageView mealIV = view.findViewById(R.id.details_recipe_image);
        TextView mealTitleTV = view.findViewById(R.id.title_tv);
        TextView mealIngredients = view.findViewById(R.id.ingredients_tv);
        TextView amounts = view.findViewById(R.id.amounts_tv);
        TextView instructions = view.findViewById(R.id.instructions_tv);
        TextView nationality = view.findViewById(R.id.country_tv);

        mealTitleTV.setText(meal.getTitle());
        Glide.with(this).load(meal.getThumbPath()).into(mealIV);
        instructions.setText(meal.getInstructions());

        String firstLetter = String.valueOf(meal.getNationality().charAt(0));
        if (firstLetter.equals("A") || firstLetter.equals("E") || firstLetter.equals("I") || firstLetter.equals("O")) {
            nationality.setText("An " + meal.getNationality().toLowerCase() + " cuisine");
        } else
            nationality.setText("A " + meal.getNationality().toLowerCase() + " cuisine");

        amounts.setText(meal.getAmountOfEach());
        mealIngredients.setText(meal.getIngredients());

        hideTabLayout();
    }

    private void hideTabLayout() {
        tabLayout.setVisibility(View.GONE);
    }
}
