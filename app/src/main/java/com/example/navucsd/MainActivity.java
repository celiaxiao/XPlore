package com.example.navucsd;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private final int[] TAB_ICONS = {R.drawable.places, R.drawable.mainpage, R.drawable.tours};
    private final String[] TAB_TITLES = {"Places", "", "Tours"};
    private final int MAIN_PAGE_INDEX = 1;
    private ViewPager mainPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CurvedBottomNavigationViewTabLayout tabLayout = findViewById(R.id.customBottomBar);
        mainPager = findViewById(R.id.main_pager);
        MainPagerAdapter pagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        mainPager.setAdapter(pagerAdapter);

        tabLayout.setupWithViewPager(mainPager);
        for (int i = 0; i < TAB_ICONS.length; i++) {
            tabLayout.getTabAt(i).setIcon(TAB_ICONS[i]);
        }
        tabLayout.setTabIconTint(getResources().getColorStateList(R.color.bottom_tab_tint));

        TabLayout.Tab mainPageTab = tabLayout.getTabAt(MAIN_PAGE_INDEX);
        mainPageTab.setCustomView(R.layout.custom_main_button);
        tabLayout.selectTab(mainPageTab);
    }

    private class MainPagerAdapter extends FragmentStatePagerAdapter {

        public MainPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Returns the fragment for each position.
         *
         * @param position the position queried
         * @return the fragment for each position
         */
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: return new PlacesPageFragment();
                case 1: return new MainPageFragment();
                case 2: return new ToursPageFragment();
            }
            // If this happens there must be a bug in our code.
            // TODO error reporting
            return new MainPageFragment();
        }

        @Override
        public int getCount() {
            return TAB_ICONS.length;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return TAB_TITLES[position];
        }
    }
}
