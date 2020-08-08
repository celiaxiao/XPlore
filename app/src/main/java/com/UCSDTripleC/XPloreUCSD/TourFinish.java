package com.UCSDTripleC.XPloreUCSD;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class TourFinish extends AppCompatActivity {

    private ImageView gifImageViewCelebration;
    private ImageButton close;
    private Button seeOther;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_finish);
        gifImageViewCelebration = findViewById(R.id.tour_finish_celebration);
        close = findViewById(R.id.tour_finish_close);
        seeOther = findViewById(R.id.tour_finish_other_tours);
        Glide.with(getApplicationContext()).load(R.drawable.tour_finish_celebrate).into(gifImageViewCelebration);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("toTour", 2);
                startActivity(intent);
            }
        });
        seeOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TourOverviewPage.class);
                startActivity(intent);
            }
        });
    }
}
