package com.example.therecipehost.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.therecipehost.Fragments.ChooseMealFragment;
import com.example.therecipehost.Fragments.ProfileFragment;
import com.example.therecipehost.Fragments.SavedFragment;

import java.util.ArrayList;
import java.util.List;

public class FragmentViewPagerAdapter extends FragmentStateAdapter {
    public final ChooseMealFragment chooseMealFragment = new ChooseMealFragment();
    public final SavedFragment savedFragment = new SavedFragment();
    public final ProfileFragment profileFragment = new ProfileFragment();

    private Fragment[] fragments = {
            chooseMealFragment,
            savedFragment,
            profileFragment
    };

    public FragmentViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments[position];
    }

    @Override
    public int getItemCount() {
        return fragments.length;
    }
}
