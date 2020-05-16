package com.example.navucsd;

import androidx.annotation.NonNull;

import com.example.navucsd.database.Location;

public class PlacesDataClass {
    private String placesName;
    private String avalability;
    private String distances;
    private static final String[] amenFilter=
            new String[]{"restroom","cafe","restaurant",
                    "busstop","parking"};
    private boolean[] amenities;
    public PlacesDataClass(String name,String ava,String distance, boolean[] amanities){
        this.placesName=name;
        this.avalability=ava;
        this.distances=distance;
        if(amanities!=null) this.amenities=amanities;
        //
        // else this.amenities=new boolean[5];

    }
    public PlacesDataClass(Location location){
        this.placesName=location.name;
        //for now hard code the distances and avalability
        this.avalability="Open · Closes at 9pm";
        this.distances="200m";
        //hard code amenities to be 5
        this.amenities=new boolean[5];
        for(int j=0;j<location.amenities.size();j++){
            if(location.amenities != null) {
                this.amenities[j] = location.amenities.get(amenFilter[j]);
            }
        }
    }


    @NonNull
    @Override
    public String toString() {
        return getPlacesName();
    }

    public String getAvalability() {
        return avalability;
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
