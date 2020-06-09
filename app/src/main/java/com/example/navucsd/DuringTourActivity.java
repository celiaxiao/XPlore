package com.example.navucsd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;


public class DuringTourActivity extends AppCompatActivity {
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
        imageViewDuringTour.setImageDrawable(getDrawable(R.drawable.geisel_landmark)); // Set up the thumbnail image for this stop
        tourNameTextView.setText("UC San Diego Landmark Tour"); // Set up tour name

        stopNameTextView.setText("1st Stop: Geisel Library"); // TODO: dynamically set the ranking of the stop; Set up the name for this stop

        stopDescriptionTextView.setText("Geisel Library is the main library building of the University of California San Diego Library. " +
                "The building's distinctive Brutalist architecture has resulted in its being featured in the " +
                "UC San Diego logo and becoming the most recognizable building on campus"); // Set up the descriptions for this stop


        // TODO: dynamically set up the amenities icon: b1 - b5 are active icons, g1 - g5 are inactive icons
        restroomIconDuringTourImageView.setImageDrawable(getDrawable(R.drawable.b1));
        cafeIconDuringTourImageView.setImageDrawable(getDrawable(R.drawable.b2));





        tourOverViewTextView.setOnClickListener(new View.OnClickListener() { // Go to TourOverViewPage
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TourOverviewPage.class);
                startActivity(intent);
                finish();
            }
        });


        // TODO: previousStopTextView onClick go to previous stop
        previousStopTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // TODO: detailsTextViewDuringTour onClick go to details page of this stop
        detailsTextViewDuringTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // TODO: directionsButton onClick go to map app
        



    }
}
