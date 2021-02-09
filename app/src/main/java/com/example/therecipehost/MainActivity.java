package com.example.therecipehost;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.therecipehost.Fragments.ChooseMealFragment;
import com.example.therecipehost.Fragments.ProfileFragment;
import com.example.therecipehost.Fragments.SavedFragment;
import com.example.therecipehost.Utils.Utils;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {
    private final int[] tabImages = {
            R.drawable.recipes,
            R.drawable.saved,
            R.drawable.profile
    };

    private final int[] tabTitles = {
            R.string.Recipes,
            R.string.Saved,
            R.string.Profile
    };

    private final Fragment[] fragments = {
            new ChooseMealFragment(),
            new SavedFragment(),
            new ProfileFragment(),
    };
    public static TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }


    public void initView() {
        tabLayout = findViewById(R.id.main_activity_tab_layout);
        tabLayout.addOnTabSelectedListener(this);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#4287f5"));
        initTabs();
    }

    private void initTabs() {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);

            if (tab != null) {
                tab.setText(getString(tabTitles[i]));
                tab.setIcon(tabImages[tab.getPosition()]);
            }
            TabLayout.Tab firstTab = tabLayout.getTabAt(0);
            if (firstTab != null) firstTab.select();
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        handleTabSelected(tab);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        handleTabSelected(tab);
    }

    private void handleTabSelected(TabLayout.Tab tab) {

        Fragment fragment = fragments[tab.getPosition()];
        Utils.changeFragment(getSupportFragmentManager(), R.id.main_activity_frame_layout, fragment, false);
    }

    public void showTabLayout(boolean show) {
        tabLayout.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            showTabLayout(true);
            getSupportFragmentManager().popBackStack();
        } else super.onBackPressed();
    }
}
