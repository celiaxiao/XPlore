package com.UCSDTripleC.XPloreUCSD;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
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
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE;


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
    private Dialog dialog;

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
        multipleIamgeRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == SCROLL_STATE_IDLE) {
                    currItem = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();

                }
            }
        });


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
            // - replace the contents of the view with that elemente
            //currItem=position;
            //if only one photo, hide the button

            if(getItemCount()==1){
                holder.buttonLeft.setVisibility(View.GONE);
                holder.buttonRight.setVisibility(View.GONE);
            }
            //set up the button
            else if (position == 0) {
                holder.buttonLeft.setVisibility(View.GONE);
                holder.buttonRight.setVisibility(View.VISIBLE);
            } else if (position == getItemCount( ) - 1) {
                holder.buttonLeft.setVisibility(View.VISIBLE);
                holder.buttonRight.setVisibility(View.GONE);
            } else {
                holder.buttonLeft.setVisibility(View.VISIBLE);
                holder.buttonRight.setVisibility(View.VISIBLE);
            }
            String photoPath;
            Log.i("landmarkImage loading: ",""+position);
            //if no other photos avaliable, use thumbnail instead
            if(currLandmark.getOtherPhotos().size()==0){
                photoPath=currLandmark.getThumbnailPhoto();
            }
             else photoPath=currLandmark.getOtherPhotos().get(position);
            // load image
            try {
                // get input stream
                //InputStream ims = getAssets().open(currLandmark.getOtherPhotos().get(position));
                InputStream ims = getAssets().open(photoPath);
                // load image as Drawable
                Drawable d = Drawable.createFromStream(ims, null);
                    // set image to ImageView
                holder.landmarkimage.setImageDrawable(d);

                //  @source https://www.jianshu.com/p/c46020080034

                holder.landmarkimage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("CLICK","click");
                        Intent intent = new Intent(view.getContext(),ZoomImageActivity.class);
                        intent.putExtra("imageResource", ""+photoPath);

                        view.getContext().startActivity(
                                intent,
                                // 注意这里的sharedView
                                // Content View（动画作用view），String（和XML一样）
                                ActivityOptions.makeSceneTransitionAnimation((Activity) view.getContext(), view, "sharedView").toBundle());
                    }
                });

            }
            catch(IOException ex) {
                ex.printStackTrace();
            }
            Log.i("item count", "load: "+currItem);
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            if(currLandmark.getOtherPhotos().size()==0) return 1;
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
                    }

                });
                //scroll to left
                buttonLeft.setOnClickListener(new View.OnClickListener( ) {
                    @Override
                    public void onClick(View view) {
                        Log.i("item count", "left: "+currItem);
                        //if not the first picture
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
                        Log.i("item count", "right: "+currItem);
                        if (currItem + 1 != getItemCount()) {
                            multipleIamgeRecycler.smoothScrollToPosition(++currItem);
                        }
                    }
                });

            }


        }

    }
}
