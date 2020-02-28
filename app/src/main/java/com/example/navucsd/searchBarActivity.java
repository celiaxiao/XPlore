package com.example.navucsd;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



//import com.google.android.libraries.places.api.net.PlacesClient;



public class searchBarActivity extends AppCompatActivity {


    ArrayAdapter<String> LocationListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bar);

        String[] mustGoList = getResources().getStringArray(R.array.list_of_must_go);
        LocationListAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mustGoList);

        AutoCompleteTextView editText=(AutoCompleteTextView) findViewById(R.id.actv);
        editText.setAdapter(LocationListAdapter);
        TextView getClickedLocation=(TextView)findViewById(R.id.getSelection);

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String input=editText.getText().toString();
                getClickedLocation.setText(input);

            }
        });


    }
}
