package com.example.navucsd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Timer;
import java.util.TimerTask;

public class SplashPageActivity extends AppCompatActivity {
    public static final int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_page);


        String appNameMultiColor = "<b><font color=#F0C308>X</font><font color=#162B49>plore UCSD</font></b>";
        TextView appNameTextView = (TextView) findViewById(R.id.AppNameTextView);
        appNameTextView.setText(Html.fromHtml(appNameMultiColor));


        ImageView splashPageGifImageView = (ImageView) findViewById(R.id.SplashPageGifImageView);
        Glide.with(this).load(getDrawable(R.drawable.splash_page_loading_dots)).into(splashPageGifImageView);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("Splash", true);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);

    }
}
