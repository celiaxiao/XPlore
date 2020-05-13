package com.example.navucsd;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
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
 *
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
 *
 * getByName(String name):
 * Example:
 * Log.i("FILTERoooooo", database.getByName("name3").name);
 *
 * Output:
 * name3
 *
 *  */

public class SearchBarDB {

    private ArrayList<Location> list = new ArrayList<>();
    private HashMap<String, Location> map = new HashMap<>();

    private  static String[] FILELIST = new String[]{
            /*Change them in the future*/
            "64Degrees.json",
            "AtkinsonHall.json",
            "JSOE.json",
            "Geisel.json",
            "BiomedicalLibrary.json",
            "CanyonViewAquaticCenter.json",
            "CanyonVista.json",
            "ConradPrebysMusicCenter.json",
            "GalbraithHall.json"
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
            String jsonString = loadJSONFromAsset(context, "test.json");
            Type arraylistType = new TypeToken<ArrayList<Location>>(){}.getType();
            this.list = gson.fromJson(jsonString, arraylistType);
            for(int i = 0; i < this.list.size(); i++){
                this.map.put(this.list.get(i).name, this.list.get(i));
            }
        }
    }

    public Location getByName(String name){
        return this.map.get(name);
    }


    public ArrayList<Location> filter(ArrayList<String> locations, ArrayList<String> amenities){
        ArrayList<Pair<Location, Integer>> trackingList = new ArrayList<>();
        ArrayList<Location> outputList = new ArrayList<>();
        for(int i = 0; i < this.list.size(); i++){
            if(locations.contains(this.list.get(i).name) == true){
                int weight = 0;
                for( int z = 0; z < amenities.size(); z++ ){
                    if( this.list.get(i).amenities.get(amenities.get(z)) == true ){
                       weight = (weight*10) + 5 - z;
                    }
                }
                if( weight > 0 ){
                    if( outputList.isEmpty()){
                        trackingList.add(new Pair<>(this.list.get(i), weight));
                        outputList.add(this.list.get(i));
                    }
                    else{
                        int origin = trackingList.size();
                        for( int j = 0; j < trackingList.size(); j++ ){
                            if( weight >= trackingList.get(j).second ){
                                trackingList.add(j, new Pair<>(this.list.get(i), weight));
                                outputList.add(j, this.list.get(i));
                                break;
                            }
                        }
                        if( origin == trackingList.size() ){
                            trackingList.add( new Pair<>(this.list.get(i), weight) );
                            outputList.add( this.list.get(i) );
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
