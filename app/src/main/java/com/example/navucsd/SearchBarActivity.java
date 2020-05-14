package com.example.navucsd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchBarActivity extends AppCompatActivity {
    private SearchBarDB sbdatebase;
    private SearchView searchBar;
    private ChipGroup chipGroup;
    //private LinearLayout amenityLinearLayout;
    private static final String[] amenFilter=
            new String[]{"restroom","cafe","restaurant",
                    "busstop","parking"};
    ArrayList<String> filteredList=new ArrayList<>();

    //dynamic location list from database
    private ArrayList<Location> locationList = new ArrayList<>();
    //list of json file name
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bar);
        searchBar = (SearchView) findViewById(R.id.actual_search_bar);
        CardView cardplaces=(CardView)findViewById(R.id.placesCardView);
        CardView cardSearch=(CardView)findViewById(R.id.searchBarCardView);

        ListView searchPlaces=(ListView)findViewById(R.id.searchHintList);
        ArrayAdapter<String> placesAdapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                getResources( ).getStringArray(R.array.placesName));

        ColorDrawable divider = new ColorDrawable(this.getResources().getColor(R.color.divider));
        searchPlaces.setDivider(divider);
        searchPlaces.setDividerHeight(3);
        int placesNumber=getResources( ).getStringArray(R.array.placesName).length;

        sbdatebase=new SearchBarDB(this,"one by one");

        //get main context from json file
        Gson gson = new Gson();
        boolean[][] dbAmentityList=new boolean[FILELIST.length][5];
        String[] placesName=new String[FILELIST.length];
        String[] availability=new String[FILELIST.length];
        String[] distances=new String[FILELIST.length];
        for(int i = 0; i < this.FILELIST.length; i++) {
            String jsonString = sbdatebase.loadJSONFromAsset(this, this.FILELIST[i]);
            Location location = gson.fromJson(jsonString, Location.class);
            locationList.add(location);
            //get amenity list from location
            placesName[i]=location.name;
            for(int j=0;j<dbAmentityList[0].length;j++){
                dbAmentityList[i][j]=locationList.get(i).amenities.get(amenFilter[j]);
            }
            //TODO:fix the hard code
            availability[i]=getResources( ).getStringArray(R.array.availability)[i];
            distances[i]=getResources( ).getStringArray(R.array.distances)[i];

        }

        //initialize the main context of list
        SearchBarPlacesView placesAdaptor=new SearchBarPlacesView(this,
                placesName, availability,distances,dbAmentityList);
        searchPlaces.setAdapter(placesAdaptor);



        //first hide the suggestion listview
        searchPlaces.setVisibility(View.GONE);


        ArrayList<String> locationlist=new ArrayList<String>();
        ArrayList<String> origin=new ArrayList<String>();

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener( ) {
            // Override onQueryTextSubmit method which is call when submitquery is searched
            @Override
            public boolean onQueryTextSubmit(String query) {
                // If the list contains the search query
                // than filter the adapter
                // using the filter method
                // with the query as its argument
                ArrayList<String> mustGoArrayList=new ArrayList<>(R.array.placesName);
                if (mustGoArrayList.contains(query)) {
                    placesAdaptor.getFilter( ).filter(query);
                    //TODO: set to intent if needed

                }

                return false;
            }

            // This method is overridden to filter
            // the adapter according to a search query
            // when the user is typing search
            @Override
            public boolean onQueryTextChange(String s) {
                if(s.isEmpty()){
                    searchPlaces.setVisibility(View.GONE);
                }
                else searchPlaces.setVisibility(View.VISIBLE);

                placesAdaptor.getFilter( ).filter(s);

                // ---------------Change-----------------
                while(!origin.isEmpty()){
                    origin.remove(0);
                }
                // --------------------------------------
                // filteredList=placesAdapter.
                //set up clike item functionality
                searchPlaces.setOnItemClickListener(new ListView.OnItemClickListener( ) {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        //if user clicks the suggestion, auto complete the search bar
                        //searchBar.setQuery(placesAdaptor.getItem(i), true);
                        //hide the listview

                        //TODO: set to intent if needed
                    }
                });


                return false;
            }
        });


        ChipGroup chipGroup=(ChipGroup)findViewById(R.id.place_tags);



        //chipGroup's method can only apply to single selection mode
        //so this is a customize version for multi selection to get checked chips
        ArrayList<String> amenList=new ArrayList<>();
        for(int i=0;i<chipGroup.getChildCount();i++){
            Chip ameChip=(Chip)chipGroup.getChildAt(i);
            int index = i;
            ameChip.setOnCheckedChangeListener(new Chip.OnCheckedChangeListener( ) {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                    placesAdaptor.filtered = origin;
                    Log.i("Origin list", String.valueOf(origin.size()));

                    // ---------------Change-----------------
                    if(origin.isEmpty()){
                        for(int i=0;i<placesAdaptor.filtered.size();i++){
                            origin.add(placesAdaptor.filtered.get(i).toString());
                        }
                    }

                    while(!locationlist.isEmpty()){
                        locationlist.remove(0);
                    }
                    Log.i("Before list first", String.valueOf(locationlist.size()));

                    for(int i=0;i<origin.size();i++){
                        locationlist.add(origin.get(i));
                    }
                    // ----------------------------------------
                    if(ameChip.isChecked()){
                        amenList.add(amenFilter[index]);
                        Log.i("add to list", amenFilter[index]);
                    }
                    else{
                        amenList.remove(amenFilter[index]);
                    }
                    Log.i("filter ", Arrays.toString(placesAdaptor.filtered.toArray()));

                    //TODO: pass in amenList and placesAdaptor.filtered for database filtering
                    //extract arraylist of filtered places' name

//                    while(!locationlist.isEmpty()){
//                        locationlist.remove(0);
//                    }
//                    Log.i("Before list first", String.valueOf(locationlist.size()));
//
//                    for(int i=0;i<placesAdaptor.filtered.size();i++){
//                        locationlist.add(placesAdaptor.filtered.get(i).toString());
//                    }
//                    Log.i("After list first", String.valueOf(locationlist.size()));


                    ArrayList<Location> listOfLocations = sbdatebase.filter(locationlist, amenList);
                    ArrayList<PlacesDataClass> filteredAmen=new ArrayList<PlacesDataClass>();
                    //TODO SOLVE THE CONFLICT
                    //placesAdaptor.getFilter().filter(searchBar.getQuery());

                    for(int k=0;k<listOfLocations.size();k++){
                        Log.i("list",listOfLocations.get(k).name);
                        filteredAmen.add(new PlacesDataClass(listOfLocations.get(k)));
                    }

                    placesAdaptor.filtered=filteredAmen;
                    placesAdaptor.notifyDataSetChanged();
                    if(!amenList.isEmpty()) {
                        searchPlaces.setVisibility(View.VISIBLE);
                    }
                    else if(searchBar.getQuery().length()==0) {
                        searchPlaces.setVisibility(View.VISIBLE);
                    }
                }
            });
        }




    }


}
