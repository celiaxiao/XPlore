package com.example.navucsd;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

	private final int[] TAB_ICONS = {
		R.drawable.places,
		R.drawable.main_page_tab_icon_active,
		R.drawable.tours
	};
	private final String[] TAB_TITLES = {"Places", "", "Tours"};
	private final int MAIN_PAGE_TAB_INDEX = 1;
	private ViewPager mainPager;
	private Location location;
	private static final int PERMISSION_REQUEST_FINE_LOCATION = 1000;
	private SharedPreferences sharedPref;
	private SharedPreferences.Editor editor;
	private String disabledLocationInApp = "disabledLocationInApp";
	// TODO: use or delete
	private double longitude;
	private double latitude;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		CurvedBottomNavigationViewTabLayout tabLayout = findViewById(R.id.customBottomBar);
		mainPager = findViewById(R.id.main_page_pager);
		MainPagerAdapter pagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
		mainPager.setAdapter(pagerAdapter);

		tabLayout.setupWithViewPager(mainPager);
		for (int i = 0; i < TAB_ICONS.length; i++) {
			if (i != MAIN_PAGE_TAB_INDEX) tabLayout.getTabAt(i).setIcon(TAB_ICONS[i]);
		}
		tabLayout.setTabIconTint(getResources().getColorStateList(R.color.bottom_tab_tint));

		TabLayout.Tab mainPageTab = tabLayout.getTabAt(MAIN_PAGE_TAB_INDEX);
		// XXX: What do the docs mean by "Components that add a listener should take care to remove
		// it when finished?
		tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				if (tab.getPosition() != MAIN_PAGE_TAB_INDEX) return;
				ImageView image_view = findViewById(R.id.main_page_tab_icon);
				image_view.setBackgroundResource(R.drawable.main_page_tab_icon_active);
			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab) {
				if (tab.getPosition() != MAIN_PAGE_TAB_INDEX) return;
				ImageView image_view = findViewById(R.id.main_page_tab_icon);
				image_view.setBackgroundResource(R.drawable.main_page_tab_icon_inactive);
			}

			@Override
			public void onTabReselected(TabLayout.Tab tab) {

			}
		});
		tabLayout.selectTab(mainPageTab);

		CircularClickableImageView image_view = findViewById(R.id.main_page_tab_icon);
		image_view.setView(mainPager);
		image_view.setOnClickListener(view -> {
			tabLayout.selectTab(tabLayout.getTabAt(MAIN_PAGE_TAB_INDEX));
		});

		// Check if user has explicitly disabled location permission in app
		sharedPref = getPreferences(Context.MODE_PRIVATE);
		editor = sharedPref.edit();
		boolean isDisabled = sharedPref.getBoolean(disabledLocationInApp, false);

		// Check if location permission is granted
		if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission( this,
				Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
			// Permission is not granted
			Log.d("location", "Permission not yet granted");
			if (!isDisabled) {
				// Has not explicitly disabled location in app
				ActivityCompat.requestPermissions(this, new String[]{
						Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_FINE_LOCATION);
			}
		}
		else {
			// Permission has already been granted
			Log.d("location", "Permission already granted");
			location = GpsUtil.getInstance(MainActivity.this).getLastLocation();
			if (location != null) {
				longitude = location.getLongitude();
				latitude = location.getLatitude();
			}
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode) {
			case PERMISSION_REQUEST_FINE_LOCATION:
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					// permission granted
					Log.d("location", "Call back: Permission granted");
					location = GpsUtil.getInstance(MainActivity.this).getLastLocation();
					if (location != null) {
						longitude = location.getLongitude();
						latitude = location.getLatitude();
					}
					editor.putBoolean(disabledLocationInApp, false);
					editor.apply();
				}
				else {
					// permission denied
					Log.d("location", "Call back: Permission denied");
					editor.putBoolean(disabledLocationInApp, true);
					editor.apply();
				}
				break;
			default:
				break;
		}
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
