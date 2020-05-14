package com.example.navucsd;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;

/**
 * This activity temporary hardcodes the landmark details page.
 */
public class LandmarkDetailsActivity extends AppCompatActivity {

    private ViewPager landmarkPager;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landmark_details);

        collapsingToolbarLayout = findViewById(R.id.landmark_collapsing_toolbar);
        Toolbar toolbar = findViewById(R.id.landmark_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(LandmarkDetailsActivity.this);
            }
        });

        ActionBar actionbar = getSupportActionBar();
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        actionbar.setDisplayHomeAsUpEnabled(true);

        TabLayout tabLayout = findViewById(R.id.landmark_tablayout);
        landmarkPager = findViewById(R.id.landmark_viewpager);
        LandmarkAdapter pagerAdapter = new LandmarkAdapter(getSupportFragmentManager());
        landmarkPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(landmarkPager);
    }

    private class LandmarkAdapter extends FragmentStatePagerAdapter {

        private final String[] TAB_TITLES = {"Overview", "History"};

        public LandmarkAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return LandmarkDetailsOverviewFragment.newInstance("","");
                case 1:
                    return LandmarkDetailsHistoryFragment.newInstance("","");
            }
            return new MainPageFragment();
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return TAB_TITLES[position];
        }
    }

}
