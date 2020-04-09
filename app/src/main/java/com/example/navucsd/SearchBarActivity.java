package com.example.navucsd;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

public class SearchBarActivity extends AppCompatActivity {

    private SearchView searchBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bar);
        searchBar = (SearchView) findViewById(R.id.actual_search_bar);
        ListView searchPlaces=(ListView)findViewById(R.id.searchHintList);
        ArrayAdapter<String> placesAdapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                getResources( ).getStringArray(R.array.placesName));
        //add title to the lisy
        View headerView = getLayoutInflater().inflate(R.layout.search_bar_list_title, null);
        searchPlaces.addHeaderView(headerView);
        //initialize the main context of list
        SearchBarPlacesView placesAdaptor=new SearchBarPlacesView(this,
                getResources( ).getStringArray(R.array.placesName),
                getResources( ).getStringArray(R.array.availability),
                getResources( ).getStringArray(R.array.distances));
        searchPlaces.setAdapter(placesAdaptor);



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

    }

}
