package com.UCSDTripleC.XPloreUCSD;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.UCSDTripleC.XPloreUCSD.database.Landmark;
import com.UCSDTripleC.XPloreUCSD.database.LandmarkDatabase;
import com.UCSDTripleC.XPloreUCSD.database.Tour;
import com.UCSDTripleC.XPloreUCSD.database.TourDatabase;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

/**
 * This is the TourOverviewPage activity which provides an overview for a tour;
 * the overview contains a startButton leading to starting the tour and
 * cardViews for different stops in the tour.
 */
public class TourOverviewPage extends AppCompatActivity implements RecyclerViewAdapterTourOverviewPage.RecyclerViewOnItemClickListener {
    private LandmarkDatabase database;
    private ArrayList<String> items; // ArrayList that provide items for the RecyclerView
    private TourDatabase tourDatabase;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button startButtonTourOverviewPage;
    private Tour tour; //tour object for this page
    private String[] places = {"Geisel Library", "Price Center", "Fallen Star", "Biomedical Library", "Galbraith Hall"}; // Array of places
    private String tourName = "UC San Diego’s Landmark Tour";
    private String tourDescription = "A tour that highlights all must-see landmarks in UC San Diego";
    private String tourTime = "90 Min";
    private ArrayList<Landmark> landmarkArrayList ;
    private TextView tourPlaceNumberTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_overview_page);
        //default places lists
        //items = new ArrayList<>(Arrays.asList(places));

        database = new LandmarkDatabase(this, "one by one");
        tourDatabase=new TourDatabase(this);
        //get the passed in tour name and picture source
        Bundle argument = getIntent().getExtras();
        int phtotsrc = 0;
        if (argument != null) {
            String tourname =argument.getString("tour name");
            tour=tourDatabase.getByName(tourname);
            phtotsrc=argument.getInt("picture src");
            landmarkArrayList=tour.getLandmarks();
            this.tourName=tourname;
            items=tour.getPlaces();
            //tourTime=ToursPageFragment.convertTime(items.size());
            tourTime=items.size()*20+" Min";
            if(items==null) Log.e("tourname",tourname);
        }

        // Basic Layout Components set up, contents of the components are temporarily
        // hardcoded using predefined constants
        ImageView tourImageView = (ImageView) findViewById(R.id.tourImageView);
        TextView tourNameTextView = (TextView) findViewById(R.id.tourNameTextView);
        TextView tourDescriptionTextView = (TextView) findViewById(R.id.tourDescriptionTextView);
        TextView tourTimeTextView = (TextView) findViewById(R.id.tourTimeTextView);
        tourPlaceNumberTextView = (TextView) findViewById(R.id.tourPlaceNumberTextView);
        if ( landmarkArrayList.size() <= 1 ){
            tourPlaceNumberTextView.setText(landmarkArrayList.size() + " stop");
        }
        else{
            tourPlaceNumberTextView.setText(landmarkArrayList.size() + " stops");
        }
        startButtonTourOverviewPage = (Button) findViewById(R.id.startButtonTourOverviewPage);


        if (argument != null){
            String review = argument.getString("Resume the tour");
            if (review != null ) {
                startButtonTourOverviewPage.setText("Resume the tour");
            }

            ArrayList<String> tourList = argument.getStringArrayList("tour array");
            if(tourList != null){
                for( int i = 0; i < tourList.size(); i++){
                    System.out.println("Resume the tour, landmark: " + tourList.get(i));
                }
                for( int i = 0; i < items.size(); i++ ){
                    System.out.println("Original tour, landmark: " + items.get(i) );
                }
                items = tour.getPlaces();;
                if ( items.size() <= 1 ){
                    tourPlaceNumberTextView.setText(String.valueOf(items.size()) + " stop");
                }
                else{
                    tourPlaceNumberTextView.setText(String.valueOf(items.size()) + " stops");
                }
            }
        }
        
        if(phtotsrc!=0){
            tourImageView.setImageDrawable(getDrawable(phtotsrc));
        }
        else tourImageView.setImageDrawable(getDrawable(R.drawable.geisel_pic));
        tourNameTextView.setText(tourName);
        //hide the tour description for now
        tourDescriptionTextView.setText(tourDescription);
        tourDescriptionTextView.setVisibility(View.GONE);
        
        tourTimeTextView.setText(tourTime);
//        tourPlaceNumberTextView.setText(tourPlaceNumber);
        startButtonTourOverviewPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), FeatureComingSoonActivity.class);
                //TODO
                Intent intent = new Intent(getApplicationContext(), DuringTourActivity.class);
                DuringTourActivity.tourArray = new DuringTourActivity.TourArray(items);
                startActivity(intent);
                finish();
            }
        });
        // --------------------------------

        //
//        ArrayList<Landmark> landmarkArrayList = new ArrayList<>();
        if(landmarkArrayList==null){
            landmarkArrayList=new ArrayList<>();
            for(int i = 0; i < items.size(); i++){
                landmarkArrayList.add(database.getByName(items.get(i)));
                if(landmarkArrayList.get(i)==null){
                    Log.e("item name",items.get(i));
                }
            }

        }


        // RecyclerView implementation
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_tour_overview_page);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mAdapter = new RecyclerViewAdapterTourOverviewPage(this, landmarkArrayList, this);
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
            landmarkArrayList.remove(position);
            mAdapter.notifyItemRemoved(position);
            if ( items.size() <= 1 ){
                tourPlaceNumberTextView.setText(String.valueOf(items.size()) + " stop");
            }
            else{
                tourPlaceNumberTextView.setText(String.valueOf(items.size()) + " stops");
            }


            Snackbar.make(mRecyclerView, deletedItem, Snackbar.LENGTH_LONG)
                    .setAction(Html.fromHtml("<font color=\"#FE372F\">Undo Delete</font>"), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    landmarkArrayList.add(position,database.getByName(deletedItem));

                    items.add(position, deletedItem);
                    startButtonTourOverviewPage.setEnabled(true);
                    startButtonTourOverviewPage.setBackgroundColor(getColor(R.color.colorSecondary));
                    mAdapter.notifyItemInserted(position);
                    mRecyclerView.scrollToPosition(position); // Scroll back to the restored item

                    tourPlaceNumberTextView.setText(String.valueOf(items.size()) + " stops");
                }
            }).show();

            if(items.isEmpty()){
                startButtonTourOverviewPage.setEnabled(false);
                startButtonTourOverviewPage.setBackgroundColor(Color.GRAY);
            }
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


    // Override the customized OnItemClick method in the customized RecyclerViewOnItemClickListener interface (which is defined in the corresponding adapter java file)
    @Override
    public void OnItemClick(int position) {
        String item = items.get(position); // A reference to the clicked item just in case we need it
//        Intent intent = new Intent(getApplicationContext(), FeatureComingSoonActivity.class);
        Intent intent = new Intent(getApplicationContext(), LandmarkDetailsActivity.class);
        intent.putExtra("placeName", item);
        startActivity(intent);
    }
}
