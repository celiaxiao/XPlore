package com.example.navucsd;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.navucsd.database.Location;
import com.example.navucsd.utils.DownloadImageTask;
import com.example.navucsd.utils.Geography;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Adapter for ViewPager in HomeActivity
 * source: https://www.jianshu.com/p/58f356eaa6e9
 */
public class AutoSlideViewPagerAdapter extends PagerAdapter {

    private Context context;
    // Duplicate the first and the last images for recyclable scrolling
    private String[] nameSet = {"Fallen Star", "Geisel Library", "Price Center", "Fallen Star", "Geisel Library"};
    private String[] distanceSet = {"350m", "<100m", "300m", "350m", "<100m"};
    private String[] imageUrl = {"https://stuartcollection.ucsd.edu/_images/artists/suh-fallenstar/Main_suh-3.jpg",
            "https://live.staticflickr.com/7177/13535412304_8571d152b8_b.jpg",
            "https://www.cannondesign.com/assets/Price1-e1515003216991.jpg",
            "https://stuartcollection.ucsd.edu/_images/artists/suh-fallenstar/Main_suh-3.jpg",
            "https://live.staticflickr.com/7177/13535412304_8571d152b8_b.jpg"};
    private ArrayList<HashMap<String, Boolean>> amenitiesList;
    private ImageView imageView;
    private TextView textName;
    private TextView textDistance;
    private String[] nameMap = {"restroom","cafe","restaurant","busstop","parking"};

    public AutoSlideViewPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return nameSet.length;
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
        imageView = view.findViewById(R.id.main_place_photo);
        new DownloadImageTask(imageView).execute(imageUrl[position]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        textName = view.findViewById(R.id.main_place_name);
        textName.setText(nameSet[position]);
        textDistance = view.findViewById(R.id.main_place_distance);
        textDistance.setText(distanceSet[position]);
        LinearLayout amenitiesIcons = view.findViewById(R.id.main_place_amenities);
        if (amenitiesList!= null && amenitiesList.get(position) != null) {
            for (int i = nameMap.length - 1; i >= 0; i--) {
                if (amenitiesList.get(position).get(nameMap[i])) {
                    ((ImageView)amenitiesIcons.getChildAt(i)).setColorFilter(context.getColor(R.color.black));
                }
                else {
                    ((ImageView)amenitiesIcons.getChildAt(i)).setColorFilter(context.getColor(R.color.amenityChip));
                }
            }
        }

        // add onClickListener for each card
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LandmarkDetailsActivity.class);
                intent.putExtra("placeName", nameSet[position]);
                context.startActivity(intent);
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

    public void setContent(ArrayList<Pair<Location, Double>> places) {
        nameSet[1] = nameSet[4] = places.get(0).first.getName();
        nameSet[0] = nameSet[3] = places.get(2).first.getName();
        nameSet[2] = places.get(1).first.getName();
        imageUrl[1] = imageUrl[4] = places.get(0).first.getThumbnailPhoto();
        imageUrl[0] = imageUrl[3] = places.get(2).first.getThumbnailPhoto();
        imageUrl[2] = places.get(1).first.getThumbnailPhoto();
        distanceSet[1] = distanceSet[4] = Geography.displayDistance(places.get(0).second);
        distanceSet[0] = distanceSet[3] = Geography.displayDistance(places.get(2).second);
        distanceSet[2] = Geography.displayDistance(places.get(1).second);
        amenitiesList = new ArrayList<HashMap<String, Boolean>>() {
            {
                add(places.get(2).first.getAmenities());
                add(places.get(0).first.getAmenities());
                add(places.get(1).first.getAmenities());
                add(places.get(2).first.getAmenities());
                add(places.get(0).first.getAmenities());
            }
        };
        Log.d("NearContent", nameSet[0]);
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}