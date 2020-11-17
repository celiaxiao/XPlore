package com.UCSDTripleC.XPloreUCSD.database;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class TourDatabase {

    private HashMap<String, Tour> map = new HashMap<>();
    public static String[] FILELIST  = new String[] {
            "tours/AlumniTour.json",
            "tours/CampusLifeTour.json",
            "tours/ERCCollegeTour.json",
            "tours/MarshallCollegeTour.json",
            "tours/MuirCollegeTour.json",
            "tours/RevelleCollegeTour.json",
            "tours/SignatureTour.json",
            "tours/SixthCollegeTour.json",
            "tours/StudySpacesTour.json",
            "tours/TheStuartCollectionTour.json",
            "tours/WarrenCollegeTour.json"
    };

    /**
     * Constructor for the tour database
     * Example Usage: TourDatabase tDatabase = new TourDatabase(this);
     * @param context context of the current activity
     */
    public TourDatabase(Context context) {
        Gson gson = new Gson();
        for (String s : FILELIST) {
            String jsonString = loadJSONFromAsset(context, s);
            Tour tour = gson.fromJson(jsonString, Tour.class);
            addLandmarksToTour(context, tour, tour.getPlaces());
            this.map.put(tour.getName(), tour);
        }
    }

    /**
     * gets the tour you desire by name
     * @param name name of the tour as in the JSON data file
     * @return
     */
    public Tour getByName(String name){
        if( name == null ){
            return null;
        }
        else{
            return this.map.get(name);
        }
    }

    /**
     * Helper function that converts the strings of the JSON file into actual landmarks
     * @param context current context (i.e. this)
     * @param tour current tour to look at
     * @param landmarks landmark strings the tour has
     */
    private void addLandmarksToTour(Context context, Tour tour, ArrayList<String> landmarks){
        LandmarkDatabase db = new LandmarkDatabase(context, "one by one");
        ArrayList<Landmark> landmarkArrayList = new ArrayList<>();
        for(String landmark : landmarks){
            landmarkArrayList.add(db.getByName(landmark));
            if(db.getByName(landmark)==null){
                Log.e("nullLandmark",landmark);
            }
        }
        tour.setLandmarks(landmarkArrayList);
    }

    /**
     *
     * @param context context of the current activity
     * @param filename name of the file being read
     * @return
     */
    public String loadJSONFromAsset(Context context, String filename) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
