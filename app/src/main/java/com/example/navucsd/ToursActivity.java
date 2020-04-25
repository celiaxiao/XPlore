package com.example.navucsd;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.ScrollView;

public class ToursActivity extends AppCompatActivity {

    ListViewForScrollView v;
    ScrollView sv;

    private String[] nameSet = {"UCSD's Landmark Tour", "Most Popular Restaurants at UCSD", "Intro to Brutalism - \nArchitecture of UCSD", "60 Years by the Beach - \nHistorical Sites at UCSD", "Let's Get Punky - \nHidden Artworks on Campus", "Revelle College"};
    private int[] timeSet = {90, 30, 40, 50, 60, 70};
    private int[] stopsSet = {5, 3, 2, 3, 4, 5};
    private int[] pictures = {R.drawable.tours_landmark, R.drawable.tours_restaurants, R.drawable.tours_brutalism, R.drawable.tours_historical_sites, R.drawable.tours_hidden_artworks, R.drawable.tours_revelle};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tours);

        v = (ListViewForScrollView) findViewById(R.id.tours_lv);
        ToursAdapter a = new ToursAdapter(this, nameSet, timeSet, stopsSet, pictures);
        v.setAdapter(a);

        // Adjustment for ListViewForScrollView
        sv = (ScrollView) findViewById(R.id.tours_sv);
        sv.smoothScrollTo(0, 0);


    }
}
