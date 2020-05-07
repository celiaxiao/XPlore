package com.example.navucsd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TourOverviewPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_overview_page);

        FloatingActionButton fabButton = (FloatingActionButton) findViewById(R.id.floating_action_add_button_tour_overview_page);
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TourOverviewPage.this, "Testing", Toast.LENGTH_LONG).show();
            }
        });


    }
}
