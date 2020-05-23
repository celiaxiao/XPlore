package com.example.navucsd;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class RecyclerViewAdapterTourOverviewPage extends RecyclerView.Adapter<RecyclerViewAdapterTourOverviewPage.ViewHolder> {
    private ArrayList<String> items;
    private Context context;

    public RecyclerViewAdapterTourOverviewPage(Context context, ArrayList<String> items) {
        this.context = context;
        this.items = items;
    }

    // ViewHolder Class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView placeNameTextView;
        ImageView restroomIconTourOverview;
        ImageView cafeIconTourOverview;
        ImageView restaurantIconTourOverview;
        ImageView busstopIconTourOverview;
        ImageView parkinglotIconTourOverview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            placeNameTextView = (TextView) itemView.findViewById(R.id.placeNameTextView);

            // Amenities ImageViews,
            // drawable b1.png ---> b5.png are activated version of amenities icons,
            // g1.png ---> g5.png are deactivated icons
            // TODO: Set up amenities icon using activated or deactivated drawable icons
            restroomIconTourOverview = (ImageView) itemView.findViewById(R.id.restroomIconTourOverview);
            cafeIconTourOverview = (ImageView) itemView.findViewById(R.id.cafeIconTourOverview);
            restaurantIconTourOverview = (ImageView) itemView.findViewById(R.id.restaurantIconTourOverview);
            busstopIconTourOverview = (ImageView) itemView.findViewById(R.id.busstopIconTourOverview);
            parkinglotIconTourOverview = (ImageView) itemView.findViewById(R.id.parkinglotIconTourOverview);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tour_overview_page_detail, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String item = items.get(position);
        holder.placeNameTextView.setText(item.toString());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}
