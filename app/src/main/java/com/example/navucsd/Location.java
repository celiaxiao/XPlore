package com.example.navucsd;

import com.example.navucsd.R;

import android.content.Context;
import android.content.res.Resources;
import android.util.Pair;

public class Location {

    public  String name;
    private String tags;
    private Pair coordinates;
    private String description;
    private Location relatedLocation;

    public Location(String locName, Context context){
        this.name = locName;
        //TODO: I HAVE NO FUCKING CLUE IF THIS WORKS OR NOT, TAKEN FROM THIS PAGE
        //  https://stackoverflow.com/questions/7493287/android-how-do-i-get-string-from-resources-using-its-name
        Resources res = context.getResources();
        //gets the tag from the strings.xml file
        this.tags = res.getString(res.getIdentifier(locName+"tag", "string", context.getPackageName()));
        //Coordinates are pair so ned to think aof another way to get it
        //this.coordinates = res.getString(res.getIdentifier(locName+"tag", "string", context.getPackageName()));
        this.description = res.getString(res.getIdentifier(locName+"tag", "string", context.getPackageName()));
        //i want to die
    }
}
