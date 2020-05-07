package org.ucsdtriplec.xploreucsd;

import androidx.annotation.NonNull;

public class PlacesDataClass {
    private String placesName;
    private String avalability; // FIXME typo here
    private String distances;

    public PlacesDataClass(String name, String ava, String distance) {
        this.placesName = name;
        this.avalability = ava;
        this.distances = distance;
    }

    @NonNull
    @Override
    public String toString() {
        return getPlacesName();
    }

    public String getAvalability() {
        return avalability;
    }

    public void setAvalability(String avalability) {
        this.avalability = avalability;
    }

    public String getDistances() {
        return distances;
    }

    public void setDistances(String distances) {
        this.distances = distances;
    }

    public String getPlacesName() {
        return placesName;
    }

    public void setPlacesName(String placesName) {
        this.placesName = placesName;
    }


}
