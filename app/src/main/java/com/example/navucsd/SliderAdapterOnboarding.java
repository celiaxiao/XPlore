package com.example.navucsd;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;


public class SliderAdapterOnboarding extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;

    int[] sliderGifs = {
            R.drawable.customization,
            R.drawable.landmark,
            R.drawable.multimedia};

    String[] sliderTitles = {
            "Customized Tour",
            "Detailed Landmarks",
            "Vivid Experience"};

    String[] sliderDescriptions = {
            "Tailor your tour just as you like based on your time constraint and interests",
            "Must-see places with facts and architectural knowledge",
            "Listen to tour guide audio as you walk, get multimedia experience"
    };

    public SliderAdapterOnboarding(Context context) {
        this.context = context;
    }


    @Override
    public int getCount() {
        return sliderGifs.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout) object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.onboarding_slider_first, container, false);

        ImageView gifImageView = (ImageView) view.findViewById(R.id.gifImageView);
        TextView gifNameTextView = (TextView) view.findViewById(R.id.gifNameTextView);
        TextView descriptionTextView = (TextView) view.findViewById(R.id.descriptionTextView);

        Glide.with(this.context).load(sliderGifs[position]).into(gifImageView);
        gifNameTextView.setText(sliderTitles[position]);
        descriptionTextView.setText(sliderDescriptions[position]);

        container.addView(view);

        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ConstraintLayout) object);
    }
}
