package org.ucsdtriplec.xploreucsd;

import android.util.Pair;

import java.util.Comparator;

public class Landmark extends Cluster {

    public String college;
    public Pair location;
    public Integer collegeVal;
    public String name;

    public Landmark(String location) {
        //initializes the "database" lmao
        super();
        this.name = location;
        this.college = collegeMap.get(location);
        this.location = locationMap.get(location);
        this.collegeVal = collegeValMap.get(this.college);
    }

    public String toString() {
        return name;
    }
}

class LandmarkComparator implements Comparator<Landmark> {
    // Overriding compare()method of Comparator

    public int compare(Landmark l1, Landmark l2) {
        if (l1.collegeVal > l2.collegeVal) {
            return 1;
        } else return -1;

    }
}
