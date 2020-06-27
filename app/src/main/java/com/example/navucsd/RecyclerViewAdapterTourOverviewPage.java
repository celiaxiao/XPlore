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
    private RecyclerViewOnItemClickListener recyclerViewOnItemClickListener; // A customized OnItemClick Listener to support onItemClick function in RecyclerView

    public RecyclerViewAdapterTourOverviewPage(Context context, ArrayList<String> items, RecyclerViewOnItemClickListener recyclerViewOnItemClickListener) {
        this.context = context;
        this.items = items;
        this.recyclerViewOnItemClickListener = recyclerViewOnItemClickListener;
    }

    // ViewHolder Class
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView placeNameTextView;
        ImageView restroomIconTourOverview;
        ImageView cafeIconTourOverview;
        ImageView restaurantIconTourOverview;
        ImageView busstopIconTourOverview;
        ImageView parkinglotIconTourOverview;

        RecyclerViewOnItemClickListener recyclerViewOnItemClickListener;

        public ViewHolder(@NonNull View itemView, RecyclerViewOnItemClickListener recyclerViewOnItemClickListener) {
            super(itemView);
            placeNameTextView = (TextView) itemView.findViewById(R.id.placeNameTextView);
            this.recyclerViewOnItemClickListener = recyclerViewOnItemClickListener; // Set the instance variable OnItemClickListener to the passed in argument OnItemClickListener


            itemView.setOnClickListener(this); // Set this ViewHolder as the onClickListener since the ViewHolder Class implements the View.OnClickListener interface


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

        @Override
        public void onClick(View view) {
            recyclerViewOnItemClickListener.OnItemClick(getAdapterPosition()); // Call the customized OnItemClick function in RecyclerViewOnItemClickListener
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tour_overview_page_detail, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, recyclerViewOnItemClickListener);
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


    public interface RecyclerViewOnItemClickListener {
        void OnItemClick(int position);
    }
}
