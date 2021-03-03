package com.example.therecipehost.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.therecipehost.Fragments.ChooseMealFragment;
import com.example.therecipehost.Models.Meal;
import com.example.therecipehost.R;
import com.example.therecipehost.Utils.Utils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static com.example.therecipehost.Constants.GlobalConstants.HISTORY;
import static com.example.therecipehost.Constants.GlobalConstants.SHARED_PREFS;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.ViewHolder> {

    private final List<Meal> mealList = new ArrayList<>();
    private final List<Meal> historySavedList = new ArrayList<>();
    private final Context context;
    private final ChooseMealFragment chooseMealFragment;

    public MealAdapter(Context context, ChooseMealFragment chooseMealFragment) {
        this.context = context;
        this.chooseMealFragment = chooseMealFragment;
    }

    @NonNull
    @Override
    public MealAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.meal_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MealAdapter.ViewHolder holder, int position) {
        final Meal meal = mealList.get(position);
        holder.title.setText(meal.getTitle());
        holder.category.setText(meal.getCategory());
        holder.nationality.setText(meal.getNationality());
        Utils.loadImage(meal.getThumbPath(), holder.thumbImage);

        handleLikedState(holder, meal.isLiked());

        holder.addToListIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                meal.setLiked(!meal.isLiked());
                handleLikedState(holder, meal.isLiked());
                chooseMealFragment.handleRecipe(meal);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.moveToDetailsFragment(meal, context);
                meal.setWasSearched(true);
                if (meal.isWasSearched()) {
                    historySavedList.add(meal);
                    saveHistoryState();
                }
            }
        });
    }


    private void handleLikedState(ViewHolder holder, boolean getLiked) {
        holder.addToListIV.setImageDrawable(context.getResources().getDrawable(getLiked ? R.drawable.added_to_favorites : R.drawable.add_to_favorites));
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    public void updateList(List<Meal> newMealList) {
        mealList.clear();
        mealList.addAll(newMealList);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title, category, nationality;
        private final ImageView thumbImage, addToListIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.meal_title_tv);
            category = itemView.findViewById(R.id.category_tv);
            nationality = itemView.findViewById(R.id.nationality_tv);
            thumbImage = itemView.findViewById(R.id.recipe_thumb_image);
            addToListIV = itemView.findViewById(R.id.add_to_favorites_btn);
        }
    }

    private void saveHistoryState() {
        SharedPreferences sharedPreferences = Objects.requireNonNull(context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE));
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String savedHistory = gson.toJson(historySavedList);
        editor.putString(HISTORY, savedHistory);
        editor.apply();
    }
}

