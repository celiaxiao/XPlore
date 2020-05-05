package org.ucsdtriplec.xploreucsd;

import android.util.Pair;

import java.util.HashMap;

public class Cluster {
    public static HashMap<String, Pair> locationMap = new HashMap<>();
    public static HashMap<String, String> collegeMap = new HashMap<>();
    public static HashMap<String, Integer> collegeValMap = new HashMap<>();

    public Cluster() {
        collegeValMap.put("Warren", 0);
        collegeValMap.put("Sixth", 1);
        collegeValMap.put("Biomed", 2);
        collegeValMap.put("Revelle", 3);
        collegeValMap.put("Muir", 4);
        collegeValMap.put("Marshall", 5);
        collegeValMap.put("ERC", 6);
        collegeValMap.put("centralRegion", 7);
        //Warren
        collegeMap.put("Canyonview pool", "Warren");
        locationMap.put("Canyonview pool", new Pair(32.880769, -117.231803));
        collegeMap.put("Warren Bear", "Warren");
        locationMap.put("Warren Bear", new Pair(32.882261, -117.234064));
        collegeMap.put("Jacobs School of Engineering Building and Fallen Star", "Warren");
        locationMap.put("Jacobs School of Engineering Building and Fallen Star", new Pair(32.881711, -117.235416));
        collegeMap.put("Atkinson Hall", "Warren");
        locationMap.put("Atkinson Hall", new Pair(32.882602, -117.234693));
        //Sixth
        collegeMap.put("Conrad Prebys Music Center", "Sixth");
        locationMap.put("Conrad Prebys Music Center", new Pair(32.878094, -117.234814));
        //Biomed Area
        collegeMap.put("Biomedical Library", "Biomed");
        locationMap.put("Biomedical Library", new Pair(32.875522, -117.236930));
        collegeMap.put("Pharmaceutical Sciences Building", "Biomed");
        locationMap.put("Pharmaceutical Sciences Building", new Pair(32.874366, -117.235775));
        //Revelle
        collegeMap.put("Keeling Apartment", "Revelle");
        locationMap.put("Keeling Apartment", new Pair(32.873986, -117.243350));
        collegeMap.put("York Hall", "Revelle");
        locationMap.put("York Hall", new Pair(32.874701, -117.240061));
        collegeMap.put("Galbraith Hall", "Revelle");
        locationMap.put("Galbraith Hall", new Pair(32.873809, -117.240962));
        collegeMap.put("Mayer Hall", "Revelle");
        locationMap.put("Mayer Hall", new Pair(32.875988, -117.239862));
        collegeMap.put("64 Degrees", "Revelle");
        locationMap.put("64 Degrees", new Pair(32.874944, -117.242034));
        //Muir
        collegeMap.put("Sun God Statue", "Muir");
        locationMap.put("Sun God Statue", new Pair(32.878723, -117.239707));
        //Marshall
        collegeMap.put("OceanView", "Marshall");
        locationMap.put("OceanView", new Pair(32.883275, -117.242692));
        collegeMap.put("Peterson Hall", "Marshall");
        locationMap.put("Peterson Hall", new Pair(32.880117, -117.240340));
        //ERC
        collegeMap.put("Rady School of Management", "ERC");
        locationMap.put("Rady School of Management", new Pair(32.886794, -117.241162));
        collegeMap.put("RIMAC Gym", "ERC");
        locationMap.put("RIMAC Gym", new Pair(32.885197, -117.240474));
        collegeMap.put("American Institute", "ERC");
        locationMap.put("American Institute", new Pair(32.885381, -117.241032));
        //Central Region
        collegeMap.put("Career Services Center", "centralRegion");
        locationMap.put("Career Services Center", new Pair(32.878693, -117.237961));
        collegeMap.put("Geisel Library", "centralRegion");
        locationMap.put("Geisel Library", new Pair(32.881324, -117.237473));
        collegeMap.put("Price Center", "centralRegion");
        locationMap.put("Price Center", new Pair(32.879838, -117.236206));
    }
}
