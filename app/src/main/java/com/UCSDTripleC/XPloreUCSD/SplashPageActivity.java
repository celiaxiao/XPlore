package com.UCSDTripleC.XPloreUCSD;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Timer;
import java.util.TimerTask;

/**
 * This is the SplashPageActivity which would show up for a constant time
 * each time the app starts
 */
public class SplashPageActivity extends AppCompatActivity {
    public static final int SPLASH_TIME_OUT = 1500;
    public static final String ONBOARDING = "onboarding";
    public static final String ONBOARDING_VERSION_CODE = "version_code";
    public static final String ONBOARDING_COMPLETED = "completed";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_page);

        String app_name = getString(R.string.app_name);
        // assume it is at least one character long
        String app_name_with_color = "<b><font color=#F0C308>" + app_name.charAt(0)
            + "</font><font color=#162B49>" + app_name.substring(1) + "</font></b>";
        TextView app_name_text_view = findViewById(R.id.app_name_text_view);
        app_name_text_view.setText(Html.fromHtml(app_name_with_color));

        ImageView splashPageGifImageView = (ImageView) findViewById(R.id.SplashPageGifImageView);
        Glide.with(this).load(getDrawable(R.drawable.dots_loading)).into(splashPageGifImageView);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // v1.1 (code 2) legacy config
                SharedPreferences legacy_preferences = getPreferences(MODE_PRIVATE);
                legacy_preferences.edit().clear().apply();

                SharedPreferences onboarding = getSharedPreferences(ONBOARDING, MODE_PRIVATE);
                if (onboarding.getBoolean(ONBOARDING_COMPLETED, false)
                    // version 1.1 (code 2) or above
                    && onboarding.getInt(ONBOARDING_VERSION_CODE, 0) >= 2
                ) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                    startActivity(new Intent(getApplicationContext(), OnboardingActivity.class));
                }
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
