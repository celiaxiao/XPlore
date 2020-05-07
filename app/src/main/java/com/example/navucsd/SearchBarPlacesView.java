import com.example.navucsd.PlacesDataClass;

import java.util.ArrayList;
import java.util.List;

/*package com.example.navucsd;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchBarPlacesView extends ArrayAdapter {
    private final String[] placesName;
    private final String[] availability;
    private final String[] distance;//default to string, could replace to int/Integer
    private final boolean[][] amentities;
    private static final String[] amenFilter=
            new String[]{"Amentity_bathromm","Amentity_cafe","Amentity_resturant",
                    "Amentity_bus","Amentity_parking"};
    private ArrayList<PlacesDataClass> places;
    private Activity context;
    private Filter filter;
    public ArrayList<PlacesDataClass> filtered;

    ArrayAdapter<String> placesAdapter;
    public SearchBarPlacesView(Activity context, String[] maintitle, String[] subtitle,
                               String[] imgid, boolean[][] amentities) {
        super(context, R.layout.search_bar_places, maintitle);
        //  Auto-generated constructor stub
        this.context=context;
        this.placesName=maintitle;
        this.availability=subtitle;
        this.distance=imgid;
        filtered =new ArrayList<PlacesDataClass>();
        for(int i=0;i<maintitle.length;i++){
            PlacesDataClass place=new PlacesDataClass(maintitle[i],subtitle[i],imgid[i],amentities[i]);
            filtered.add(place);
        }
        this.places=filtered;
        filter=new AppFilter<PlacesDataClass>(filtered);
        this.amentities=amentities;
    }

    //set up the list overall view with title, avalibility, and distances
    public View getView(int position, View view, ViewGroup parent) {

        //get the xml layout file as an item for the list
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.search_bar_places, null,true);
        //set up the list view item
        TextView nameText = (TextView) rowView.findViewById(R.id.NameOfThePlace);
        TextView avaiText = (TextView) rowView.findViewById(R.id.availability);
        TextView distanceText = (TextView) rowView.findViewById(R.id.distance);
        ImageView bathroomIcon=(ImageView)rowView.findViewById(R.id.bathroomIcon);
        ImageView resturantIcon=(ImageView)rowView.findViewById(R.id.resturantIcon);
        ImageView cafeIcon=(ImageView)rowView.findViewById(R.id.cafeIcon);
        ImageView busStationIcon=(ImageView)rowView.findViewById(R.id.busStationIcon);
        ImageView parkingIcon=(ImageView)rowView.findViewById(R.id.parkingIcon);

        //initialize places information
        nameText.setText(filtered.get(position).getPlacesName());
        avaiText.setText(filtered.get(position).getAvalability());
        distanceText.setText(filtered.get(position).getDistances());
        //initialize amentities
        boolean[] amen=filtered.get(position).getAmenities();
        if(amen[0]) bathroomIcon.setImageResource(R.drawable.b1);
        if(amen[1]) cafeIcon.setImageResource(R.drawable.b2);
        if(amen[2]) resturantIcon.setImageResource(R.drawable.b3);
        if(amen[3]) busStationIcon.setImageResource(R.drawable.b4);
        if(amen[4]) parkingIcon.setImageResource(R.drawable.b5);
        return rowView;
    };

    @Override
    public int getCount() {
        return filtered.size( );
    }

    @Override
    public PlacesDataClass getItem(int position) {
        return filtered.get(position);
    }

    @Override
    public Filter getFilter() {
        if (filter == null)
            filter = new AppFilter<PlacesDataClass>(places);
        return filter;
    }

    /**
     * Class for filtering in Arraylist listview. Objects need a valid
     * 'toString()' method.
     *
     * inspired by Alxandr (http://stackoverflow.com/a/2726348/570168)
     *
     */
/*
    private class AppFilter<T> extends Filter {

        private ArrayList<T> sourceObjects;

        public AppFilter(List<T> objects) {
            sourceObjects = new ArrayList<T>();
            synchronized (this) {
                sourceObjects.addAll(objects);
            }
        }

        @Override
        protected FilterResults performFiltering(CharSequence chars) {
            String filterSeq = chars.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if (filterSeq != null && filterSeq.length() > 0) {
                ArrayList<T> filter = new ArrayList<T>();

                for (T object : sourceObjects) {
                    // the filtering itself:
                    if (object.toString().toLowerCase().contains(filterSeq))
                        filter.add(object);
                }
                result.count = filter.size();
                result.values = filter;
            } else {
                // add all objects
                synchronized (this) {
                    result.values = sourceObjects;
                    result.count = sourceObjects.size();
                }
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            // NOTE: this function is *always* called from the UI thread.
            filtered = (ArrayList<PlacesDataClass>) results.values;
            notifyDataSetChanged( );

        }
    }


}
*/
