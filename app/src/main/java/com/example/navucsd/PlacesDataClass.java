package com.example.navucsd;

import androidx.annotation.NonNull;

public class PlacesDataClass {
    private String placesName;
    private String avalability;
    private String distances;
    private boolean[] amenities;
    public PlacesDataClass(String name,String ava,String distance, boolean[] amanities){
        this.placesName=name;
        this.avalability=ava;
        this.distances=distance;
        if(amanities!=null) this.amenities=amanities;
        //
        // else this.amenities=new boolean[5];
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

    public void setAvalability(String avalability) {
        this.avalability = avalability;
    }

    public void setDistances(String distances) {
        this.distances = distances;
    }

    public void setPlacesName(String placesName) {
        this.placesName = placesName;
    }

    public void setAmenities(boolean[] amenities) {
        this.amenities = amenities;
    }
}
