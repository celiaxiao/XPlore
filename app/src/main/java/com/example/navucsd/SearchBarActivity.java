package com.example.navucsd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;

public class SearchBarActivity extends AppCompatActivity {

    private SearchView searchBar;
    private ChipGroup chipGroup;
    private LinearLayout amenityLinearLayout;
    private static final String[] amenFilter=
            new String[]{"Amentity_bathromm","Amentity_cafe","Amentity_resturant",
                    "Amentity_bus","Amentity_parking"};


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
        //add title to the lisy
        /*View headerView = getLayoutInflater().inflate(R.layout.search_bar_list_title, null);
        searchPlaces.addHeaderView(headerView);*/
        ColorDrawable divider = new ColorDrawable(this.getResources().getColor(R.color.divider));
        searchPlaces.setDivider(divider);
        searchPlaces.setDividerHeight(3);
        int placesNumber=getResources( ).getStringArray(R.array.placesName).length;
        boolean[][] HardcodeAmentityList=new boolean[placesNumber][placesNumber];
        for(int i=0;i<HardcodeAmentityList.length;i++){
            for(int j=0;j<HardcodeAmentityList[0].length;j++){
                if(j%2==0) HardcodeAmentityList[i][j]=true;
            }
        }

        //initialize the main context of list
        SearchBarPlacesView placesAdaptor=new SearchBarPlacesView(this,
                getResources( ).getStringArray(R.array.placesName),
                getResources( ).getStringArray(R.array.availability),
                getResources( ).getStringArray(R.array.distances),HardcodeAmentityList);
        searchPlaces.setAdapter(placesAdaptor);

        //first hide the suggestion listview
        searchPlaces.setVisibility(View.GONE);

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

        //set up amentities button
        ImageView bathroomIcon=(ImageView)findViewById(R.id.bathroomIcon);
        ImageView resturantIcon=(ImageView)findViewById(R.id.resturantIcon);
        ImageView cafeIcon=(ImageView)findViewById(R.id.cafeIcon);
        ImageView busStationIcon=(ImageView)findViewById(R.id.busStationIcon);
        ImageView parkingIcon=(ImageView)findViewById(R.id.parkingIcon);

        int[] checkedIcon=new int[]{R.drawable.b1, R.drawable.b2,
                R.drawable.b3, R.drawable.b4, R.drawable.b5};
        chipGroup=(ChipGroup)findViewById(R.id.place_tags);
        Chip bathroom=findViewById(R.id.bathroom);


        chipGroup.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {

                ArrayList<String> checkedAmenity=new ArrayList<>();
                ChipGroup chg = chipGroup;
                int chipsCount = chg.getChildCount();
                if (chipsCount == 0) {
                    //
                } else {
                    int i = 0;
                    while (i < chipsCount) {
                        Chip chip = (Chip) chg.getChildAt(i);
                        if (chip.isChecked() ) {
                            //this is the checked amenity
                            checkedAmenity.add(amenFilter[i]);

                        }
                        i++;
                    };
                    //this is the filtered list
                    ArrayList<String> filteredList=new ArrayList<>();
                    for(int j=0;j<placesAdapter.getCount();j++){
                        filteredList.add(placesAdapter.getItem(j));
                    }
                }



            }
        });

    }

}
