package com.example.navucsd;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * Adapter for ViewPager in HomeActivity
 * source: https://www.jianshu.com/p/58f356eaa6e9
 */
public class AutoSlideViewPagerAdapter extends PagerAdapter {

    private Context context;
    // Duplicate the first and the last images for recyclable scrolling
    private Integer[] images = {R.drawable.fallen_star,     // jump to images[3]
            R.drawable.geisel, R.drawable.price_center_east, R.drawable.fallen_star,
            R.drawable.geisel};    // jump to images[1]
    private String[] nameSet = {"Fallen Star", "Geisel Library", "Price Center", "Fallen Star", "Geisel Library"};
    private String[] distanceSet = {"350m", "<100m", "300m", "350m", "<100m"};

    public AutoSlideViewPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        // fill ImageView with image at given index position
        // create a new view
        CardView view = (CardView) LayoutInflater.from(container.getContext())
                .inflate(R.layout.main_page_places_item, container, false);
        ImageView imageView = view.findViewById(R.id.tours_photo);
        imageView.setImageResource(images[position]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        TextView textName = view.findViewById(R.id.tours_name);
        textName.setText(nameSet[position]);
        TextView textDistance = view.findViewById(R.id.place_distance);
        textDistance.setText(distanceSet[position]);

        // add onClickListener for each card
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, LandmarkDetailsActivity.class));
            }
        });

        // add view to ViewPager
        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}