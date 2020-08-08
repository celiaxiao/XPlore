package com.UCSDTripleC.XPloreUCSD;

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
            R.drawable.splash_1_2,
            R.drawable.splash_2_2,
            R.drawable.splash_3_2,
            R.drawable.splash_page_4_1};

    String[] sliderTitles = {
            "Nearby Adventures",
            "Customized Tours",
            "Quality Contents",
            "Welcome to the first-ever touring app for UC San Diego"};

    String[] sliderDescriptions = {
            "Start exploring nearby landmarks, tours, and amenities from anywhere on campus.",
            "Choose from our preset tours or create your own tour that suits your interests.",
            "Learn more about the campus with informative, multimedia contents carefully curated by student insiders.",
            ""
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
        //not gif image
        // Glide.with(this.context).load(sliderGifs[position]).into(gifImageView);
        gifImageView.setImageResource(sliderGifs[position]);
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
