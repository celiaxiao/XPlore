package com.example.navucsd;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * The main activity of NavUCSD.
 */
public class MainPageActivity extends AppCompatActivity {

	private RecyclerView recyclerViewSig;
	private LinearLayoutManager layoutManager;
	private MainSignatureAdapter sigAdapter;
	private AutoSlideViewPager autoSlideViewPager;
	private AutoSlideViewPagerAdapter autoSlideViewPagerAdapter;

	/**
	 * If the encyclopedia landmark list has been clicked, used to prevent multiple clicks.
	 */
	private boolean clicked;

	/**
	 * Sets the main page up.
	 * @param savedInstanceState ignored, passed to parent constructor
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_page);

		Button guidedTourTrack = findViewById(R.id.tourTrackBtn);
		Button GotoEncyclopedia = findViewById((R.id.encyclopediaBtn));

		// go to the student selection page
		// TODO change to lambda
		guidedTourTrack.setOnClickListener(view -> {
			if (clicked) return;
			clicked = true;
			startActivity(new Intent(this, TourActivity.class));
		});

		// go to the encyclopedia page
		GotoEncyclopedia.setOnClickListener(view -> {
			if (clicked) return;
			clicked = true;
			startActivity(new Intent(this, EncyclopediaActivity.class));
		});

		// set up auto slide viewpager
		autoSlideViewPager = findViewById(R.id.auto_slider);
		autoSlideViewPagerAdapter = new AutoSlideViewPagerAdapter(this);
		autoSlideViewPager.setAdapter(autoSlideViewPagerAdapter);
		autoSlideViewPager.setAutoPlay(true);

		recyclerViewSig = findViewById(R.id.recycler_main_sig_land);

		// use this setting to improve performance if you know that changes
		// in content do not change the layout size of the RecyclerView
		recyclerViewSig.setHasFixedSize(true);

		// use a linear layout manager
		layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
		DividerItemDecoration dividerItemDecorationSig = new DividerItemDecoration(recyclerViewSig.getContext(),
				LinearLayoutManager.HORIZONTAL) {
			@Override
			public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
				int position = parent.getChildAdapterPosition(view);
				int px_16 = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16,getResources().getDisplayMetrics()));
				// hide the divider for the last child
				if (position == state.getItemCount() - 1) {
					outRect.set(0,0, px_16,0);
				} else {
					super.getItemOffsets(outRect, view, parent, state);
				}
			}
		};
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			dividerItemDecorationSig.setDrawable(getDrawable(R.drawable.horizontal_divider_20dp));
		}
		else {
			dividerItemDecorationSig.setDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.horizontal_divider_20dp));
		}
		recyclerViewSig.addItemDecoration(dividerItemDecorationSig);

		recyclerViewSig.setLayoutManager(layoutManager);

		// specify an adapter (see also next example)
		sigAdapter = new MainSignatureAdapter();
		recyclerViewSig.setAdapter(sigAdapter);

	}

	/**
	 * Called on resume of this activity and resets the {@code clicked} attribute.
	 */
	@Override
	protected void onResume() {
		super.onResume();
		clicked = false;
	}
}
