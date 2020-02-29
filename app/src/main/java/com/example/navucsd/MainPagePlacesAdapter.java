package com.example.navucsd;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MainPagePlacesAdapter extends RecyclerView.Adapter<MainPagePlacesAdapter.MyViewHolder> {

    private String[] nameSet = {"Geisel Library", "Price Center", "Fallen Star", "Jacobs School of Engineering"};
    private String[] distanceSet = {"<100m", "300m", "350m", "350m"};

    // Provide a suitable constructor (depends on the kind of dataset)
    public MainPagePlacesAdapter() {
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MainPagePlacesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
        // create a new view
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext())
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
        holder.imageViewPlacePhoto.setImageResource(R.drawable.geisel);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return nameSet.length;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textViewPlaceName, textViewPlaceDistance;
        public ImageView imageViewPlacePhoto;

        public MyViewHolder(RelativeLayout v) {
            super(v);
            textViewPlaceName = v.findViewById(R.id.place_name);
            textViewPlaceDistance = v.findViewById(R.id.place_distance);
            imageViewPlacePhoto = v.findViewById(R.id.place_photo);
        }
    }
}