package com.example.navucsd;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class FeatureComingSoonActivity extends AppCompatActivity {

    private ImageView gifImageViewComingSoonPage;
    private TextView AppNameTextViewComingSoonPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feature_coming_soon);

        gifImageViewComingSoonPage = (ImageView) findViewById(R.id.gifImageViewComingSoonPage);
        AppNameTextViewComingSoonPage = (TextView) findViewById(R.id.AppNameTextViewComingSoonPage);

        String appNameMultiColor = "<b><font color=#F0C308>X</font><font color=#162B49>plore UCSD</font></b>";
        Glide.with(getApplicationContext()).load(R.drawable.coming_soon).into(gifImageViewComingSoonPage);
        AppNameTextViewComingSoonPage.setText(Html.fromHtml(appNameMultiColor));
    }
}