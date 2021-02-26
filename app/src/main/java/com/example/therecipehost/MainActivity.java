package com.example.therecipehost;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.therecipehost.Adapters.FragmentViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {
    private final int[] tabImages = {R.drawable.recipes, R.drawable.saved, R.drawable.profile};
    private final int[] tabTitles = {R.string.Recipes, R.string.Saved, R.string.Profile};
    public static TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        viewPager2.setOffscreenPageLimit(1);
        tabLayoutMediator.attach();
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
