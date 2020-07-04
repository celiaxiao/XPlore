package com.UCSDTripleC.XPloreUCSD;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.UCSDTripleC.XPloreUCSD.database.Landmark;
import com.UCSDTripleC.XPloreUCSD.database.LandmarkDatabase;
import com.UCSDTripleC.XPloreUCSD.utils.ClickTracker;
import com.UCSDTripleC.XPloreUCSD.utils.Utils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainPageFragment extends Fragment {

	private LandmarkDatabase database;
	private FusedLocationProviderClient fusedLocationClient;
	private LinearLayoutManager layoutManager;
	private AutoSlideViewPager autoSlideViewPager;
	private AutoSlideViewPagerAdapter autoSlideViewPagerAdapter;
	private TextView placesNearText;
	private SwipeRefreshLayout swipeContainer;
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
		displayPlacesNearYou();
	}

	/**
	 * Sets up the recycler view.
	 *
	 * @param view the recycler view to setup
	 * @param tracker the click tracker used
	 * @param names the names of the cards
	 * @param urls the URLs of the cards' images
	 */
	private void setupRecyclerView(
			RecyclerView view,
			ClickTracker tracker,
			String[] names,
			String[] urls
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
		HorizontalRecyclerAdapter adapter = new HorizontalRecyclerAdapter(
				tracker,
				16,
				20,
				getContext()
		);
		adapter.setContent(names, urls);
		view.setAdapter(adapter);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		placesNearText = view.findViewById(R.id.text_view_main);

		// set up auto slide viewpager
		autoSlideViewPager = view.findViewById(R.id.auto_slider);
		autoSlideViewPagerAdapter = new AutoSlideViewPagerAdapter(getContext());
		autoSlideViewPager.setAdapter(autoSlideViewPagerAdapter);
		autoSlideViewPager.setAutoPlay(true);

		swipeContainer = view.findViewById(R.id.swipeContainer);
		swipeContainer.setOnRefreshListener(() -> {
			displayPlacesNearYou();
			swipeContainer.setRefreshing(false);
		});
		swipeContainer.setColorSchemeResources(R.color.colorSecondary);

		database = new LandmarkDatabase(getContext(), "one by one");
		fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

		String[] must_see_landmarks = new String[]{"Fallen Star", "Sun God", "Dr. Seuss Statue"};
		String[] academic_spots = new String[]{
				"Biomedical Library",
				"Jacobs Building",
				"Peterson Hall"
		};
		String[] one_day_as_student = new String[]{"Price Center", "Main Gym", "64 Degrees"};

		setupRecyclerView(
				view.findViewById(R.id.main_page_must_see_landmarks_recycler_view),
				clickTracker,
				must_see_landmarks,
				Utils.nameToUrl(database, must_see_landmarks)
		);


		setupRecyclerView(
				view.findViewById(R.id.main_page_academic_spots_recycler_view),
				clickTracker,
				academic_spots,
				Utils.nameToUrl(database, academic_spots)
		);
		setupRecyclerView(
				view.findViewById(R.id.main_page_campus_life_recycler_view),
				clickTracker,
				one_day_as_student,
				Utils.nameToUrl(database, one_day_as_student)
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

	private void displayPlacesNearYou() {
		if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			placesNearText.setVisibility(View.GONE);
			autoSlideViewPager.setVisibility(View.GONE);
		} else {
			fusedLocationClient
					.getLastLocation()
					.addOnSuccessListener(getActivity(), location -> {
						// Got last known location. In some rare situations this can be null.
						if (location != null) {
							placesNearText.setVisibility(View.VISIBLE);
							autoSlideViewPager.setVisibility(View.VISIBLE);
							ArrayList<Pair<Landmark, Double>> arrayList = database.nearestLocations(
									new Pair<>(location.getLatitude(), location.getLongitude()), 3
							);
							Log.d("Near", arrayList.size() + "");
							autoSlideViewPagerAdapter.setContent(arrayList);
						}
						else {
							placesNearText.setVisibility(View.GONE);
							autoSlideViewPager.setVisibility(View.GONE);
						}
					});
		}
	}
}
