package com.example.therecipehost.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.therecipehost.Fragments.ChooseMealFragment;
import com.example.therecipehost.Models.Meal;
import com.example.therecipehost.R;
import com.example.therecipehost.Utils.Utils;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private List<Meal> previouslySearchedRecipeList = ChooseMealFragment.mealList;

    public HistoryAdapter(List<Meal> previouslySearchedRecipeList, ChooseMealFragment chooseMealFragment) {
        this.previouslySearchedRecipeList = previouslySearchedRecipeList;
    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder holder, int position) {
        final Meal meal = previouslySearchedRecipeList.get(position);
        Utils.loadImage(meal.getThumbPath(), holder.historyIV);
    }


    @Override
    public int getItemCount() {
        return previouslySearchedRecipeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView historyIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            historyIV = itemView.findViewById(R.id.history_iv);
        }
    }
}
