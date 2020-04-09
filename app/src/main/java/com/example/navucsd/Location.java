package com.example.navucsd;

import com.example.navucsd.R;

import android.content.Context;
import android.content.res.Resources;
import android.util.Pair;

import java.util.ArrayList;

public class Location {

    public  String name;
    private Pair coordinates;
    private String about;
    //This is a file name
    private String thumbnailPhoto;
    private ArrayList<String> otherPhotos;
    private ArrayList<String> amenities;
    private ArrayList<String> descriptive;
    private ArrayList<String> relatedPlaces;
    private ArrayList<String> relatedTours;
    //This is a file name
    private String audio;
    private String description;
    private ArrayList<Module> modules;


    public Location(String name, Pair coordinates, String about, String thumbnailPhoto,
                    ArrayList<String> otherPhotos, ArrayList<String> amenities,
                    ArrayList<String> descriptive, ArrayList<String> relatedPlaces,
                    ArrayList<String> relatedTours, String audio, String description,
                    ArrayList<Module> modules) {
        this.name = name;
        this.coordinates = coordinates;
        this.about = about;
        this.thumbnailPhoto = thumbnailPhoto;
        this.otherPhotos = otherPhotos;
        this.amenities = amenities;
        this.descriptive = descriptive;
        this.relatedPlaces = relatedPlaces;
        this.relatedTours = relatedTours;
        this.audio = audio;
        this.description = description;
        this.modules = modules;
    }
}

class Module{
    private String name;
    private String description;
    //These are video links
    private ArrayList<String> videos;
    //These are video links
    private ArrayList<String> links;


    public Module(String name, String description, ArrayList<String> videos, ArrayList<String> links) {
        this.name = name;
        this.description = description;
        this.videos = videos;
        this.links = links;
    }

}
