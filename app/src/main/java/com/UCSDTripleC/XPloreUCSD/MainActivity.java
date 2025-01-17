package com.UCSDTripleC.XPloreUCSD;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
	private final int TOUR_PAGE_TAB_INDEX = 2;
	private ViewPager mainPager;
	private Location location;
	private static final int PERMISSION_REQUEST_FINE_LOCATION = 1000;
	private SharedPreferences sharedPref;
	private SharedPreferences.Editor editor;
	private String disabledLocationInApp = "disabledLocationInApp";
	private boolean isDisabled;
	private double longitude;
	private double latitude;
	private final String title_1 = "Location Not Enabled";
	private final String msg_1 = "Please turn on location services.";
	private final String title_2 = "No Location Permission";
	private final String msg_2 = "Please turn on location services.";
	private final String posBtnMsg = "SETTINGS";
	private final String negBtnMsg = "CANCEL";
	private final String tourKey = "toTour";
	private static final int REQUEST_LOCATION_SETTING = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		CurvedBottomNavigationViewTabLayout tabLayout = findViewById(R.id.customBottomBar);
		mainPager = findViewById(R.id.main_page_pager);
		mainPager.setOffscreenPageLimit(2);
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
				image_view.setImageResource(R.drawable.main_page_tab_icon_active);
			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab) {
				if (tab.getPosition() != MAIN_PAGE_TAB_INDEX) return;
				ImageView image_view = findViewById(R.id.main_page_tab_icon);
				image_view.setImageResource(R.drawable.main_page_tab_icon_inactive);
			}

			@Override
			public void onTabReselected(TabLayout.Tab tab) {

			}
		});
		if (getIntent().hasExtra(tourKey)) {
			tabLayout.selectTab(tabLayout.getTabAt(TOUR_PAGE_TAB_INDEX));
		}
		else {
			tabLayout.selectTab(mainPageTab);
		}

		CircularClickableImageView image_view = findViewById(R.id.main_page_tab_icon);
		image_view.setView(mainPager);
		image_view.setContentRatio(280f / 297f);
		image_view.setOnClickListener(view -> {
			tabLayout.selectTab(tabLayout.getTabAt(MAIN_PAGE_TAB_INDEX));
		});
	}

	@Override
	public void onResume() {
		super.onResume();

		// Check if user has explicitly disabled location permission in app
		sharedPref = getPreferences(Context.MODE_PRIVATE);
		editor = sharedPref.edit();
		isDisabled = sharedPref.getBoolean(disabledLocationInApp, false);

		// Check for status of GPS and location permission
		if (isLocPermissionEnable(this) && isLocServiceEnable(this)){
			Log.d("location", "Both location permission and GPS are available");
			location = GpsUtil.getInstance(MainActivity.this).getLastLocation();
			if (location != null) {
				longitude = location.getLongitude();
				latitude = location.getLatitude();
			}
		}
		// Check GPS
		else if (!isLocServiceEnable(this)) {
			Log.d("location", "GPS is not available");
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(title_1);
			builder.setMessage(msg_1);
			builder.setPositiveButton(posBtnMsg, (dialog, which) -> {
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivityForResult(intent, REQUEST_LOCATION_SETTING);
			});
			builder.setNegativeButton(negBtnMsg, (dialog, which) -> {
				Log.d("location", "Call back of msg alert: GPS denied");
			});
			builder.show();
		}
		// Check location permission
		else if (!isLocPermissionEnable(this)) {
			Log.d("location", "Permission is not available");
			if (!isDisabled) {
				// Has not explicitly disabled location in app
				ActivityCompat.requestPermissions(this, new String[]{
						Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_FINE_LOCATION);
			}
			else {
				// Has explicitly disabled location in app
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle(title_2);
				builder.setMessage(msg_2);
				builder.setPositiveButton(posBtnMsg, (dialog, which) -> {
					Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
					Uri uri = Uri.fromParts("package", getPackageName(), null);
					intent.setData(uri);
					startActivityForResult(intent, REQUEST_LOCATION_SETTING);
				});
				builder.setNegativeButton(negBtnMsg, (dialog, which) -> {
					Log.d("location", "Call back of msg alert: Permission denied");
				});
				builder.show();
			}
		}
	}

	private boolean isLocServiceEnable(Context context) {
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		if (gps || network) {
			return true;
		}
		return false;
	}

	private boolean isLocPermissionEnable(Context context) {
		return Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission( context,
				Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED;
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
