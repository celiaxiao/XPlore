package com.UCSDTripleC.XPloreUCSD;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.Arrays;

public class TransportationGuideActivity extends AppCompatActivity {

    private CollapsingToolbarLayout collapsingToolbarLayout;

    // Related Links
    private RecyclerView parkingLinksRecycler;
    private ParkingLinksAdapter parkingLinksAdapter;
    private LinearLayoutManager layoutManager;
    private String[] parkingarray=new String[]{"Find Parking Lots Near Me: https://www.google.com/maps/search/parking+lot+ucsd/@32.8743262,-117.2308927,14z/data=!3m1!4b1",
    "How to Park: https://transportation.ucsd.edu/parking/visitor/index.html",
    "Park with ParkMobile App: https://transportation.ucsd.edu/parking/pay-by-app.html"};
    private ArrayList<String> parkinglist=new ArrayList<String>(Arrays.asList(parkingarray));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transportation_guide);
        //set up collapsing tool bar
        collapsingToolbarLayout = findViewById(R.id.landmark_collapsing_toolbar);

        Toolbar toolbar = findViewById(R.id.transport_guide_toolbar);
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

        //
        // Set up related links
        parkingLinksRecycler = findViewById(R.id.parking_recycler);
        parkingLinksRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        parkingLinksRecycler.setLayoutManager(layoutManager);
        parkingLinksAdapter = new ParkingLinksAdapter();
        parkingLinksAdapter.setLinks(parkinglist);
        parkingLinksRecycler.addItemDecoration(getRecyclerViewDivider(R.drawable.trans_guide_divider));
        parkingLinksRecycler.setAdapter(parkingLinksAdapter);
    }
    /**
     * get the divider
     * @source: https://www.jianshu.com/p/9631600f7149
     * @param drawableId divider id
     * @return
     */
    public RecyclerView.ItemDecoration getRecyclerViewDivider(@DrawableRes int drawableId) {
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(drawableId));
        return itemDecoration;
    }

    private class ParkingLinksAdapter extends RecyclerView.Adapter<ParkingLinksAdapter.MyViewHolder> {

        private ArrayList<String> Links = new ArrayList<>();

        public void setLinks(ArrayList<String> links) {
            Links = links;
            notifyDataSetChanged();
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ParkingLinksAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            TextView v = (TextView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.overview_links_item, parent, false);

            ParkingLinksAdapter.MyViewHolder vh = new ParkingLinksAdapter.MyViewHolder(v);
            return vh;
        }



        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ParkingLinksAdapter.MyViewHolder holder, int position) {
            String link = Links.get(position);
            String[] split;
            if (!link.startsWith("http")) {
                split = link.split(":", 2);
            }
            else {
                split = new String[]{"Link", link};
            }
            holder.linkText.setText(split[0]);
            holder.linkText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browse = new Intent(Intent.ACTION_VIEW , Uri.parse(split[1].replace(" ","")));
                    try {
                        startActivity(browse);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return Links.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView linkText;

            public MyViewHolder(TextView v) {
                super(v);
                linkText = v;
            }
        }
    }

}