package com.example.navucsd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * This is the DuringTourActivity which provides some descriptions about the
 * stop being viewed, a directionsButton leading to Google Maps and show the
 * walking directions to the stop being viewed and a "details" textView which
 * when being clicked on leads to the stop's location detail page.
 *
 *
 * This activity is temporarily hard-coded with Geisel Library as the stop being viewed
 */
public class DuringTourActivity extends AppCompatActivity {
    private String stopName; // Name of this stop

    private ImageView imageViewDuringTour;
    private TextView tourNameTextView;
    private TextView tourOverViewTextView;
    private TextView stopNameTextView;
    private TextView previousStopTextView;
    private TextView stopDescriptionTextView;
    private ImageView restroomIconDuringTourImageView;
    private ImageView cafeIconDuringTourImageView;
    private ImageView restaurantIconDuringTourImageView;
    private ImageView busstopIconDuringTourImageView;
    private ImageView parkinglotIconDuringTourImageView;
    private Button directionsButton;
    private TextView detailsTextViewDuringTour;
    private Button nextStopButton;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_during_tour);


        imageViewDuringTour = (ImageView) findViewById(R.id.imageViewDuringTour);
        tourNameTextView = (TextView) findViewById(R.id.tourNameTextView);
        tourOverViewTextView = (TextView) findViewById(R.id.tourOverviewTextView);
        stopNameTextView = (TextView) findViewById(R.id.stopNameTextView);
        previousStopTextView = (TextView) findViewById(R.id.previousStopTextView);
        stopDescriptionTextView = (TextView) findViewById(R.id.stopDescriptionTextView);
        restroomIconDuringTourImageView = (ImageView) findViewById(R.id.restroomIconDuringTourImageView);
        cafeIconDuringTourImageView = (ImageView) findViewById(R.id.cafeIconDuringTourImageView);
        restaurantIconDuringTourImageView = (ImageView) findViewById(R.id.restaurantIconDuringTourImageView);
        busstopIconDuringTourImageView = (ImageView) findViewById(R.id.busstopIconDuringTourImageView);
        parkinglotIconDuringTourImageView = (ImageView) findViewById(R.id.parkinglotIconDuringTourImageView);
        directionsButton = (Button) findViewById(R.id.directionsButton);
        detailsTextViewDuringTour = (TextView) findViewById(R.id.detailsTextViewDuringTour);
        nextStopButton = (Button) findViewById(R.id.nextStopButton);



        // TODO: dynamically set up basic components in this activity
        stopName = "Geisel Library";
        imageViewDuringTour.setImageDrawable(getDrawable(R.drawable.geisel_landmark)); // Set up the thumbnail image for this stop
        tourNameTextView.setText("UC San Diego Landmark Tour"); // Set up tour name

        stopNameTextView.setText("1st" + " Stop: " + stopName); // TODO: dynamically set the ranking of the stop; Set up the name for this stop

        stopDescriptionTextView.setText("Geisel Library is the main library building of the University of California San Diego Library. " +
                "The building's distinctive Brutalist architecture has resulted in its being featured in the " +
                "UC San Diego logo and becoming the most recognizable building on campus." +
                "------------------------------------------------------------------------" +
                "------------------------------------------------------------------------"); // TODO: Dynamically Set up the descriptions for this stop
        stopDescriptionTextView.setMovementMethod(new ScrollingMovementMethod()); // Making this textView scrollable


        // TODO: dynamically set up the amenities icon: icon_<amenityName>_white is the activated state of the icons
        restroomIconDuringTourImageView.setImageDrawable(getDrawable(R.drawable.icon_restroom_white));
        cafeIconDuringTourImageView.setImageDrawable(getDrawable(R.drawable.icon_cafe_white));




        // tourOverviewTextView onClick go back to tourOverviewPage
        tourOverViewTextView.setOnClickListener(new View.OnClickListener() { // Go to TourOverViewPage
            @Override
            public void onClick(View view) {
                Intent tourOverviewIntent = new Intent(getApplicationContext(), TourOverviewPage.class);
                startActivity(tourOverviewIntent);
            }
        });


        // TODO: previousStopTextView onClick go to previous stop
        previousStopTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // detailsTextViewDuringTour onClick go to details page of this stop
        detailsTextViewDuringTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailsIntent = new Intent(getApplicationContext(), LandmarkDetailsActivity.class);
                detailsIntent.putExtra("placeName", stopName);
                startActivity(detailsIntent);
            }
        });


        // Click on directions button would lead to Google Maps app
        directionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a Uri from an intent string. Use the result to create an Intent.
                // TODO: dynamically pass in the location of this stop as an URL
                // TODO: details about how to open Google Maps: https://developers.google.com/maps/documentation/urls/android-intents#search_for_a_location
                Uri gmmIntentUri = Uri.parse("google.navigation:q=Geisel+Library,+San+Diego+US&mode=w");

                // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                // Make the Intent explicit by setting the Google Maps package
                mapIntent.setPackage("com.google.android.apps.maps");

                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    // Attempt to start an activity that can handle the Intent
                    startActivity(mapIntent);
                }
            }
        });



    }
}
