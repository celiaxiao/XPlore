package com.UCSDTripleC.XPloreUCSD.database;

import java.util.ArrayList;

/**
 * Underlying data structure for the preset tours backend
 */
public class Tour {

    private String name;
    private String type;
    private ArrayList<Landmark> landmarks;
    //strings of places to be converted to landmark objects (if need be)
    private ArrayList<String> places;
    private ArrayList<String> graphicsWithBackground;
    private ArrayList<String> graphicsWithoutBackground;

    /**
     * Constructor for the tour
     *
     * @param name name of tour
     * @param landmarks landmarks that this tour features
     * @param places string of the landmark name that will be used to convert into landmark
     *                        object
     * @param graphicsWithBackground string of file location of graphics with background
     * @param graphicsWithoutBackground string of file location of graphics without background
     */
    public Tour(String name, String type, ArrayList<Landmark> landmarks,
                ArrayList<String> places,
                ArrayList<String> graphicsWithBackground,
                ArrayList<String> graphicsWithoutBackground) {
        this.name = name;
        this.type = type;
        this.landmarks = landmarks;
        this.places = places;
        this.graphicsWithBackground = graphicsWithBackground;
        this.graphicsWithoutBackground = graphicsWithoutBackground;
    }

    public String getName() {
        return name;
    }

    public String getType() { return type;}

    public ArrayList<Landmark> getLandmarks() {
        return landmarks;
    }

    public ArrayList<String> getPlaces() {
        return places;
    }

    public ArrayList<String> getGraphicsWithBackground() {
        return graphicsWithBackground;
    }

    public ArrayList<String> getGraphicsWithoutBackground() {
        return graphicsWithoutBackground;
    }


    public void setLandmarks(ArrayList<Landmark> landmarks) {
        this.landmarks = landmarks;
    }
}
