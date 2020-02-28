package com.example.navucsd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainPageActivity extends AppCompatActivity {

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
    }
}