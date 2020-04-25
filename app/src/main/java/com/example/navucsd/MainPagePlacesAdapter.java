package com.example.navucsd;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class MainPagePlacesAdapter extends RecyclerView.Adapter<MainPagePlacesAdapter.MyViewHolder> {

    private static int PLACE_NUMBER = 3;

    private String[] nameSet = {"Geisel Library", "Price Center", "Fallen Star", "Jacobs School of Engineering"};
    private String[] distanceSet = {"<100m", "300m", "350m", "350m"};
    private int[] pictures = {R.drawable.geisel_landmark, R.drawable.price_center_east, R.drawable.fallen_star, R.drawable.ucsd};

    // Provide a suitable constructor (depends on the kind of dataset)
    public MainPagePlacesAdapter() {
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MainPagePlacesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_page_places_item, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textViewPlaceName.setText(nameSet[position]);
        holder.textViewPlaceDistance.setText(distanceSet[position]);
        holder.imageViewPlacePhoto.setImageResource(pictures[position]);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return PLACE_NUMBER;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textViewPlaceName, textViewPlaceDistance;
        public ImageView imageViewPlacePhoto;

        public MyViewHolder(CardView v) {
            super(v);
            textViewPlaceName = v.findViewById(R.id.tours_name);
            textViewPlaceDistance = v.findViewById(R.id.place_distance);
            imageViewPlacePhoto = v.findViewById(R.id.tours_photo);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), LandmarkDetailsActivity.class);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}