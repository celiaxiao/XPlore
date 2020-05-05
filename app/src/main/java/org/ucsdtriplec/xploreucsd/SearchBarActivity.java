package org.ucsdtriplec.xploreucsd;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import java.util.ArrayList;

import android.view.View;

public class SearchBarActivity extends AppCompatActivity {


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

                    alertDialog();
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
    private void alertDialog() {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setMessage("Sorry we can't find your input location. Explore our main page?");
        dialog.setTitle("Location not found");
        dialog.setPositiveButton("Comfirm",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {

                    }
                });

        AlertDialog alertDialog=dialog.create();
        alertDialog.show();
    }

}
