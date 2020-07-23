package com.UCSDTripleC.XPloreUCSD;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.viewpager.widget.ViewPager;

import com.UCSDTripleC.XPloreUCSD.database.LandmarkDatabase;
import com.UCSDTripleC.XPloreUCSD.database.Landmark;
import com.UCSDTripleC.XPloreUCSD.utils.Utils;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * This activity temporary hardcodes the landmark details page.
 */
public class LandmarkDetailsActivity extends AppCompatActivity {

    private ViewPager landmarkPager;
    //multiple image thumbnail
    private RecyclerView multipleIamgeRecycler;
    private LandmarkImageAdapter landmarkImageAda;
    private LinearLayoutManager layoutManager;
    private int currItem = 0;

    private CollapsingToolbarLayout collapsingToolbarLayout;

    private LandmarkDatabase database;
    private Landmark currLandmark;
    private String currLocationName;
    private static final String DEFAULT_LOCATION = "Fallen Star";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landmark_details);

        database = new LandmarkDatabase(this, "one by one");

        Bundle argument = getIntent().getExtras();
        if (argument != null) {
            currLocationName = argument.getString("placeName");
            if (currLocationName != null) {
                currLandmark = database.getByName(currLocationName);
            }
        }
        else {
            currLandmark = database.getByName(DEFAULT_LOCATION);
        }

        collapsingToolbarLayout = findViewById(R.id.landmark_collapsing_toolbar);
        collapsingToolbarLayout.setTitle(currLandmark.getName());
        Toolbar toolbar = findViewById(R.id.landmark_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ActionBar actionbar = getSupportActionBar();
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        actionbar.setDisplayHomeAsUpEnabled(true);

        //set up landmark image recyclerview
        multipleIamgeRecycler=findViewById(R.id.landmark_image_recyclerview);
        //define the layoutManager to be horizontal linearLayout
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        multipleIamgeRecycler.setLayoutManager(layoutManager);
        landmarkImageAda = new LandmarkImageAdapter();
        multipleIamgeRecycler.setAdapter(landmarkImageAda);
        //soul of the recyclerview, auto snap to next position
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(multipleIamgeRecycler);


        TabLayout tabLayout = findViewById(R.id.landmark_tablayout);
        landmarkPager = findViewById(R.id.landmark_viewpager);
        LandmarkAdapter pagerAdapter = new LandmarkAdapter(getSupportFragmentManager());
        landmarkPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(landmarkPager);
    }

    private class LandmarkAdapter extends FragmentStatePagerAdapter {

        private final String[] TAB_TITLES = {"Overview", "History"};

        public LandmarkAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return LandmarkDetailsOverviewFragment.newInstance(currLandmark, database);
                case 1:
                    return LandmarkDetailsHistoryFragment.newInstance("","");
            }
            return new MainPageFragment();
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return TAB_TITLES[position];
        }
    }

    //set up landmark image recyclerview adapter
    private class LandmarkImageAdapter extends RecyclerView.Adapter<LandmarkImageAdapter.MyViewHolder> {


        public LandmarkImageAdapter() {
        }

        // Create new views (invoked by the layout manager)
        @Override
        public LandmarkImageAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                    int viewType) {
            // create a new view
            ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext( ))
                    .inflate(R.layout.landmark_details_image, parent, false);

            MyViewHolder vh = new MyViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            currItem=position;
            //set up the button
            if (currItem == 0) {
                holder.buttonLeft.setVisibility(View.GONE);
                holder.buttonRight.setVisibility(View.VISIBLE);
            } else if (currItem == getItemCount( ) - 1) {
                holder.buttonLeft.setVisibility(View.VISIBLE);
                holder.buttonRight.setVisibility(View.GONE);
            } else {
                holder.buttonLeft.setVisibility(View.VISIBLE);
                holder.buttonRight.setVisibility(View.VISIBLE);
            }
            Log.i("landmark image",""+position);
            // load image
            try {
                // get input stream
                //InputStream ims = getAssets().open(currLandmark.getOtherPhotos().get(position));
                InputStream ims = getAssets().open(currLandmark.getThumbnailPhoto());
                // load image as Drawable
                Drawable d = Drawable.createFromStream(ims, null);
                    // set image to ImageView
                holder.landmarkimage.setImageDrawable(d);

            }
            catch(IOException ex) {
                ex.printStackTrace();
            }


        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return currLandmark.getOtherPhotos().size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            private  ConstraintLayout constraintLayout;
            private ImageButton buttonLeft, buttonRight;
            private ImageView landmarkimage;

            public MyViewHolder(ConstraintLayout v) {
                super(v);
                buttonLeft = v.findViewById(R.id.imageButtonLeft);
                buttonRight = v.findViewById(R.id.imageButtonRight);
                landmarkimage = (ImageView)v.findViewById(R.id.landmark_details_image);
                v.setOnClickListener(new View.OnClickListener( ) {
                    @Override
                    public void onClick(View v) {
                        //zoom the image
//
                    }

                });
                //scroll to left
                buttonLeft.setOnClickListener(new View.OnClickListener( ) {
                    @Override
                    public void onClick(View view) {
                        if (currItem - 1 != -1) {
                            multipleIamgeRecycler.smoothScrollToPosition(--currItem);
                            //updateButtons( );
                        }
                    }
                });
                //scroll to right
                buttonRight.setOnClickListener(new View.OnClickListener( ) {
                    @Override
                    public void onClick(View view) {
                        if (currItem + 1 != getItemCount()) {
                            multipleIamgeRecycler.smoothScrollToPosition(++currItem);
                        }
                    }
                });

            }

        }

    }
}