package com.example.navucsd;

import android.util.Pair;

import java.util.HashMap;

public class Cluster{
    public static HashMap<String, Pair> locationMap = new HashMap<>();
    public static HashMap<String, String> collegeMap = new HashMap<>();
    public static HashMap<String, Integer> collegeValMap = new HashMap<>();

    public Cluster(){
        collegeValMap.put("Warren", 0);
        collegeValMap.put("Sixth", 1);
        collegeValMap.put("Biomed", 2);
        collegeValMap.put("Revelle", 3);
        collegeValMap.put("Muir", 4);
        collegeValMap.put("Marshall", 5);
        collegeValMap.put("ERC", 6);
        collegeValMap.put("centralRegion", 7);
        //Warren
        collegeMap.put("canyonViewPool", "Warren");
        locationMap.put("canyonViewPool",new Pair(32.880769, -117.231803));
        collegeMap.put("warrenBear", "Warren");
        locationMap.put("warrenBear",new Pair(32.882261, -117.234064));
        collegeMap.put("JSOE", "Warren");
        locationMap.put("JSOE",new Pair(32.881711, -117.235416));
        collegeMap.put("atkinsonHall", "Warren");
        locationMap.put("atkinsonHall",new Pair(32.882602, -117.234693));
        //Sixth
        collegeMap.put("conradPrebysMusicCenter", "Sixth");
        locationMap.put("conradPrebysMusicCenter",new Pair(32.878094, -117.234814));
        //Biomed Area
        collegeMap.put("biomedicalLibrary", "Biomed");
        locationMap.put("biomedicalLibrary",new Pair(32.875522, -117.236930));
        collegeMap.put("pharmaceuticalSciencesBuilding", "Biomed");
        locationMap.put("pharmaceuticalSciencesBuilding",new Pair(32.874366, -117.235775));
        //Revelle
        collegeMap.put("keelingAparmtnet", "Revelle");
        locationMap.put("keelingAparmtnet",new Pair(32.873986, -117.243350));
        collegeMap.put("yorkHall", "Revelle");
        locationMap.put("yorkHall",new Pair(32.874701, -117.240061));
        collegeMap.put("galbraith", "Revelle");
        locationMap.put("galbraith",new Pair(32.873809, -117.240962));
        collegeMap.put("mayerHall", "Revelle");
        locationMap.put("mayerHall",new Pair(32.875988, -117.239862));
        collegeMap.put("64Degrees", "Revelle");
        locationMap.put("64Degrees",new Pair(32.874944, -117.242034));
        //Muir
        collegeMap.put("sunGodStatue", "Muir");
        locationMap.put("sunGodStatue",new Pair(32.878723, -117.239707));
        //Marshall
        collegeMap.put("oceanView", "Marshall");
        locationMap.put("oceanView", new Pair(32.883275, -117.242692));
        //ERC
        collegeMap.put("radySchoolOfManagement", "ERC");
        locationMap.put("radySchoolOfManagement", new Pair(32.886794, -117.241162));
        collegeMap.put("RIMAC", "ERC");
        locationMap.put("RIMAC", new Pair(32.885197, -117.240474));
        collegeMap.put("americanInstitute", "ERC");
        locationMap.put("americanInstitute", new Pair(32.885381, -117.241032));
        //Central Region
        collegeMap.put("careerServiceCenter", "centralRegion");
        locationMap.put("americanInstitute", new Pair(32.878693, -117.237961));
        collegeMap.put("geiselLibrary", "centralRegion");
        locationMap.put("americanInstitute", new Pair(32.881324, -117.237473));
        collegeMap.put("priceCenter", "centralRegion");
        locationMap.put("americanInstitute", new Pair(32.879838, -117.236206));
    }
}
