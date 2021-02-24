package com.example.therecipehost;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.therecipehost.Adapters.FragmentViewPagerAdapter;
import com.example.therecipehost.Fragments.ChooseMealFragment;
import com.example.therecipehost.Fragments.ProfileFragment;
import com.example.therecipehost.Fragments.SavedFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {
    private final int[] tabImages = {R.drawable.recipes, R.drawable.saved, R.drawable.profile};
    private final int[] tabTitles = {R.string.Recipes, R.string.Saved, R.string.Profile};
    private final Fragment[] fragments = {new ChooseMealFragment(), new SavedFragment(), new ProfileFragment()};
    public static TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        if (savedInstanceState != null) {
//            isChooseMealFragmentAlreadyAlive = savedInstanceState.getBoolean(IS_FRAGMENT_ALIVE_STR);
//            chooseMealFragment = new ChooseMealFragment();
//        }
        initView();
    }


    public void initView() {
        tabLayout = findViewById(R.id.main_activity_tab_layout);
        ViewPager2 viewPager2 = findViewById(R.id.view_pager);
        viewPager2.setAdapter(new FragmentViewPagerAdapter(this));
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText(tabTitles[0]);
                        tab.setIcon(tabImages[0]);
                        break;
                    case 1:
                        tab.setText(tabTitles[1]);
                        tab.setIcon(tabImages[1]);
                        break;
                    case 2:
                        tab.setText(tabTitles[2]);
                        tab.setIcon(tabImages[2]);
                        break;
                }
            }
        }

        );
        tabLayoutMediator.attach();
//        tabLayout.addOnTabSelectedListener(this);
//        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#4287f5"));
        initTabs();
    }

    private void initTabs() {
//        for (int i = 0; i < tabLayout.getTabCount(); i++) {
//            TabLayout.Tab tab = tabLayout.getTabAt(i);
//
//            if (tab != null) {
//                tab.setText(getString(tabTitles[i]));
//                tab.setIcon(tabImages[tab.getPosition()]);
//            }
//            TabLayout.Tab firstTab = tabLayout.getTabAt(0);
//            if (firstTab != null) firstTab.select();
//        }
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
//        switch (tab.getPosition()) {
//            case 0:
//                chooseMealFragment = new ChooseMealFragment();
//                Utils.changeFragment(getSupportFragmentManager(), R.id.main_activity_frame_layout, chooseMealFragment, false);
//                break;
//            case 1:
//                SavedFragment savedFragment = (SavedFragment) fragments[tab.getPosition()];
//                Utils.changeFragment(getSupportFragmentManager(), R.id.main_activity_frame_layout, savedFragment, false);
//                break;
//            case 2:
//                ProfileFragment profileFragment = (ProfileFragment) fragments[tab.getPosition()];
//                Utils.changeFragment(getSupportFragmentManager(), R.id.main_activity_frame_layout, profileFragment, false);
//                break;
//
//        }
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
