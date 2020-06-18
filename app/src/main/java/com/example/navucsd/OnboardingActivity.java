package com.example.navucsd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * This is the OnboardingActivity shows up only when the app is opened by the
 * user for the first time, and it provides some introduction to the app
 */
public class OnboardingActivity extends AppCompatActivity {
    private ViewPager mViewPagerOnboarding; // Store Gifs
    private LinearLayout mDotLayout; // linearLayout to display dot indicators
    private SliderAdapterOnboarding sliderAdapter; // Adapter for viewPager
    private ImageView[] mDots; // Dot indicators
    private int dotsCount;
    private Button startButton; // Button that leads to main activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        mViewPagerOnboarding = (ViewPager) findViewById(R.id.viewPagerOnboarding);
        mDotLayout = (LinearLayout) findViewById(R.id.linearLayoutOnboarding);

        sliderAdapter = new SliderAdapterOnboarding(this);

        mViewPagerOnboarding.setAdapter(sliderAdapter);

        dotsCount = sliderAdapter.getCount();
        mDots = new ImageView[dotsCount];

        // Set all dots as inactive
        for (int i = 0; i < dotsCount; i++) {
            mDots[i] = new ImageView(this);
            mDots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.dots_indicator_inactive));

            // Set params for the linearLayout
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                            (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(19, 0, 19, 0);
            mDotLayout.addView(mDots[i], params);
        }

        // Set the first dot as the active dot
        mDots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.dots_indicator_active));


        startButton = findViewById(R.id.startButtonOnboarding);
        // Set startButton as invisible since on the first page this button should be invisible
        startButton.setVisibility(View.INVISIBLE);
        mViewPagerOnboarding.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

            @Override
            public void onPageSelected(int position) {
                // Set all dots as inactive
                for (int i = 0; i < dotsCount; i++) {
                    mDots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.dots_indicator_inactive));
                }

                // Set the corresponding dot as active
                mDots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.dots_indicator_active));


                // startButton visible only on the last page
                if (position != sliderAdapter.getCount() - 1) {
                    startButton.setVisibility(View.INVISIBLE);
                } else {
                    startButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) { }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });


    }

}
