package com.example.navucsd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

public class TourOverviewPage extends AppCompatActivity {
    private ArrayList<String> items; // ArrayList that provide items for the RecyclerView
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String[] places = {"Geisel Library", "Price Center", "Fallen Star",
            "Bear", "Biomedical Library", "Galbraith Hall"}; // Array of places

    private String tourName = "UC San Diego’s Landmark Tour";
    private String tourDescription = "A tour that highlights all must-see landmarks in UC San Diego";
    private String tourTime = "90 Min";
    private String tourPlaceNumber = "5 Stops";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_overview_page);
        items = new ArrayList<>(Arrays.asList(places));


        // Basic Layout Components set up, contents of the components are temporarily
        // set up with instance variables
        ImageView tourImageView = (ImageView) findViewById(R.id.tourImageView);
        TextView tourNameTextView = (TextView) findViewById(R.id.tourNameTextView);
        TextView tourDescriptionTextView = (TextView) findViewById(R.id.tourDescriptionTextView);
        TextView tourTimeTextView = (TextView) findViewById(R.id.tourTimeTextView);
        TextView tourPlaceNumberTextView = (TextView) findViewById(R.id.tourPlaceNumberTextView);

        tourImageView.setImageDrawable(getDrawable(R.drawable.geisel_pic));
        tourNameTextView.setText(tourName);
        tourDescriptionTextView.setText(tourDescription);
        tourTimeTextView.setText(tourTime);
        tourPlaceNumberTextView.setText(tourPlaceNumber);
        // --------------------------------


        // RecyclerView implementation
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_tour_overview_page);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mAdapter = new RecyclerViewAdapterTourOverviewPage(this, items);
        mRecyclerView.setLayoutManager(mLayoutManager);
        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        // -----------------------------

    }


    ItemTouchHelper.SimpleCallback itemTouchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            items.remove(viewHolder.getAdapterPosition());
            mAdapter.notifyDataSetChanged();
        }
    };
}
