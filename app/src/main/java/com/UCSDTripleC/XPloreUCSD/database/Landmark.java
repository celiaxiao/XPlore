package com.UCSDTripleC.XPloreUCSD.database;

import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
/*
    beautiful location database
 */
public class Landmark {
    public  String name;
    public Pair coordinates;
    public String about;
    //This is a file name
    public String thumbnailPhoto;
    public ArrayList<String> otherPhotos;
    public HashMap<String, Boolean> amenities;
    public ArrayList<String> descriptive;
    //maybe should be Landmark
    public ArrayList<String> relatedPlaces;
    public ArrayList<String> relatedTours;
    //This is a file name
    public String audio;
    public ArrayList<String> videos;
    public ArrayList<String> links;
    public ArrayList<History> history;

    public Landmark(String name, Pair coordinates, String about, String thumbnailPhoto,
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

    public String getName() {
        return name;
    }

    public Pair getCoordinates() {
        return coordinates;
    }

    public String getAbout() {
        return about;
    }

    public String getThumbnailPhoto() {
        return thumbnailPhoto;
    }

    public ArrayList<String> getOtherPhotos() {
        return otherPhotos;
    }

    public HashMap<String, Boolean> getAmenities() {
        return amenities;
    }

    public ArrayList<String> getDescriptive() {
        return descriptive;
    }

    public ArrayList<String> getRelatedPlaces() {
        return relatedPlaces;
    }

    public ArrayList<String> getRelatedTours() {
        return relatedTours;
    }

    public String getAudio() {
        return audio;
    }

    public ArrayList<String> getVideos() {
        return videos;
    }

    public ArrayList<String> getLinks() {
        return links;
    }

    public ArrayList<History> getHistory() {
        return history;
    }
}

