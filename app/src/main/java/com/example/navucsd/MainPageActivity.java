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

public class MainPageActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private MainPagePlacesAdapter mAdapter;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        Button guidedTourTrack = findViewById(R.id.tourTrackBtn);
        Button GotoEncyclopedia = findViewById((R.id.encyclopediaBtn));

        //go to the student selection page
        guidedTourTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(MainPageActivity.this,
                        TourActivity.class);
                startActivity(startIntent);
            }
        });

        //go to the encyclopedia page
        GotoEncyclopedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(MainPageActivity.this,
                        LandmarkDetailsActivity.class);
                startActivity(startIntent);
            }
        });

        swipeContainer = findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(MainPageActivity.this, "Refreshing", Toast.LENGTH_SHORT).show();
                swipeContainer.setRefreshing(false);
            }
        });
        swipeContainer.setColorSchemeResources(R.color.colorSecondary);

        recyclerView = findViewById(R.id.recycler_main);
//        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new MainPagePlacesAdapter();
        recyclerView.setAdapter(mAdapter);

    }
}
