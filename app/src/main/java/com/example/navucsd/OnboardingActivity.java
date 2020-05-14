package com.example.navucsd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OnboardingActivity extends AppCompatActivity {
    private ViewPager mViewPagerOnboarding;
    private LinearLayout mDotLayout;
    private SliderAdapterOnboarding sliderAdapter;
    private TextView[] mDots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        mViewPagerOnboarding = (ViewPager) findViewById(R.id.viewPagerOnboarding);
        mDotLayout = (LinearLayout) findViewById(R.id.linearLayoutOnboarding);

        sliderAdapter = new SliderAdapterOnboarding(this);

        mViewPagerOnboarding.setAdapter(sliderAdapter);

        addDotsIndicator();


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPreferences = getPreferences(this.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("Onboarding finished", true);
        editor.commit();

    }

    public void addDotsIndicator() {
        mDots = new TextView[3];

        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(Color.parseColor("#006A96"));

            mDotLayout.addView(mDots[i]);
        }
    }
}
