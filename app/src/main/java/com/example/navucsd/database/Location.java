package com.example.navucsd.database;

import com.example.navucsd.R;

import android.content.Context;
import android.content.res.Resources;
import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class Location {

    public  String name;
    public Pair coordinates;
    public String about;
    //This is a file name
    public String thumbnailPhoto;
    public ArrayList<String> otherPhotos;
    public HashMap<String, Boolean> amenities;
    public ArrayList<String> descriptive;
    //maybe should be Location
    public ArrayList<String> relatedPlaces;
    public ArrayList<String> relatedTours;
    //This is a file name
    public String audio;
    public ArrayList<String> videos;
    public ArrayList<String> links;


    public Location(String name, Pair coordinates, String about, String thumbnailPhoto,
                    ArrayList<String> otherPhotos, HashMap<String,Boolean> amenities,
                    ArrayList<String> descriptive, ArrayList<String> relatedPlaces,
                    ArrayList<String> relatedTours, String audio, ArrayList<String> links,
                    ArrayList<String> videos) {
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
        this.links = links;
        this.videos = videos;
    }
}

class Module{
    public String name;
    public String description;
    //These are video links
    public ArrayList<String> videos;
    //These are video links
    public ArrayList<String> links;


    public Module(String name, String description, ArrayList<String> videos, ArrayList<String> links) {
        this.name = name;
        this.description = description;
        this.videos = videos;
        this.links = links;
    }

}
