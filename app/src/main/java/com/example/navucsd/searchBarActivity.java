package com.example.navucsd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import java.util.ArrayList;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


//import com.google.android.libraries.places.api.net.PlacesClient;



public class searchBarActivity extends AppCompatActivity {


    ArrayAdapter<String> LocationListAdapter;
    ArrayList<String> mustGoArrayList;
    SearchView searchBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bar);

        //set up the string list that appears in the search bar hint
        String[] mustGoList = getResources( ).getStringArray(R.array.list_of_must_go);
        mustGoArrayList = new ArrayList<>( );
        for (String i : mustGoList) {
            mustGoArrayList.add(i);
        }
        LocationListAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mustGoList);

        ListView searchHintList = (ListView) findViewById(R.id.searchHintList);
        searchHintList.setAdapter(LocationListAdapter);

        searchBar = (SearchView) findViewById(R.id.search_bar);
        //first hide the suggestion listview
        searchHintList.setVisibility(View.GONE);



        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener( ) {
            // Override onQueryTextSubmit method
            // which is call
            // when submitquery is searched

            @Override
            public boolean onQueryTextSubmit(String query) {
                // If the list contains the search query
                // than filter the adapter
                // using the filter method
                // with the query as its argument
                if (mustGoArrayList.contains(query)) {
                    LocationListAdapter.getFilter( ).filter(query);
                    //TODO: set to intent if needed
                } else {
                    // Search query not found in List View
                    Toast
                            .makeText(searchBarActivity.this,
                                    "Not found",
                                    Toast.LENGTH_LONG)
                            .show( );
                }
                return false;
            }

            // This method is overridden to filter
            // the adapter according to a search query
            // when the user is typing search
            @Override
            public boolean onQueryTextChange(String s) {
                if(s.isEmpty()){
                    searchHintList.setVisibility(View.GONE);
                }
                else searchHintList.setVisibility(View.VISIBLE);
                LocationListAdapter.getFilter( ).filter(s);

                //set up clike item functionality
                searchHintList.setOnItemClickListener(new ListView.OnItemClickListener( ) {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //it user clicks the suggestion, auto complete the search bar
                        searchBar.setQuery(LocationListAdapter.getItem(i), true);
                        //TODO: set to intent if needed
                    }
                });
                return false;
            }
        });

    }

}
