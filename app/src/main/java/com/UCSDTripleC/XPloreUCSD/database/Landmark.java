package com.UCSDTripleC.XPloreUCSD.database;

import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * All the details associated with a landmark.
 *
 * Sorted in alphabetically order by name and name only.
 */
public class Landmark implements Comparable<Landmark> {
    private String name;
    private Pair<String, String> coordinates;
    private String about;
    // path
    private String thumbnailPhoto;
    private ArrayList<String> otherPhotos;
    private HashMap<String, Boolean> amenities;
    private ArrayList<String> descriptive;
    // FIXME maybe should be Landmark
    private ArrayList<String> relatedPlaces;
    private ArrayList<String> relatedTours;
    // path
    private String audio;
    private ArrayList<String> links;
    private ArrayList<String> videos;
    private ArrayList<History> history;

    public Landmark(
        String name,
        Pair<String, String> coordinates,
        String about,
        String thumbnailPhoto,
        ArrayList<String> otherPhotos,
        HashMap<String,Boolean> amenities,
        ArrayList<String> descriptive,
        ArrayList<String> relatedPlaces,
        ArrayList<String> relatedTours,
        String audio,
        ArrayList<String> links,
        ArrayList<String> videos,
        ArrayList<History> history
    ) {
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
        this.history = history;
    }

    public String getName() {
        return name;
    }

    public Pair<String, String> getCoordinates() {
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

    /**
     * Compares this object with the specified object for order solely using the name field.
     *
     * @param landmark the object to compare to
     * @return a negative integer, zero, or a positive integer as this object is less than, equal
     * to, or greater than the specified object
     * @throws NullPointerException if {@code landmark} is null
     */
    @Override
    public int compareTo(Landmark landmark) {
        return name.compareTo(landmark.name);
    }
}

