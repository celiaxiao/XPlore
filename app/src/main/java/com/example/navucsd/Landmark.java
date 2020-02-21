package com.example.navucsd;

import android.util.Pair;

public class Landmark extends Cluster{

    public String college;
    public Pair location;

    public Landmark(String location){
        super();
        this.college = super.collegeMap.get(location);
        this.location = super.locationMap.get(location);
    }
}
