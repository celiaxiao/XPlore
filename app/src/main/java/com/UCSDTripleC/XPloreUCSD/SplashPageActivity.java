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
    public static final String APP_FIRST_RUN = "App first run";

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
                SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
                if (sharedPreferences.getBoolean(APP_FIRST_RUN, true)) {
                    Intent intent = new Intent(getApplicationContext(), OnboardingActivity.class);
                    startActivity(intent);
                    finish(); // Close onboarding
                    sharedPreferences.edit().putBoolean(APP_FIRST_RUN, false).commit(); // Tell the app it has already finished its first run
                } else {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("Splash", true);
                    startActivity(intent);
                    finish();
                }

            }
        }, SPLASH_TIME_OUT);

    }
}
