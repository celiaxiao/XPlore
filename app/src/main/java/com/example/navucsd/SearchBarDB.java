package com.example.navucsd;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.RequiresApi;

import com.example.navucsd.database.Location;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/*
 * Initialization of the database:
 * SearchBarDB(Context context, String order)
 * Order:
 * Case 1: order is "one by one", the constructor will load the location files  one by one
 * Case 2: order is "whole", which means constructor will load the location files which contain
 *         all of the locations.
 * Example(in onCreate function of SearchBarActivity):
 *
 * Case 1:
 * SearchBarDB database = new SearchBarDB(this, "one by one");
 *
 * Case 2:
 * SearchBarDB database = new SearchBarDB(this, "whole");
 */

/* Functions we have:
 * public Location getByName(String name)
 * public ArrayList<Pair<Location, Double>> nearestLocations(Pair<Double, Double>  userLocation, int num)
 * public double distant(Pair<Double, Double>  p1, Pair<Double, Double>  p2)
 * public ArrayList<Location> filter(ArrayList<String> locations, ArrayList<String> amen)
 * public ArrayList<Pair<Location, Double>> locationWithDistance(Pair<Double, Double>  userLocation)
 * public String loadJSONFromAsset(Context context, String filename)
 * public ArrayList<Pair<Location, Double>> filterWithDistance(ArrayList<String> locations, ArrayList<String> amen, Pair<Double, Double>  userLocation)
 * */



public class SearchBarDB {

    private ArrayList<Location> list = new ArrayList<>();
    private HashMap<String, Location> map = new HashMap<>();

    private static String[] FILELIST = new String[] {
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
            "location/MainGym.json"
    };

    SearchBarDB(Context context, String order){
        if(order.equals("one by one")){
            Gson gson = new Gson();
            for(int i = 0; i < this.FILELIST.length; i++){
                String jsonString = loadJSONFromAsset(context, this.FILELIST[i]);
                Location location = gson.fromJson(jsonString, Location.class);
                this.list.add(location);
                this.map.put(location.name, location);
            }
        }
        else if(order.equals("whole")){
            Gson gson = new Gson();
            //"test.json" should be changed in future.
            String jsonString = loadJSONFromAsset(context, "location/test.json");
            Type arraylistType = new TypeToken<ArrayList<Location>>(){}.getType();
            this.list = gson.fromJson(jsonString, arraylistType);
            for(int i = 0; i < this.list.size(); i++){
                this.map.put(this.list.get(i).name, this.list.get(i));
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
     * SearchBarDB database = new SearchBarDB(this, "whole");
     * ArrayList<Location> listOfLocations = database.filter(locations, amenities);
     * Log.i("FILTERoooooo", database.getByName("name3").name);
     *
     * Output:
     * name3
     *
     *  */
    public Location getByName(String name){
        if( name == null ){
            return null;
        }
        else{
            return this.map.get(name);
        }
    }

    public Double getDistance(String locationName, Pair<Double, Double>  userLocation){
        Location location = getByName(locationName);
        if( location == null){
            return -1.0;
        }
        String first = (String) location.coordinates.first;
        String second = (String) location.coordinates.second;
        Pair locationCoor = new Pair<Double, Double>(Double.parseDouble(first), Double.parseDouble(second));
        double dist = this.distanceBetween(userLocation, locationCoor);
        return dist;
    }


    /*
     * userLocation: coordinate of the user userLocation.first should be Latitude and
     *               userLocation.second should be Longitude
     *
     * num: the number of n nearest Locations to the user.
     *
     * output: the ArrayList of Pair<Location, Double>, Pair.first are Location object and
     *         Pair.second are the distance between user and the locations.
     * */
    public ArrayList<Pair<Location, Double>> nearestLocations(Pair<Double, Double>  userLocation, int num){
        ArrayList<Pair<Location, Double>> nearestList = new ArrayList<>();
        for(int i = 0; i < this.list.size(); i++){
            String first = (String) this.list.get(i).coordinates.first;
            String second = (String) this.list.get(i).coordinates.second;
            Pair locationCoor = new Pair<Double, Double>(Double.parseDouble(first), Double.parseDouble(second));
            double dist = this.distanceBetween(userLocation, locationCoor);
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
     * output: the ArrayList of Pair<Location, Double>, which contains all the locations. Pair.first
     *         are Location object and Pair.second are the distance between user and the locations.
     */
    public ArrayList<Pair<Location, Double>> locationWithDistance(Pair<Double, Double> userLocation) {
        ArrayList<Pair<Location, Double>> distanceList = new ArrayList<>();
        for(int i = 0; i < this.list.size(); i++){
            String first = (String) this.list.get(i).coordinates.first;
            String second = (String) this.list.get(i).coordinates.second;
            Pair<Double, Double> locationCoor = new Pair<>(
                Double.parseDouble(first),
                Double.parseDouble(second)
            );
            double dist = this.distanceBetween(userLocation, locationCoor);
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
     * ArrayList<Location> listOfLocations
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
     * SearchBarDB database = new SearchBarDB(this, "whole");
     * ArrayList<Location> listOfLocations = database.filter(locations, amenities);
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
    public ArrayList<Location> filter(ArrayList<String> locations, ArrayList<String> amen){
        ArrayList<Pair<Location, Integer>> trackingList = new ArrayList<>();
        ArrayList<Location> outputList = new ArrayList<>();
        ArrayList<String> amenities = new ArrayList<>();
        for( int i = 0; i < amen.size(); i++ ){
            if(amen.get(i) != null){
                amenities.add(amen.get(i));
            }
        }
        if(amenities.size() == 0){
            for( int i = 0; i < locations.size(); i++){
                Location location = this.getByName(locations.get(i));
                if(location != null){
                    outputList.add(location);
                }
            }
        }
        else{
            for( int i = 0; i < locations.size(); i++){
                Location location = this.getByName(locations.get(i));
                if( location != null ){
                    int weight = 0;
                    for( int z = 0; z < amenities.size(); z++ ){
                        if( location.amenities.get(amenities.get(z)) == true ){
                            weight = (weight*10) + 5 - z;
                        }
                    }
                    if( weight > 0 ){
                        if( outputList.isEmpty()){
                            trackingList.add(new Pair<>(location, weight));
                            outputList.add(location);
                        }
                        else{
                            int origin = trackingList.size();
                            for( int j = 0; j < trackingList.size(); j++ ){
                                if( weight >= trackingList.get(j).second ){
                                    trackingList.add(j, new Pair<>(location, weight));
                                    outputList.add(j, location);
                                    break;
                                }
                            }
                            if( origin == trackingList.size() ){
                                trackingList.add( new Pair<>(location, weight) );
                                outputList.add( location );
                            }
                        }
                    }
                }
            }
        }
        return outputList;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<Pair<Location, Double>> filterWithDistance(ArrayList<String> locations, ArrayList<String> amen, Pair<Double, Double>  userLocation){
        ArrayList<Pair<Location, Integer>> trackingList = new ArrayList<>();
        ArrayList<Pair<Location, Double>> outputList = new ArrayList<>();
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
                Location location = this.getByName(locations.get(i));
                if(location != null){
                    Pair<Location, Double> item = new Pair<>(location, getDistance(locations.get(i), userLocation));
                    outputList.add(item);
                }
            }
        }
        else{
            for( int i = 0; i < locations.size(); i++){
                Location location = this.getByName(locations.get(i));
                if( location != null ){
                    int weight = 0;
                    for( int z = 0; z < amenities.size(); z++ ){
                        if( location.amenities.get(amenities.get(z)) == true ){
//                            weight = (weight*10) + 5 - z;
                            weight += 1;
                        }
                    }
                    if( weight > 0 ){
                        if( outputList.isEmpty()){
                            trackingList.add(new Pair<>(location, weight));
                            Pair<Location, Double> item = new Pair<>(location, getDistance(locations.get(i), userLocation));
                            outputList.add(item);
                        }
                        else{
                            int origin = trackingList.size();
                            for( int j = 0; j < trackingList.size(); j++ ){
                                if( weight == amenities.size() && weight > trackingList.get(j).second ){
                                    trackingList.add(j, new Pair<>(location, weight));
                                    Pair<Location, Double> item = new Pair<>(location, getDistance(locations.get(i), userLocation));
                                    outputList.add(j, item);
                                    break;
                                }
                            }
                            if( origin == trackingList.size() ){
                                trackingList.add( new Pair<>(location, weight) );
                                Pair<Location, Double> item = new Pair<>(location, getDistance(locations.get(i), userLocation));
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
