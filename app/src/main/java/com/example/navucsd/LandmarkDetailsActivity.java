package com.example.navucsd;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * This activity temporary hardcodes the landmark details page.
 */
public class LandmarkDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landmark_details);
    }

    public void go_to_nav(View view) {
        Toast.makeText(this, "Go to navigation", Toast.LENGTH_SHORT).show();
    }
}
