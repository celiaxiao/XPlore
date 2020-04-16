package com.example.navucsd;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * The main activity of NavUCSD.
 */
public class MainPageActivity extends AppCompatActivity {

	private RecyclerView recyclerView;
	private LinearLayoutManager layoutManager;
	private MainPagePlacesAdapter mAdapter;
	private SwipeRefreshLayout swipeContainer;

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
		guidedTourTrack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent startIntent = new Intent(MainPageActivity.this,
						TourActivity.class);
				startActivity(startIntent);
			}
		});

		// go to the encyclopedia page
		GotoEncyclopedia.setOnClickListener(view ->
				startActivity(new Intent(this, EncyclopediaActivity.class)));

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

		recyclerView = findViewById(R.id.recycler_main);
		// TODO fix this
//        recyclerView.setHasFixedSize(true);
		layoutManager = new LinearLayoutManager(this);
		recyclerView.setLayoutManager(layoutManager);
		mAdapter = new MainPagePlacesAdapter();
		recyclerView.setAdapter(mAdapter);

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
