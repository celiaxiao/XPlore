package com.UCSDTripleC.XPloreUCSD.database;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/*
 * Initialization of the database:
 * LandmarkDatabase(Context context, String order)
 * Order:
 * Case 1: order is "one by one", the constructor will load the location files  one by one
 * Case 2: order is "whole", which means constructor will load the location files which contain
 *         all of the locations.
 * Example(in onCreate function of SearchBarActivity):
 *
 * Case 1:
 * LandmarkDatabase database = new LandmarkDatabase(this, "one by one");
 *
 * Case 2:
 * LandmarkDatabase database = new LandmarkDatabase(this, "whole");
 */

/* Functions we have:
 * public Landmark getByName(String name)
 * public ArrayList<Pair<Landmark, Double>> nearestLocations(Pair<Double, Double>  userLocation, int num)
 * public double distant(Pair<Double, Double>  p1, Pair<Double, Double>  p2)
 * public ArrayList<Landmark> filter(ArrayList<String> locations, ArrayList<String> amen)
 * public ArrayList<Pair<Landmark, Double>> locationWithDistance(Pair<Double, Double>  userLocation)
 * public String loadJSONFromAsset(Context context, String filename)
 * public ArrayList<Pair<Landmark, Double>> filterWithDistance(ArrayList<String> locations, ArrayList<String> amen, Pair<Double, Double>  userLocation)
 * */



public class LandmarkDatabase {

    private static String LOCATIONS_JSON = "location/placesMin.json";
    private ArrayList<Landmark> list = new ArrayList<>();
    private HashMap<String, Landmark> map = new HashMap<>();

    /**
     * The tag used in log messages.
     */
    private static String TAG = LandmarkDatabase.class.getName();

    public static String[] FILELIST = new String[] {
            /*Change them in the future*/
            "location/64Degrees.json",
            "location/AtkinsonHall.json",
            "location/JSOE.json",
            "location/Geisel.json",
            "location/BiomedicalLibrary.json",
            "location/CanyonViewAquaticCenter.json",
            "location/CanyonVista.json",
            "location/ConradPrebysMusicCenter.json",
            "location/GalbraithHall.json",
            "location/MayerHall.json",
            "location/OceanviewRestaurant.json",
            "location/PetersonHall.json",
            "location/PriceCenter.json",
            "location/RadySchoolOfManagement.json",
            "location/SunGod.json",
            "location/WarrenBear.json",
            "location/DrSeussStatue.json",
            "location/MainGym.json",
            "location/JacobsBuilding.json",
            "location/UCSDBookstore.json",
            "location/YorkHall.json",
            "location/CareerServiceCenter.json",
            "location/RIMACGym.json",
            "location/PinesRoots.json",
            "location/CafeVentanas.json",
            "location/TheBistro.json",
            "location/StudentServicesCenter.json"
    };

    public LandmarkDatabase(Context context, String order){
        if(order.equals("one by one")){
            Gson gson = new Gson();
            for(int i = 0; i < this.FILELIST.length; i++){
                String jsonString = loadJSONFromAsset(context, this.FILELIST[i]);
                Landmark landmark = gson.fromJson(jsonString, Landmark.class);
                this.list.add(landmark);
                this.map.put(landmark.getName(), landmark);
            }
        }
        else if(order.equals("whole")){
            Gson gson = new Gson();
            //"test.json" should be changed in future.
            String jsonString = loadJSONFromAsset(context, "location/placesMin.json");
            Type arraylistType = new TypeToken<ArrayList<Landmark>>(){}.getType();
            this.list = gson.fromJson(jsonString, arraylistType);
            for(int i = 0; i < this.list.size(); i++){
                this.map.put(this.list.get(i).getName(), this.list.get(i));
            }
        }
    }


    /*
     * getByName(String name):
     * Example:
     * locations.add("name1"); // location name1 contain "parking", "busstop"
     * locations.add("name2"); // location name2 contain "parking", "cafe", "busstop"
     * locations.add("name3"); // location name3 contain nothing
     * locations.add("name4"); // location name4 contain "busstop"
     *
     * amenities.add("parking");
     * amenities.add("cafe");
     * amenities.add("busstop");
     *
     * LandmarkDatabase database = new LandmarkDatabase(this, "whole");
     * ArrayList<Landmark> listOfLocations = database.filter(locations, amenities);
     * Log.i("FILTERoooooo", database.getByName("name3").name);
     *
     * Output:
     * name3
     *
     *  */
    public Landmark getByName(String name){
        if( name == null ){
            return null;
        }
        else{
            return this.map.get(name);
        }
    }

    public Double getDistance(String locationName, Pair<Double, Double>  userLocation){
        Landmark landmark = getByName(locationName);
        if( landmark == null){
            return -1.0;
        }
        double latitude = landmark.getLatitude();
        double longitude = landmark.getLongitude();
        Pair<Double, Double> coordinate = new Pair<>(latitude, longitude);
        // TODO use latitude and longitude explicitly
        return distanceBetween(userLocation, coordinate);
    }

    /**
     * Loads locations from assets.
     *
     * @param context the context used to get assets
     * @return a list of {@link Landmark}s, or {@code null} on error
     */
    public static ArrayList<Landmark> getLocations(Context context) {
        try (BufferedReader reader = newBufferedReaderFromAsset(context, LOCATIONS_JSON)) {
            if (reader == null) return null;
            HashMap<String, Landmark> location_map = new Gson().fromJson(
                    reader,
                    new TypeToken<HashMap<String, Landmark>>(){}.getType()
            );
            return new ArrayList<Landmark>(location_map.values());
        } catch (JsonSyntaxException | IOException e) {
            // TODO proper error reporting
            // FIXME What's up with the Timber lint?
            Log.e(TAG, "failed to parse JSON file: \"" + LOCATIONS_JSON + "\"", e);
            return null;
        }
    }

    /**
     * Returns a {@link BufferedReader} of a file in the assets folder.
     *
     * @param context the context used to get assets
     * @param fileName the name of the file to load
     * @return a {@link BufferedReader} to the file, or {@code null} on error
     */
    private static BufferedReader newBufferedReaderFromAsset(Context context, String fileName) {
        try {
            return new BufferedReader(new InputStreamReader(
                    context.getAssets().open(fileName),
                    StandardCharsets.UTF_8
            ));
        } catch (IOException e) {
            // TODO proper error reporting
            // FIXME What's up with the Timber lint?
            Log.e(TAG, "failed to read file: \"" + fileName + "\"", e);
            return null;
        }
    }

    /*
     * userLocation: coordinate of the user userLocation.first should be Latitude and
     *               userLocation.second should be Longitude
     *
     * num: the number of n nearest Locations to the user.
     *
     * output: the ArrayList of Pair<Landmark, Double>, Pair.first are Landmark object and
     *         Pair.second are the distance between user and the locations.
     * */
    public ArrayList<Pair<Landmark, Double>> nearestLocations(Pair<Double, Double>  userLocation, int num){
        ArrayList<Pair<Landmark, Double>> nearestList = new ArrayList<>();
        for(int i = 0; i < this.list.size(); i++){
            Landmark landmark = list.get(i);
            double latitude = landmark.getLatitude();
            double longitude = landmark.getLongitude();
            Pair<Double, Double> coordinate = new Pair<>(latitude, longitude);
            // TODO use latitude and longitude explicitly
            double dist = distanceBetween(userLocation, coordinate);
            int origin = nearestList.size();
            for( int j = 0; j < nearestList.size(); j++ ){
                if( dist < nearestList.get(j).second ){
                    nearestList.add(j, new Pair<>(this.list.get(i), dist));
                    if(nearestList.size() > num){
                        nearestList.remove(num);
                    }
                    break;
                }
            }
            if( origin == nearestList.size() ){
                if( nearestList.size() < num ){
                    nearestList.add(new Pair<>(this.list.get(i), dist));
                }
            }
        }
        return nearestList;
    }

    /*
     * userLocation: coordinate of the user userLocation.first should be Latitude and
     *               userLocation.second should be Longitude
     *
     * output: the ArrayList of Pair<Landmark, Double>, which contains all the locations. Pair.first
     *         are Landmark object and Pair.second are the distance between user and the locations.
     */
    public ArrayList<Pair<Landmark, Double>> locationWithDistance(Pair<Double, Double> userLocation) {
        ArrayList<Pair<Landmark, Double>> distanceList = new ArrayList<>();
        for(int i = 0; i < this.list.size(); i++){
            Landmark landmark = list.get(i);
            double latitude = landmark.getLatitude();
            double longitude = landmark.getLongitude();
            Pair<Double, Double> coordinate = new Pair<>(latitude, longitude);
            // TODO use latitude and longitude explicitly
            double dist = distanceBetween(userLocation, coordinate);
            distanceList.add(new Pair<>(this.list.get(i), dist));
        }
        for( int i = 0; i < distanceList.size()-1; i++ ){
            for( int j = i+1; j < distanceList.size(); j++ ){
                if(distanceList.get(i).second > distanceList.get(j).second){
                    Collections.swap(distanceList, i, j);
                }
            }
        }
        return distanceList;
    }

    /**
     * Calculates the distance between two points in meters.
     *
     * @param p1 the coordinates of the first point in (latitude, longitude) format
     * @param p2 the coordinates of the second point in (latitude, longitude) format
     * @return distance between the two points in meters
     */
    public static double distanceBetween(Pair<Double, Double> p1, Pair<Double, Double> p2) {
        float[] result = new float[1];
        android.location.Location.distanceBetween(p1.first, p1.second, p2.first, p2.second, result);
        return result[0];
    }

    /*
     * filter function:
     * This function required two parameter:
     * ArrayList<String> locations, which contain the names of the locations
     * ArrayList<String> amenities, which contain the amenities(String)
     *
     * Output:
     * ArrayList<Landmark> listOfLocations
     *
     * Example:
     * locations.add("name1"); // location name1 contain "parking", "busstop"
     * locations.add("name2"); // location name2 contain "parking", "cafe", "busstop"
     * locations.add("name3"); // location name3 contain nothing
     * locations.add("name4"); // location name4 contain "busstop"
     *
     * amenities.add("parking");
     * amenities.add("cafe");
     * amenities.add("busstop");
     *
     * LandmarkDatabase database = new LandmarkDatabase(this, "whole");
     * ArrayList<Landmark> listOfLocations = database.filter(locations, amenities);
     *
     * Log.i("FILTERoooooo", "After filtering: ");
     * for(int i = 0; i < locList.size(); i++){
     *		Log.i("FILTERoooooo", locList.get(i).name);
     * 	}
     *
     * Output:
     * After filtering:
     * name2
     * name1
     * name4
     */
    public ArrayList<Landmark> filter(ArrayList<String> locations, ArrayList<String> amen){
        ArrayList<Pair<Landmark, Integer>> trackingList = new ArrayList<>();
        ArrayList<Landmark> outputList = new ArrayList<>();
        ArrayList<String> amenities = new ArrayList<>();
        for( int i = 0; i < amen.size(); i++ ){
            if(amen.get(i) != null){
                amenities.add(amen.get(i));
            }
        }
        if(amenities.size() == 0){
            for( int i = 0; i < locations.size(); i++){
                Landmark landmark = this.getByName(locations.get(i));
                if(landmark != null){
                    outputList.add(landmark);
                }
            }
        }
        else{
            for( int i = 0; i < locations.size(); i++){
                Landmark landmark = this.getByName(locations.get(i));
                if( landmark != null ){
                    int weight = 0;
                    for( int z = 0; z < amenities.size(); z++ ){
                        // FIXME potential null pointer
                        if( landmark.getAmenities().get(amenities.get(z)) == true ){
                            weight = (weight*10) + 5 - z;
                        }
                    }
                    if( weight > 0 ){
                        if( outputList.isEmpty()){
                            trackingList.add(new Pair<>(landmark, weight));
                            outputList.add(landmark);
                        }
                        else{
                            int origin = trackingList.size();
                            for( int j = 0; j < trackingList.size(); j++ ){
                                if( weight >= trackingList.get(j).second ){
                                    trackingList.add(j, new Pair<>(landmark, weight));
                                    outputList.add(j, landmark);
                                    break;
                                }
                            }
                            if( origin == trackingList.size() ){
                                trackingList.add( new Pair<>(landmark, weight) );
                                outputList.add(landmark);
                            }
                        }
                    }
                }
            }
        }
        return outputList;
    }



    public ArrayList<Pair<Landmark, Double>> filterWithDistance(ArrayList<String> locations, ArrayList<String> amen, Pair<Double, Double>  userLocation){
        ArrayList<Pair<Landmark, Integer>> trackingList = new ArrayList<>();
        ArrayList<Pair<Landmark, Double>> outputList = new ArrayList<>();
        ArrayList<String> amenities = new ArrayList<>();
        for( int i = 0; i < locations.size()-1; i++ ){
            for( int j = i+1; j < locations.size(); j++ ){
                if(getDistance(locations.get(i), userLocation) > getDistance(locations.get(j), userLocation)){
                    Collections.swap(locations, i, j);
                }
            }
        }
        for( int i = 0; i < amen.size(); i++ ){
            if(amen.get(i) != null){
                amenities.add(amen.get(i));
            }
        }
        if(amenities.size() == 0){
            for( int i = 0; i < locations.size(); i++){
                Landmark landmark = this.getByName(locations.get(i));
                if(landmark != null){
                    Pair<Landmark, Double> item = new Pair<>(landmark, getDistance(locations.get(i), userLocation));
                    outputList.add(item);
                }
            }
        }
        else{
            for( int i = 0; i < locations.size(); i++){
                Landmark landmark = this.getByName(locations.get(i));
                if( landmark != null ){
                    int weight = 0;
                    for( int z = 0; z < amenities.size(); z++ ){
                        // FIXME potential null pointer
                        if( landmark.getAmenities().get(amenities.get(z)) == true ){
//                            weight = (weight*10) + 5 - z;
                            weight += 1;
                        }
                    }
                    if( weight > 0 ){
                        if( outputList.isEmpty()){
                            trackingList.add(new Pair<>(landmark, weight));
                            Pair<Landmark, Double> item = new Pair<>(landmark, getDistance(locations.get(i), userLocation));
                            outputList.add(item);
                        }
                        else{
                            int origin = trackingList.size();
                            for( int j = 0; j < trackingList.size(); j++ ){
                                if( weight == amenities.size() && weight > trackingList.get(j).second ){
                                    trackingList.add(j, new Pair<>(landmark, weight));
                                    Pair<Landmark, Double> item = new Pair<>(landmark, getDistance(locations.get(i), userLocation));
                                    outputList.add(j, item);
                                    break;
                                }
                            }
                            if( origin == trackingList.size() ){
                                trackingList.add( new Pair<>(landmark, weight) );
                                Pair<Landmark, Double> item = new Pair<>(landmark, getDistance(locations.get(i), userLocation));
                                outputList.add(item);
                            }
                        }
                    }
                }
            }
        }
        return outputList;
    }


    /*
		Function that reads in a file and then spits out their json string
	 */
    public String loadJSONFromAsset(Context context, String filename) {
        String json = null;
        Log.d("WHAT", "I'm in this function call");
        try {
            Log.d("WHAT", "in try block");
            InputStream is = context.getAssets().open(filename);
            Log.d("WHAT","after opening file");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            Log.d("WHAT", "finished reading buffer");
            json = new String(buffer, "UTF-8");
            Log.d("WHAT", json);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
