package com.example.navucsd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OnboardingActivity extends AppCompatActivity {
    private ViewPager mViewPagerOnboarding; // Store Gifs
    private LinearLayout mDotLayout; // linearLayout to display dot indicators
    private SliderAdapterOnboarding sliderAdapter; // Adapter for viewPager
    private ImageView[] mDots; // Dot indicators
    private int dotsCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        SharedPreferences sharedPreferences = getPreferences(this.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("Onboarding finished", true);
        editor.commit();
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
            }

            @Override
            public void onPageScrollStateChanged(int state) { }
        });


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
