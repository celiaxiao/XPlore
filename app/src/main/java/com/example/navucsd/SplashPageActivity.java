package com.example.navucsd;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class SplashPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_page);

        ImageView splashPageGifImageView = (ImageView) findViewById(R.id.SplashPageGifImageView);
        Glide.with(this).load(getDrawable(R.drawable.splash_page_loading_dots)).into(splashPageGifImageView);

    }
}
