package com.example.therecipehost.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.therecipehost.Adapters.SavedMealAdapter;
import com.example.therecipehost.Models.Meal;
import com.example.therecipehost.R;
import com.example.therecipehost.Utils.Utils;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static com.example.therecipehost.Constants.GlobalConstants.MEAL;
import static com.example.therecipehost.Constants.GlobalConstants.SHARED_PREFS;
import static com.example.therecipehost.MainActivity.tabLayout;


public class SavedFragment extends Fragment {
    public List<Meal> savedMealList;
    private SavedMealAdapter savedMealAdapter;
    private ItemTouchHelper.SimpleCallback itemTouchHelperCallback;
    private CoordinatorLayout coordinatorLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_saved, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        getSavedList();
    }

    public void getSavedList() {
        if (!Utils.getSavedMealList(getContext()).isEmpty()) {
            updateMealList();
            savedMealAdapter.updateProducts(savedMealList);
        }else savedMealAdapter.updateProducts(new ArrayList<>());
    }

    private void initView(View view) {
        coordinatorLayout = view.findViewById(R.id.coordinator_layout);
        RecyclerView likedRV = view.findViewById(R.id.favorites_rv_saved);
        savedMealAdapter = new SavedMealAdapter(getContext());
        LinearLayoutManager verticalLayout = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        likedRV.setLayoutManager(verticalLayout);
        likedRV.setAdapter(savedMealAdapter);
        handleSwiping();
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(likedRV);
    }

    public void handleSwiping() {
        itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Meal deletedMeal = Utils.getSavedMealList(requireContext()).get(position);
                remove(deletedMeal);
            }
        };
    }

    public void remove(Meal deletedMeal) {
        deletedMeal.setLiked(false);
        Utils.remove(requireContext(), deletedMeal);
        updateMealList();
        savedMealAdapter.updateProducts(Utils.getSavedMealList(requireContext()));
        handleSnackBar(deletedMeal);
    }

    private void handleSnackBar(Meal deletedMeal) {
        Snackbar deleteSV = Snackbar.make(coordinatorLayout, "Recipe was removed from list", Snackbar.LENGTH_LONG);

        deleteSV.setAction("retrieve item?", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrieve(deletedMeal);
            }
        });
        deleteSV.show();
        /*Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tabLayout.setVisibility(View.VISIBLE);
            }
        }, 3000);*/
        //if (savedMealList.isEmpty()) Utils.toast(getContext(), "You have no saved or liked recipes at the moment");
    }

    private void retrieve(Meal deletedMeal) {
        savedMealList.add(deletedMeal);
        Snackbar retrieveSB = Snackbar.make(coordinatorLayout, "item retrieved", Snackbar.LENGTH_LONG);
        retrieveSB.show();
        Utils.saveRecipe(requireContext(), deletedMeal);
        updateMealList();
        savedMealAdapter.updateProducts(Utils.getSavedMealList(requireContext()));
    }

    private void updateMealList() {
        savedMealList = Utils.getSavedMealList(requireContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        getSavedList();
    }
}