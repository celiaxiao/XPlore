package com.example.navucsd;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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

	/**
	 * Called on resume of this activity and resets the {@code clicked} attribute.
	 */
	@Override
	protected void onResume() {
		super.onResume();
		clicked = false;
	}
}
