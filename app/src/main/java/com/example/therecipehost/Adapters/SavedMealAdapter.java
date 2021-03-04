package com.example.therecipehost.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.therecipehost.Fragments.ChooseMealFragment;
import com.example.therecipehost.Fragments.SavedFragment;
import com.example.therecipehost.Models.Meal;
import com.example.therecipehost.R;
import com.example.therecipehost.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class SavedMealAdapter extends RecyclerView.Adapter<SavedMealAdapter.ViewHolder> {
    public List<Meal> meals = new ArrayList<>();
    private final Context context;

    public SavedMealAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public SavedMealAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SavedMealAdapter.ViewHolder holder, int position) {

        final Meal meal = meals.get(position);
        Utils.loadImage(meal.getThumbPath(), holder.imageInSaved);
        holder.titleInSaved.setText(meal.getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.moveToDetailsFragment(meal, context);
            }
        });

    }


    @Override
    public int getItemCount() {
        return meals.size();
    }

    public void updateProducts(List<Meal> newMealList) {
        meals.clear();
        meals.addAll(newMealList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleInSaved;
        ImageView imageInSaved;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageInSaved = itemView.findViewById(R.id.saved_iv);
            titleInSaved = itemView.findViewById(R.id.saved_title);
        }
    }

}
