package com.example.navucsd;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainPageFragment extends Fragment {

	private RecyclerView recyclerViewSig;
	private LinearLayoutManager layoutManager;
	private HorizontalRecyclerAdapter sigAdapter;
	private AutoSlideViewPager autoSlideViewPager;
	private AutoSlideViewPagerAdapter autoSlideViewPagerAdapter;

	public MainPageFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_main_page, container, false);
	}

	/**
	 * Sets up the recycler view.
	 *
	 * @param view the recycler view to setup
	 * @param names the names of the cards
	 * @param images the images id of the cards
	 */
	private void setupRecyclerView(RecyclerView view, String[] names, int[] images) {
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

		view.setAdapter(new HorizontalRecyclerAdapter(names, images, 16, 20));
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		// set up auto slide viewpager
		autoSlideViewPager = view.findViewById(R.id.auto_slider);
		autoSlideViewPagerAdapter = new AutoSlideViewPagerAdapter(getContext());
		autoSlideViewPager.setAdapter(autoSlideViewPagerAdapter);
		autoSlideViewPager.setAutoPlay(true);

		setupRecyclerView(
			view.findViewById(R.id.main_page_must_see_landmarks_recycler_view),
			new String[]{"Fallen Star", "Sun God", "Dr. Seuss Statue"},
			new int[]{R.drawable.fallen_star, R.drawable.sun_god, R.drawable.dr_seuss_statue}
		);

		setupRecyclerView(
			view.findViewById(R.id.main_page_academic_spots_recycler_view),
			new String[]{"Fallen Star", "Sun God", "Dr. Seuss Statue"},
			new int[]{R.drawable.fallen_star, R.drawable.sun_god, R.drawable.dr_seuss_statue}
		);

		setupRecyclerView(
			view.findViewById(R.id.main_page_campus_life_recycler_view),
			new String[]{"Fallen Star", "Sun God", "Dr. Seuss Statue"},
			new int[]{R.drawable.fallen_star, R.drawable.sun_god, R.drawable.dr_seuss_statue}
		);
	}
}
