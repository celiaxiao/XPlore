package com.example.navucsd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.navucsd.database.Location;
import com.google.android.material.chip.Chip;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

public class SearchBarActivity extends AppCompatActivity  {
    private SearchBarDB sbdatebase;
    private SearchView searchBar;
    private CardView searchResultsCard;
    private android.location.Location currentLocation;
    //private LinearLayout amenityLinearLayout;
    private static final String[] amenFilter =
            new String[]{"restroom", "cafe", "restaurant",
                    "busstop", "parking"};
    private TextView chipBadge;
    private TextView noResultsFoundText;
    //dynamic location list from database
    private ArrayList<Location> locationList = new ArrayList<>( );
    ArrayList<Pair<Location, Double>> distancePair;
    ArrayList<String> amenList; //list of selected amenities
    //list of json file name
    private static String[] FILELIST = new String[]{
            /*Change them in the future*/
            "64Degrees.json",
            "AtkinsonHall.json",
            "JSOE.json",
            "Geisel.json",
            "BiomedicalLibrary.json",
            "CanyonViewAquaticCenter.json",
            "CanyonVista.json",
            "ConradPrebysMusicCenter.json",
            "GalbraithHall.json",
            "MayerHall.json",
            "OceanviewRestaurant.json",
            "PetersonHall.json",
            "PriceCenter.json",
            "RadySchoolOfManagement.json",
            "SunGod.json",
            "WarrenBear.json"
    };

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        } else {
            return true;// Permission has already been granted }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bar);
        searchBar = (SearchView) findViewById(R.id.actual_search_bar);
        searchResultsCard = (CardView) findViewById(R.id.placesCardView);
        chipBadge=(TextView)findViewById(R.id.chipBadge);
        noResultsFoundText=(TextView)findViewById(R.id.noResultFoundText);
        //first hide the badge, since no selection has been made
        chipBadge.setVisibility(View.GONE);
        //also hide the no results text, as no input has been made
        noResultsFoundText.setVisibility(View.GONE);
        ListView searchPlaces = (ListView) findViewById(R.id.searchHintList);
        ColorDrawable divider = new ColorDrawable(this.getResources( ).getColor(R.color.divider));
        searchPlaces.setDivider(divider);
        searchPlaces.setDividerHeight(3);
        searchBar.setIconifiedByDefault(false);
        searchBar.requestFocus();
        int placesNumber = getResources( ).getStringArray(R.array.placesName).length;

        sbdatebase = new SearchBarDB(this, "one by one");

        //get main context from json file
        Gson gson = new Gson( );
        boolean[][] dbAmentityList = new boolean[FILELIST.length][5];
        String[] placesName = new String[FILELIST.length];
        String[] availability = new String[FILELIST.length];
        String[] distances = new String[FILELIST.length];

        //check location permission, if no, hide the distances
         currentLocation = null;
        if(checkPermission()) {
            //get the current location
            currentLocation =
                    GpsUtil.getInstance(SearchBarActivity.this).getLastLocation( );

        }if(currentLocation!=null){
            distancePair=
                    sbdatebase.locationWithDistance(
                            new Pair<>(currentLocation.getLatitude(),currentLocation.getLongitude()));
            for(int i = 0; i < distancePair.size(); i++) {
                Location location = distancePair.get(i).first;
                locationList.add(location);
                //get amenity list from location
                placesName[i] = location.name;
                for (int j = 0; j < dbAmentityList[0].length; j++) {
                    dbAmentityList[i][j] = locationList.get(i).amenities.get(amenFilter[j]);
                }
                //get the distance, current unit is meter
                distances[i] = distanceToString(distancePair.get(i).second);
                //temporarily hide the availability
                availability[i] = "";
            }
        }
        else {
            //TODO:if no permission, hide the distance
            for (int i = 0; i < this.FILELIST.length; i++) {
                String jsonString = sbdatebase.loadJSONFromAsset(this, this.FILELIST[i]);
                Location location = gson.fromJson(jsonString, Location.class);
                locationList.add(location);
                //get amenity list from location
                placesName[i] = location.name;
                for (int j = 0; j < dbAmentityList[0].length; j++) {
                    dbAmentityList[i][j] = locationList.get(i).amenities.get(amenFilter[j]);
                }
                //hide the distance
                distances[i] = "";
                //temporarily hide the availability
                availability[i] = "";
            }
        }



        //initialize the main context of list
        SearchBarPlacesView placesAdaptor = new SearchBarPlacesView(this,
                placesName, availability, distances, dbAmentityList);
        searchPlaces.setAdapter(placesAdaptor);


        //first hide the suggestion listview
        searchResultsCard.setVisibility(View.GONE);


        ArrayList<String> locationlist = new ArrayList<String>( );
        ArrayList<String> origin = new ArrayList<String>( );

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener( ) {
            // Override onQueryTextSubmit method which is call when submitquery is searched
            @Override
            public boolean onQueryTextSubmit(String query) {
                // If the list contains the search query
                // than filter the adapter
                // using the filter method
                // with the query as its argument
                //placesAdaptor.getFilter( ).filter(query);
                //TODO: set to intent if needed
                /*Intent intent = new Intent(getApplicationContext(),LandmarkDetailsActivity.class);

                intent.putExtra("placeName", "Geisel Library");
                //hard code to geisel details page
                startActivity(intent);*/

                return false;
            }

            // This method is overridden to filter
            // the adapter according to a search query
            // when the user is typing search
            @Override
            public boolean onQueryTextChange(String s) {
                if(s.isEmpty() && amenList.isEmpty()){
                    searchResultsCard.setVisibility(View.GONE);
                }else {
                    searchResultsCard.setVisibility(View.VISIBLE);
                }
                placesAdaptor.getFilter( ).filter(s);
                //if no results available, hide the result card
                //self-checking if there is any results, as filter results only available after
                //this method ends
                boolean noResult=true;
                ArrayList<PlacesDataClass> tempFilter=placesAdaptor.getFiltered( );
                if(tempFilter.isEmpty()) {
                    ArrayList<String> temp_filter=new ArrayList<String>(Arrays.asList(placesName));
                    for(String a:temp_filter){
                        if(a.toLowerCase().contains(s.toLowerCase())){
                            noResult=false;
                        }
                    }
                }else {
                    for (PlacesDataClass object : tempFilter) {
                        // the filtering itself:
                        if (object.toString( ).toLowerCase( ).contains(s.toLowerCase( )))
                            noResult=false;
                    }
                }//if no results available then display the no results text
                if (noResult) {
                    noResultsFoundText.setVisibility(View.VISIBLE);
                } else noResultsFoundText.setVisibility(View.GONE);
                Log.i("results number", "query: "+s+"; no result: "+noResult);
                // ---------------Change-----------------
                while (!origin.isEmpty( )) {
                    origin.remove(0);
                }

                return false;
            }
        });

        //set on listview clicked redirection to landmark details page
        searchPlaces.setOnItemClickListener(new ListView.OnItemClickListener( ) {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //if user clicks the suggestion, auto complete the search bar
                //searchBar.setQuery(placesAdaptor.getItem(i), true);
                //hide the listview
                Log.i("click",""+placesAdaptor.getItem(i).toString());
                //TODO: set intent if needed
                //hard code to geisel details page

                Intent intent = new Intent(getApplicationContext(),LandmarkDetailsActivity.class);
                intent.putExtra("placeName", ""+placesAdaptor.getItem(i).toString());
                //hard code to geisel details page
                startActivity(intent);
            }
        });

        //get the checked list from 5 filter chips
        Chip[] chipgroup=new Chip[5];
        chipgroup[0]=(Chip)findViewById(R.id.bathroom) ;
        chipgroup[1]=(Chip)findViewById(R.id.cafe) ;
        chipgroup[2]=(Chip)findViewById(R.id.resturant) ;
        chipgroup[3]=(Chip)findViewById(R.id.bus) ;
        chipgroup[4]=(Chip)findViewById(R.id.parking) ;
        amenList = new ArrayList<String>( );
        for (int i = 0; i < chipgroup.length; i++) {
            Chip ameChip =  chipgroup[i];
            int index = i;
            ameChip.setOnCheckedChangeListener(new Chip.OnCheckedChangeListener( ) {
                /**
                 * handle the amenity chip selection change and refilter the list
                 * @param compoundButton
                 * @param b
                 */
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                    placesAdaptor.filtered = origin;
                    Log.i("Origin list", String.valueOf(origin.size( )));

                    // ---------------Change-----------------
                    if (origin.isEmpty( )) {
                        for (int i = 0; i < placesAdaptor.filtered.size( ); i++) {
                            origin.add(placesAdaptor.filtered.get(i).toString( ));
                        }
                    }

                    while (!locationlist.isEmpty( )) {
                        locationlist.remove(0);
                    }
                    Log.i("Before list first", String.valueOf(locationlist.size( )));

                    for (int i = 0; i < origin.size( ); i++) {
                        locationlist.add(origin.get(i));
                    }
                    // ----------------------------------------
                    if (ameChip.isChecked( )) {
                        amenList.add(amenFilter[index]);
                        Log.i("add to list", amenFilter[index]);
                    } else {
                        amenList.remove(amenFilter[index]);
                    }
                    Log.i("filter ", Arrays.toString(placesAdaptor.filtered.toArray( )));

                    ArrayList<PlacesDataClass> filteredAmen;
                    if (checkPermission() && currentLocation!=null) {
                        ArrayList<Pair<Location, Double>> listOfLocations = sbdatebase.filterWithDistance(
                                locationlist, amenList,
                                new Pair<>(currentLocation.getLatitude( ), currentLocation.getLongitude( )));

                        filteredAmen = new ArrayList<PlacesDataClass>( );

                        for (int k = 0; k < listOfLocations.size( ); k++) {
                            Log.i("list", listOfLocations.get(k).first.name);
                            filteredAmen.add(new PlacesDataClass(listOfLocations.get(k)));
                        }
                    }
                    else{
                        ArrayList<Location> listOfLocations=sbdatebase.filter(locationlist, amenList);
                        filteredAmen = new ArrayList<PlacesDataClass>( );

                        for (int k = 0; k < listOfLocations.size( ); k++) {
                            filteredAmen.add(new PlacesDataClass(listOfLocations.get(k)));
                        }
                    }
                    placesAdaptor.filtered = filteredAmen;
                    placesAdaptor.notifyDataSetChanged( );
                    //if no input or selection made to the chips, then hide the results
                    if (!amenList.isEmpty( )) {
                        searchResultsCard.setVisibility(View.VISIBLE);
                    } else if (amenList.isEmpty( ) && searchBar.getQuery( ).length( ) == 0) {
                        searchResultsCard.setVisibility(View.GONE);
                    }

                    //if no chip is selected, hide the badge
                    if(amenList.isEmpty()) chipBadge.setVisibility(View.GONE);
                    else {
                        chipBadge.setText(""+amenList.size());
                        chipBadge.setVisibility(View.VISIBLE);
                    }
                }

            });
        }


    }

    static String distanceToString(Double distance){
        //if less than 0.1 miles, unit is feet
        if(distance.compareTo(new Double(160.934))<0){
            return Math.round(distance*3.28084)+" ft";
        }
        //else convert to miles, round to one decimal value
        return Math.round(distance*0.000621371*10)/10.0+" mi";
    }


}
