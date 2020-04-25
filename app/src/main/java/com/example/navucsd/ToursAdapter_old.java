package com.example.navucsd;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ToursAdapter_old extends RecyclerView.Adapter<ToursAdapter_old.MyViewHolder> {

    private static int PLACE_NUMBER = 6;

    private String[] nameSet = {"UCSD's Landmark Tour", "Most Popular Restaurants at UCSD", "Intro to Brutalism - Architecture of UCSD", "60 Years by the Beach - Historical Sites at UCSD", "Let's Get Punky - Hidden Artworks on Campus", "Revelle College"};
    private String[] timeSet = {"90min", "34min", "30min", "50min", "60min", "70min"};
    private String[] stopsSet = {"5 Stops", "3 Stops", "2 Stops", "3 Stops", "4 Stops", "5 Stops"};
    private int[] pictures = {R.drawable.tours_brutalism, R.drawable.tours_brutalism, R.drawable.tours_brutalism, R.drawable.tours_historical_sites, R.drawable.tours_hidden_artworks, R.drawable.tours_revelle};

    // Provide a suitable constructor (depends on the kind of dataset)
    public ToursAdapter_old() {
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ToursAdapter_old.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tours, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textViewName.setText(nameSet[position]);
        holder.textViewTime.setText(timeSet[position]);
        holder.textViewStops.setText(stopsSet[position]);
        holder.imageViewPhoto.setImageResource(pictures[position]);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return PLACE_NUMBER;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textViewName, textViewTime, textViewStops;
        public ImageView imageViewPhoto;

        public MyViewHolder(CardView v) {
            super(v);
            textViewName = v.findViewById(R.id.tours_name);
            textViewTime = v.findViewById(R.id.tours_time);
            textViewStops = v.findViewById(R.id.tours_stops);
            imageViewPhoto = v.findViewById(R.id.tours_photo);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(v.getContext(), LandmarkDetailsActivity.class);
//                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}