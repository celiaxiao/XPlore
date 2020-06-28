package com.example.navucsd;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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

import com.example.navucsd.database.Location;
import com.example.navucsd.utils.DownloadImageSaveTask;
import com.example.navucsd.utils.DownloadImageTask;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * This activity temporary hardcodes the landmark details page.
 */
public class LandmarkDetailsActivity extends AppCompatActivity {

    private ViewPager landmarkPager;
    private ImageView landmarkThumbnail;
    private HashMap<String, Bitmap> images = new HashMap<>();
    private CollapsingToolbarLayout collapsingToolbarLayout;

    private SearchBarDB database;
    private Location currLocation;
    private String currLocationName;
    private static final String DEFAULT_LOCATION = "JSOE (Fallen Star)";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landmark_details);

        database = new SearchBarDB(this, "one by one");

        Bundle argument = getIntent().getExtras();
        if (argument != null) {
            currLocationName = argument.getString("placeName");
            if (currLocationName != null) {
                currLocation = database.getByName(currLocationName);
            }
        }
        else {
            currLocation = database.getByName(DEFAULT_LOCATION);
        }

        collapsingToolbarLayout = findViewById(R.id.landmark_collapsing_toolbar);
        collapsingToolbarLayout.setTitle(currLocation.getName());
        Toolbar toolbar = findViewById(R.id.landmark_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ActionBar actionbar = getSupportActionBar();
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        actionbar.setDisplayHomeAsUpEnabled(true);

        landmarkThumbnail = findViewById(R.id.landmark_title_image);

        if (images.containsKey(currLocation.getThumbnailPhoto())) {
            landmarkThumbnail.setImageBitmap(images.get(currLocation.getThumbnailPhoto()));
        }
        else {
            new DownloadImageSaveTask(landmarkThumbnail, images).execute(currLocation.getThumbnailPhoto());
        }

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
                    return LandmarkDetailsOverviewFragment.newInstance(currLocation, database);
                case 1:
                    return LandmarkDetailsHistoryFragment.newInstance("","");
            }
            return new MainPageFragment();
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return TAB_TITLES[position];
        }
    }

}
