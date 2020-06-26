package com.example.navucsd;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navucsd.utils.ClickTracker;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainPageFragment extends Fragment {

	private SearchBarDB database;
	private FusedLocationProviderClient fusedLocationClient;
	private RecyclerView recyclerViewSig;
	private LinearLayoutManager layoutManager;
	private HorizontalRecyclerAdapter sigAdapter;
	private AutoSlideViewPager autoSlideViewPager;
	private AutoSlideViewPagerAdapter autoSlideViewPagerAdapter;
	/**
	 * The click tracker used in this fragment.
	 */
	private ClickTracker clickTracker;

	/**
	 * Constructor that initializes the click tracker.
	 */
	public MainPageFragment() {
		clickTracker = new ClickTracker();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_main_page, container, false);
	}

	/**
	 * Called on resume of this fragment and resets the {@code clickTracker}.
	 */
	@Override
	public void onResume() {
		super.onResume();
		clickTracker.reset();
	}

	/**
	 * Sets up the recycler view.
	 *
	 * @param view the recycler view to setup
	 * @param tracker the click tracker used
	 * @param names the names of the cards
	 * @param images the images id of the cards
	 */
	private void setupRecyclerView(
			RecyclerView view,
			ClickTracker tracker,
			String[] names,
			int[] images
	) {
		layoutManager = new LinearLayoutManager(
				getContext(),
				LinearLayoutManager.HORIZONTAL,
				false
		);
		DividerItemDecoration dividerItemDecorationSig = new DividerItemDecoration(view.getContext(),
				LinearLayoutManager.HORIZONTAL) {
			@Override
			public void getItemOffsets(
					Rect outRect,
					View view,
					RecyclerView parent,
					RecyclerView.State state
			) {
				int position = parent.getChildAdapterPosition(view);
				int px_16 = Math.round(TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_DIP,
						16,
						getResources().getDisplayMetrics()
				));
				// hide the divider for the last child
				if (position == state.getItemCount() - 1) {
					outRect.set(0, 0, px_16, 0);
				} else {
					super.getItemOffsets(outRect, view, parent, state);
				}
			}
		};
		dividerItemDecorationSig.setDrawable(getResources().getDrawable(
				R.drawable.vertical_divider_20dp)
		);
		view.addItemDecoration(dividerItemDecorationSig);

		// use this setting to improve performance if you know that changes
		// in content do not change the layout size of the RecyclerView
		view.setHasFixedSize(true);

		// use a linear layout manager
		view.setLayoutManager(layoutManager);

		view.setAdapter(new HorizontalRecyclerAdapter(tracker, names, images, 16, 20));
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// set up auto slide viewpager
		autoSlideViewPager = view.findViewById(R.id.auto_slider);
		autoSlideViewPagerAdapter = new AutoSlideViewPagerAdapter(getContext());
		autoSlideViewPager.setAdapter(autoSlideViewPagerAdapter);
		autoSlideViewPager.setAutoPlay(true);

		database = new SearchBarDB(getContext(), "one by one");
		fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
		if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		else {
			fusedLocationClient.getLastLocation()
					.addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
						@Override
						public void onSuccess(Location location) {
							// Got last known location. In some rare situations this can be null.
							if (location != null) {
								ArrayList<Pair<com.example.navucsd.database.Location, Double>> arrayList = database.nearestLocations(new Pair<>(location.getLatitude(), location.getLongitude()),3);
								Log.d("Near", arrayList.size() + "");
								autoSlideViewPagerAdapter.setContent(arrayList);
							}
						}
					});
		}

		setupRecyclerView(
			view.findViewById(R.id.main_page_must_see_landmarks_recycler_view),
			clickTracker,
			new String[] {"Fallen Star", "Sun God", "Dr. Seuss Statue"},
			new int[] {R.drawable.fallen_star, R.drawable.sun_god, R.drawable.dr_seuss_statue}
		);

		setupRecyclerView(
			view.findViewById(R.id.main_page_academic_spots_recycler_view),
			clickTracker,
			new String[] {"Biomedical Library", "Jacobs Building", "Peterson Hall"},
			new int[] {
				R.drawable.biomedical_library,
				R.drawable.jacobs_building,
				R.drawable.peterson_hall,
			}
		);

		setupRecyclerView(
			view.findViewById(R.id.main_page_campus_life_recycler_view),
			clickTracker,
			new String[] {"Price Center", "Main Gym", "64 Degrees"},
			new int[] {R.drawable.price_center, R.drawable.main_gym, R.drawable._64_degrees}
		);

		View.OnClickListener comingSoon = clickTracker.getOnClickListener(
			FeatureComingSoonActivity.class
		);

		view
			.findViewById(R.id.main_page_ucsd_landmark_tour_card_view)
			.setOnClickListener(comingSoon);
		view
			.findViewById(R.id.main_page_academic_highlights_tour_card_view)
			.setOnClickListener(comingSoon);
		view
			.findViewById(R.id.main_page_one_day_as_student_tour_card_view)
			.setOnClickListener(comingSoon);
	}
}
