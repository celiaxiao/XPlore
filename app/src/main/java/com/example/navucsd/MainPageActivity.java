package com.example.navucsd;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;

import android.util.TypedValue;

import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * The main activity of NavUCSD.
 */
public class MainPageActivity extends AppCompatActivity {

	private RecyclerView recyclerViewNear, recyclerViewSig;
	private LinearLayoutManager layoutManager;
	private MainPagePlacesAdapter mAdapter;
	private MainSignatureAdapter sigAdapter;
	private SwipeRefreshLayout swipeContainer;

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

		//TODO: TEST CODE PLEASE REMOVE WHEN DONE
		ArrayList<String> test = new ArrayList<>();
		ArrayList<String> otherPhotos = new ArrayList<>();
		otherPhotos.add("somelink/somestuff");
		otherPhotos.add("somelink/somestuff2");
		ArrayList<String> descriptive = new ArrayList<>();
		descriptive.add("Must see");
		ArrayList<String> relatedPlaces = new ArrayList<>();
		relatedPlaces.add("Muir");
		relatedPlaces.add("Sun God");
		ArrayList<String> relatedTours = new ArrayList<>();
		relatedTours.add("the best tour");
		relatedTours.add("the other best tour");
		HashMap<String, Boolean> ameneitiesTest = new HashMap<>();
		ameneitiesTest.put("restaurant", true);
		ameneitiesTest.put("cafe", true);
		ameneitiesTest.put("restroom", true);
		ameneitiesTest.put("parking", true);
		ameneitiesTest.put("busstop", true);
		ArrayList<String> links = new ArrayList<>();
		links.add("ucsd.edu");
		links.add("hehehe.com");
		test.add("what");
		ArrayList<String> videos = new ArrayList<>();
		videos.add("https://www.youtube.com/watch?v=dQw4w9WgXcQ");
		Location loc = new Location("name", new Pair("x-coord", "y-coord"), "short description",
				"somedirectory/somefile",otherPhotos,ameneitiesTest,
				descriptive,relatedPlaces,relatedTours, "somedirectory/somefile",links, videos);
		Gson gson = new Gson();
		String json = gson.toJson(loc);
		Log.d("WHAT", json);
//		String testJson = loadJSONFromAsset();
//		Location tstLoc = gson.fromJson(testJson, Location.class);
		//TODO: END OF TEST CODE

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

		swipeContainer = findViewById(R.id.swipeContainer);
		// TODO change to lambda
		swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				Toast.makeText(MainPageActivity.this, "Refreshing", Toast.LENGTH_SHORT).show();
				swipeContainer.setRefreshing(false);
			}
		});
		swipeContainer.setColorSchemeResources(R.color.colorSecondary);

		recyclerViewNear = findViewById(R.id.recycler_main);
		// TODO fix this
        recyclerViewNear.setHasFixedSize(true);
		layoutManager = new LinearLayoutManager(this);
		DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewNear.getContext(),
				LinearLayoutManager.VERTICAL) {
			@Override
			public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
				int position = parent.getChildAdapterPosition(view);
				// hide the divider for the last child
				if (position == state.getItemCount() - 1) {
					outRect.setEmpty();
				} else {
					super.getItemOffsets(outRect, view, parent, state);
				}
			}
		};
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			dividerItemDecoration.setDrawable(getDrawable(R.drawable.horizontal_divider_16dp));
		}
		else {
			dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.horizontal_divider_16dp));
		}
		recyclerViewNear.addItemDecoration(dividerItemDecoration);

		recyclerViewNear.setLayoutManager(layoutManager);
		mAdapter = new MainPagePlacesAdapter();
		recyclerViewNear.setAdapter(mAdapter);

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


	/*
		Function that reads in a file and then spits out their json string
	 */
	public String loadJSONFromAsset() {
		String json = null;
		Log.d("WHAT", "I'm in this function call");
		try {
			Log.d("WHAT", "in try block");
			InputStream is = this.getAssets().open("test.json");
			Log.d("WHAT","after opening file");
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			Log.d("WHAT", "finished reading buffer");
			json = new String(buffer, "UTF-8");
			Log.d("WHAT", json);
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
		return json;
	}

}
