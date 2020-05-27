package com.example.navucsd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

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
        // hardcoded using predefined constants
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
        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(mRecyclerView); // Link the itemTouchHelper to the recyclerView to implement swipe function
        mRecyclerView.setAdapter(mAdapter);
        // -----------------------------

    }



    // ItemTouchHelper to implement swipe functions
    ItemTouchHelper.SimpleCallback itemTouchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();

            // TODO change this String Deleted Item to Places Object
            String deletedItem = items.get(position); // The deleted item

            items.remove(position);
            mAdapter.notifyItemRemoved(position);


            Snackbar.make(mRecyclerView, deletedItem, Snackbar.LENGTH_LONG)
                    .setAction(Html.fromHtml("<font color=\"#FE372F\">Undo Delete</font>"), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    items.add(position, deletedItem);
                    mAdapter.notifyItemInserted(position);
                    mRecyclerView.scrollToPosition(position); // Scroll back to the restored item
                }
            }).show();
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {


            // Use a customized dependency from GitHub to implement swipe to delete function
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(TourOverviewPage.this, R.color.delete_red))
                    .addSwipeLeftActionIcon(R.drawable.ic_delete_white_24dp)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };



}
