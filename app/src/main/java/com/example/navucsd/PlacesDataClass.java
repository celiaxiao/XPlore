package com.example.navucsd;

import android.util.Pair;

import androidx.annotation.NonNull;

import com.example.navucsd.database.Location;
import com.example.navucsd.utils.Geography;

public class PlacesDataClass {
    private String placesName;
    private String availability;
    private String distances;
    private static final String[] amenFilter=
            new String[]{"restroom","cafe","restaurant",
                    "busstop","parking"};
    private boolean[] amenities;

    public PlacesDataClass(String name, String availability, String distance, boolean[] amenities) {
        this.placesName = name;
        this.availability = availability;
        this.distances = distance;
        this.amenities = amenities;
    }

    public PlacesDataClass(Location location){
        this.placesName=location.name;
        //for now hard code the distances and availability
        this.availability = "";
        //hide the distances
        this.distances="";
        //hard code amenities to be 5
        this.amenities=new boolean[5];
        // FIXME meaningless if and potential NullPointerException
        for(int j=0;j<location.amenities.size();j++){
            if(location.amenities != null) {
                this.amenities[j] = location.amenities.get(amenFilter[j]);
            }
        }
    }

    public PlacesDataClass(Pair<Location, Double> pair) {
        placesName = pair.first.name;
        // for now hard code the distances and availability
        availability = "";
        distances = Geography.displayDistance(pair.second);
        // hard code amenities to be 5
        amenities = new boolean[5];
        for (int j = 0; j < pair.first.amenities.size(); j++) {
            if (pair.first.amenities != null) {
                this.amenities[j] = pair.first.amenities.get(amenFilter[j]);
            }
        }
    }

    @NonNull
    @Override
    public String toString() {
        return getPlacesName();
    }

    public String getAvailability() {
        return availability;
    }

    public String getDistances() {
        return distances;
    }

    public String getPlacesName() {
        return placesName;
    }

    public boolean[] getAmenities() {
        return amenities;
    }

    public void setPlacesName(String placesName) {
        this.placesName = placesName;
    }

    public void setAmenities(boolean[] amenities) {
        this.amenities = amenities;
    }
}
