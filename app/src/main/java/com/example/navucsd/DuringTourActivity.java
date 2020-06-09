package com.example.navucsd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DuringTourActivity extends AppCompatActivity {
    private TextView tourOverViewTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_during_tour);

        tourOverViewTextView = (TextView) findViewById(R.id.tourOverviewTextView);
        tourOverViewTextView.setOnClickListener(new View.OnClickListener() { // Go to TourOverViewPage
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TourOverviewPage.class);
                startActivity(intent);
                finish();
            }
        });


        // TODO: Previous Stop onClick
    }
}
