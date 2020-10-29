package com.UCSDTripleC.XPloreUCSD;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.UCSDTripleC.XPloreUCSD.database.Landmark;

import java.util.ArrayList;

public class RecyclerViewAdapterTourOverviewPage extends RecyclerView.Adapter<RecyclerViewAdapterTourOverviewPage.ViewHolder> {
    private ArrayList<Landmark> items;
    private Context context;
    private RecyclerViewOnItemClickListener recyclerViewOnItemClickListener; // A customized OnItemClick Listener to support onItemClick function in RecyclerView

    public RecyclerViewAdapterTourOverviewPage(Context context, ArrayList<Landmark> items, RecyclerViewOnItemClickListener recyclerViewOnItemClickListener) {
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
        String item = items.get(position).getName();
        holder.placeNameTextView.setText(item.toString());

        String[] strArray = {"cafe",  "restaurant", "restroom"};

        if(items.get(position).getAmenities().get(strArray[0])){
            holder.cafeIconTourOverview.setImageDrawable(context.getDrawable(R.drawable.icon_cafe_white));
            holder.cafeIconTourOverview.setColorFilter(Color.WHITE);
        }
        else{
            holder.cafeIconTourOverview.setColorFilter(Color.GRAY);
        }
        if(items.get(position).getAmenities().get(strArray[1])){
            holder.restaurantIconTourOverview.setImageDrawable(context.getDrawable(R.drawable.icon_restaurant_white));
            holder.restaurantIconTourOverview.setColorFilter(Color.WHITE);
        }
        else{
            holder.restaurantIconTourOverview.setColorFilter(Color.GRAY);
        }
        if(items.get(position).getAmenities().get(strArray[2])){
            holder.restroomIconTourOverview.setImageDrawable(context.getDrawable(R.drawable.icon_restroom_white));
            holder.restroomIconTourOverview.setColorFilter(Color.WHITE);
        }
        else{
            holder.restroomIconTourOverview.setColorFilter(Color.GRAY);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public interface RecyclerViewOnItemClickListener {
        void OnItemClick(int position);
    }
}
