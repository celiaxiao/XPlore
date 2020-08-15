package com.UCSDTripleC.XPloreUCSD;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.UCSDTripleC.XPloreUCSD.database.Landmark;
import com.UCSDTripleC.XPloreUCSD.database.LandmarkDatabase;
import com.UCSDTripleC.XPloreUCSD.utils.ClickTracker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static com.UCSDTripleC.XPloreUCSD.R.id.busstopIconDuringTourImageView;

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
    public static class TourArray{
        public class DoubleLink{
            public DoubleLink prev = null;
            public DoubleLink next = null;
            public Pair<String, Integer> value;
            DoubleLink(String name, Integer index){
                this.value = new Pair<>(name, index);
                this.prev = this;
                this.next = this;
            }
        }
        public DoubleLink current;
        public DoubleLink first;
        public int size = 0;
        TourArray(ArrayList<String> items){
            this.first = new DoubleLink(items.get(0), 1);
            System.out.println("The place" + 1 + " is "+items.get(0));
            this.current = first;
            this.size++;
            for(int i = 1; i < items.size(); i++){
                DoubleLink newNode = new DoubleLink(items.get(i), (i+1));
                System.out.println("The place" + (i+1) + " is "+items.get(i));
                this.current.next = newNode;
                newNode.prev = this.current;
                newNode.next = this.first;
                this.first.prev = newNode;
                this.current = this.current.next;
                this.size++;
            }
            this.current = this.first;
        }

        public DoubleLink current(){
            DoubleLink node = this.current;
            return node;
        }

        public DoubleLink next(){
            DoubleLink node = this.current.next;
            this.current = this.current.next;
            return node;
        }

        public DoubleLink prev(){
            DoubleLink node = this.current.prev;
            this.current = this.current.prev;
            return node;
        }
    }
    public static TourArray tourArray;

    private LandmarkDatabase database;


    private String stopName; // Name of this stop
    private int indexOfStop = 1;
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

    /**
     * Prevents the problem of double clicks opening activity twice.
     */
    private ClickTracker clickTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_during_tour);

        database = new LandmarkDatabase(this, "one by one");


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

        try {
            Pair<String, Integer> pair = tourArray.current().value;
            if(pair != null){
                System.out.println("This place is "+pair.first);
                Landmark landmark = database.getByName(pair.first);
                int index = pair.second;
                setView(landmark, index);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // tourOverviewTextView onClick go back to tourOverviewPage
        // Go back to the tourOverviewPage
        tourOverViewTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tourOverviewIntent = new Intent(getApplicationContext(), TourOverviewPage.class);
                tourOverviewIntent.putExtra("Resume the tour", "Resume the tour");
                ArrayList<String> tourList = new ArrayList<>();
                TourArray.DoubleLink node = tourArray.first;
                tourList.add(node.value.first);
                while( tourList.size() < tourArray.size){
                    tourList.add(node.next.value.first);
                    tourList.add(node.value.first);
                    node = node.next;
                }
                tourOverviewIntent.putStringArrayListExtra("tour array", tourList);
                startActivity(tourOverviewIntent);
            }
        });
        clickTracker = new ClickTracker();
//        Intent tourOverviewIntent = new Intent(getApplicationContext(), TourOverviewPage.class);
//        tourOverViewTextView.setOnClickListener(clickTracker.getOnClickListener(tourOverviewIntent));

        // TODO: previousStopTextView onClick go to previous stop
        // TODO: change this onclicklistener to clicktracker
        previousStopTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Pair<String, Integer> pair = tourArray.prev().value;
                    System.out.println("This place is "+pair.first);
                    if(pair != null && indexOfStop != 1){
                        Landmark landmark = database.getByName(pair.first);
                        int index = pair.second;
                        indexOfStop = index;
                        setView(landmark, index);
                        if((tourArray.size - index) == 1 ){
                            Pair<String, Integer> next = tourArray.next().value;
                            nextStopButton.setText("Last stop: " + next.first);
                            next = tourArray.prev().value;
                        }
                        else if((tourArray.size - index) == 0 ){
                            nextStopButton.setText("Finish the tour");
                        }
                        else{
                            nextStopButton.setText("Next Stop");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        if(tourArray.current().value.second == 1){
            previousStopTextView.setVisibility(View.INVISIBLE);
        }
        else{
            previousStopTextView.setVisibility(View.VISIBLE);
        }

        // TODO: nextStopButton onClick go to next stop
        // TODO: change this onclicklistener to clicktracker
        if ( tourArray.size == 2 ){
            Pair<String, Integer> next = tourArray.next().value;
            nextStopButton.setText("Last stop: " + next.first);
            next = tourArray.prev().value;
        }
        else if(tourArray.size == 1){
            nextStopButton.setText("Finish the tour");
        }
        nextStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(tourArray.current().value.second == tourArray.size){
                        Intent intent = new Intent(getApplicationContext(), TourFinish.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Pair<String, Integer> pair = tourArray.next().value;
                        System.out.println("This place is "+pair.first);
                        if(pair != null && indexOfStop != tourArray.size){
                            Landmark landmark = database.getByName(pair.first);
                            int index = pair.second;
                            indexOfStop = index;
                            setView(landmark, index);
                            if((tourArray.size - index) == 1 ){
                                Pair<String, Integer> next = tourArray.next().value;
                                nextStopButton.setText("Last stop: " + next.first);
                                next = tourArray.prev().value;
                            }
                            else if((tourArray.size - index) == 0 ){
                                nextStopButton.setText("Finish the tour");
                            }
                            else{
                                nextStopButton.setText("Next Stop");
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // detailsTextViewDuringTour onClick go to details page of this stop
//        Intent detailsIntent = new Intent(getApplicationContext(), LandmarkDetailsActivity.class);
//        System.out.println("Go to the landmark detail " + tourArray.current().value.first);
//        detailsIntent.putExtra("placeName", tourArray.current().value.first);
//        detailsTextViewDuringTour.setOnClickListener(clickTracker.getOnClickListener(detailsIntent));

        detailsTextViewDuringTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailsIntent = new Intent(getApplicationContext(), LandmarkDetailsActivity.class);
                detailsIntent.putExtra("placeName", tourArray.current().value.first);
                startActivity(detailsIntent);
            }
        });




        // Create a Uri from an intent string. Use the result to create an Intent.
        // TODO: dynamically pass in the location of this stop as an URL
        // TODO: details about how to open Google Maps: https://developers.google.com/maps/documentation/urls/android-intents#search_for_a_location
        Uri gmmIntentUri = Uri.parse("google.navigation:q=Geisel+Library,+San+Diego+US&mode=w");

        // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        // Make the Intent explicit by setting the Google Maps package
        mapIntent.setPackage("com.google.android.apps.maps");

        // Click on directions button would lead to Google Maps app
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            // Attempt to start an activity that can handle the Intent
            directionsButton.setOnClickListener(clickTracker.getOnClickListener(mapIntent));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reset the status of the clickTracker
        clickTracker.reset();
    }

    protected void setView(Landmark landmark, int index) throws IOException {

        // TODO: dynamically set up basic components in this activity
        stopName = landmark.getName();

        Drawable drawable = Drawable.createFromStream(getAssets().open(landmark.getThumbnailPhoto()), null);

        imageViewDuringTour.setImageDrawable(drawable);
        tourNameTextView.setText("UC San Diego Landmark Tour"); // Set up tour name

        if(index == 1){
            stopNameTextView.setText("1st" + " Stop: " + stopName); // TODO: dynamically set the ranking of the stop; Set up the name for this stop
        }
        else if(index == 2){
            stopNameTextView.setText("2nd" + " Stop: " + stopName); // TODO: dynamically set the ranking of the stop; Set up the name for this stop
        }
        else{
            stopNameTextView.setText(index + "th" + " Stop: " + stopName); // TODO: dynamically set the ranking of the stop; Set up the name for this stop
        }

        if(tourArray.current().value.second == 1){
            previousStopTextView.setVisibility(View.INVISIBLE);
        }
        else{
            previousStopTextView.setVisibility(View.VISIBLE);
        }

        stopDescriptionTextView.setText(landmark.getAbout()); // TODO: Dynamically Set up the descriptions for this stop
        stopDescriptionTextView.setMovementMethod(new ScrollingMovementMethod()); // Making this textView scrollable

        String[] strArray = {"parking", "cafe", "busstop", "restaurant", "restroom"};
        HashMap<String,Boolean> hashMap = landmark.getAmenities();

        if(hashMap.get(strArray[0])){
            parkinglotIconDuringTourImageView.setImageDrawable(getDrawable(R.drawable.icon_restroom_white));
            parkinglotIconDuringTourImageView.setColorFilter(Color.WHITE);
        }
        else {
            parkinglotIconDuringTourImageView.setColorFilter(Color.GRAY);
        }
        if(hashMap.get(strArray[1])){
            cafeIconDuringTourImageView.setImageDrawable(getDrawable(R.drawable.icon_cafe_white));
            cafeIconDuringTourImageView.setColorFilter(Color.WHITE);
        }
        else{
            cafeIconDuringTourImageView.setColorFilter(Color.GRAY);
        }
        if(hashMap.get(strArray[2])){
            busstopIconDuringTourImageView.setImageDrawable(getDrawable(R.drawable.icon_busstop_white));
            busstopIconDuringTourImageView.setColorFilter(Color.WHITE);
        }
        else{
            busstopIconDuringTourImageView.setColorFilter(Color.GRAY);
        }
        if(hashMap.get(strArray[3])){
            restaurantIconDuringTourImageView.setImageDrawable(getDrawable(R.drawable.icon_restaurant_white));
            restaurantIconDuringTourImageView.setColorFilter(Color.WHITE);
        }
        else{
            restaurantIconDuringTourImageView.setColorFilter(Color.GRAY);
        }
        if(hashMap.get(strArray[4])){
            restroomIconDuringTourImageView.setImageDrawable(getDrawable(R.drawable.icon_restroom_white));
            restroomIconDuringTourImageView.setColorFilter(Color.WHITE);
        }
        else{
            restroomIconDuringTourImageView.setColorFilter(Color.GRAY);
        }
    }
}